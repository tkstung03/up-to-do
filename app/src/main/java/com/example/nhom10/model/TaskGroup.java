package com.example.nhom10.model;

import java.util.List;

public class TaskGroup {
    private String title;
    private String color;
    private List<Task> taskList;

    public TaskGroup(String title, String color, List<Task> taskList) {
        this.title = title;
        this.color = color;
        this.taskList = taskList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
