package com.example.nhom10.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.User;
import com.example.nhom10.objects.UserSession;

public class UserDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public UserDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }
    public boolean checkUser(String username, String password){
        String sqlQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.rawQuery(sqlQuery,selectionArgs);
        if (cursor.getCount() != 0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public int getUserId(String username, String password){
        String sqlQuery = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.rawQuery(sqlQuery,selectionArgs);
        int userId = 0;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("user_id"));
        }
        cursor.close();
        return userId;
    }

    public long insert(User obj){
        ContentValues values = new ContentValues();
        values.put("username",obj.getUsername());
        values.put("email",obj.getEmail());
        values.put("password",obj.getPassword());
        return db.insert("users", null, values);
    }

    public boolean checkExistUser(String username){
        String sqlQuery = "SELECT * FROM users WHERE username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sqlQuery,selectionArgs );
        if (cursor.getCount() != 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkExistEmail(String email){
        String sqlQuery = "SELECT * FROM users WHERE email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(sqlQuery,selectionArgs );
        if (cursor.getCount() != 0){
            return true;
        } else {
            return false;
        }
    }
}
