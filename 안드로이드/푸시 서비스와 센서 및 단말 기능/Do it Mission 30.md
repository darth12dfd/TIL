# 공지 사항 수신 팝업 만들기

* 푸시로 보내온 공지사항(Do it Mission 29)을 받았을 때 화면에 팝업으로 보여주는 기능을 만들기

* 팝업은 액티비티를 사용해 만든 후 서비스에서 띄울 수 있다.

## 조건

* 팝업 화면은 액티비티로 만들고 대화상자 모양으로 보이도록 테마를 설정한다.

* 공지사항을 푸시로 수신하면 서비스에서 팝업 화면을 띄워준다.

* 팝업 화면의 위쪽에는 수신 시각을 보여준다.

* 가운데 부분에는 수신한 공지 내용을 보여준다.

* 아래쪽 부분에 있는 [확인] 버튼을 누르면 팝업이 사라지도록 한다.

## 참고

* 푸시 메시지는 서비스에서 수신하게 된다. 즉, 사용자에게 보여주려면 액티비티를 만들어 띄우거나 WindowManager를 사용해 띄워줄 수 있는 플로팅 화면으로 보여주어야 한다.

## 소스코드

### 메인 액티비티 XML 레이아웃(팝업창)

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <TextView
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:textColor="#000000"
        android:textSize="16sp"
        android:text="10시 20분"
        android:layout_alignParentTop="true"
        android:layout_alignParentRight="true"
        android:layout_margin="10dp"
        android:id="@+id/textView"/>

    <EditText
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:layout_margin="8dp"
        android:id="@+id/editText"
        android:layout_above="@+id/button"
        android:layout_below="@+id/textView"
        android:inputType="text"
        android:hint="내용"
        android:gravity="top|left"
        android:ems="10"/>

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:text="확인"
        android:layout_margin="10dp"
        android:id="@+id/button"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"/>

</RelativeLayout>
```

### 파이어베이스 메시징 서비스 클래스

```java
package org.techtown.mission30;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG="FMS";

    private static SimpleDateFormat format = new SimpleDateFormat("HH시 mm분");

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onNewToken(@NonNull String s) {//새로운 토큰을 확인했을 때 호출되는 메서드
        super.onNewToken(s);
        Log.e(TAG,"onNewToken 호출됨:"+s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {//새로운 메시지를 받았을 대 호출되는 메서드
        Log.d(TAG,"onMessageReceived() 호출됨.");

        String from=remoteMessage.getFrom();
        Map<String,String> data=remoteMessage.getData();
        String contents=data.get("contents");

        Log.d(TAG,"from: "+from+", contents : "+contents);

        sendToActivity(getApplicationContext(), from, contents);
    }

    private void sendToActivity(Context context, String from, String contents){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("command", "show");
        intent.putExtra("from", from);

        String dateStr = format.format(new Date());
        intent.putExtra("date", dateStr);
        intent.putExtra("contents", contents);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }
}
```

### 메인 액티비티 클래스

```java
package org.techtown.mission30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        editText=findViewById(R.id.editText);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {//등록 id 확인을 위한 리스너 설정
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken= instanceIdResult.getToken();

                println("등록 id: "+newToken);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent passedIntent = getIntent();
        processIntent(passedIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        println("onNewIntent 호출됨");

        if(intent!=null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            if (command != null) {
                if (command.equals("show")) {
                    String date = intent.getStringExtra("date");
                    String contents = intent.getStringExtra("contents");

                    textView.setText(date);
                    editText.setText(contents);
                }
            }
        }
    }

    public void println(String data){
        Log.d("FMS",data+"\n");
    }

}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission30">

    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoitMission30">
        <service
            android:name=".MyFirebaseMessagingService"
            android:enabled="true"
            android:exported="true"
            android:stopWithTask="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT"/>
            </intent-filter>
        </service>

        <activity android:name=".MainActivity"
            android:theme="@style/Theme.AppCompat.Light.Dialog"
            android:label="공지사항 수신">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### 그래들(프로젝트 수준) 파일

```gradle
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.1.1"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath 'com.google.gms:google-services:4.3.4'
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### 그래들(모듈 수준) 파일

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.mission30"
        minSdkVersion 16
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.google.firebase:firebase-messaging:21.0.1'
}

apply plugin:'com.google.gms.google-services'
```
