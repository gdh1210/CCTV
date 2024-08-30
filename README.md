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

---
08-24(금)

