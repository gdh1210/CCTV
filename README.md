![Android Studio](https://img.shields.io/badge/android%20studio-346ac1?style=for-the-badge&logo=android%20studio&logoColor=white)
CCTV AIoT 프로젝트
---
08-22(목)

바탕화면 밋밋해서 그라데이션 파랑색 강조로 제작
    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <gradient
        android:angle="90"
        android:centerColor="#202020"
        android:endColor="#000000"
        android:startColor="#102040"
        android:type="linear" />
    </shape>

색감 예시
<img src="https://github.com/user-attachments/assets/04751f85-2e46-481f-b790-45b54a721435" width="400" height="400">

로그인 화면 제작 (ID 부분만 설정)

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
    
        if (username.isEmpty() || password.isEmpty())

            tv1.setText("아이디와 비밀번호를 입력하세요!");
        } else if (!username.equals("admin")) {
            tv1.setText("아이디가 일치하지 않습니다");
        } else {}
    }

로그인 버튼의 화면전환 기능의 필요
<img src="https://github.com/user-attachments/assets/3e68a324-ec70-4050-92ec-cf7f57cf2dbb" width="400" height="700">

메인 연결 부분 작성

    public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    //CCTV 센서
    public void navigateToCCTVControl(View view) {}

    //조명 센서
    public void controlLight(View view) {}

    //온도 센서
    public void checkTemperature(View view) {}

    //음성 통화
    public void VoiceTalk(View view) {}

    //환경 설정
    public void config(View view) {}

    //로그인으로 돌아가기
    public void back_to_login(View view) {}

버튼의 기능구현 필요
<img src="https://github.com/user-attachments/assets/48eff80c-e72b-4759-bed4-2ce1bc00ac09" width="400" height="700">

시행착오 및 정리
UI 부분을 제작하는데 디자인적 요소에 대한 많은 고민을 하여 적지않은 시간을 소모
그라데이션 적용 및 밝기 조절 글자 크기 획일화 가독성을 높이 는데 집중함 이후에 만들 xml은 이번에 만든 그라데이션 목록과 기능을 그대로 사용
목록
bg_gradient
bg_gradient2
bg_gradient3
bt_cleanline
bt_gradient

버튼의 경우 신버전으로 넘어오면서 그냥 background에 그라데이션을 적용시 변하지않음
<Button></Button> 대신 <androidx.appcompat.widget.AppCompatButton></> 사용

---
08-24(금)


    public class ControlCCTVActivity extends AppCompatActivity {
    private MyHomeCCTV surfaceView1;
    private MyHomeCCTV surfaceView2;
    private MyHomeCCTV surfaceView3;

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
