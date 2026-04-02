package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esma.webtech.model.SimpleStudent;
import com.esma.webtech.util.DBConnection;
import com.esma.webtech.util.DatabaseInitializer;
import com.google.gson.Gson;

public class StudentsServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseInitializer.initialize();

        List<SimpleStudent> students = new ArrayList<>();

        String sql = "SELECT id, full_name, department, short_bio, main_skill, image_url FROM students";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                SimpleStudent student = new SimpleStudent(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("department"),
                        resultSet.getString("short_bio"),
                        resultSet.getString("main_skill"),
                        resultSet.getString("image_url")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter errorOut = response.getWriter();
            errorOut.print("{\"error\":\"Failed to fetch students.\"}");
            errorOut.flush();
            return;
        }

        String json = gson.toJson(students);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}