package com.example.nhom10.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.nhom10.R;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TagDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Category;
import com.example.nhom10.model.Tag;
import com.example.nhom10.model.Task;
import com.example.nhom10.service.ReminderReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskBottomDialogFragment extends BottomSheetDialogFragment {

    private TaskDAO taskDAO;
    private TagDAO tagDAO;
    private CategoryDAO categoryDAO;
    private TaskTagsDAO taskTagsDAO;
    private int categoryId = -1;
    private final ArrayList<Integer> tagIds = new ArrayList<>();

    private TextView textViewCategory;
    private TextView textViewCategoryName;
    private ImageView imageViewCateIcon;
    private LinearLayout tagsContainer;
    private ImageView imageViewTagIcon;

    public static TaskBottomDialogFragment newInstance() {
        return new TaskBottomDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();
        taskDAO = new TaskDAO(context);
        tagDAO = new TagDAO(context);
        categoryDAO = new CategoryDAO(context);
        taskTagsDAO = new TaskTagsDAO(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_task_form, container, false);

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnShowListener(d -> {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) d;
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackgroundResource(R.drawable.dialogbg);
                }
            });
        }

        EditText editTextName = view.findViewById(R.id.editName);
        EditText editTextNote = view.findViewById(R.id.editNote);
        ImageView imageViewNote = view.findViewById(R.id.imageViewNote);
        LinearLayout categoryLayout = view.findViewById(R.id.layoutCategory);
        LinearLayout tagsLayout = view.findViewById(R.id.layoutTags);
        LinearLayout dateLayout = view.findViewById(R.id.layoutDateBtn);
        LinearLayout timeLayout = view.findViewById(R.id.layoutTimeBtn);
        LinearLayout remindLayout = view.findViewById(R.id.layoutRemind);
        FloatingActionButton fabSave = view.findViewById(R.id.fabSave);
        ImageView cancelButton = view.findViewById(R.id.cancelButton);

        textViewCategory = view.findViewById(R.id.textViewCategory);
        textViewCategoryName = view.findViewById(R.id.textViewCategoryName);
        imageViewCateIcon = view.findViewById(R.id.imageViewCateIcon);
        tagsContainer = view.findViewById(R.id.layoutTagsContainer);
        imageViewTagIcon = view.findViewById(R.id.imageViewTagIcon);

        ImageView imageViewCloseDate = view.findViewById(R.id.imageViewCloseDate);
        ImageView imageViewCloseTime = view.findViewById(R.id.imageViewCloseTime);
        ImageView imageViewDate = view.findViewById(R.id.imageViewDate);
        ImageView imageViewTime = view.findViewById(R.id.imageViewTime);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewTime = view.findViewById(R.id.textViewTime);

        imageViewCloseDate.setVisibility(View.GONE);
        imageViewCloseTime.setVisibility(View.GONE);

        getParentFragmentManager().setFragmentResultListener("UPDATED_CATEGORY", this, this::handleUpdatedCategory);
        getParentFragmentManager().setFragmentResultListener("UPDATED_TAG", this, this::handleUpdatedTag);

        categoryLayout.setOnClickListener(v -> {
            ChooseCategoryFragment dialogFragment = ChooseCategoryFragment.newInstance(categoryId);
            dialogFragment.show(getParentFragmentManager(), "ChooseCategoryFragment");
        });

        tagsLayout.setOnClickListener(v -> {
            ChooseTagFragment dialogFragment = ChooseTagFragment.newInstance(tagIds);
            dialogFragment.show(getParentFragmentManager(), "ChooseTagFragment");
        });

        remindLayout.setOnClickListener(v -> showDateTimePickerDialog());

        //Lắng nghe sự kiện thay đổi input
        editTextNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    imageViewNote.setColorFilter(ContextCompat.getColor(requireContext(), R.color.textColor));
                } else {
                    imageViewNote.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lavender));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final Calendar selectedDate = Calendar.getInstance();
        dateLayout.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);

                textViewDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                textViewDate.setVisibility(View.VISIBLE);
                imageViewCloseDate.setVisibility(View.VISIBLE);
                imageViewDate.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lavender));

            }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        imageViewCloseDate.setOnClickListener(v -> {
            textViewDate.setText("");
            textViewDate.setVisibility(View.GONE);
            imageViewCloseDate.setVisibility(View.GONE);
            imageViewDate.clearColorFilter();
        });

        final Calendar selectedTime = Calendar.getInstance();
        timeLayout.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, hourOfDay, minute) -> {
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                textViewTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                textViewTime.setVisibility(View.VISIBLE);
                imageViewCloseTime.setVisibility(View.VISIBLE);
                imageViewTime.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lavender));

            }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        imageViewCloseTime.setOnClickListener(v -> {
            textViewTime.setText("");
            textViewTime.setVisibility(View.GONE);
            imageViewCloseTime.setVisibility(View.GONE);
            imageViewTime.clearColorFilter();
        });

        fabSave.setOnClickListener(v -> {
            String title = editTextName.getText().toString();
            String note = editTextNote.getText().toString();

            if (title.isEmpty()) {
                editTextName.setError("Vui lòng nhập tiêu đề");
                return;
            }

            selectedDate.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
            selectedDate.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
            selectedDate.set(Calendar.SECOND, selectedTime.get(Calendar.SECOND));
            selectedDate.set(Calendar.MILLISECOND, selectedTime.get(Calendar.MILLISECOND));
            Date dueDate = selectedDate.getTime();

            Task task = new Task();
            task.setTitle(title);
            task.setNote(note);
            task.setDueDate(dueDate);
            task.setCategoryId(categoryId);

            long taskId = taskDAO.insertTask(task);

            if (taskId != -1) {
                task.setTaskId((int) taskId);
                taskTagsDAO.create(task.getTaskId(), tagIds.stream().mapToInt(Integer::intValue).toArray());
                Toast.makeText(requireContext(), "Nhiệm vụ đã được lưu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Lỗi khi lưu nhiêm vụ", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void showDateTimePickerDialog() {
        final Calendar selectedDateTime = Calendar.getInstance();

        // Chọn ngày
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            selectedDateTime.set(year, month, dayOfMonth);

            // Sau khi chọn ngày, mở TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timeView, hourOfDay, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);

                // Hiển thị thông báo xác nhận
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận nhắc nhở")
                        .setMessage("Bạn muốn đặt nhắc nhở vào lúc: " +
                                String.format("%02d:%02d, %02d/%02d/%d",
                                        hourOfDay, minute,
                                        dayOfMonth, month + 1, year))
                        .setPositiveButton("Lưu", (dialog, which) -> {
                            setReminder(selectedDateTime.getTimeInMillis());
                        })
                        .setNegativeButton("Hủy", null)
                        .show();

            }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setReminder(long reminderTimeMillis) {
        Context context = requireContext();
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = ContextCompat.getSystemService(context, AlarmManager.class);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
            Toast.makeText(context, "Đã đặt nhắc nhở!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdatedCategory(String s, Bundle bundle) {
        categoryId = bundle.getInt("updatedCategoryId");

        if (categoryId == -1) {
            textViewCategory.setVisibility(View.VISIBLE);
            textViewCategoryName.setVisibility(View.GONE);
            imageViewCateIcon.clearColorFilter();
            imageViewCateIcon.setImageResource(R.drawable.baseline_folder_open_24);
        } else {
            Category selectedCategory = categoryDAO.getCategoryById(categoryId);
            if (selectedCategory != null) {
                textViewCategory.setVisibility(View.GONE);
                textViewCategoryName.setVisibility(View.VISIBLE);
                textViewCategoryName.setText(selectedCategory.getName());

                // Xử lý icon
                String icon = selectedCategory.getIcon();
                if (icon != null && !icon.isEmpty()) {
                    int iconResId = getResources().getIdentifier(selectedCategory.getIcon(), "drawable", requireContext().getPackageName());
                    if (iconResId != 0) {
                        imageViewCateIcon.setImageResource(iconResId);
                    } else {
                        imageViewCateIcon.setImageResource(R.drawable.baseline_folder_open_24);
                    }
                } else {
                    imageViewCateIcon.setImageResource(R.drawable.baseline_folder_open_24);
                }

                // Xử lý màu
                GradientDrawable backgroundDrawable = (GradientDrawable) textViewCategoryName.getBackground();
                String color = selectedCategory.getColor();
                if (color != null && !color.isEmpty()) {
                    int parsedColor = Color.parseColor(color);
                    backgroundDrawable.setColor(parsedColor);
                    imageViewCateIcon.setColorFilter(parsedColor);
                } else {
                    backgroundDrawable.setColor(ContextCompat.getColor(requireContext(), R.color.dark_gray));
                    imageViewCateIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lavender));
                }
            }
        }
    }

    private void handleUpdatedTag(String s, Bundle bundle) {
        int[] newTagIds = bundle.getIntArray("updatedTagIds");
        if (newTagIds != null) {
            tagIds.clear();
            for (int tagId : newTagIds) {
                tagIds.add(tagId);
            }
        }
        renderTags();
        if (!tagIds.isEmpty()) {
            imageViewTagIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lavender));
        } else {
            imageViewTagIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.textColor));
        }
    }

    private void renderTags() {
        tagsContainer.removeAllViews();

        for (int tagId : tagIds) {
            Tag tag = tagDAO.getTagById(tagId);
            if (tag == null) {
                continue;
            }
            TextView tagView = new TextView(requireContext());
            tagView.setText(tag.getName());
            tagView.setPadding(16, 8, 16, 8);
            tagView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.tags_background));

            GradientDrawable backgroundDrawable = (GradientDrawable) tagView.getBackground();
            String color = tag.getColor();
            if (color != null && !color.isEmpty()) {
                int parsedColor = Color.parseColor(color);
                backgroundDrawable.setColor(parsedColor);
            } else {
                backgroundDrawable.setColor(ContextCompat.getColor(requireContext(), R.color.dark_gray));
            }

            tagView.setTextColor(Color.WHITE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 8, 0);
            tagView.setLayoutParams(layoutParams);

            tagsContainer.addView(tagView);
        }
    }
}
