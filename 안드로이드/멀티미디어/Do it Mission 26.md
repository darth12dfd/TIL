# 증강 현실로 쿠폰 보여주기

* 증강 현실에서 쿠폰을 보여주는 화면을 가상으로 만들어보기. 카메라 미리보기 화면을 보여주고 그 위에 쿠폰 이미지를 보여주면 증강 현실에서 보여주는 것처럼 만들 수 있다.

## 조건

* 앱을 실행하면 카메라 미리보기 화면이 보이도록 하고 아래쪽에는 [쿠폰 보이기]와 [쿠폰 감추기] 버튼을 배치한다.

* [쿠폰 보이기] 버튼을 누르면 카메라 미리보기 화면 위에 쿠폰 아이콘을 보여준다.

* [쿠본 감추기] 버튼을 누르면 쿠폰 아이콘이 보이지 않도록 한다.

* 쿠폰 아이콘을 선택하면 토스트 메시지로 선택된 쿠폰 정보를 간단하게 보여준다.

* 카메라 미리보기와 쿠폰 아이콘을 같이 보여주도록 프레임 레이아웃 안에 카메라 미리보기와 쿠폰 아이콘 두 개의 레이아웃을 추가한다. 그런 다음 소스코드에서 쿠폰 레이아웃의 visibility 속성을 조절한다.

## 참고

* 증강 현실에서는 센서도 사용하지만 여기서는 센서는 사용하지 않고 카메라 미리보기 화면 위에 쿠폰 이미지를 보여주는 것 까지만 해본다.

* 센서를 사용하지 않으므로 단말을 움직여도 쿠폰 이미지가 따라서 움직이지는 않는다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <FrameLayout
        android:id="@+id/previewLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@id/buttonLayout">

        <org.techtown.mission26.CameraSurfaceView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/cameraView"/>

        <RelativeLayout
            android:id="@+id/iconLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent">


            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="80dp"
                android:layout_marginTop="200dp"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imageView"
                    android:layout_width="50dp"
                    android:layout_height="50dp"
                    android:scaleType="centerInside"
                    app:srcCompat="@drawable/coupon" />

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_horizontal"
                    android:text="VIPS"
                    android:textColor="#FFFFFFFF"
                    android:textStyle="bold"
                    android:textSize="18sp"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="180dp"
                android:layout_marginLeft="200dp"
                android:orientation="vertical">


                <ImageView
                    android:id="@+id/imageView2"
                    android:layout_width="50dp"
                    android:layout_height="50dp"
                    android:layout_gravity="center_horizontal"
                    android:scaleType="centerInside"
                    app:srcCompat="@drawable/coupon" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_horizontal"
                    android:text="스타벅스"
                    android:textColor="#FFFFFFFF"
                    android:textSize="18sp"
                    android:textStyle="bold" />
            </LinearLayout>


        </RelativeLayout>


    </FrameLayout>

    <LinearLayout
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:layout_margin="8dp"
        android:id="@+id/buttonLayout"
        android:orientation="horizontal"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true">

        <Button
            android:layout_height="wrap_content"
            android:layout_width="120dp"
            android:id="@+id/button"
            android:textSize="16sp"
            android:text="쿠폰보이기"
            android:textStyle="bold"/>

        <Button
            android:layout_height="wrap_content"
            android:layout_width="120dp"
            android:id="@+id/button2"
            android:textSize="16sp"
            android:text="쿠폰감추기"
            android:layout_marginLeft="10dp"
            android:textStyle="bold"/>
    </LinearLayout>
</RelativeLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.mission26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    FrameLayout previewLayout;
    CameraSurfaceView cameraView;

    Button button;
    Button button2;

    RelativeLayout iconLayout;

    ImageView imageView;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewLayout=findViewById(R.id.previewLayout);

        cameraView=findViewById(R.id.cameraView);

        imageView=findViewById(R.id.imageView);
        imageView2=findViewById(R.id.imageView2);

        TextView textView=findViewById(R.id.textView);
        TextView textView2=findViewById(R.id.textView2);

        iconLayout=findViewById(R.id.iconLayout);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconLayout.setVisibility(View.VISIBLE);
            }
        });

        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconLayout.setVisibility(View.INVISIBLE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"쿠폰 정보 : "+textView.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"쿠폰 정보: " + textView2.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this,101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this,"permissions denied : "+strings.length,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this,"permissions granted : "+strings.length,Toast.LENGTH_LONG).show();
    }
}
```

### 카메라 서피스뷰 클래스

```java
package org.techtown.mission26;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{//서피스뷰 클래스를 상속하고 콜백 인터페이스를 구현하는 새로운 카메라 서피스뷰 클래스 정의
    private SurfaceHolder mHolder;
    private Camera camera;

    int surfaceWidth;
    int surfaceHeight;

    int bitmapWidth = 0;
    int bitmapHeight = 0;


    public CameraSurfaceView(Context context) {
        super(context);

        //생성자에 서피스홀더 객채 참조 후 설정
        mHolder=getHolder();
        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);

        //생성자에 서피스홀더 객채 참조 후 설정
        mHolder=getHolder();
        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    //서피스뷰가 만들어질 때 카메라 객체를 참조한 후 미리보기 화면으로 홀더 객체 설정
    public void surfaceCreated(SurfaceHolder holder){
        camera= Camera.open();

        try{
            camera.setPreviewDisplay(mHolder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //서피스뷰의 화면 크기가 바뀌는 등의 변경 시점에 미리 보기 시작
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;

        // rotate preview display for several devices
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                camera.setDisplayOrientation(90);
            } else {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setRotation(90);
                camera.setParameters(parameters);
            }

            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException exception) {
            camera.release();
        }


        camera.startPreview();
    }

    //서피스뷰가 없어질 때 미리보기 중지
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera=null;
    }
}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission26">

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CAMERA"/>

    <uses-feature android:name="android.hardware.camera"
        android:required="true"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoitMission26">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### 그래들 파일

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.mission26"
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

allprojects {
    repositories {
        maven { url 'https://jitpack.io'}
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.pedroSG94:AutoPermissions:1.0.3'
}
```
