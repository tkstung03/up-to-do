package com.example.nhom10.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.objects.UserSession;

public class TasksDAO {
    private SQLiteDatabase db;
    private int user_id;
    public TasksDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        user_id = userSession.getUserId();
    }
}
