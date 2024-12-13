package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Tag;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public TagDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    public long addTag(String name, String color) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("color", color);
        values.put("user_id", userId);

        return db.insert("tags", null, values);
    }

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String selectQuery = "SELECT tag_id, name, color FROM tags WHERE user_id = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("tag_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));

                tags.add(new Tag(id, name, color, userId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return tags;
    }

    public int updateTag(int tagId, String newName, String newColor) {
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("color", newColor);

        String whereClause = "tag_id = ? AND user_id = ?";
        String[] whereArgs = {String.valueOf(tagId), String.valueOf(userId)};

        return db.update("tags", values, whereClause, whereArgs);
    }

    public int deleteTag(int id) {
        String whereClause = "tag_id = ? AND user_id = ?";
        String[] whereArgs = {String.valueOf(id), String.valueOf(userId)};

        return db.delete("tags", whereClause, whereArgs);
    }

    public Tag getTagById(int tagId) {
        Tag tag = null;
        String selectQuery = "SELECT tag_id, name, color FROM tags WHERE tag_id = ? AND user_id = ?";
        String[] whereArgs = {String.valueOf(tagId), String.valueOf(userId)};

        Cursor cursor = db.rawQuery(selectQuery, whereArgs);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("tag_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));

            tag = new Tag(id, name, color, userId);
            cursor.close();
        }
        return tag;
    }
}
