package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Task;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.Calendar;
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

    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM tasks WHERE user_id = ? ORDER BY due_date ASC";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)})) {
            while (cursor.moveToNext()) {
                taskList.add(parseTask(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public boolean insertTask(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("note", task.getNote());
        contentValues.put("due_date", task.getDueDate().getTime());
        contentValues.put("user_id", userId);
        contentValues.put("category_id", task.getCategoryId());

        long result = db.insert("tasks", null, contentValues);
        return result != -1;
    }

    public void updateTaskStatus(int taskId, boolean isCompleted) {
        ContentValues values = new ContentValues();
        values.put("is_completed", isCompleted ? 1 : 0);

        db.update("tasks", values, "task_id = ?", new String[]{String.valueOf(taskId)});
    }

    public List<Task> getTasksForToday() {
        Calendar calendar = Calendar.getInstance();
        long startOfDay = getStartOfDay(calendar).getTime();
        long endOfDay = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfDay, endOfDay);
    }

    public List<Task> getTasksForThisWeek() {
        Calendar calendar = Calendar.getInstance();
        long startOfWeek = getStartOfWeek(calendar).getTime();
        calendar.add(Calendar.DATE, 7);
        long endOfWeek = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfWeek, endOfWeek);
    }

    public List<Task> getTasksForThisMonth() {
        Calendar calendar = Calendar.getInstance();
        long startOfMonth = getStartOfMonth(calendar).getTime();
        calendar.add(Calendar.MONTH, 1);
        long endOfMonth = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfMonth, endOfMonth);
    }

    private List<Task> getTasksByDateRange(long startDate, long endDate) {
        List<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM tasks WHERE user_id = ? AND due_date BETWEEN ? AND ? ORDER BY due_date ASC";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(startDate), String.valueOf(endDate)})) {
            while (cursor.moveToNext()) {
                taskList.add(parseTask(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    private Task parseTask(Cursor cursor) {
        Task task = new Task();
        task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        task.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
        task.setDueDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("due_date"))));
        int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("is_completed"));
        task.setCompleted(isCompleted == 1);
        task.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        return task;
    }

    private Date getStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private Date getStartOfWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return getStartOfDay(calendar);
    }

    private Date getStartOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getStartOfDay(calendar);
    }
}