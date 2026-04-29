package com.esma.webtech.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL"
                + ");";

        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL UNIQUE, "
                + "full_name TEXT NOT NULL, "
                + "department TEXT NOT NULL, "
                + "short_bio TEXT NOT NULL, "
                + "main_skill TEXT NOT NULL, "
                + "image_url TEXT, "
                + "FOREIGN KEY (user_id) REFERENCES users(id)"
                + ");";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(createUsersTable);
            statement.execute(createStudentsTable);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}