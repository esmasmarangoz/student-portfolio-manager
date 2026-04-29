package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.esma.webtech.util.DBConnection;

public class AddStudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            out.print("{\"success\":false,\"message\":\"Not authorized. Only admin can add students.\"}");
            out.flush();
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String fullName = request.getParameter("fullName");
        String department = request.getParameter("department");
        String shortBio = request.getParameter("shortBio");
        String mainSkill = request.getParameter("mainSkill");
        String imageUrl = request.getParameter("imageUrl");

        if (isEmpty(username) || isEmpty(email) || isEmpty(password) ||
                isEmpty(fullName) || isEmpty(department) || isEmpty(shortBio) || isEmpty(mainSkill)) {

            out.print("{\"success\":false,\"message\":\"Please fill all required fields.\"}");
            out.flush();
            return;
        }

        if (isEmpty(imageUrl)) {
            imageUrl = "images/profile.jpg";
        }

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            String userSql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, 'student')";

            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {

                userStmt.setString(1, username);
                userStmt.setString(2, email);
                userStmt.setString(3, password);

                userStmt.executeUpdate();

                int userId = -1;

                try (PreparedStatement idStmt = conn.prepareStatement("SELECT last_insert_rowid()");
                     ResultSet rs = idStmt.executeQuery()) {

                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }

                if (userId == -1) {
                    conn.rollback();
                    out.print("{\"success\":false,\"message\":\"Could not create user account.\"}");
                    out.flush();
                    return;
                }

                String studentSql = "INSERT INTO students(user_id, full_name, department, short_bio, main_skill, image_url) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                    studentStmt.setInt(1, userId);
                    studentStmt.setString(2, fullName);
                    studentStmt.setString(3, department);
                    studentStmt.setString(4, shortBio);
                    studentStmt.setString(5, mainSkill);
                    studentStmt.setString(6, imageUrl);

                    studentStmt.executeUpdate();
                }

                conn.commit();
                out.print("{\"success\":true,\"message\":\"Student added successfully.\"}");

            } catch (Exception e) {
                conn.rollback();

                String errorMessage = e.getMessage();

                if (errorMessage != null && errorMessage.contains("UNIQUE")) {
                    out.print("{\"success\":false,\"message\":\"Username or email already exists. Please use different values.\"}");
                } else {
                    e.printStackTrace();
                    out.print("{\"success\":false,\"message\":\"Error adding student.\"}");
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