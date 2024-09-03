package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    //CCTV 센서
    public void navigateToCCTVControl(View view) {
        Intent intent = new Intent(MainActivity.this, ControlCCTVActivity.class);
        startActivity(intent);
    }

    //조명 센서
    public void controlLight(View view) {
        Intent intent = new Intent(MainActivity.this, ControlLightActivity.class);
        startActivity(intent);
    }

    //온도 센서
    public void checkTemperature(View view) {
        Intent intent = new Intent(MainActivity.this, CheckTemperatureActivity.class);
        startActivity(intent);
    }

    //음성 통화
    public void VoiceTalk(View view) {
        Intent intent = new Intent(MainActivity.this, voice.class);
        startActivity(intent);
    }

    //로그인으로 돌아가기
    public void back_to_login(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
