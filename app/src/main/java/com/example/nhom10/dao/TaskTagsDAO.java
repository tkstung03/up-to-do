package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Tag;
import com.example.nhom10.objects.UserSession;

import java.util.HashSet;
import java.util.Set;

public class TaskTagsDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public TaskTagsDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    public void create(int taskId, Set<Tag> selectedTags) {
        db.beginTransaction();
        try {
            for (Tag tag : selectedTags) {
                ContentValues values = new ContentValues();
                values.put("task_id", taskId);
                values.put("tag_id", tag.getTagId());

                db.insertOrThrow("tasks_tags", null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public Set<Tag> getAllByTaskId(int taskId) {
        Set<Tag> tags = new HashSet<>();

        String query = "SELECT t.tag_id, t.name, t.color FROM tags t " +
                "JOIN tasks_tags tt ON t.tag_id = tt.tag_id " +
                "WHERE tt.task_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(taskId)});

        try {
            while (cursor.moveToNext()) {
                int tagId = cursor.getInt(cursor.getColumnIndexOrThrow("tag_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));

                Tag tag = new Tag(tagId, name, color, userId);
                tags.add(tag);
            }
        } finally {
            cursor.close();
        }

        return tags;
    }

    public void deleteByTaskId(int taskId) {
        String query = "DELETE FROM tasks_tags WHERE task_id =?";
        db.execSQL(query, new String[]{String.valueOf(taskId)});
    }

    public int getTagCountByTaskId(int taskId) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM tasks_tags WHERE task_id = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(taskId)});
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }
}
