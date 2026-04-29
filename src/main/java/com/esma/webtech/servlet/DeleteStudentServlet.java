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

public class DeleteStudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null) {
            out.print("{\"success\":false,\"message\":\"Not authorized\"}");
            out.flush();
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            out.print("{\"success\":false,\"message\":\"Only admin can delete students\"}");
            out.flush();
            return;
        }

        String studentId = request.getParameter("studentId");

        if (studentId == null || studentId.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"Student id is required\"}");
            out.flush();
            return;
        }

        String getUserIdSql = "SELECT user_id FROM students WHERE id = ?";
        String deleteStudentSql = "DELETE FROM students WHERE id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            int userId = -1;

            try (PreparedStatement getUserIdStatement = connection.prepareStatement(getUserIdSql)) {
                getUserIdStatement.setInt(1, Integer.parseInt(studentId));

                try (ResultSet rs = getUserIdStatement.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                    } else {
                        out.print("{\"success\":false,\"message\":\"Student not found.\"}");
                        out.flush();
                        return;
                    }
                }
            }

            try (PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentSql);
                 PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSql)) {

                deleteStudentStatement.setInt(1, Integer.parseInt(studentId));
                int deletedRows = deleteStudentStatement.executeUpdate();

                deleteUserStatement.setInt(1, userId);
                deleteUserStatement.executeUpdate();

                connection.commit();

                if (deletedRows > 0) {
                    out.print("{\"success\":true,\"message\":\"Student deleted successfully.\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Student not found.\"}");
                }
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Delete failed.\"}");
        }

        out.flush();
    }
}