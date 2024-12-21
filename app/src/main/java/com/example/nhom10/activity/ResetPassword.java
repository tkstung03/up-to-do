package com.example.nhom10.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.utils.EmailSender;

import java.security.SecureRandom;

import javax.mail.MessagingException;


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

        sendEmail(email, newPassword);

        finish();
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

    private void sendEmail(String toEmail, String newPassword) {
        // Email credentials
        String fromEmail = "nhahanggame111@gmail.com";
        String appPassword = "jowmvpsxozjaoblc";

        // Nội dung email
        String subject = "Khôi phục mật khẩu";
        String messageBody = "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                "        .email-container { max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                "        .header { text-align: center; background: #4CAF50; color: white; padding: 10px 0; border-radius: 8px 8px 0 0; }" +
                "        .content { margin: 20px; font-size: 16px; color: #333; }" +
                "        .highlight { font-weight: bold; color: #4CAF50; }" +
                "        .footer { text-align: center; font-size: 12px; color: #888; margin-top: 20px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='email-container'>" +
                "        <div class='header'>" +
                "            <h2>Khôi phục mật khẩu</h2>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Chào bạn,</p>" +
                "            <p>Mật khẩu mới của bạn là:</p>" +
                "            <p class='highlight'>" + newPassword + "</p>" +
                "            <p>Vui lòng đăng nhập lại và thay đổi mật khẩu nếu cần.</p>" +
                "            <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>© 2023 Up todo. Mọi quyền được bảo lưu.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Gửi email
        new Thread(() -> {
            try {
                EmailSender emailSender = new EmailSender(fromEmail, appPassword);
                emailSender.sendEmail(toEmail, subject, messageBody);
                runOnUiThread(() -> Toast.makeText(this, "Mật khẩu mới đã được gửi tới email của bạn", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Không gửi được email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}