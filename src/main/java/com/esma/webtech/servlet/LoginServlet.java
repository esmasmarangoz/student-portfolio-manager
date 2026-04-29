package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.esma.webtech.util.DBConnection;
import com.esma.webtech.util.DatabaseInitializer;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseInitializer.initialize();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false,\"message\":\"Username and password are required.\"}");
            out.flush();
            return;
        }

        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String dbUsername = resultSet.getString("username");
                    String role = resultSet.getString("role");

                    HttpSession session = request.getSession();
                    session.setAttribute("userId", userId);
                    session.setAttribute("username", dbUsername);
                    session.setAttribute("role", role);

                    PrintWriter out = response.getWriter();
                    out.print("{\"success\":true,\"message\":\"Login successful.\",\"role\":\"" + role + "\"}");
                    out.flush();
                } else {
                    PrintWriter out = response.getWriter();
                    out.print("{\"success\":false,\"message\":\"Invalid username or password.\"}");
                    out.flush();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false,\"message\":\"Database error during login.\"}");
            out.flush();
        }
    }
}