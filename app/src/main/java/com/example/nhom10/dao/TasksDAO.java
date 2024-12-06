package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Tasks;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksDAO {
    private SQLiteDatabase db;
    private int userId;

    public TasksDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    public List<Tasks> getTasks() {
        List<Tasks> tasksList = new ArrayList<>();

        String query = "SELECT * FROM Tasks WHERE user_id = ? ORDER BY dueDate ASC";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)})) {
            while (cursor.moveToNext()) {
                tasksList.add(parseTask(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasksList;
    }

    public boolean insertTask(Tasks task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("note", task.getNote());
        contentValues.put("dueDate", task.getDueDate().getTime());
        contentValues.put("createAt", task.getCreateAt().getTime());
        contentValues.put("updateAt", task.getUpdateAt().getTime());
        contentValues.put("user_id", userId);
        contentValues.put("categoryId", task.getCategoryId());

        long result = db.insert("Tasks", null, contentValues);
        return result != -1;
    }

    public void updateTaskStatus(int taskId, boolean isCompleted) {
        ContentValues values = new ContentValues();
        values.put("isCompleted", isCompleted ? 1 : 0);

        db.update("tasks", values, "taskId = ?", new String[]{String.valueOf(taskId)});
    }

    public List<Tasks> getTasksForToday() {
        Calendar calendar = Calendar.getInstance();
        long startOfDay = getStartOfDay(calendar).getTime();
        long endOfDay = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfDay, endOfDay);
    }

    public List<Tasks> getTasksForThisWeek() {
        Calendar calendar = Calendar.getInstance();
        long startOfWeek = getStartOfWeek(calendar).getTime();
        calendar.add(Calendar.DATE, 7);
        long endOfWeek = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfWeek, endOfWeek);
    }

    public List<Tasks> getTasksForThisMonth() {
        Calendar calendar = Calendar.getInstance();
        long startOfMonth = getStartOfMonth(calendar).getTime();
        calendar.add(Calendar.MONTH, 1);
        long endOfMonth = getEndOfDay(calendar).getTime();

        return getTasksByDateRange(startOfMonth, endOfMonth);
    }

    private List<Tasks> getTasksByDateRange(long startDate, long endDate) {
        List<Tasks> tasksList = new ArrayList<>();

        String query = "SELECT * FROM Tasks WHERE user_id = ? AND dueDate BETWEEN ? AND ? ORDER BY dueDate ASC";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(startDate), String.valueOf(endDate)})) {
            while (cursor.moveToNext()) {
                tasksList.add(parseTask(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasksList;
    }

    private Tasks parseTask(Cursor cursor) {
        Tasks task = new Tasks();
        task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("taskId")));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        task.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
        task.setDueDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("dueDate"))));
        task.setCreateAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("createAt"))));
        task.setUpdateAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("updateAt"))));
        int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted"));
        task.setCompleted(isCompleted == 1);
        task.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")));
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