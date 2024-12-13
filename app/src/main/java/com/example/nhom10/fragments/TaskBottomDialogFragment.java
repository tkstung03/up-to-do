package com.example.nhom10.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nhom10.R;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskBottomDialogFragment extends BottomSheetDialogFragment {

    private TaskDAO taskDAO;
    private TaskTagsDAO taskTagsDAO;
    private int categoryId = -1;
    private final ArrayList<Integer> tagIds = new ArrayList<>();

    public static TaskBottomDialogFragment newInstance() {
        return new TaskBottomDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();
        taskDAO = new TaskDAO(context);
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
        LinearLayout categoryLayout = view.findViewById(R.id.layoutCategory);
        LinearLayout tagsLayout = view.findViewById(R.id.layoutTags);
        LinearLayout dateLayout = view.findViewById(R.id.layoutDate);
        LinearLayout timeLayout = view.findViewById(R.id.layoutTime);
        FloatingActionButton fabSave = view.findViewById(R.id.fabSave);
        ImageView cancelButton = view.findViewById(R.id.cancelButton);

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

        final Calendar selectedDate = Calendar.getInstance();
        dateLayout.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                Toast.makeText(requireContext(), "Ngày đã chọn: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        final Calendar selectedTime = Calendar.getInstance();
        timeLayout.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, hourOfDay, minute) -> {
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                Toast.makeText(requireContext(), "Thời gian đã chọn: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
            }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        fabSave.setOnClickListener(v -> {
            String title = editTextName.getText().toString();
            String note = editTextNote.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(requireContext(), "Task đã được lưu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Lỗi khi lưu task", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void handleUpdatedCategory(String s, Bundle bundle) {
        categoryId = bundle.getInt("updatedCategoryId");
        Toast.makeText(requireContext(), "Danh mục đã chọn: " + categoryId, Toast.LENGTH_SHORT).show();
    }

    private void handleUpdatedTag(String s, Bundle bundle) {
        int[] newTagIds = bundle.getIntArray("updatedTagIds");
        if (newTagIds != null) {
            tagIds.clear();
            for (int tagId : newTagIds) {
                tagIds.add(tagId);
            }
        }
        Toast.makeText(requireContext(), "Tags đã chọn: " + tagIds, Toast.LENGTH_SHORT).show();
    }

}
