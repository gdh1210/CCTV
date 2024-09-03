package com.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

public class ControlCCTVActivity extends AppCompatActivity {
    private MyHomeCCTV surfaceView1, surfaceView2, surfaceView3;
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

        initializeSurfaceViews();
        showSurfaceView(1);

        checkAndRequestBluetoothPermission();
    }

    private void initializeSurfaceViews() {
        surfaceView1 = findViewById(R.id.cctvView1);
        surfaceView2 = findViewById(R.id.cctvView2);
        surfaceView3 = findViewById(R.id.cctvView3);

        surfaceView1.setUrl("http://63.142.183.154:6103/mjpg/video.mjpg");
        surfaceView2.setUrl("http://63.142.183.154:6103/mjpg/video.mjpg");
        surfaceView3.setUrl("http://79.141.146.83/axis-cgi/mjpg/video.cgi");
    }

    private void checkAndRequestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
        } else {
            initializeBluetooth();
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            showToast("Bluetooth를 지원하지 않는 기기입니다.");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToBluetoothDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeBluetooth();
            } else {
                showToast("Bluetooth 연결을 위해 권한이 필요합니다.");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void connectToBluetoothDevice() {
        new Thread(() -> {
            try {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                runOnUiThread(() -> showToast("Bluetooth 연결 성공"));
            } catch (IOException | SecurityException e) {
                Log.e("Bluetooth", "Bluetooth 연결 실패", e);
                runOnUiThread(() -> showToast("Bluetooth 연결 실패"));
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            connectToBluetoothDevice();
        } else if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK) {
            processVoiceCommand(data);
        }
    }

    private void processVoiceCommand(Intent data) {
        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (matches != null && !matches.isEmpty()) {
            handleVoiceCommand(matches.get(0));
        } else {
            showToast("음성 인식 결과가 없습니다.");
        }
    }

    private void handleVoiceCommand(String command) {
        String btCommand;
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
                btCommand = command; // 선택지에 없는 입력은 그대로 사용
                break;
        }
        sendUdpMessage(btCommand);
    }

    private void sendBluetoothCommand(String command) {
        if (outputStream != null && bluetoothSocket != null && bluetoothSocket.isConnected()) {
            new Thread(() -> {
                try {
                    outputStream.write(command.getBytes());
                    runOnUiThread(() -> showToast("명령 전송 성공: " + command));
                } catch (IOException e) {
                    Log.e("Bluetooth", "명령 전송 실패: " + command, e);
                    runOnUiThread(() -> showToast("명령 전송 실패"));
                }
            }).start();
        } else {
            Log.e("Bluetooth", "Bluetooth가 연결되지 않았습니다.");
        }
    }

    public void showSurfaceView(int viewNumber) {
        runOnUiThread(() -> {
            surfaceView1.setVisibility(View.GONE);
            surfaceView2.setVisibility(View.GONE);
            surfaceView3.setVisibility(View.GONE);

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

        clearSurfaceViewFrames();
    }

    private void clearSurfaceViewFrames() {
        if (surfaceView1 != null) {
            surfaceView1.stopThread();
            surfaceView1.clearCurrentFrame();
        }
        if (surfaceView2 != null) {
            surfaceView2.stopThread();
            surfaceView2.clearCurrentFrame();
        }
        if (surfaceView3 != null) {
            surfaceView3.stopThread();
            surfaceView3.clearCurrentFrame();
        }
    }

    public void showCCTVSelection(View view) {
        final String[] cctvOptions = {"CAM1", "CAM2", "CAM3"};

        new AlertDialog.Builder(this)
                .setTitle("CCTV 선택")
                .setItems(cctvOptions, (dialog, which) -> {
                    showSurfaceView(which + 1);
                    showToast(cctvOptions[which] + "로 전환합니다.");
                })
                .show();
    }

    public void controlCCTVMovement(View view) {
        String command = "";
        int id = view.getId();

        if (id == R.id.UPbt) {
            command = "U";
        } else if (id == R.id.DOWNbt) {
            command = "D";
        } else if (id == R.id.LEFTbt) {
            command = "L";
        } else if (id == R.id.RIGHTbt) {
            command = "R";
        }

        sendBluetoothCommand(command);
        sendUdpMessage(command);
    }

    private void sendUdpMessage(String msgText) {
        new Thread(() -> {
            try (DatagramSocket ds = new DatagramSocket()) {
                InetAddress ia = InetAddress.getByName("192.168.0.34");
                // 명시적으로 UTF-8로 인코딩
                byte[] sendData = msgText.getBytes("UTF-8");
                DatagramPacket dp = new DatagramPacket(sendData, sendData.length, ia, 9999);
                ds.send(dp);
            } catch (Exception e) {
                Log.e("UDPClient", "Exception: " + e.getMessage(), e);
            }
        }).start();
    }

    public void detectIntrusion(View view) {
        showToast("침입 감지 기능을 활성화합니다.");
    }

    public void communicate(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "명령어를 말하세요");
        try {
            startActivityForResult(intent, VOICE_REQUEST_CODE);
        } catch (Exception e) {
            showToast("음성 인식에 실패했습니다.");
        }
    }

    public void back_to_main(View view) {
        finish();
        startActivity(new Intent(ControlCCTVActivity.this, MainActivity.class));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSurfaceViewFrames();

        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            Log.e("Bluetooth", "Error closing resources", e);
        }
    }
}
