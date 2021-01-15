# 공지 내용 전송 기능 만들기

* 두 명의 직원에게 공지 내용 전송하는 기능 만들기

* 화면에 보이는 두 명의 직원에게 공지 내용을 입력하고 전송하면 상대방에게 푸시로 공지가 보내지도록 한다.

## 조건

* 화면의 위쪽에는 리싸이클러뷰를 추가하고 친구 두 명을 아이템으로 미리 넣어준다.

* 화면의 아래쪽에는 공지 내용을 입력할 수 있는 입력 상자와 버튼을 배치한다.

* 푸시 전송에 필요한 상대방 단말 등록 ID는 미리 복사하여 넣어둔다.

* 입력상자에 내용을 입력하고 [전송] 버튼을 누르면 푸시 기능을 이용해 상대방 단말로 메시지를 보낸다.

## 참고

* 공지 내용을 푸시로 전송하려면 상대방 단말의등록 ID와 함께 FCM 사이트에서 등록했을 때 확인할 수 있는 정보들이 필요하다.

* 푸시 받은 메시지를 출력받는 방식은 Do it Mission 30에서 수행한다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    </androidx.recyclerview.widget.RecyclerView>

    <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="내용" />

    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="전송" />
</LinearLayout>
```

### 친구 클래스

```java
package org.techtown.mission29;

public class Friend {
    String name;
    String mobile;
    String regId;

    public Friend(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public Friend(String name, String mobile, String regId) {
        this.name = name;
        this.mobile = mobile;
        this.regId = regId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "FriendItem{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", regId='" + regId + '\'' +
                '}';
    }
}
```

### 친구 어댑터 클래스

```java
package org.techtown.mission29;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    ArrayList<Friend> items=new ArrayList<>();

    public void addItem(Friend item){
        items.add(item);
    }

    public void setItems(ArrayList<Friend> items){
        this.items=items;
    }

    public Friend getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Friend item){
        items.set(position, item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.friend_item,parent,false);//인플레이션을 통해 뷰 객체 만들기

        return new ViewHolder(itemView);//뷰홀더 객체를 생성하면서 뷰 객체를 전달하고 그 뷰홀더 객체를 반환하기
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView){//뷰홀더 생성자로 전달되는 뷰 객체 참조하기
            super(itemView);
            
            //뷰 객체에 들어 있는 텍스트뷰 참조하기
            textView=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.textView2);
        }

        public void setItem(Friend item){
            textView.setText(item.getName());
            textView2.setText(item.getMobile());
        }

    }
}
```

### 친구 아이템 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">


    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardBackgroundColor="#FFFFFFFF"
        app:cardCornerRadius="10dp"
        app:cardElevation="5dp"
        app:cardUseCompatPadding="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <ImageView
                android:id="@+id/imageView"
                android:layout_width="80dp"
                android:layout_height="80dp"
                android:padding="5dp"
                tools:srcCompat="@tools:sample/avatars" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_margin="5dp"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="이름"
                    android:textSize="30sp" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="전화번호"
                    android:textColor="#FF0000FF"
                    android:textSize="20sp" />
            </LinearLayout>
        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.mission29;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    static RequestQueue requestQueue;//요청 큐

    RecyclerView recyclerView;//리싸이클러 뷰
    FriendAdapter adapter;//어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);//리싸이클러뷰 참조

        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);//리싸이클러뷰에 레이아웃 매니저 설정하기
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FriendAdapter();//어댑터 생성

        //어댑터에 아이템 추가
        adapter.addItem(new Friend("이규원","010-5226-9456","eWDY276PTfikFULhmOG_ru:APA91bGUJTP3u2UDnq2bbfX0nql9DHcLh3FQ5OOtaLF1W-mYrwMloLC2ngtck6qmLJgH1plESyD7-UuIRxEzLSRfKrqNMrejHNtBIKht-adinmLXl2xQUSvGfCNQvZE-sY153XHUKYcB"));
        adapter.addItem(new Friend("이유민","010-6675-9456","eWDY276PTfikFULhmOG_ru:APA91bGUJTP3u2UDnq2bbfX0nql9DHcLh3FQ5OOtaLF1W-mYrwMloLC2ngtck6qmLJgH1plESyD7-UuIRxEzLSRfKrqNMrejHNtBIKht-adinmLXl2xQUSvGfCNQvZE-sY153XHUKYcB"));

        recyclerView.setAdapter(adapter);//리싸이클러뷰에 어댑터 설정
        editText=findViewById(R.id.editText);

        //버튼 클릭시 입력 상자에 있는 내용을 단말에 푸시
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input=editText.getText().toString();
                send(input);
            }
        });


        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void send(String input){
        JSONObject requestData=new JSONObject();

        try{
            requestData.put("priority","high");

            JSONObject dataObj=new JSONObject();
            dataObj.put("contents",input);
            requestData.put("data",dataObj);

            JSONArray idArray=new JSONArray();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Friend item = adapter.getItem(i);
                String regId = item.getRegId();
                println("regId #" + i + " : " + regId);

                idArray.put(i, regId);
            }

            requestData.put("registration_ids", idArray);
        }catch (Exception e){
            e.printStackTrace();
        }

        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
                println("onRequestCompleted() 호출됨.");

                Toast.makeText(getApplicationContext(), "공지사항 전송함", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestStarted() {
                println("onRequestStarted() 호출됨.");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                println("onRequestWithError() 호출됨.");
            }
        });
    }

    public interface SendResponseListener {
        public void onRequestStarted();
        public void onRequestCompleted();
        public void onRequestWithError(VolleyError error);
    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Authorization","key=AAAAs4_0IRE:APA91bF9u0cAgdOHZnF0Fh1oLTEuz1r4r7nE21fugfEiEcEsBFv6LN83YaWVtdqWIunV6CVHLGmTBSYrtfpjfR4Vlaltq0Ci83oly3IEcRn36WaLPSwwOtsb2RPQZ7d12mHvneO4UG4v");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        requestQueue.add(request);
    }

    public void println(String data) {
        Log.d("FMS", data);
    }
}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission29">

    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoitMission29">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
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
        applicationId "org.techtown.mission29"
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
    implementation 'com.android.volley:volley:1.1.0'
}
```
