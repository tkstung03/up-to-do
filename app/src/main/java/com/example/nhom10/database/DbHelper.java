package com.example.nhom10.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "group10.db";
    private static final int DB_VERSION = 1;


    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE Categories (" +
            "categoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "userId INTEGER" +
            ");";

    private static final String CREATE_TABLE_TAGS = "CREATE TABLE Tags (" +
            "tagId INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "createAt INTEGER," +
            "color TEXT" +
            ");";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE Tasks (" +
            "taskId INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "description TEXT, " +
            "dueDate INTEGER, " +
            "createAt INTEGER, " +
            "updateAt INTEGER, " +
            "user_id INTEGER, " +
            "categoryId INTEGER, " +
            "FOREIGN KEY(user_id) REFERENCES Users(userId), " +
            "FOREIGN KEY(categoryId) REFERENCES Categories(categoryId)" +
            ");";

    private static final String CREATE_TABLE_TASKS_TAGS = "CREATE TABLE Tasks_Tags (" +
            "taskId INTEGER, " +
            "tagId INTEGER, " +
            "PRIMARY KEY (taskId, tagId), " +
            "FOREIGN KEY(taskId) REFERENCES Tasks(taskId), " +
            "FOREIGN KEY(tagId) REFERENCES Tags(tagId)" +
            ");";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE Users (" +
            "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, " +
            "email TEXT, " +
            "password TEXT" +
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Categories");
        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Tasks_Tags");

        onCreate(db);
    }
}

