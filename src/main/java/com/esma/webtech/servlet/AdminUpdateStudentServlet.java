package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.esma.webtech.util.DBConnection;

public class AdminUpdateStudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            out.print("{\"success\":false,\"message\":\"Not authorized\"}");
            out.flush();
            return;
        }

        String id = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String department = request.getParameter("department");
        String shortBio = request.getParameter("shortBio");
        String mainSkill = request.getParameter("mainSkill");
        String imageUrl = request.getParameter("imageUrl");

        if (id == null || id.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"Student id is required\"}");
            out.flush();
            return;
        }

        String sql = "UPDATE students SET full_name = ?, department = ?, short_bio = ?, main_skill = ?, image_url = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, department);
            ps.setString(3, shortBio);
            ps.setString(4, mainSkill);
            ps.setString(5, imageUrl);
            ps.setInt(6, Integer.parseInt(id));

            int updatedRows = ps.executeUpdate();

            if (updatedRows > 0) {
                out.print("{\"success\":true,\"message\":\"Student updated successfully.\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Student not found.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Update failed.\"}");
        }

        out.flush();
    }
}