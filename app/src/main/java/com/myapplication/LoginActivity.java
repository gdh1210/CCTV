package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText et1, et2;
    private TextView tv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et1 = findViewById(R.id.et_username);
        et2 = findViewById(R.id.et_password);
        tv1 = findViewById(R.id.tv_error);
    }

    public void login(View v) {
        String username = et1.getText().toString();
        String password = et2.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            tv1.setText("아이디와 비밀번호를 입력하세요!");
        } else if (!username.equals("admin")) {
            tv1.setText("아이디가 일치하지 않습니다");
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
