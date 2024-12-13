package com.example.nhom10.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.adapter.TagAdapter;
import com.example.nhom10.dao.TagDAO;
import com.example.nhom10.model.Tag;
import com.example.nhom10.objects.UserSession;

import java.util.List;

public class TagManagement extends AppCompatActivity {
    private TagDAO tagDAO;
    private ListView listViewTags;
    private Button btnAddTag;
    private TagAdapter adapter;
    private List<Tag> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_management);

        UserSession.getInstance().setUserId(1); // Thiết lập ID người dùng tạm thời
        tagDAO = new TagDAO(this);

        listViewTags = findViewById(R.id.listViewTags);
        btnAddTag = findViewById(R.id.btnAddTag);

        loadTags();

        btnAddTag.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm Tag");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText inputName = new EditText(this);
            inputName.setHint("Tên tag");
            layout.addView(inputName);

            final EditText inputColor = new EditText(this);
            inputColor.setHint("Mã màu (vd: #FF5733)");
            layout.addView(inputColor);

            builder.setView(layout);

            builder.setPositiveButton("Thêm", (dialog, which) -> {
                String tagName = inputName.getText().toString();
                String tagColor = inputColor.getText().toString();

                if (!tagName.isEmpty() && !tagColor.isEmpty()) {
                    tagDAO.addTag(tagName, tagColor); // Sử dụng TagDAO
                    loadTags();
                }
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        listViewTags.setOnItemLongClickListener((parent, view, position, id) -> {
            Tag selectedTag = tags.get(position);

            CharSequence[] options = {"Sửa", "Xóa"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tùy chọn");
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) { // Sửa
                    AlertDialog.Builder editBuilder = new AlertDialog.Builder(this);
                    editBuilder.setTitle("Sửa Tag");

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText editName = new EditText(this);
                    editName.setText(selectedTag.getName());
                    layout.addView(editName);

                    final EditText editColor = new EditText(this);
                    editColor.setText(selectedTag.getColor());
                    layout.addView(editColor);

                    editBuilder.setView(layout);

                    editBuilder.setPositiveButton("Cập nhật", (dialog1, which1) -> {
                        String newName = editName.getText().toString();
                        String newColor = editColor.getText().toString();

                        if (!newName.isEmpty() && !newColor.isEmpty()) {
                            tagDAO.updateTag(selectedTag.getTagId(), newName, newColor); // Sử dụng TagDAO
                            loadTags();
                        }
                    });
                    editBuilder.setNegativeButton("Hủy", (dialog1, which1) -> dialog1.cancel());
                    editBuilder.show();
                } else if (which == 1) { // Xóa
                    tagDAO.deleteTag(selectedTag.getTagId()); // Sử dụng TagDAO
                    loadTags();
                }
            });
            builder.show();
            return true;
        });
    }

    private void loadTags() {
        tags = tagDAO.getAllTags(); // Lấy dữ liệu từ TagDAO
        adapter = new TagAdapter(this, tags);
        listViewTags.setAdapter(adapter);
    }
}