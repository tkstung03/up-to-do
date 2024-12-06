package com.example.nhom10.model;

public class Tag {
    private int tagId;
    private String name;
    private String color;
    private int userId;

    public Tag() {
    }

    public Tag(int tagId, String name, String color, int userId) {
        this.tagId = tagId;
        this.name = name;
        this.color = color;
        this.userId = userId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
