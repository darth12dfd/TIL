# 웹으로 가져온 데이터 보여주기

* 웹으로 가져온 데이터 원본과 함께 웹뷰에서 해당 페이지도 같이볼 수 있도록 한다.

* HTML 문서를 웹뷰에서 보면 웹브라우저에서 보는 것처럼 표시된다.

## 조건

* 웹사이트 주소를 입력할 수 있는 입력상자와 버튼을 화면 상단에 배치한다.

* [요청] 버튼을 누르면 웹으로 데이터를 가져온다.

* 화면의 가운데 부분에는 가져온 데이터를 보여주도록 한다.

* 화면의 아래쪽에는 웹뷰를 추가하고 웹으로 가져온 데이터를 웹뷰에 넣어 보여준다.

## 참고

* 웹사이트에서 가져와야 할 페이지나 이미지는 여러 개일 수 있다. 따라서 웨뷰에서 표시되는 웹 문서는 완벽하지 않을 수 있다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="horizontal"
        android:padding="5dp">

        <EditText
            android:id="@+id/editText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:ems="10"
            android:hint="사이트 주소 입력"
            android:inputType="textPersonName" />

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="요청" />

    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:orientation="vertical"
        android:padding="5dp">

        <TextView
            android:id="@+id/textView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="데이터 원본"
            android:padding="5dp"
            android:textSize="18sp" />

        <WebView
            android:id="@+id/webView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </LinearLayout>
</LinearLayout>
```

### 메인 액티비티 소스파일

```java
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();//스레드에서 UI 접근하기 위한 핸들러

    TextView textView;//데이터 원본 출력할 변수
    EditText editText;//사이트 주소 입력

    WebView webView;//웹페이지 출력

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        //웹뷰 설정 수정
        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);



        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = editText.getText().toString().trim();

                RequestThread thread = new RequestThread(urlStr);
                thread.start();
            }
        });
    }

    class RequestThread extends Thread {
        String urlStr;

        public RequestThread(String str) {
            urlStr = str;
        }

        public void run() {
            try {
                final String output = request(urlStr);
                handler.post(new Runnable() {
                    public void run() {
                        textView.setText(output);
                        webView.loadData(output, "text/html; charset=utf-8", "utf-8");
                    }
                });

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        //웹서버에 요청하기
        private String request(String urlStr) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    boolean redirect = false;

                    int status = conn.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
                            redirect = true;
                    }

                    System.out.println("Response Code ... " + status);

                    if (redirect) {

                        String newUrl = conn.getHeaderField("Location");
                        String cookies = conn.getHeaderField("Set-Cookie");

                        conn = (HttpURLConnection) new URL(newUrl).openConnection();
                        conn.setRequestProperty("Cookie", cookies);

                        System.out.println("Redirect to URL : " + newUrl);
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                }
            } catch(Exception ex) {
                Log.e("MainActivity", "Exception in processing response.", ex);
                ex.printStackTrace();
            }

            return output.toString();
        }

    }


}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission19">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:usesCleartextTraffic="true"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoItMission19">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
