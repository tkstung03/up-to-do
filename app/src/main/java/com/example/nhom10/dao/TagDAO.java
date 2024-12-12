package com.example.nhom10.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Tag;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagDAO implements ITagDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public TagDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    @Override
    @SuppressLint("Range")
    public Tag getById(int id) {
        Tag tag = null;
        String query = "SELECT tag_id, name, color FROM tags WHERE tag_id = ? AND user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id), String.valueOf(userId)});

        try {
            if (cursor.moveToFirst()) {
                int tagId = cursor.getInt(cursor.getColumnIndex("tag_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                tag = new Tag(tagId, name, color, userId);
            }
        } finally {
            cursor.close();
        }
        return tag;
    }

    @Override
    @SuppressLint("Range")
    public List<Tag> getAll() {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT tag_id, name, color FROM tags WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        try {
            while (cursor.moveToNext()) {
                int tagId = cursor.getInt(cursor.getColumnIndex("tag_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                tags.add(new Tag(tagId, name, color, userId));
            }
        } finally {
            cursor.close();
        }

        return tags.isEmpty() ? Collections.emptyList() : tags;
    }

    @Override
    public boolean update(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("name", tag.getName());
        values.put("color", tag.getColor());

        int rowsAffected = db.update("tags", values, "tag_id = ? AND user_id = ?",
                new String[]{String.valueOf(tag.getTagId()), String.valueOf(userId)});
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int id) {
        int rowsAffected = db.delete("tags", "tag_id = ? AND user_id = ?", new String[]{String.valueOf(id), String.valueOf(userId)});
        return rowsAffected > 0;
    }
}
