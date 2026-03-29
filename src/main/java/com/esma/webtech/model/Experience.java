package com.esma.webtech.model;

public class Experience {

    private String title;
    private String company;
    private String year;

    // Constructor
    public Experience(String title, String company, String year) {
        this.title = title;
        this.company = company;
        this.year = year;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getYear() {
        return year;
    }
}