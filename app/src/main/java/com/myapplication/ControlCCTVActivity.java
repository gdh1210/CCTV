package com.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.Manifest;
import androidx.annotation.NonNull;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ControlCCTVActivity extends AppCompatActivity {
    private MyHomeCCTV surfaceView1;
    private MyHomeCCTV surfaceView2;
    private MyHomeCCTV surfaceView3;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    private static final int REQUEST_ENABLE_BT = 2;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String DEVICE_ADDRESS = "98:DA:60:07:6C:91";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int VOICE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlcctv);

        // SurfaceView 초기화
        surfaceView1 = findViewById(R.id.cctvView1);
        surfaceView2 = findViewById(R.id.cctvView2);
        surfaceView3 = findViewById(R.id.cctvView3);

        surfaceView1.setUrl("http://192.168.0.128:8080");
        surfaceView2.setUrl("http://63.142.183.154:6103/mjpg/video.mjpg");
        surfaceView3.setUrl("http://79.141.146.83/axis-cgi/mjpg/video.cgi");

        showSurfaceView(1);

        // 권한 체크 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSION);
        } else {
            // 권한이 이미 부여되었으면 Bluetooth 초기화
            initializeBluetooth();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 Bluetooth 초기화
                initializeBluetooth();
            } else {
                // 권한이 거부되면 사용자에게 알림
                Toast.makeText(this, "Bluetooth 연결을 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth가 꺼져있는 경우, 사용자가 Bluetooth를 켜도록 유도
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            // Bluetooth 연결을 백그라운드 스레드에서 처리
            new Thread(this::connectToBluetoothDevice).start();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                connectToBluetoothDevice();
            } else {
                Toast.makeText(this, "Bluetooth 활성화가 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String command = matches.get(0);
                handleVoiceCommand(command);
            } else {
                Toast.makeText(this, "음성 인식 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void connectToBluetoothDevice() {
        new Thread(() -> {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                runOnUiThread(() -> Toast.makeText(this, "Bluetooth 연결 성공", Toast.LENGTH_SHORT).show());
            } catch (IOException | SecurityException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Bluetooth 연결 실패", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // SurfaceView의 스레드를 안전하게 종료
        if (surfaceView1 != null) {
            surfaceView1.stopThread();
            surfaceView1.clearCurrentFrame();  // 비트맵 메모리 해제
        }
        if (surfaceView2 != null) {
            surfaceView2.stopThread();
            surfaceView2.clearCurrentFrame();  // 비트맵 메모리 해제
        }
        if (surfaceView3 != null) {
            surfaceView3.stopThread();
            surfaceView3.clearCurrentFrame();  // 비트맵 메모리 해제
        }

        // Bluetooth 리소스 정리
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Bluetooth를 통해 아두이노로 명령 전송
    private void sendBluetoothCommand(String command) {
        if (outputStream != null && bluetoothSocket != null && bluetoothSocket.isConnected()) {
            new Thread(() -> {
                try {
                    outputStream.write(command.getBytes());
                    runOnUiThread(() -> Toast.makeText(ControlCCTVActivity.this, "명령 전송 성공: " + command, Toast.LENGTH_SHORT).show());
                    Log.d("Bluetooth", "명령 전송 성공: " + command); // 로그 추가
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ControlCCTVActivity.this, "명령 전송 실패", Toast.LENGTH_SHORT).show());
                    Log.e("Bluetooth", "명령 전송 실패: " + command, e); // 로그 추가
                }
            }).start();
        } else {
            runOnUiThread(() -> Log.e("Bluetooth", "Bluetooth가 연결되지 않았습니다.")); // 로그 추가
        }
    }
    // SurfaceView 간 전환을 통해 CCTV 화면을 바꾸는 메서드
    public void showSurfaceView(int viewNumber) {
        runOnUiThread(() -> {
            surfaceView1.setVisibility(View.GONE);
            surfaceView2.setVisibility(View.GONE);
            surfaceView3.setVisibility(View.GONE);
        });
        // SurfaceView 스레드를 안전하게 종료
        if (surfaceView1 != null) {
            surfaceView1.stopThread();
            surfaceView1.clearCurrentFrame();  // 비트맵 메모리 해제
        }
        if (surfaceView2 != null) {
            surfaceView2.stopThread();
            surfaceView2.clearCurrentFrame();  // 비트맵 메모리 해제
        }
        if (surfaceView3 != null) {
            surfaceView3.stopThread();
            surfaceView3.clearCurrentFrame();  // 비트맵 메모리 해제
        }
        runOnUiThread(() -> {
            switch (viewNumber) {
                case 1:
                    surfaceView1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    surfaceView2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    surfaceView3.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    public void showCCTVSelection(View view) {
        final String[] cctvOptions = {"CAM1", "CAM2", "CAM3"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CCTV 선택");

        builder.setItems(cctvOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    showSurfaceView(1);
                    Toast.makeText(this, "CAM1로 전환합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    showSurfaceView(2);
                    Toast.makeText(this, "CAM2로 전환합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    showSurfaceView(3);
                    Toast.makeText(this, "CAM3로 전환합니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        builder.show();
    }

    public void controlCCTVMovement(View view) {
        String command = "";
        if (view.getId() == R.id.UPbt) {
            command = "U";
        } else if (view.getId() == R.id.DOWNbt) {
            command = "D";
        } else if (view.getId() == R.id.LEFTbt) {
            command = "L";
        } else if (view.getId() == R.id.RIGHTbt) {
            command = "R";
        }
        sendBluetoothCommand(command);
        Log.d("UDPClient", "Send: " + command);
        sendMsg(command);
    }

    private void handleVoiceCommand(String command) {
        String btCommand = "";
        switch (command) {
            case "위로":
                btCommand = "U";
                break;
            case "아래로":
                btCommand = "D";
                break;
            case "왼쪽":
                btCommand = "L";
                break;
            case "오른쪽":
                btCommand = "R";
                break;
            default:
                Toast.makeText(this, "알 수 없는 명령: " + command, Toast.LENGTH_SHORT).show();
                return;
        }
        sendBluetoothCommand(btCommand);
        Log.d("UDPClient", "Send: " + btCommand);
        sendMsg(btCommand);
    }

    public void sendMsg(String msgText) {
        new Thread(() -> {
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket();
                InetAddress ia = InetAddress.getByName("192.168.0.128");
                DatagramPacket dp = new DatagramPacket(msgText.getBytes(), msgText.getBytes().length, ia, 7777);
                ds.send(dp);
            } catch (Exception e) {
                Log.e("UDPClient", "Exception: " + e.getMessage(), e);
            } finally {
                if (ds != null && !ds.isClosed()) {
                    ds.close();
                }
            }
        }).start();
    }

    public void detectIntrusion(View view) {
        // 침입 감지 기능 (리눅스 서버 연동 필요)
        Toast.makeText(this, "침입 감지 기능을 활성화합니다.", Toast.LENGTH_SHORT).show();
    }

    public void communicate(View view) {
        // 음성 인식 인텐트 호출
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "명령어를 말하세요");
        try {
            startActivityForResult(intent, VOICE_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "음성 인식에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void back_to_main(View view) {
        finish();
        Intent intent = new Intent(ControlCCTVActivity.this, MainActivity.class);
        startActivity(intent);
    }
}