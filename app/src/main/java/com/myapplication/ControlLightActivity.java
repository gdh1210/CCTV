package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ControlLightActivity extends AppCompatActivity {

    // ToggleButton 객체 선언
    private ToggleButton tb1, tb2, tb3, tb4, tb5, tb6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controllight);

        // ToggleButton 객체 초기화
        tb1 = findViewById(R.id.controllighttb_1);
        tb2 = findViewById(R.id.controllighttb_2);
        tb3 = findViewById(R.id.controllighttb_3);
        tb4 = findViewById(R.id.controllighttb_4);
        tb5 = findViewById(R.id.controllighttb_5);
        tb6 = findViewById(R.id.controllighttb_6);

        // ToggleButton 클릭 리스너 설정
        tb1.setOnClickListener(this::onToggleClicked);
        tb2.setOnClickListener(this::onToggleClicked);
        tb3.setOnClickListener(this::onToggleClicked);
        tb4.setOnClickListener(this::onToggleClicked);
        tb5.setOnClickListener(this::onToggleClicked);
        tb6.setOnClickListener(this::onToggleClicked);
    }

    // ToggleButton 클릭 이벤트 처리
    public void onToggleClicked(View view) {
        boolean isChecked = ((ToggleButton) view).isChecked();
        String command = "";
        // 각 ToggleButton에 대해 ON/OFF 상태에 따른 작업 수행
        if (view.getId() == R.id.controllighttb_1) {
            if (isChecked) {
                tb1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "a";
            } else {
                tb1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "b";
            }
        } else if (view.getId() == R.id.controllighttb_2) {
            if (isChecked) {
                tb2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "c";
            } else {
                tb2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "d";
            }
        } else if (view.getId() == R.id.controllighttb_3) {
            if (isChecked) {
                tb3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "e";
            } else {
                tb3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "f";
            }
        } else if (view.getId() == R.id.controllighttb_4) {
            if (isChecked) {
                tb4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "g";
            } else {
                tb4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "h";
            }
        } else if (view.getId() == R.id.controllighttb_5) {
            if (isChecked) {
                tb5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "i";
            } else {
                tb5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "j";
            }
        } else if (view.getId() == R.id.controllighttb_6) {
            if (isChecked) {
                tb6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
                command = "k";
            } else {
                tb6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
                command = "l";
            }
        }
        Log.d("UDPClient", "Send: " + command);
        sendMsg2(command);
    }

    public void sendMsg2(String msgText) { new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                DatagramSocket ds = new DatagramSocket();
                InetAddress ia = InetAddress.getByName(
                        "192.168.0.39");
                DatagramPacket dp=new DatagramPacket(
                        msgText.getBytes(),
                        msgText.getBytes().length,
                        ia, 7777);
                ds.send(dp);
                ds.close();
            } catch (Exception e) {
                Log.d("UDPClient", "Exception: " + e.getMessage());
            }
        }
    }).start();
    }

    public void back_to_main(View view) {
        Intent intent = new Intent(ControlLightActivity.this, MainActivity.class);
        startActivity(intent);
    }
}