package com.example.nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom10.database.DbHelper;
import com.example.nhom10.model.Category;
import com.example.nhom10.objects.UserSession;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final SQLiteDatabase db;
    private final int userId;

    public CategoryDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();
    }

    // Thêm danh mục mới
    public boolean insertCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("icon", category.getIcon());
        values.put("color", category.getColor());
        values.put("user_id", userId); // Thay "userId" thành "user_id"

        long result = db.insert("categories", null, values); // Thay "Category" thành "categories"
        return result != -1;
    }

    // Cập nhật danh mục
    public int updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("icon", category.getIcon());
        values.put("color", category.getColor());

        return db.update("categories", values, "category_id = ? AND user_id = ?", // Thay "Category" thành "categories"
                new String[]{String.valueOf(category.getCategoryId()), String.valueOf(userId)}); // Thay "categoryId" và "userId" thành "category_id" và "user_id"
    }

    // Xóa danh mục
    public int deleteCategory(int categoryId) {
        return db.delete("categories", "category_id = ? AND user_id = ?", // Thay "Category" thành "categories"
                new String[]{String.valueOf(categoryId), String.valueOf(userId)}); // Thay "categoryId" và "userId" thành "category_id" và "user_id"
    }

    // Lấy tất cả danh mục của người dùng
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = db.query("categories", null, "user_id = ?", // Thay "Category" thành "categories"
                new String[]{String.valueOf(userId)}, null, null, null); // Thay "userId" thành "user_id"

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id"))); // Thay "categoryId" thành "category_id"
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                category.setIcon(cursor.getString(cursor.getColumnIndexOrThrow("icon")));
                category.setColor(cursor.getString(cursor.getColumnIndexOrThrow("color")));
                category.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))); // Thay "userId" thành "user_id"
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return categories;
    }
}
