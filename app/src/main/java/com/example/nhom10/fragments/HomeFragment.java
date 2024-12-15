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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private TaskTagsDAO taskTagsDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(requireContext());
        categoryDAO = new CategoryDAO(requireContext());
        taskTagsDAO = new TaskTagsDAO(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar = view.findViewById(R.id.searchBar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<TaskGroup> parentItemList = getTaskGroups();
        TaskGroupAdapter parentAdapter = new TaskGroupAdapter(parentItemList, taskDAO, categoryDAO, taskTagsDAO);
        recyclerView.setAdapter(parentAdapter);

        handleSearch(searchBar);

        return view;
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
    }

    private List<TaskGroup> getTaskGroups() {
        List<TaskGroup> parentList = new ArrayList<>();
        List<Task> allTasks = taskDAO.getTasks();

        // Phân loại Task theo nhóm
        List<Task> todayTasks = new ArrayList<>();
        List<Task> tomorrowTasks = new ArrayList<>();
        List<Task> thisWeekTasks = new ArrayList<>();
        List<Task> thisMonthTasks = new ArrayList<>();
        List<Task> allTasksList = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        // Lấy ngày mai
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        // Lấy thời điểm đầu và cuối của tuần
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date weekStart = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        Date weekEnd = calendar.getTime();

        // Lấy thời điểm đầu và cuối của tháng
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthEnd = calendar.getTime();

        for (Task task : allTasks) {
            Date taskDueDate = task.getDueDate();
            if (taskDueDate == null) continue;

            if (isSameDay(taskDueDate, today)) {
                todayTasks.add(task);
            } else if (isSameDay(taskDueDate, tomorrow)) {
                tomorrowTasks.add(task);
            } else if (taskDueDate.after(today) && taskDueDate.before(weekEnd)) {
                thisWeekTasks.add(task);
            } else if (taskDueDate.after(weekEnd) && taskDueDate.before(monthEnd)) {
                thisMonthTasks.add(task);
            }
            allTasksList.add(task);
        }

        // Tạo các TaskGroup
        parentList.add(new TaskGroup("Hôm nay", "#fcbb6d", todayTasks));
        parentList.add(new TaskGroup("Ngày mai", "#6ebcf4", tomorrowTasks));
        parentList.add(new TaskGroup("Trong suốt tuần", "#708ddb", thisWeekTasks));
        parentList.add(new TaskGroup("Tháng này", "#9471e8", thisMonthTasks));
        parentList.add(new TaskGroup("Tất cả", "#b75be2", allTasksList));
        parentList.add(new TaskGroup("Đã hoàn thành", "#00bfae", completedTasks));

        return parentList;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}