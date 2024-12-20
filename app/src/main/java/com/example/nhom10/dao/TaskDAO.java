package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Task;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public TaskDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    private Task parseTask(Cursor cursor) {
        Task task = new Task();
        task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        task.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
        int dueDateIndex = cursor.getColumnIndexOrThrow("due_date");
        if (!cursor.isNull(dueDateIndex)) {
            task.setDueDate(new Date(cursor.getLong(dueDateIndex)));
        } else {
            task.setDueDate(null);
        }
        int reminderTimeIndex = cursor.getColumnIndexOrThrow("reminder_time");
        if (!cursor.isNull(reminderTimeIndex)) {
            task.setReminderTime(new Date(cursor.getLong(reminderTimeIndex)));
        } else {
            task.setReminderTime(null);
        }
        int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("is_completed"));
        task.setCompleted(isCompleted == 1);
        task.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        return task;
    }

    public Task findById(int taskId) {
        String query = "SELECT * FROM tasks WHERE user_id = ? AND task_id = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(taskId)})) {
            if (cursor.moveToNext()) {
                return parseTask(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> findAll() {
        List<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM tasks WHERE user_id = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)})) {
            while (cursor.moveToNext()) {
                taskList.add(parseTask(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public long save(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("note", task.getNote());
        if (task.getDueDate() != null) {
            contentValues.put("due_date", task.getDueDate().getTime());
        } else {
            contentValues.putNull("due_date");
        }
        if (task.getReminderTime() != null) {
            contentValues.put("reminder_time", task.getReminderTime().getTime());
        } else {
            contentValues.putNull("reminder_time");
        }
        contentValues.put("user_id", userId);
        contentValues.put("category_id", task.getCategoryId());

        return db.insert("tasks", null, contentValues);
    }

    public long updateTaskStatus(int taskId, boolean isCompleted) {
        ContentValues values = new ContentValues();
        values.put("is_completed", isCompleted ? 1 : 0);

        return db.update("tasks", values, "task_id = ?", new String[]{String.valueOf(taskId)});
    }

    public long updateTaskDueDate(int taskId, Date dueDate) {
        ContentValues contentValues = new ContentValues();
        if (dueDate != null) {
            contentValues.put("due_date", dueDate.getTime());
        } else {
            contentValues.putNull("due_date");
        }
        return db.update("tasks", contentValues, "task_id = ?", new String[]{String.valueOf(taskId)});
    }

    public long updateTaskReminderTime(long taskId, Date reminderTime) {
        ContentValues contentValues = new ContentValues();
        if (reminderTime != null) {
            contentValues.put("reminder_time", reminderTime.getTime());
        } else {
            contentValues.putNull("reminder_time");
        }
        return db.update("tasks", contentValues, "task_id = ?", new String[]{String.valueOf(taskId)});
    }

    public long updateTaskCategory(int taskId, int categoryId) {
        ContentValues values = new ContentValues();
        values.put("category_id", categoryId);
        return db.update("tasks", values, "task_id = ?", new String[]{String.valueOf(taskId)});
    }

    public long updateTaskTitleAndNote(int taskId, String newTitle, String newNote) {
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        values.put("note", newNote);

        return db.update("tasks", values, "task_id = ? AND user_id = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }

    public long delete(int taskId) {
        return db.delete("tasks", "task_id = ? AND user_id = ?", new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }
    public int getTaskCompletedByDay(Date findDate){

        return 0;
    }
}