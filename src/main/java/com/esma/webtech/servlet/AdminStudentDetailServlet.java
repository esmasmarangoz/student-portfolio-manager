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

public class AdminStudentDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            out.print("{\"success\":false,\"message\":\"Not authorized\"}");
            out.flush();
            return;
        }

        String studentId = request.getParameter("id");

        if (studentId == null || studentId.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"Student id is required\"}");
            out.flush();
            return;
        }

        String sql = "SELECT id, full_name, department, short_bio, main_skill, image_url "
                + "FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(studentId));

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
                    out.print("{\"success\":false,\"message\":\"Student not found\"}");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error loading student\"}");
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