package com.esma.webtech.model;

public class Student {

    // Basic student fields
    private String fullName;
    private String department;
    private String university;
    private String bio;
    private String email;

    // Constructor to fill all fields
    public Student(String fullName, String department, String university, String bio, String email) {
        this.fullName = fullName;
        this.department = department;
        this.university = university;
        this.bio = bio;
        this.email = email;
    }

    // Getters for JSON conversion
    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public String getUniversity() {
        return university;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }
}