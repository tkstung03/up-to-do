package com.example.nhom10.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private TaskTagsDAO taskTagsDAO;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(requireContext());
        categoryDAO = new CategoryDAO(requireContext());
        taskTagsDAO = new TaskTagsDAO(requireContext());

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
        TaskGroupAdapter parentAdapter = new TaskGroupAdapter(parentItemList, taskDAO, categoryDAO, taskTagsDAO);
        recyclerView.setAdapter(parentAdapter);

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
        TaskGroupAdapter adapter = (TaskGroupAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateTaskGroups(updatedTaskGroups);
        }
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
                filteredTaskGroups.add(new TaskGroup(group.getTitle(), group.getColor(), filteredTasks));
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

        // Phân loại Task theo nhóm
        List<Task> todayTasks = new ArrayList<>();
        List<Task> tomorrowTasks = new ArrayList<>();
        List<Task> thisWeekTasks = new ArrayList<>();
        List<Task> thisMonthTasks = new ArrayList<>();

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        // Lấy ngày mai
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        // Lấy thời điểm đầu và cuối của tuần
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        Date weekEnd = calendar.getTime();

        // Lấy thời điểm đầu và cuối của tháng
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthEnd = calendar.getTime();

        for (Task task : allTasks) {
            Date taskDueDate = task.getDueDate();
            if (taskDueDate == null) continue;

            if (Utils.isSameDay(taskDueDate, today)) {
                todayTasks.add(task);
            } else if (Utils.isSameDay(taskDueDate, tomorrow)) {
                tomorrowTasks.add(task);
            } else if (taskDueDate.after(today) && taskDueDate.before(weekEnd)) {
                thisWeekTasks.add(task);
            } else if (taskDueDate.after(weekEnd) && taskDueDate.before(monthEnd)) {
                thisMonthTasks.add(task);
            }
        }

        // Tạo các TaskGroup
        parentList.add(new TaskGroup("Hôm nay", "#fcbb6d", todayTasks));
        parentList.add(new TaskGroup("Ngày mai", "#6ebcf4", tomorrowTasks));
        parentList.add(new TaskGroup("Trong suốt tuần", "#708ddb", thisWeekTasks));
        parentList.add(new TaskGroup("Tháng này", "#9471e8", thisMonthTasks));
        parentList.add(new TaskGroup("Tất cả", "#b75be2", allTasks));

        return parentList;
    }

}