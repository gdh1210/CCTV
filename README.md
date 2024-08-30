![Android Studio](https://img.shields.io/badge/android%20studio-346ac1?style=for-the-badge&logo=android%20studio&logoColor=white)<br>
# CCTV AIoT 프로젝트
---
08-22(목)

바탕화면 밋밋해서 그라데이션 파랑색 강조로 제작

```xml
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
```
    
색감 예시
<div align="center">
<img src="https://github.com/user-attachments/assets/04751f85-2e46-481f-b790-45b54a721435" width="400" height="400">
</div>

로그인 화면 제작 (ID 부분만 설정)

```java
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
```

로그인 버튼의 화면전환 기능의 필요

<div align="center">
<img src="https://github.com/user-attachments/assets/3e68a324-ec70-4050-92ec-cf7f57cf2dbb" width="400" height="700">
</div>
메인 연결 부분 작성

```java
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
```

버튼의 기능구현 필요

<div align="center">
<img src="https://github.com/user-attachments/assets/48eff80c-e72b-4759-bed4-2ce1bc00ac09" width="400" height="700">
</div>

시행착오 및 정리
* UI 부분을 제작하는데 디자인적 요소에 대한 많은 고민을 하여 적지않은 시간을 소모
* 그라데이션 적용 및 밝기 조절 글자 크기 획일화 가독성을 높이 는데 집중함 이후에 만들 xml은 이번에 만든 그라데이션 목록과 기능을 그대로 사용

목록
> bg_gradient
bg_gradient2
bg_gradient3
bt_cleanline
bt_gradient

문제점

* 버튼의 경우 신버전으로 넘어오면서 그냥 background에 그라데이션을 적용시 변하지않음

해결법

* < Button> </ Button> 대신 <androidx.appcompat.widget.AppCompatButton></> 사용

---
08-24(금)

로그인 부분에 미완성된 연결부분 완료

```java
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
```

메인부분도 일단 빈 레이아웃 생성 후 연결

```java
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
```

시행착오 및 정리

문제점

* 처음에 화면전환 부분을 setContentView(R.layout.레이아웃이름) 으로 작성했는데 로그인과 메인만 왔다갔다 하면 문제가 없지만 ControlCCTVActivity 
에서 되돌아 나올 때 앱이 정지하는 문제가 발생

해결법

* 문제 분석 결과 데이터를 전달 하는 과정에서 setContentView(R.layout.레이아웃이름) 을 사용해 전환하는 경우 데이터가 소실되어 java.lang.IllegalStateException 문제가 발견됨 Intent를 사용하여 액티비티를 유지하여 데이터 손실을 방지하며 전환간의 상호 데이터 전달이 용이하고 복잡한 UI의 전환 처리에 적합한 형태로 변경

CCTV 화면 연결부분 작성

```java
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
        } catch (IOException e) {
            e.printStackTrace();
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

    public void detectIntrusion(View view) {
        // 침입 감지 기능 (리눅스 서버 연동 필요)
        Toast.makeText(this, "침입 감지 기능을 활성화합니다.", Toast.LENGTH_SHORT).show();
    }

    public void back_to_main(View view) {
        finish();
        Intent intent = new Intent(ControlCCTVActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
```

<div align="center">
<img src="https://github.com/user-attachments/assets/74294fa2-4732-484e-9b06-edc293677e8c" width="400" height="700">
</div>
