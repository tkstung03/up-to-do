package com.example.nhom10.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.adapter.TaskGroupAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Task;
import com.example.nhom10.model.TaskGroup;
import com.example.nhom10.utils.Utils;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private TaskTagsDAO taskTagsDAO;
    private RecyclerView recyclerView;
    private TaskGroupAdapter taskGroupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();
        taskDAO = new TaskDAO(context);
        categoryDAO = new CategoryDAO(context);
        taskTagsDAO = new TaskTagsDAO(context);

        // Lắng nghe kết quả từ dialog
        getParentFragmentManager().setFragmentResultListener("taskAdded", this, (requestKey, result) -> {
            boolean isTaskAdded = result.getBoolean("isTaskAdded", false);
            if (isTaskAdded) {
                updateRecyclerView();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar = view.findViewById(R.id.searchBar);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<TaskGroup> parentItemList = getTaskGroups();
        taskGroupAdapter = new TaskGroupAdapter(parentItemList, taskDAO, categoryDAO, taskTagsDAO, this::updateRecyclerView);
        recyclerView.setAdapter(taskGroupAdapter);

        handleSearch(searchBar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        List<TaskGroup> updatedTaskGroups = getTaskGroups();
        taskGroupAdapter.updateTaskGroups(updatedTaskGroups);
    }

    private void handleSearch(EditText searchBar) {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterTasksBySearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filterTasksBySearch(String keyword) {
        List<TaskGroup> originalTaskGroups = getTaskGroups();

        if (keyword == null || keyword.trim().isEmpty()) {
            updateRecyclerView();
            return;
        }

        List<TaskGroup> filteredTaskGroups = new ArrayList<>();
        for (TaskGroup group : originalTaskGroups) {
            List<Task> filteredTasks = new ArrayList<>();
            for (Task task : group.getTaskList()) {
                if (task.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        (task.getNote() != null && task.getNote().toLowerCase().contains(keyword.toLowerCase()))) {
                    filteredTasks.add(task);
                }
            }

            if (!filteredTasks.isEmpty()) {
                filteredTaskGroups.add(new TaskGroup(group.getTitle(), group.getColor(), group.getTextColor(), filteredTasks));
            }
        }

        TaskGroupAdapter adapter = (TaskGroupAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateTaskGroups(filteredTaskGroups);
        }
    }

    private List<TaskGroup> getTaskGroups() {
        List<TaskGroup> parentList = new ArrayList<>();
        List<Task> allTasks = taskDAO.findAll();

        List<Task> overdueTasks = new ArrayList<>();
        List<Task> noDeadlineTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();
        List<Task> todayTasks = new ArrayList<>();
        List<Task> tomorrowTasks = new ArrayList<>();
        List<Task> thisWeekTasks = new ArrayList<>();
        List<Task> thisMonthTasks = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate weekStart = today.with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());

        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
                continue;
            }

            if (task.getDueDate() == null) {
                noDeadlineTasks.add(task);
                continue;
            }

            LocalDate taskDueDate = Utils.convertToLocalDate(task.getDueDate());
            if (taskDueDate.isBefore(today)) {
                overdueTasks.add(task);
            } else if (taskDueDate.isEqual(today)) {
                todayTasks.add(task);
            } else if (taskDueDate.isEqual(tomorrow)) {
                tomorrowTasks.add(task);
            } else if (!taskDueDate.isBefore(today) && taskDueDate.isBefore(weekEnd.plusDays(1))) {
                thisWeekTasks.add(task);
            } else if (!taskDueDate.isBefore(weekEnd.plusDays(1)) && taskDueDate.isBefore(monthEnd.plusDays(1))) {
                thisMonthTasks.add(task);
            }
        }

        parentList.add(createTaskGroup("Quá hạn", R.color.overdue_color, R.color.white, overdueTasks));
        parentList.add(createTaskGroup("Không có hạn", R.color.no_deadline_color, R.color.white, noDeadlineTasks));
        parentList.add(createTaskGroup("Hôm nay", R.color.today_color, R.color.white, todayTasks));
        parentList.add(createTaskGroup("Đã hoàn thành", R.color.completed_color, R.color.black, completedTasks));
        parentList.add(createTaskGroup("Ngày mai", R.color.tomorrow_color, R.color.white, tomorrowTasks));
        parentList.add(createTaskGroup("Trong suốt tuần", R.color.this_week_color, R.color.white, thisWeekTasks));
        parentList.add(createTaskGroup("Tháng này", R.color.this_month_color, R.color.white, thisMonthTasks));
        parentList.add(createTaskGroup("Tất cả", R.color.all_tasks_color, R.color.white, allTasks));

        return parentList;
    }

    private TaskGroup createTaskGroup(String title, int bgColorRes, int textColorRes, List<Task> tasks) {
        Context context = requireContext();
        return new TaskGroup(
                title,
                ContextCompat.getColor(context, bgColorRes),
                ContextCompat.getColor(context, textColorRes),
                tasks
        );
    }

}