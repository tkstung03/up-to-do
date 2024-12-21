package com.example.nhom10.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;

import java.security.SecureRandom;


public class ResetPassword extends AppCompatActivity {

    EditText edtEmail;
    Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edtEmail = findViewById(R.id.edt_email_forgot_password);
        btnSendEmail = findViewById(R.id.btn_send_email);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ResetPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendPasswordResetEmail(email);
            }
        });
    }
    private void sendPasswordResetEmail(String email) {
        String newPassword = generateNewPassword();

        boolean emailSent = sendEmail(email, newPassword);

        if (emailSent) {
            Toast.makeText(ResetPassword.this, "Mật khẩu mới đã được gửi tới email của bạn", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(ResetPassword.this, "Không thể gửi email, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateNewPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int choice = random.nextInt(3);
            if (choice == 0) {
                password.append((char) ('A' + random.nextInt(26))); // Chữ hoa
            } else if (choice == 1) {
                password.append((char) ('a' + random.nextInt(26))); // Chữ thường
            } else {
                password.append((char) ('0' + random.nextInt(10))); // Số
            }
        }

        return password.toString();
    }

    private boolean sendEmail(String email, String newPassword) {
        return true;
    }
}