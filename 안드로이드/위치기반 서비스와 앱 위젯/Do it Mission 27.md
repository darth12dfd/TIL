# 지도 위에 친구 위치를 사진으로 표시하기

* 지도 위에 친구들의 위치를 표시하기. 위치 좌표는 가상으로 정하고 친구들이 있는 위치에 친구의 사진이나 아이콘을 보여주도록 한다.

## 조건

* 친구들의 위치를 경위도 좌표로 두 군데 설정한다.

* 친구들의 위치는 가상 위치로 지정하여 소스코드에 넣어둔다.

* 지도의 특정 영역을 보여준 후 그 위에 친구들의 위치를 실제 친구의 사진으로 표시한다.

* 사진을 누르면 친구 정보를 토스트 메시지로 간단하게 보여준다.

## 참고

* 지도 위에 아이콘이나 사진을 표시하려면 마커를 만들어 지도에 추가해야 한다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:id="@+id/button"
        android:text="내위치 확인하기"/>

    <fragment
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:id="@+id/map"
        class="com.google.android.gms.maps.SupportMapFragment"
        android:layout_below="@+id/button"/>

</RelativeLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.location.mission27;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private static final String TAG = "MainActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;

    MarkerOptions myLocationMarker;

    MarkerOptions friendMarker1;
    MarkerOptions friendMarker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");

                map = googleMap;

                try {
                    map.setMyLocationEnabled(true);
                } catch(SecurityException e) {e.printStackTrace();}

            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch(Exception e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyLocation();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }


    //현재 위치 요청하기 위한 메서드
    private void requestMyLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            //요청 위치 업데이트
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);

                            addPictures(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //사용자의 현재 위치 표시하기
    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

            showMyLocationMarker(location);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //내 위치 마커 표시
    private void showMyLocationMarker(Location location) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocationMarker.title("● 내 위치\n");
            myLocationMarker.snippet("● GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    //친구의 위치를 사진으로 표시
    private void addPictures(Location location) {
        int pictureResId = R.drawable.friend03;
        String msg = "● 김성수\n" + "● 010-7788-1234";

        if (friendMarker1 == null) {
            friendMarker1 = new MarkerOptions();
            friendMarker1.position(new LatLng(location.getLatitude()+30, location.getLongitude()+30));
            friendMarker1.title("● 친구 1\n");
            friendMarker1.snippet(msg);
            friendMarker1.icon(BitmapDescriptorFactory.fromResource(pictureResId));
            map.addMarker(friendMarker1);
        } else {
            friendMarker1.position(new LatLng(location.getLatitude()+30, location.getLongitude()+30));
        }

        pictureResId = R.drawable.friend04;
        msg = "● 이현수\n" + "● 010-5512-4321";


        if (friendMarker2 == null) {
            friendMarker2 = new MarkerOptions();
            friendMarker2.position(new LatLng(location.getLatitude()+20, location.getLongitude()-10));
            friendMarker2.title("● 친구 2\n");
            friendMarker2.snippet(msg);
            friendMarker2.icon(BitmapDescriptorFactory.fromResource(pictureResId));
            map.addMarker(friendMarker2);
        } else {
            friendMarker2.position(new LatLng(location.getLatitude()+20, location.getLongitude()-10));
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (map != null) {
            try {
                map.setMyLocationEnabled(false);
            } catch(SecurityException e) {e.printStackTrace();}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            try {
                map.setMyLocationEnabled(true);
            } catch(SecurityException e) {e.printStackTrace();}
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }

}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.location.mission27">

    <permission android:name="org.techtown.location.mission27.permission.MAP_RECEIVE"
        android:protectionLevel="signature"/>

    <uses-permission android:name="org.techtown.location.mission27.permission.MAPS_RECEIVE"/>

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>

    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

    <uses-feature android:glEsVersion="0x00020000"
        android:required="true"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoitMission27">

        <uses-library android:name="com.google.android.maps"/>
        <uses-library android:name="org.apache.http.legacy"
            android:required="false"/>

        <meta-data
            android:name="com.google.android.maps.v2.API_KEY"
            android:value="AIzaSyCEJmQCuFZl99L_c-ay-_a31FltRYRLUqw"/>

        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version"/>

        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### 그래들(모듈 수준)

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.location.mission27"
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
        maven { url 'http://jitpack.io' }
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.pedroSG94:AutoPermissions:1.0.3'
}
```
