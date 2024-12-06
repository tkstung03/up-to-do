package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Tasks;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksDAO {
    private SQLiteDatabase db;
    private int user_id;

    public TasksDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        user_id = userSession.getUserId();
    }

    public List<Tasks> getTasks() {
        List<Tasks> tasksList = new ArrayList<>();

        // Lọc theo user_id
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(user_id)};

        Cursor cursor = db.query(
                "Tasks",              // Tên bảng
                null,                 // Chọn tất cả các cột
                selection,            // WHERE clause
                selectionArgs,        // Điều kiện WHERE
                null,                 // Không group by
                null,                 // Không having
                "dueDate ASC"         // Sắp xếp theo dueDate tăng dần
        );

        if (cursor.moveToFirst()) {
            do {
                Tasks task = new Tasks();
                task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("taskId")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                task.setDueDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("dueDate"))));
                task.setCreateAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("createAt"))));
                task.setUpdateAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("updateAt"))));
                task.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")));

                tasksList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasksList;
    }

    public boolean insertTask(Tasks task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("note", task.getNote());
        contentValues.put("dueDate", task.getDueDate().getTime());
        contentValues.put("createAt", task.getCreateAt().getTime());
        contentValues.put("updateAt", task.getUpdateAt().getTime());
        contentValues.put("user_id", user_id);
        contentValues.put("categoryId", task.getCategoryId());
        long result = db.insert("Tasks", null, contentValues);
        return result != -1;
    }
}
