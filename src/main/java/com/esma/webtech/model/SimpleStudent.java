package com.esma.webtech.model;

public class SimpleStudent {

    private int id;
    private String fullName;
    private String department;
    private String shortBio;
    private String mainSkill;
    private String imageUrl;

    public SimpleStudent(int id, String fullName, String department, String shortBio, String mainSkill, String imageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.department = department;
        this.shortBio = shortBio;
        this.mainSkill = mainSkill;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public String getShortBio() {
        return shortBio;
    }

    public String getMainSkill() {
        return mainSkill;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}