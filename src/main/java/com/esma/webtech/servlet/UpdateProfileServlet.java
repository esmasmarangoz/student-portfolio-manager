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

public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        String fullName = request.getParameter("fullName");
        String department = request.getParameter("department");
        String shortBio = request.getParameter("shortBio");
        String mainSkill = request.getParameter("mainSkill");
        String imageUrl = request.getParameter("imageUrl");

        String sql = "UPDATE students SET full_name = ?, department = ?, short_bio = ?, main_skill = ?, image_url = ? "
                   + "WHERE user_id = (SELECT id FROM users WHERE username = ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, department);
            ps.setString(3, shortBio);
            ps.setString(4, mainSkill);
            ps.setString(5, imageUrl);
            ps.setString(6, username);

            int updatedRows = ps.executeUpdate();

            if (updatedRows > 0) {
                out.print("{\"success\":true,\"message\":\"Profile updated successfully.\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Profile update failed.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Database error during update.\"}");
        }

        out.flush();
    }
}