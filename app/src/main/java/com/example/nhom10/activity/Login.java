package com.example.nhom10.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.dao.UserDAO;
import com.example.nhom10.objects.UserSession;

public class Login extends AppCompatActivity {

    TextView register;
    Button btnLogin;
    EditText edtUsername, edtPassword;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Login.this, Register.class);
                startActivity(intent2);
            }
        });
    }

    public void getWidget() {
        register = findViewById(R.id.register_prompt);
        btnLogin = findViewById(R.id.btn_login);
        edtUsername = findViewById(R.id.txt_username);
        edtPassword = findViewById(R.id.txt_password);
        userDAO = new UserDAO(this);
    }

    public void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(Login.this, "Vui lòng nhập tài khoản, mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean checkUser = userDAO.checkUser(username, password);
        if (checkUser) {
            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            int user_id = userDAO.getUserId(username, password);
            if (user_id == 0) {
                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putInt("user_id", user_id);
                editor.apply();

                UserSession userSession = UserSession.getInstance();
                userSession.setUserId(user_id);
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(Login.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }



}