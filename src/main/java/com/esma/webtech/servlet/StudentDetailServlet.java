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

import com.esma.webtech.model.SimpleStudent;
import com.esma.webtech.util.DBConnection;
import com.esma.webtech.util.DatabaseInitializer;
import com.google.gson.Gson;

public class StudentDetailServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseInitializer.initialize();

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Student id is required.\"}");
            out.flush();
            return;
        }

        String sql = "SELECT id, full_name, department, short_bio, main_skill, image_url FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, Integer.parseInt(idParam));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    SimpleStudent student = new SimpleStudent(
                            resultSet.getInt("id"),
                            resultSet.getString("full_name"),
                            resultSet.getString("department"),
                            resultSet.getString("short_bio"),
                            resultSet.getString("main_skill"),
                            resultSet.getString("image_url")
                    );

                    String json = gson.toJson(student);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    PrintWriter out = response.getWriter();
                    out.print(json);
                    out.flush();
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    PrintWriter out = response.getWriter();
                    out.print("{\"error\":\"Student not found.\"}");
                    out.flush();
                }
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Invalid student id.\"}");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Failed to fetch student detail.\"}");
            out.flush();
        }
    }
}