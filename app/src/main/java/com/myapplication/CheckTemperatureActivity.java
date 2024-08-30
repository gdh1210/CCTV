package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

public class CheckTemperatureActivity extends AppCompatActivity {

    private TextView currentTemperatureTextView;
    private TextView targetTemperatureTextView;
    private SeekBar temperatureSeekBar;
    private Button increaseButton;
    private Button applyButton;
    private Button decreaseButton;
    private static final int MIN_TEMPERATURE = 16; // 설정할 최소 온도 값
    private static final int MAX_TEMPERATURE = 40; // 설정할 최대 온도 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controltemp);

        // UI 요소 초기화
        currentTemperatureTextView = findViewById(R.id.temptv_2);
        targetTemperatureTextView = findViewById(R.id.temptv_4);
        temperatureSeekBar = findViewById(R.id.tempbar_1);
        increaseButton = findViewById(R.id.tempbt_1);
        applyButton = findViewById(R.id.tempbt_2);
        decreaseButton = findViewById(R.id.tempbt_3);

        // SeekBar 설정
        temperatureSeekBar.setMax(MAX_TEMPERATURE); // 최대값 설정
        temperatureSeekBar.setProgress(28); // 기본값 설정
        targetTemperatureTextView.setText(String.valueOf(28));

        // SeekBar 변경에 따른 텍스트 업데이트
        temperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 최소값을 확인하고 최소값보다 작으면 최소값으로 설정
                int adjustedProgress = Math.max(progress, MIN_TEMPERATURE);
                seekBar.setProgress(adjustedProgress);
                targetTemperatureTextView.setText(String.valueOf(adjustedProgress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 터치 시작 시 처리할 작업이 있으면 여기에 작성
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 터치 중단 시 처리할 작업이 있으면 여기에 작성
            }
        });

        // 증가 버튼 클릭 이벤트 처리
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = temperatureSeekBar.getProgress();
                if (currentProgress < MAX_TEMPERATURE) {
                    temperatureSeekBar.setProgress(currentProgress + 1);
                    targetTemperatureTextView.setText(String.valueOf(currentProgress + 1));
                }
            }
        });

        // 적용 버튼 클릭 이벤트 처리
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetTemperature = targetTemperatureTextView.getText().toString();
                currentTemperatureTextView.setText(targetTemperature);
            }
        });

        // 감소 버튼 클릭 이벤트 처리
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = temperatureSeekBar.getProgress();
                if (currentProgress > MIN_TEMPERATURE) {
                    temperatureSeekBar.setProgress(currentProgress - 1);
                    targetTemperatureTextView.setText(String.valueOf(currentProgress - 1));
                }
            }
        });
    }

    public void back_to_main(View view) {
        Intent intent = new Intent(CheckTemperatureActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

