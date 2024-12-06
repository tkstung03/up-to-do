package com.example.nhom10.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "group10.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT NOT NULL UNIQUE, " +
            "email TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE categories (" +
            "category_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "icon TEXT, " +
            "color TEXT, " +
            "user_id INTEGER, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id)" +
            ");";

    private static final String CREATE_TABLE_TAGS = "CREATE TABLE tags (" +
            "tag_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "color TEXT, " +
            "user_id INTEGER, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id)" +
            ");";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE tasks (" +
            "task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "note TEXT, " +
            "due_date DATE, " +
            "user_id INTEGER, " +
            "category_id INTEGER, " +
            "is_completed INTEGER DEFAULT 0, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id), " +
            "FOREIGN KEY(category_id) REFERENCES categories(category_id)" +
            ");";

    private static final String CREATE_TABLE_TASKS_TAGS = "CREATE TABLE tasks_tags (" +
            "task_tag_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "task_id INTEGER NOT NULL, " +
            "tag_id INTEGER NOT NULL, " +
            "FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE, " +
            "FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE" +
            ");";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_TASKS_TAGS);

        db.execSQL(InitData.INSERT_USERS);
        db.execSQL(InitData.INSERT_CATEGORIES);
        db.execSQL(InitData.INSERT_TAGS);
        db.execSQL(InitData.INSERT_TASKS);
        db.execSQL(InitData.INSERT_TASKS_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS tags");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS tasks_tags");

        onCreate(db);
    }
}

