package com.example.nhom10.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nhom10.R;
import com.example.nhom10.adapter.CategoryAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryFragment extends Fragment {
    private ListView listView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private CategoryDAO categoryDAO;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Khởi tạo các thành phần giao diện
        listView = view.findViewById(R.id.categoryListView);

        // Khởi tạo DAO và danh sách dữ liệu
        categoryDAO = new CategoryDAO(requireContext());
        categoryList = categoryDAO.getAllCategories(); // Lấy dữ liệu từ cơ sở dữ liệu

        // Nếu danh sách rỗng, thêm dữ liệu mẫu
        if (categoryList.isEmpty()) {
            addSampleCategories();
        }

        // Thiết lập adapter
        adapter = new CategoryAdapter(requireContext(), categoryList, categoryDAO);
        listView.setAdapter(adapter);

        // Xử lý sự kiện khi nhấn vào một danh mục
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Category category = categoryList.get(position);
            Toast.makeText(requireContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public void showAddCategoryDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_category);

        // Ánh xạ các phần tử trong layout
        ImageView closeButton = dialog.findViewById(R.id.cancelButton);
        TextView createText = dialog.findViewById(R.id.createText);
        EditText editCategoryName = dialog.findViewById(R.id.editCategoryName);

        LinearLayout colorSelectionLayout = dialog.findViewById(R.id.colorSelectionLayout);
        ImageView color1 = dialog.findViewById(R.id.color1);
        ImageView color2 = dialog.findViewById(R.id.color2);
        ImageView color3 = dialog.findViewById(R.id.color3);
        ImageView color4 = dialog.findViewById(R.id.color4);
        ImageView color5 = dialog.findViewById(R.id.color5);

        LinearLayout iconSelectionLayout = dialog.findViewById(R.id.iconSelectionLayout);
        ImageView icon1 = dialog.findViewById(R.id.icon1);
        ImageView icon2 = dialog.findViewById(R.id.icon2);
        ImageView icon3 = dialog.findViewById(R.id.icon3);
        ImageView icon4 = dialog.findViewById(R.id.icon4);
        ImageView icon5 = dialog.findViewById(R.id.icon5);

        FloatingActionButton fabSaveCategory = dialog.findViewById(R.id.fabSave);

        // Các giá trị mặc định
        final String[] selectedColor = {"#FFFFFF"};  // Màu mặc định là màu trắng
        final String[] selectedIcon = {"baseline_home_24"};  // Biểu tượng mặc định

        // Sự kiện cho các ô màu sắc
        color1.setOnClickListener(v -> selectedColor[0] = "#FF0000"); // Màu đỏ
        color2.setOnClickListener(v -> selectedColor[0] = "#33B5E5"); // Màu xanh
        color3.setOnClickListener(v -> selectedColor[0] = "#99CC00"); // Màu xanh lá
        color4.setOnClickListener(v -> selectedColor[0] = "FFBB33"); // Màu cam
        color5.setOnClickListener(v -> selectedColor[0] = "#AA66CC"); // Màu tím

        // Sự kiện cho các biểu tượng
        icon1.setOnClickListener(v -> selectedIcon[0] = "baseline_home_24");
        icon2.setOnClickListener(v -> selectedIcon[0] = "ic_work");
        icon3.setOnClickListener(v -> selectedIcon[0] = "ic_personal");
        icon4.setOnClickListener(v -> selectedIcon[0] = "ic_cart");
        icon5.setOnClickListener(v -> selectedIcon[0] = "ic_question");

        // Khi nhấn nút lưu, thực hiện thêm danh mục vào cơ sở dữ liệu
        fabSaveCategory.setOnClickListener(view -> {
            String categoryName = editCategoryName.getText().toString();
            if (categoryName.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Category mới với thông tin đã cập nhật
            Category category = new Category(0, categoryName, selectedIcon[0], selectedColor[0], 0);

            // Lưu vào cơ sở dữ liệu
            boolean isInserted = categoryDAO.insertCategory(category);

            if (isInserted) {
                Toast.makeText(requireContext(), "Danh mục đã được thêm", Toast.LENGTH_SHORT).show();

                // Cập nhật lại danh sách và thông báo cho adapter
                categoryList.clear();
                categoryList.addAll(categoryDAO.getAllCategories());
                adapter.notifyDataSetChanged();  // Thông báo adapter cập nhật danh sách

            } else {
                Toast.makeText(requireContext(), "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        // Khi nhấn nút đóng, đóng dialog
        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Hàm thêm dữ liệu mẫu vào danh sách
    private void addSampleCategories() {
        // Sử dụng dữ liệu mẫu có sẵn trong InitData để thêm vào cơ sở dữ liệu
        categoryDAO.insertCategory(new Category(0, "Công việc", "ic_work", null, 1));
        categoryDAO.insertCategory(new Category(0, "Cá nhân", "ic_personal", null, 2));

        // Cập nhật lại danh sách sau khi thêm
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
    }
}
