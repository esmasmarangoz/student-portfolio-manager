package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esma.webtech.util.DBConnection;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (isEmpty(username) || isEmpty(email) || isEmpty(password)) {
            out.print("{\"success\":false,\"message\":\"All fields are required.\"}");
            out.flush();
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String userSql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, 'student')";

                try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                    userStmt.setString(1, username);
                    userStmt.setString(2, email);
                    userStmt.setString(3, password);
                    userStmt.executeUpdate();
                }

                int userId = -1;

                try (PreparedStatement idStmt = conn.prepareStatement("SELECT last_insert_rowid()");
                     ResultSet rs = idStmt.executeQuery()) {

                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }

                if (userId == -1) {
                    conn.rollback();
                    out.print("{\"success\":false,\"message\":\"User creation failed.\"}");
                    out.flush();
                    return;
                }

                String studentSql = "INSERT INTO students(user_id, full_name, department, short_bio, main_skill, image_url) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                    studentStmt.setInt(1, userId);
                    studentStmt.setString(2, username);
                    studentStmt.setString(3, "Not specified");
                    studentStmt.setString(4, "Edit your profile information.");
                    studentStmt.setString(5, "Not specified");
                    studentStmt.setString(6, "images/profile.jpg");
                    studentStmt.executeUpdate();
                }

                conn.commit();
                out.print("{\"success\":true,\"message\":\"Account created successfully. You can now login.\"}");

            } catch (Exception e) {
                conn.rollback();

                String errorMessage = e.getMessage();

                if (errorMessage != null && errorMessage.contains("UNIQUE")) {
                    out.print("{\"success\":false,\"message\":\"Username or email already exists.\"}");
                } else {
                    e.printStackTrace();
                    out.print("{\"success\":false,\"message\":\"Register failed.\"}");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Database connection error.\"}");
        }

        out.flush();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}