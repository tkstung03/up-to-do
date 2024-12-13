package com.example.nhom10.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nhom10.R;
import com.example.nhom10.fragments.AboutFragment;
import com.example.nhom10.fragments.CalendarFragment;
import com.example.nhom10.fragments.CategoryFragment;
import com.example.nhom10.fragments.HomeFragment;
import com.example.nhom10.fragments.PersonalFragment;
import com.example.nhom10.fragments.SettingFragment;
import com.example.nhom10.fragments.ShareFragment;
import com.example.nhom10.fragments.TaskBottomDialogFragment;
import com.example.nhom10.objects.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
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

        floatingActionButton.setOnClickListener(view -> {
            // Khi nhấn FAB trong CategoryFragment, mở hộp thoại thêm danh mục
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof CategoryFragment) {
                // Gọi hàm mở hộp thoại thêm danh mục trong CategoryFragment
                ((CategoryFragment) currentFragment).showAddCategoryDialog();
            } else {
                TaskBottomDialogFragment bottomDialogFragment = TaskBottomDialogFragment.newInstance();
                bottomDialogFragment.show(getSupportFragmentManager(), "TaskBottomDialogFragment");
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("is_logged_in");
            editor.remove("user_id");
            editor.apply();
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            UserSession userSession = UserSession.getInstance();
            userSession.clearSession();
            Intent intent = new Intent(MainActivity.this, Welcome.class);
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

    public void logOut() {
        UserSession userSession = UserSession.getInstance();
        userSession.clearSession();
        Intent intent = new Intent(MainActivity.this, Welcome.class);
        startActivity(intent);
        finish();

    }

    private void createNotificationChannel() {
        CharSequence name = "ReminderChannel";
        String description = "Kênh cho nhắc nhở nhiệm vụ";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("REMINDER_CHANNEL", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

}