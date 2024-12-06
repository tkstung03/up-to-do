package com.example.nhom10.model;

import java.util.Date;

public class Tasks {
    private int taskId;
    private String title;
    private String note;
    private Date dueDate;
    private Date createAt;
    private Date updateAt;
    private int user_id;
    private int categoryId;
    private boolean isCompleted;

    public Tasks() {
    }

    public Tasks(int taskId, String title, String note, Date dueDate, Date createAt, Date updateAt, int user_id, int categoryId, boolean isCompleted) {
        this.taskId = taskId;
        this.title = title;
        this.note = note;
        this.dueDate = dueDate;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.user_id = user_id;
        this.categoryId = categoryId;
        this.isCompleted = isCompleted;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
