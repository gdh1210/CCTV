<div align="center">
<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=350&&section=header&text=CCTV%20AIoT%20project&fontSize=90">
</div>

<div align="center">
<img src="https://img.shields.io/badge/android%20studio-346ac1?style=for-the-badge&logo=android%20studio&logoColor=white">
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
<img src="https://img.shields.io/badge/-Arduino-00979D?style=for-the-badge&logo=Arduino&logoColor=white">
</div>

# 최초의 프로젝트 계획안

AI 스마트 CCTV 시스템 프로젝트(8월22일 ~ 8월30일)
 1. 리모콘 기능(UDP 통신 : 서버(PC), 클라이언트(안드로이드)) : 1~2일
 2. 음성 인식 기능(SpeechToText : text -> 서버(PC, ChatGPT 파이썬 연동) : 2일
 3. 통화기능(SIP 프로토콜) : 2일
 4. webcam IP streaming (Java OpenCV 라이브러리 이용) : 1일

## 목차

1. [08-22(목)   UI/UX제작 로그인,메인메뉴 구성](#0822목)
   
2. [08-23(금)   CCTV연결 비트맵 영상처리 구현](#0823금)
   
3. [08-26(월)   UI/UX제작 조명,온도 조절장치 구성](#0826월)
   > 번외 UDP 서버구성 https://github.com/gdh1210/Server_Host

4. [08-27(화)   블루투스, UDP 서버 통신 연결 구현](#0827화)
   > 번외 아두이노 코딩 https://github.com/gdh1210/Arduino-subomotor-
   
5. [08-28(수)   ](#0828수)
   
6. [08-29(목)   ](#0829목)
    
7. [08-30(금)   ](#0830금)

---
### 08.22(목)

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

## LoginActivity.java
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
<img src="https://github.com/user-attachments/assets/3e68a324-ec70-4050-92ec-cf7f57cf2dbb" width="400" height="800">
</div>


## MainActivity.java
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
<img src="https://github.com/user-attachments/assets/48eff80c-e72b-4759-bed4-2ce1bc00ac09" width="400" height="800">
</div>

시행착오 및 정리
* UI 부분을 제작하는데 디자인적 요소에 대한 많은 고민을 하여 적지않은 시간을 소모
* 그라데이션 적용 및 밝기 조절 글자 크기 획일화 가독성을 높이 는데 집중함 이후에 만들 xml은 이번에 만든 그라데이션 목록과 기능을 그대로 사용

목록
> bg_gradient,
bg_gradient2,
bg_gradient3,
bt_cleanline,
bt_gradient

:x:문제점

* 버튼의 경우 신버전으로 넘어오면서 그냥 background에 그라데이션을 적용시 변하지않음

:o:해결법

* < Button> </ Button> 대신 <androidx.appcompat.widget.AppCompatButton></> 사용

---
### 08.23(금)

## LoginActivity.java
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

## MainActivity.java
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

 :x:문제점

* 처음에 화면전환 부분을 setContentView(R.layout.레이아웃이름) 으로 작성했는데 로그인과 메인만 왔다갔다 하면 문제가 없지만 ControlCCTVActivity 
에서 되돌아 나올 때 앱이 정지하는 문제가 발생

 :o:해결법

* 문제 분석 결과 데이터를 전달 하는 과정에서 setContentView(R.layout.레이아웃이름) 을 사용해 전환하는 경우 데이터가 소실되어 java.lang.IllegalStateException 문제가 발견됨 Intent를 사용하여 액티비티를 유지하여 데이터 손실을 방지하며 전환간의 상호 데이터 전달이 용이하고 복잡한 UI의 전환 처리에 적합한 형태로 변경

## ControlCCTVActivity
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
블루투스통신기능 구현필요

## MyHomeCCTV.java
다량의 스레드의 사용과 코드가 길어져서 복잡하기 때문에 영상 이미지 비트맵 처리부분을 분리하여 연결
```java
public class MyHomeCCTV extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread threadSView;
    private boolean threadRunning = true;
    private final SurfaceHolder holder;
    private Bitmap currentFrame;
    private String urlString;

    public Bitmap getCurrentFrame() {
        return currentFrame;
    }

    public MyHomeCCTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public void clearCurrentFrame() {
        if (currentFrame != null && !currentFrame.isRecycled()) {
            currentFrame.recycle();
            currentFrame = null;
        }
    }

    public void setUrl(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadRunning = true;
        if (threadSView == null || !threadSView.isAlive()) {
            threadSView = new Thread(this);
            threadSView.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface 크기나 포맷이 변경되었을 때 처리할 내용이 있다면 추가
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    public void stopThread() {
        threadRunning = false;
        if (threadSView != null && threadSView.isAlive()) {
            try {
                threadSView.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadSView = null;
    }

    @Override
    public void run() {
        final int maxImgSize = 1000000;
        byte[] arr = new byte[maxImgSize];
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());

            while (threadRunning) {
                int i = 0;
                while (i < 1000) {
                    int b = in.read();
                    if (b == 0xff) {
                        int b2 = in.read();
                        if (b2 == 0xd8) break;
                    }
                    i++;
                }
                if (i >= 1000) {
                    Log.e("MyHomeCCTV", "Bad head!");
                    continue;
                }

                arr[0] = (byte) 0xff;
                arr[1] = (byte) 0xd8;
                i = 2;
                while (i < maxImgSize) {
                    int b = in.read();
                    if (b == -1) break;
                    arr[i++] = (byte) b;
                    if (b == 0xff) {
                        int b2 = in.read();
                        if (b2 == -1) break;
                        arr[i++] = (byte) b2;
                        if (b2 == 0xd9) break;
                    }
                }

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr, 0, i);
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                byteArrayInputStream.close();

                if (bitmap != null) {
                    // SurfaceView 크기에 맞게 비트맵 조정
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        synchronized (holder) {
                            canvas.drawColor(Color.BLACK);
                            float scaleWidth = (float) getWidth() / bitmap.getWidth();
                            float scaleHeight = (float) getHeight() / bitmap.getHeight();
                            float scale = Math.min(scaleWidth, scaleHeight);

                            int scaledWidth = (int) (bitmap.getWidth() * scale);
                            int scaledHeight = (int) (bitmap.getHeight() * scale);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

                            canvas.drawBitmap(scaledBitmap, 0, 0, null);
                            scaledBitmap.recycle(); // scale bitmap을 해제합니다.
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }
                } else {
                    Log.e("MyHomeCCTV", "Failed to decode bitmap");
                }
            }
        } catch (Exception e) {
            Log.e("MyHomeCCTV", "Error: " + e.toString());
        }
    }

}
```

비트맵 이미지 처리부분은 이미 제공되어있는 오픈 소스를 활용하여 복사 후 세부사항 작성<br>
공용 IP 캠 주소 받아서 영상동작 테스트 결과 성공

<div align="center">
<img src="https://github.com/user-attachments/assets/74294fa2-4732-484e-9b06-edc293677e8c" width="400" height="800">
</div>

시행착오 및 정리

 :x:문제점

* 영상 통신과정에서 상당히 많은 문제들이 있었는데 몇가지 예를 들자면
1. 영상전환과 영상처리를 하나의 스레드에 넣었더니 과부하로 ANR 오류로 어플이 정지하기도 하고
2. 각 surfaceView 마다 스레드를 지정하고 초기화를 제대로 해주지 않아 스레드간 충돌로 강제 종료되기도 하고
3. 화면 전환시 스레드 종료 및 초기화 코드를 넣어주지 않아 한번 전환하면 다시 안켜지기도 하고
4. 그외에도 방화벽 보안 문제로 영상을 못받거나 url 주소가 특이하게 mjpeg 가 없는경우 비트맵을 읽어오지 못하기도 하고
     대부분이 통신이나 스레드 문제로 참 많은 오류들이 있었다

 :o:해결법

1. 스레드를 surfaceView 마다 새로 배정하고 영상처리는 분리하여 과부하를 방지하고
2. UI를 업데이트 하는 surfaceView는 백그라운드에서 작동하도록 runOnUiThread() 처리를 통해 메모리를 덜 사용하게 하였다
3. showSurfaceView 의 화면전환과 onDestroy 의 화면전환 시의 초기화 및 파괴 작업을 더 철저히 하고
4. 안드로이드의 보안 업데이트로 인해 https:// 로 보안이 되어있는 사이트가 아니면 접근을 막아버리기 때문에 허용해주는 xml을 따로 만들어 주었다
  
# neftwork_security_config.xml
도메인의 보안 허용

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">220.233.144.165</domain>
        <domain includeSubdomains="true">80.75.112.38</domain>
        <domain includeSubdomains="true">63.142.183.154</domain>
        <domain includeSubdomains="true">192.168.0.1</domain>
        <domain includeSubdomains="true">192.168.0.100</domain>
        <domain includeSubdomains="true">192.168.0.108</domain>
        <domain includeSubdomains="true">192.168.0.63</domain>
        <domain includeSubdomains="true">192.168.137.228</domain>
        <domain includeSubdomains="true">202.239.224.34</domain>
        <domain includeSubdomains="true">192.168.0.128</domain>
        <domain includeSubdomains="true">79.141.146.83</domain>
        <domain includeSubdomains="true">192.168.0.109</domain>
        <domain includeSubdomains="true">192.168.0.39</domain>
    </domain-config>
</network-security-config>
```
---
### 08.26(월)

## ControlLightActivity.java
조명 관리 부분 작성 (버튼 동작과 UI부분만 작성)

```java
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
        // 각 ToggleButton에 대해 ON/OFF 상태에 따른 작업 수행
        if (view.getId() == R.id.controllighttb_1) {
            if (isChecked) {
                tb1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        } else if (view.getId() == R.id.controllighttb_2) {
            if (isChecked) {
                tb2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        } else if (view.getId() == R.id.controllighttb_3) {
            if (isChecked) {
                tb3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        } else if (view.getId() == R.id.controllighttb_4) {
            if (isChecked) {
                tb4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        } else if (view.getId() == R.id.controllighttb_5) {
            if (isChecked) {
                tb5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        } else if (view.getId() == R.id.controllighttb_6) {
            if (isChecked) {
                tb6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_on, 0, 0);
            } else {
                tb6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.main_off, 0, 0);
            }
        }
    }

    public void back_to_main(View view) {
        Intent intent = new Intent(ControlLightActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
```
우선은 동작만 가능하도록 구현

<div align="center">
<img src="https://github.com/user-attachments/assets/e1e14b42-2833-4603-acff-b52ba472db9b" width="400" height="800">
</div>

## CheckTemperatureActivity.java
온도 설정 부분 작성(버튼 동작과 UI부분만 작성)

```java
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
```
마찬가지로 우선은 동작만 가능하도록 구현

<div align="center">
<img src="https://github.com/user-attachments/assets/bf9f39c4-1e1a-4c51-9bed-11e696c25d55" width="400" height="800">
</div>

시행착오 및 정리

* UI 제작부분이다 보니까 약간은 생소한 Seekbar 를 세로형태로 돌려서 만들때를 제외하곤 순조롭게 진행했던 것 같다.
* https://github.com/gdh1210/Server_Host 에서 추가로 서버를 제작

---
### 08.27(화)

# ControlCCTVActivity.java

저번의 미완성된 각종 통신기능을 구현 하였다
1. 블루투스 통신기능
2. UDP 서버로 명령전달
 
```java
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

    public void back_to_main(View view) {
        finish();
        Intent intent = new Intent(ControlCCTVActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
```
상하좌우 버튼을 클릭시 정상적으로 U,D,L,R 이 서버로 전송되는 것을 확인 할 수가 있다.

<div align="center">
<img src="https://github.com/user-attachments/assets/bc3e9691-c56b-4559-b2c9-f7da13dff0ed" width="400" height="800">
<img src="https://github.com/user-attachments/assets/76ca85ea-ed5c-438b-9a48-8ac7590cfbeb" width="400" height="600">
</div>

블루투스에서 아두이노로 직접 연결할 수 있는 기능을 구현 하였고<br>
인텔리 제이에서 열어놓은 UDP 서버와도 통신이 가능하도록 구현하였다.

시행착오 및 정리

:x:문제점
* 블루투스 기능을 구현하기 전 권한을 받아오거나 허용을 요청하는 사항에대한 예외 처리및 로직 구현이 변수가 많아서 작성하는데 꽤나 애먹었다.
* 같은 유선상의 서버통신인 에뮬레이터로는 성공하는 반면 안드로이드(핸드폰)에서 실행 하는 경우 서버에서 수신을 못하는 일이 발생하였다.

:o:해결법
* 블루투스를 앱실행시 권한여부를 확인하는 코드와 권한이 없을시 예외처리부분을 작성했고 매번 킬때 마다 허용해주지 않아도 알아서 권한이 들어가도록 설정했다.
* 컴퓨터의 인터넷 에서 (네트워크 및 인터넷 설정 - 속성(연결된 wifi) - 네트워크 프로필 - 공용) 으로 변경해주니 해결되었다.
* 통신이 되지 않는 정확한 원인은 알 수 없었으나 통신과 관련된 윈도우즈의 정책과 관련이 있지 않을까 추측된다.

# ControlLightActivity.java
조명 관리 부분 UDP 통신 가능하게 조정
```java
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
                        "192.168.0.128");
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
```
조명의 ON / OFF 또한 정상적으로 서버에 연결되어 문자를 전송하는것을 확인할 수 있었다.

<div align="center">
<img src="https://github.com/user-attachments/assets/d9b75e0b-96c5-4a9f-8831-cb76c5aa52ba" width="400" height="800">
<img src="https://github.com/user-attachments/assets/3b773154-4bb6-406d-8c63-47108812535c" width="400" height="600">
</div>

시행착오 및 정리

* 이미 서보모터 제어를 위해 서버 연결부분이 해결되어 있으므로 기능 구현 만 진행하면되서 딱히 어려운 부분은 없었다.
* 서버와 핸드폰은 연결시키는 과정에서 연결이 안되는 이유를 찾아다니는데 4시간을 넘게 쓴거 같다 따로 오류코드가 나오는 것도 아니고 실행이 멈추는 것도 아니다 보니 해결법을 차근차근 하나씩 찾아봐야만 해서 정말 많은 시간을 버렸다 해결법을 찾았지만 결국 그 원인조차도 제대로 알 수가 없어 추측만 할 뿐이다.

---
### 08.28(수) open ai서버 연결



---
### 08.29(목)
# ControlCCTVActivity.java

밑의 코드를 추가 하여 핸드폰에 내장된 음성 인식 기능을 활용하여 고정된 음성 명령을 서버로 전달 하게 만들었다.

```java
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
                showToast("알 수 없는 명령: " + command);
                return;
        }
        sendBluetoothCommand(btCommand);
        sendUdpMessage(btCommand);
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
```
시행착오 및 정리

<div align="center">
<img src="https://github.com/user-attachments/assets/0fe666ad-49e5-41c1-8c61-321c7ad8f3ed" width="400" height="800">
<img src="https://github.com/user-attachments/assets/3b773154-4bb6-406d-8c63-47108812535c" width="400" height="600">
</div>

---
### 08.30(금)  음성통화기능

# voice.java

오픈소스를 활용하여 필요한 부분만 짜집기 하여 코드를 가져와 사용하였다

```java
public class voice extends Activity
{
    public static String LOGTAG = "AJVoIP";
    EditText mParams = null;
    EditText mDestNumber = null;
    Button mBtnStart = null;
    Button mBtnCall = null;
    Button mBtnHangup = null;
    Button mBtnTest = null;
    TextView mStatus = null;
    TextView mNotifications = null;
    SipStack mysipclient = null;
    Context ctx = null;
    public static voice instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Message messageToMainThread = new Message();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);
        ctx = this;
        instance = this;

        mParams = (EditText) findViewById(R.id.parameters_view);
        mDestNumber = (EditText) findViewById(R.id.dest_number);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnHangup = (Button) findViewById(R.id.btn_hangup);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mStatus = (TextView) findViewById(R.id.status);
        mNotifications = (TextView) findViewById(R.id.notifications);
        mNotifications.setMovementMethod(new ScrollingMovementMethod());

        DisplayLogs("oncreate");

        StringBuilder parameters = new StringBuilder();

        parameters.append("loglevel=5\r\n");
        parameters.append("serveraddress=192.168.0.108\r\n");
        parameters.append("username=103\r\n");
        parameters.append("password=103\r\n");

        mParams.setText(parameters.toString());
        mDestNumber.setText(""); //default call-to number for our test (testivr3 is a music IVR access number on our test server at voip.mizu-voip.com)

        DisplayStatus("Ready.");

        mBtnStart.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)  //Start button click
            {
                DisplayLogs("Start on click");

                try{
                    // start SipStack if it's not already running
                    if (mysipclient == null) //check if AJVoIP instance already exists
                    {
                        DisplayLogs("Starting SIPStack");

                        //initialize the SIP engine
                        mysipclient = new SipStack();
                        mysipclient.Init(ctx);

                        //subscribe to notification events
                        MyNotificationListener listener = new MyNotificationListener();
                        mysipclient.SetNotificationListener(listener);

                        SetParameters(); //pass the configuration (parameters can be changed also later at run-time)

                        DisplayLogs("SIPStack Start");

                        //start the SIP engine
                        mysipclient.Start();
                        //mysipclient.Register();
                        instance.CheckPermissions();

                        DisplayLogs("SIPStack Started");
                    }
                    else
                    {
                        DisplayLogs("SIPStack already started");
                    }
                }catch (Exception e) { DisplayLogs("ERROR, StartSIPStack"); }
            }
        });

        mBtnCall.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Call button click
            {
                DisplayLogs("Call on click");

                String number = mDestNumber.getText().toString().trim();
                if (number == null || number.length() < 1)
                {
                    DisplayStatus("ERROR, Invalid destination number");
                    return;
                }

                if (mysipclient == null) {
                    DisplayStatus("ERROR, cannot initiate call because SipStack is not started");
                    return;
                }

                instance.CheckPermissions();

                if (mysipclient.Call(-1, number))
                {
                }
            }
        });

        mBtnHangup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Hangup button click
            {
                DisplayLogs("Hangup on click");

                if (mysipclient == null)
                    DisplayStatus("ERROR, cannot hangup because SipStack is not started");
                else
                    mysipclient.Hangup();
            }
        });

        mBtnTest.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Test button click
            {
                //any test code here
                DisplayLogs("Toogle loudspeaker");
                if (mysipclient == null)
                    DisplayStatus("ERROR, SipStack not started");
                else
                    mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
            }
        });
    }

    public void SetParameters()
    {
        String params = mParams.getText().toString();
        if (params == null || mysipclient == null) return;
        params = params.trim();

        DisplayLogs("SetParameters: " + params);

        mysipclient.SetParameters(params);
    }

    void CheckPermissions()
    {
        if (Build.VERSION.SDK_INT >= 23 && ctx.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            //we need RECORD_AUDIO permission before to make/receive any call
            DisplayStatus("Microphone permission required");
            ActivityCompat.requestPermissions(voice.this, new String[]{Manifest.permission.RECORD_AUDIO}, 555);
        }
    }

    class MyNotificationListener extends SIPNotificationListener
    {
        //here are some examples about how to handle the notifications:
        @Override
        public void onAll(SIPNotification e) {
            //we receive all notifications (also) here. we just print them from here
            DisplayLogs(e.getNotificationTypeText()+" notification received: " + e.toString());
        }

        //handle connection (REGISTER) state
        @Override
        public void onRegister( SIPNotification.Register e)
        {
            //check if/when we are registered to the SIP server
            if(!e.getIsMain()) return; //we ignore secondary accounts here

            switch(e.getStatus())
            {
                case SIPNotification.Register.STATUS_INPROGRESS: DisplayStatus("Registering..."); break;
                case SIPNotification.Register.STATUS_SUCCESS: DisplayStatus("Registered successfully."); break;
                case SIPNotification.Register.STATUS_FAILED: DisplayStatus("Register failed because "+e.getReason()); break;
                case SIPNotification.Register.STATUS_UNREGISTERED: DisplayStatus("Unregistered."); break;
            }
        }

        //an example for STATUS handling
        @Override
        public void onStatus( SIPNotification.Status e)
        {
            if(e.getLine() == -1) return; //we are ignoring the global state here (but you might check only the global state instead or look for the particular lines separately if you must handle multiple simultaneous calls)

            //log call state
            if(e.getStatus() >= SIPNotification.Status.STATUS_CALL_SETUP && e.getStatus() <= SIPNotification.Status.STATUS_CALL_FINISHED)
            {
                DisplayStatus("Call state is: "+ e.getStatusText());
            }

            //catch outgoing call connect
            if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_OUT)
            {
                DisplayStatus("Outgoing call connected to "+ e.getPeer());
            }
            //catch incoming calls
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_RINGING && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call from "+ e.getPeerDisplayname());

                //auto accepting the incoming call (instead of auto accept, you might present an Accept/Reject button for the user which will call Accept / Reject)
                mysipclient.Accept(e.getLine());
            }
            //catch incoming call connect
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call connected");
            }

        }

        //print important events (EVENT)
        @Override
        public void onEvent( SIPNotification.Event e)
        {
            DisplayStatus("Important event: "+e.getText());
        }

        //IM handling
        @Override
        public void onChat( SIPNotification.Chat e)
        {
            DisplayStatus("Message from "+e.getPeer()+": "+e.getMsg());

            //auto answer
            mysipclient.SendChat(-1, e.getPeer(),"Received");

        }
    }

    public void DisplayStatus(String stat)
    {
        try{
            if (stat == null) return;
            if (mStatus != null) {
                if ( stat.length() > 70)
                    mStatus.setText(stat.substring(0,60)+"...");
                else
                    mStatus.setText(stat);
            }
            DisplayLogs(stat);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayStatus", e); }
    }

    public void DisplayLogs(String logmsg)
    {
        try{
            if (logmsg == null || logmsg.length() < 1) return;

            if ( logmsg.length() > 2500) logmsg = logmsg.substring(0,300)+"...";
            logmsg = "["+ new java.text.SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime()) +  "] " + logmsg + "\r\n";

            Log.v(LOGTAG, logmsg);
            if (mNotifications != null) mNotifications.append(logmsg);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayLogs", e); }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        try{
            super.onDestroy();
            DisplayLogs("ondestroy");
            if (mysipclient != null)
            {
                DisplayLogs("Stop SipStack");
                mysipclient.Stop(true);
            }

            mysipclient = null;
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, on destroy", e); }
    }
}
```

---
# 마치며
사실 이 프로젝트는 완성이라기 보다는 미완성에 가깝다 기능적인 부분에 집중하느라 세세한 부분(로그인의 화이트리스트 관리, 환경설정) 에서 구현되지 않은 부분들이 많으며 <br>
컴퓨터 환경은 이리저리 바꿔가면서 많이 테스트 해봤는데 핸드폰 환경은 워낙 다양하다 보니 다른 핸드폰에 적용하면 UI 부분이 많이 틀어지는 것을 확인했지만 수정하지는 못했다. <br>

참조
서보모터 조립 참조 영상 - https://www.youtube.com/watch?v=cuxyNSaGRo <br>
자바 UDP 서버 생성법 - https://ddoriya.tistory.com/entry/JAVA-UDP-server-Client-%EB%A7%8C%EB%93%A4%EA%B8%B0 <br>
파이썬 UDP 서버 생성법 - https://w-world.tistory.com/217 <br>
OpenAI API를 활용하는법(chat gpt) - https://github.com/openai/openai-quickstart-python <br>
jSerialComm JAR 컴파일하여 사용하기 - https://www.xanthium.in/cross-platform-serial-port-programming-tutorial-java-jdk-arduino-embedded-system-tutorial <br>
자바와 아두이노 직렬통신 - https://philstories.medium.com/the-landscape-of-serial-communication-with-java-in-2021-com-ports-c5d9741aa263 <br>
자바와 아두이노 시리얼의 연결 - https://fazecast.github.io/jSerialComm/?source=post_page-----c5d9741aa263-------------------------------- <br>
안드로이드의 음성 텍스트 인식 - https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac <br>
음성통화 라이브러리 설명 - https://www.mizu-voip.com/Software/SIPSDK/AndroidSIPSDK.aspx <br>
음성통화 VOIP 서비스 구현 - https://blog.naver.com/romanst/220635666592 <br>
음성통화 호스트 - https://www.myvoipapp.com/ <br>
음성통화 클라이언트 활용2 - https://www.microsip.org/ <br>



