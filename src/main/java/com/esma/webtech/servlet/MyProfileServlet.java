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

public class MyProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            out.print("{\"success\":false,\"message\":\"Not logged in\"}");
            out.flush();
            return;
        }

        String username = (String) session.getAttribute("username");

        String sql = "SELECT s.id, s.full_name, s.department, s.short_bio, s.main_skill, s.image_url "
                   + "FROM students s "
                   + "JOIN users u ON s.user_id = u.id "
                   + "WHERE u.username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = "{"
                            + "\"success\":true,"
                            + "\"id\":" + rs.getInt("id") + ","
                            + "\"fullName\":\"" + escapeJson(rs.getString("full_name")) + "\","
                            + "\"department\":\"" + escapeJson(rs.getString("department")) + "\","
                            + "\"shortBio\":\"" + escapeJson(rs.getString("short_bio")) + "\","
                            + "\"mainSkill\":\"" + escapeJson(rs.getString("main_skill")) + "\","
                            + "\"imageUrl\":\"" + escapeJson(rs.getString("image_url")) + "\""
                            + "}";

                    out.print(json);
                } else {
                    out.print("{\"success\":false,\"message\":\"Profile not found\"}");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error loading profile\"}");
        }

        out.flush();
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}