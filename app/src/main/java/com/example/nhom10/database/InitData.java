package com.example.nhom10.database;

public class InitData {
    public static final String INSERT_USERS = "INSERT INTO Users (username, email, password) VALUES " +
            "('tuncs', 'tuncs@gmail.com', '123456'), " +
            "('chuson', 'chuson@gmail.com', '123456');";


    public static final String INSERT_CATEGORIES = "INSERT INTO Categories (name, userId) VALUES " +
            "('Công việc', 1), " +
            "('Cá nhân', 2);";


    public static final String INSERT_TAGS = "INSERT INTO Tags (name, createAt,color) VALUES " +
            "('Urgent', " + System.currentTimeMillis() + ",red), " +
            "('Quan trọng', " + System.currentTimeMillis() + ",yellow), " +
            "('Low Priority', " + System.currentTimeMillis() + ",blue);";


    public static final String INSERT_TASKS = "INSERT INTO Tasks (title, description, dueDate, createAt, updateAt, user_id, categoryId) VALUES " +
            "('Thi Android', '', " + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ", 1, 1), " +
            "('Ăn tối', 'Ăn no', " + (System.currentTimeMillis() + 86400000) + ", " + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ", 2, 2);";

    public static final String INSERT_TASKS_TAGS = "INSERT INTO Tasks_Tags (taskId, tagId) VALUES " +
            "(1, 1), " +
            "(2, 2);";
}
