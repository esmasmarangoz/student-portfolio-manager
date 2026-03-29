package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.esma.webtech.model.Experience;

public class ExperienceServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Example experience list
        List<Experience> experiences = Arrays.asList(
                new Experience("Intern Developer", "Tech Company", "2024"),
                new Experience("Frontend Project", "University", "2023")
        );

        String json = gson.toJson(experiences);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}