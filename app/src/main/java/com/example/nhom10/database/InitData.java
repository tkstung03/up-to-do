package com.example.nhom10.database;

public class InitData {
    public static final String INSERT_USERS = "INSERT INTO users (username, email, password) VALUES " +
            "('tuncs', 'tuncs@gmail.com', '123456'), " +
            "('chuson', 'chuson@gmail.com', '123456');";

    public static final String INSERT_CATEGORIES = "INSERT INTO categories (name, icon, color, user_id) VALUES " +
            "('Công việc','ic_work','#8687E7', 1), " +
            "('Cá nhân','ic_personal', '#8687E7', 2);";

    public static final String INSERT_TAGS = "INSERT INTO tags (name, color, user_id) VALUES " +
            "('Urgent', 'red', 1), " +
            "('Quan trọng', 'yellow', 2), " +
            "('Low Priority', 'blue', 1);";

    public static final String INSERT_TASKS = "INSERT INTO tasks (title, note, due_date, user_id, category_id, is_completed) VALUES " +
            "('Thi Android', '', '2024-12-07', 1, 1, 0), " +
            "('Ăn tối', 'Ăn no', '2024-12-07', 2, 2, 0);";

    public static final String INSERT_TASKS_TAGS = "INSERT INTO tasks_tags (task_id, tag_id) VALUES " +
            "(1, 1), " +
            "(2, 2);";
}
