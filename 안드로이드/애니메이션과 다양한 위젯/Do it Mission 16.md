# 웹브라우저 화면 구성

* 주소를 입력하여 웹을 검색하는 웹브라우저 화면 만들기

* 화면의 위쪽에는 주소를 입력하는 패널이 있고, 이 패널에 주소를 입력한 후 [이동] 버튼을 누르면 사라지게 하여 결과 웹페이지만 보이도록 한다.

## 조건

* 웹페이지를 보여줄 수 있도록 웹브라우저를 포함하는 레이아웃을 구성한다.

* 레이아웃을 구성할 때의 화면의 위쪽에는 주소를 입력할 수 있는 입력 상자와 [이동] 버튼이 들어가도록 패널을 배치한다.

* 주소를 입력한 후 [이동] 버튼을 누르면 해당 페이지로 이동하여 웹페이지 화면을 보여주고 상단의 패널은 슬라이딩으로 사라지게 만든다.

* 상단의 패널을 다시 사용하고 싶을 때 패널을 볼 수 있도록 [패널 열기] 버튼을 배치한다. [패널 열기] 버튼을 누르면 상단의 패널이 슬라이딩으로 보이도록 한다.

* 주소를 입력하는 입력상자에는 URL을 입력할 수 있도록 설정하여 적당한 키패드를 띄우도록 한다.

## 참고

* 웹브라우저를 포함하기 위해서는 레이아웃에 ```<WebView>``` 태그를 사용한다.

* 웹사이트 주소를 입력받기 위한 입력상자에 키 입력 유형을 설정하여 적당한 키패드를 띄우려면 ```inputType``` 속성을 사용한다.

* 슬라이딩 패널을 만들때는 뷰의 가시성 속성과 애니메이션을 사용한다.

## 소스코드

### 애니메이션 리소스 XML

#### 이동 버튼 클릭 시 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/accelerate_decelerate_interpolator">
    <translate
        android:fromXDelta="0%p"
        android:toXDelta="-100%p"
        android:duration="1000"
        android:repeatCount="0"
        />

</set>
```

#### 패널 열기 버튼 클릭 시 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/accelerate_decelerate_interpolator">
    <translate
        android:fromXDelta="-100%p"
        android:toXDelta="0%p"
        android:duration="1000"
        android:repeatCount="0"
        />

</set>
```

### 메인 액티비티

#### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0"?>
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <FrameLayout
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:background="#ffffffff"
        android:layout_alignParentTop="true"
        android:id="@+id/searchLayout">

        <Button
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:id="@+id/backButton"
            android:visibility="gone"
            android:text="패널 열기"
            android:layout_gravity="center_horizontal"/>
        <RelativeLayout
            android:layout_height="wrap_content"
            android:layout_width="match_parent"
            android:background="#aaaaaaaa"
            android:id="@+id/urlLayout"
            android:padding="4dp">

            <EditText
                android:layout_height="wrap_content"
                android:layout_width="match_parent"
                android:id="@+id/urlInput"
                android:inputType="textUri"
                android:layout_toLeftOf="@+id/searchButton"/>

            <Button
                android:layout_height="wrap_content"
                android:layout_width="100dp"
                android:id="@+id/searchButton"
                android:text="이동"
                android:layout_alignBaseline="@+id/urlInput"
                android:layout_alignParentRight="true"/>

        </RelativeLayout>
    </FrameLayout>
    <WebView
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:id="@+id/webview"
        android:layout_below="@+id/searchLayout"/>
</RelativeLayout>
```

#### 메인 액티비티 소스 파일

```java
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    WebView webview;
    EditText urlInput;

    Button handleButton;
    RelativeLayout urlLayout;

    Animation translateUpsideAnim;
    Animation translateDownsideAnim;

    boolean isUrlInputOpen = false;

    boolean loadRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);

        WebSettings webSettings = webview.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished() called.");

                // start animation
                if (loadRequested) {
                    urlLayout.startAnimation(translateUpsideAnim);
                    loadRequested = false;
                }
            }
        });


        urlInput = findViewById(R.id.urlInput);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlStr = urlInput.getText().toString().trim();
                if (urlStr.length() < 1) {
                    Toast.makeText(getApplicationContext(), "사이트 주소를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!urlStr.startsWith("http://")) {
                    urlStr = "http://" + urlStr;
                    urlInput.setText(urlStr);
                }

                loadRequested = true;
                webview.loadUrl(urlStr);

            }
        });

        handleButton = findViewById(R.id.backButton);
        handleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleButton.startAnimation(translateUpsideAnim);
            }
        });

        urlLayout = findViewById(R.id.urlLayout);

        translateUpsideAnim = AnimationUtils.loadAnimation(this, R.anim.translate_pannel_back);
        translateDownsideAnim = AnimationUtils.loadAnimation(this, R.anim.translate_pannel);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateUpsideAnim.setAnimationListener(animListener);
        translateDownsideAnim.setAnimationListener(animListener);

        isUrlInputOpen = true;
    }

    private void showHandleButton() {
        handleButton.setVisibility(View.VISIBLE);
        urlLayout.setVisibility(View.GONE);

        isUrlInputOpen = false;
    }

    private void showUrlInput() {
        handleButton.setVisibility(View.GONE);
        urlLayout.setVisibility(View.VISIBLE);

        isUrlInputOpen = true;
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            if (isUrlInputOpen) {
                showHandleButton();
            } else {
                showUrlInput();
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission16">

    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:usesCleartextTraffic="true"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
