package com.example.nhom10.model;

public class Category {
    private int categoryId;
    private String name;
    private String icon;
    private String color;
    private int userId;

    public Category() {
    }

    public Category(int categoryId, String name, String icon, String color, int userId) {
        this.categoryId = categoryId;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
