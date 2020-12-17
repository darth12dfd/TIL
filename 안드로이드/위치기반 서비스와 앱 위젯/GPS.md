# GPS로 나의 위치 확인하기

* 안드로이드 기반의 스마트폰 위치는 위치 관리자라는 시스템 서비스가 관리한다. 실제로 android.location 패키지에는 이 클래스를 포함하여 위치 정보를 확인하거나 확인된 위치 정보를 사용하는데 필요한 클래스들이 정의되어 있다.

* 나의 현재 위치를 확인하는 가장 기본적인 방법은 위치 관리자에게 위치 정보를 요청하는 것으로 다음과 같은 단계를 거쳐야 한다.

| 단계 | 설명 |
| ---- | ---- |
| 1단계 | 위치 관리자 객체 참조하기 -> 위치 관리자는 시스템 서비스로 제공되므로, getSystemService() 메서드를 이용해 위치 관리자 객체를 참조한다. |
| 2단계 | 위치 리스너 구현하기 -> 위치 관리자가 알려주는 현재 위치는 위치 리스너를 통해 받게 되므로 새로운 리스너를 구현하여 전달 받은 위치 정보를 처리한다. |
| 3단계 | 위치 정보 업데이트 요청하기 -> 위치 관리자에게 위치 정보가 변경될 때마다 알려달라고 요청하기 위해 requestLocationUpdates() 메서드를 호출한다. 이 메서드의 파라미터로 2단계에서 구현한 위치 리스너 객체를 전달한다. |
| 4단계 | 매니페스트에 권한 추가하기 -> GPS를 사용할 수 있도록 매니페스트 파일에 ACCESS_FINE_LOCATION 권한을 추가하고 위험권한을 위한 설정과 코드를 추가한다. |

## 실습

* 내 위치를 확인하는 기능 만들기

* 먼저 메인 액티비티 XML 레이아웃을 열고 텍스튜 하나와 버튼 하나를 추가한다. 버튼에는 '내 위치 확인하기'라는 글자가표시되도록 하며 버튼을 누르면 내 위치 확인을 시작하도록 하고, 위치가 확인되면 텍스트뷰에 보이도록 한다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        android:textSize="30sp"
        android:text="내 위치"
        android:id="@+id/textView"/>

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        android:text="내 위치 확인하기"
        android:id="@+id/button"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        android:layout_marginBottom="8dp"
        android:layout_marginTop="8dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 1단계: 위치 관리자 객체 참조하기

* 위치 관리자는 시스템 서비스 이므로 객체를 참조하기 위해서 ```getSystemService()``` 메서드를 사용한다.

* 메인 액티비티 클래스 파일을 열고 버튼을 클릭했을 때 ```startLocationService()``` 메서드가 호출되도록 한다. 이 메서드 안에는 내 위치를 확인하기 위한 코드를 입력한다.

```java
package org.techtown.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
            }
        });
    }

    public void startLocationService(){
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);//Location Manager 객체 참조하기

        try{
            Location location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//이전에 확인했던 위치 정보 가져오기
            if(location!=null){
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
                String message="최근 위치 -> Latitude : "+latitude+"\nLongitude"+longitude;

                textView.setText(message);
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
}
```

* 위치 관리자를 위해 정의한 상수의 이름은 Context.LOCATION_SERVICE 이다. 따라서 이 상수로 시스템 서비스 객체를 참조한 후 최근 위치 정보를 확인해보는 코드를 넣으면 나의 최근 위치를 확인할 수 있다.

```위치 관라기 객체를 참조한 후 바로 최근 위치 정보를 확인할 때 단말의 GPS 모듈이 처음 사용되는 상태이면 GPS가 켜지면서 초기화된 후 사용된다.```

* 최근 위치 정보를 확인하기 위해 사용하는 ```getLastKnownLocation()``` 메서드에는 위치 정보를 제공하는 위치 제공자 정보를 파라미터로 전달한다.

* 안드로이드는 위치 제공차를 크게 GPS_PROVIDER와 NETWORK_PROVIDER로 구분하고 있으며, 이 두개의 값 중 하나를 파라미터로 전달하면 된다. 실제 앱에서는 대부분 GPS를 이용하게 되므로 GPS_PROVIDER를 전달하면 Location 객체가 반환된다.

* Location 객체는 위도와 경도 값을 가지고 있으며, ```getLatitude()``` 와 ```getLongitude()``` 메서드로 그 값을 확인할 수 있다.

```안드로이드는 겨우이도 좌표계를 사용하며, 위도와 경도의 값은 double 타입으로 되어 있다. 보통 소수점 일곱자리까지 표현된다.```

* 이때 ```getLastKnownLocation()```을 사용하려 한다면, 권한을 추가해야 하낟. 매니페스트 파일을 열고 아래와 같은 권한을 추가한다.

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

### 2단계: 위치 리스너 구현하기

* 위치 리스너는 위치 관리자에서 전달하는 위치 정보를 받기 위해 정의된 인터페이스이다. 즉, 위치 관리자가 위치 정보를 전달할 때 호출되므로 위치 정보를 받아 처리하려면 이 리스너의 ```onLocationChanged()``` 메서드를 구현해야 한다.

* 메인 액티비티 클래스 안에 LocationListener 인터페이스를 구현하는 클래스를 내부 클래스로 정의한다.

```java
class GPSListener implements LocationListener{
        public void onLocationChanged(Location location){
            Double latitude=location.getLatitude();
            Double longitude=location.getLongitude();
            String message="내 위치-> Latitude : "+latitude+"\nLongitude : "+longitude;
            textView.setText(message);
        }

        public void onProviderDisabled(String provideR){}

        public void onProviderEnabled(String provider){}

        public void onStatusChanged(String provider, int status,Bundle extras){}
    }
```

* 위치 리스너에는 위치 제공자의 상태를 확인하는 메서드와 함께 위치 정보를 전달할 때 호출되는 ```onLocationChanged()``` 메서드가 정의되어 있다.

### 3단계: 위치 정보 업데이트 요청하기

* 위치 관리자는 일정한 시간 간격으로 위치 정보를 확인하거나 일정 거리 이상을 이동했을 때 위치 정보를 전달하는 기능을 제공한다.

* 위치 관리자에게 현재 위치를 알려달라고 요청학 ㅣ위해서는 ```requestLocationUpdates()``` 메서드를 호출해야 하는데 파라미터로는 최소 시간과 최소 거리 그리고 위치 리스너 객체가 전달되어야 한다.

* 1단계에서 작성한 ```startLocationService()``` 메서드를 아래와 같이 수정한다.

```java
public void startLocationService(){
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);//Location Manager 객체 참조하기

        try{
            Location location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//이전에 확인했던 위치 정보 가져오기
            if(location!=null){
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
                String message="최근 위치 -> Latitude : "+latitude+"\nLongitude"+longitude;

                textView.setText(message);
            }

            GPSListener gpsListener=new GPSListener();//리스너 객체 생성

            long minTime=10000;
            float minDistance=0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);//위치 요청하기
            Toast.makeText(getApplicationContext(),"내 위치확인 요청함",Toast.LENGTH_LONG).show();

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
```

* ```startLocationService()``` 메서드는 위치 정보 수신을 시작하기 위해 만든 것이다. 그 안에서 ```requestLocationUpdates()``` 메서드가 호출되는 것을 볼 수 있다.

* 여기서는 최소 시간으로는 10초 최소 거리는 0으로 하여 10초마다 위치 정보를 전달받게 된다.

### 4단계: 위험 권한을 위한 코드 추가하기

* GPS를 이용해 위치 정보를 받기 위한 권한은 CCESS_FINE_LOCATION으로 정의되어 있으므로 1단계에서 매니페스트에 이 권한을 추가했다. 

* 하지만 이 권한은 위험 권한으로 분류되어 있으므로 위험  권한을 부여하는 설정과 코드를 추가한다. 먼저 그래들 파일을 열고 AutoPermissions 라이브러리를 사용하기 위한 코드를 한 줄 추가한다.

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
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

* 그리고 메인 액티비티 자바 파일에 위험 권한을 자동으로 부여하기 위한 코드를 추가하면서 코드를 완성시킨다.

```java
package org.techtown.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this,101);
    }

    public void startLocationService(){
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);//Location Manager 객체 참조하기

        try{
            Location location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//이전에 확인했던 위치 정보 가져오기
            if(location!=null){
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
                String message="최근 위치 -> Latitude : "+latitude+"\nLongitude"+longitude;

                textView.setText(message);
            }

            GPSListener gpsListener=new GPSListener();//리스너 객체 생성

            long minTime=10000;
            float minDistance=0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);//위치 요청하기
            Toast.makeText(getApplicationContext(),"내 위치확인 요청함",Toast.LENGTH_SHORT).show();

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this,"permissions denied: "+strings.length,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this,"permissions granted: "+strings.length,Toast.LENGTH_LONG).show();
    }

    class GPSListener implements LocationListener{
        public void onLocationChanged(Location location){
            Double latitude=location.getLatitude();
            Double longitude=location.getLongitude();

            String message="내 위치-> Latitude : "+latitude+"\nLongitude : "+longitude;
            textView.setText(message);
        }

        public void onProviderDisabled(String provideR){}

        public void onProviderEnabled(String provider){}

        public void onStatusChanged(String provider, int status,Bundle extras){}
    }

}
```
