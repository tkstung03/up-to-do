package com.example.nhom10.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nhom10.R;
import com.example.nhom10.dao.TasksDAO;
import com.example.nhom10.fragments.AboutFragment;
import com.example.nhom10.fragments.CalendarFragment;
import com.example.nhom10.fragments.CategoryFragment;
import com.example.nhom10.fragments.HomeFragment;
import com.example.nhom10.fragments.PersonalFragment;
import com.example.nhom10.fragments.SettingFragment;
import com.example.nhom10.fragments.ShareFragment;
import com.example.nhom10.model.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private TasksDAO tasksDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasksDAO = new TasksDAO(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                openFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.bottom_category) {
                openFragment(new CategoryFragment());
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                openFragment(new CalendarFragment());
                return true;
            } else if (itemId == R.id.bottom_profile) {
                openFragment(new PersonalFragment());
                return true;
            }
            return false;
        });
        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());

        fab.setOnClickListener(view -> showBottomDialog());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        EditText editTextName = dialog.findViewById(R.id.editName);
        EditText editTextNote = dialog.findViewById(R.id.editNote);
        LinearLayout categoryLayout = dialog.findViewById(R.id.layoutCategory);
        LinearLayout dateLayout = dialog.findViewById(R.id.layoutDate);
        LinearLayout timeLayout = dialog.findViewById(R.id.layoutTime);
        LinearLayout remindLayout = dialog.findViewById(R.id.layoutRemind);
        FloatingActionButton fabSave = dialog.findViewById(R.id.fabSave);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        final String[] selectedCategory = {""};
        final Calendar selectedDate = Calendar.getInstance();
        final Calendar selectedTime = Calendar.getInstance();

        categoryLayout.setOnClickListener(v -> {
            String[] categories = {"Work", "Personal", "Shopping", "Others"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn Category");
            builder.setItems(categories, (dialogInterface, which) -> {
                selectedCategory[0] = categories[which];
                Toast.makeText(this, "Đã chọn: " + selectedCategory[0], Toast.LENGTH_SHORT).show();
            });
            builder.show();
        });

        dateLayout.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                Toast.makeText(this, "Ngày đã chọn: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        timeLayout.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);
                Toast.makeText(this, "Thời gian đã chọn: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
            }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        remindLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Nhắc nhở sẽ được thiết lập sau khi lưu task", Toast.LENGTH_SHORT).show();
        });

        fabSave.setOnClickListener(view -> {
            String title = editTextName.getText().toString();
            String note = editTextNote.getText().toString();

            selectedDate.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
            selectedDate.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
            selectedDate.set(Calendar.SECOND, selectedTime.get(Calendar.SECOND));
            selectedDate.set(Calendar.MILLISECOND, selectedTime.get(Calendar.MILLISECOND));
            Date dueDate = selectedDate.getTime();

            Date createAt = new Date();
            Date updateAt = createAt;
            int categoryId = 1;

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                return;
            }

            Tasks task = new Tasks();
            task.setTitle(title);
            task.setNote(note);
            task.setDueDate(dueDate);
            task.setCreateAt(createAt);
            task.setUpdateAt(updateAt);

            boolean isInserted = tasksDAO.insertTask(task);

            if (isInserted) {
                Toast.makeText(this, "Task đã được lưu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi lưu task", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_settings) {
            openFragment(new SettingFragment());
        } else if (itemId == R.id.nav_share) {
            openFragment(new ShareFragment());
        } else if (itemId == R.id.nav_about) {
            openFragment(new AboutFragment());
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}