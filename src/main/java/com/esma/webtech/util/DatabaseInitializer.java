package com.esma.webtech.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "full_name TEXT NOT NULL, "
                + "department TEXT NOT NULL, "
                + "short_bio TEXT NOT NULL, "
                + "main_skill TEXT NOT NULL, "
                + "image_url TEXT"
                + ");";

        String insertSampleStudents = "INSERT INTO students (full_name, department, short_bio, main_skill, image_url) "
                + "SELECT 'Esma Sevvval Marangoz', 'Computer Engineering', "
                + "'Student interested in web technologies and software development.', "
                + "'Java', 'images/profile.jpg' "
                + "WHERE NOT EXISTS (SELECT 1 FROM students WHERE full_name = 'Esma Sevvval Marangoz');";

        String insertSampleStudents2 = "INSERT INTO students (full_name, department, short_bio, main_skill, image_url) "
                + "SELECT 'Bozhena Bursali', 'Computer Engineering', "
                + "'Student focused on software projects and problem solving.', "
                + "'JavaScript', 'images/profile.jpg' "
                + "WHERE NOT EXISTS (SELECT 1 FROM students WHERE full_name = 'Bozhena Bursali');";

        String insertSampleStudents3 = "INSERT INTO students (full_name, department, short_bio, main_skill, image_url) "
                + "SELECT 'Ayse Demir', 'Software Engineering', "
                + "'Student interested in frontend development and UI design.', "
                + "'HTML/CSS', 'images/profile.jpg' "
                + "WHERE NOT EXISTS (SELECT 1 FROM students WHERE full_name = 'Ayse Demir');";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createStudentsTable);
            statement.executeUpdate(insertSampleStudents);
            statement.executeUpdate(insertSampleStudents2);
            statement.executeUpdate(insertSampleStudents3);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}