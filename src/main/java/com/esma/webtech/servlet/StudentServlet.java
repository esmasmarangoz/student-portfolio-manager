package com.esma.webtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.esma.webtech.model.Student;

public class StudentServlet extends HttpServlet {

    // Gson converts Java object to JSON
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Temporary static student data
        Student student = new Student(
                "Esma Sevvval Marangoz",
                "Computer Engineering",
                "Biruni University",
                "Third-year computer engineering student interested in web technologies and software development.",
                "esmasevvalmarangoz@gmail.com"
        );

        // Convert object to JSON
        String studentJson = gson.toJson(student);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(studentJson);
        out.flush();
    }
}