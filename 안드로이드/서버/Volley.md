# Volley

* 웹서버에 요창하고 응답을 받을 때는 HttpURLConnection 객체를 사용할 수 있지만 요청과 응답에 필요한 코드의 양이 많다.

* 그리고 스레드를 사용하면서 넣어야 하는 코드도 만혹, 핸들러에 대한 이해가 부족할 경우 앱이 비정상 종료 될 수도 있다.

* 위에서 묘사한 문제들을 해결하가 위해 여러가지 라이브러리들이 만들어졌는데, 그중에서 가장 많이 사용되는 것이 Volley 이다.

* Volley 라이브러리는 웹 요청과 응답을 단순화하기 위해 만들어진 라이브러리이다.

* Volley를 사용하려면 먼저 요청(Request) 객체를 만들고 이 요청 객체를 요청 큐(Request Queue)라는 곳에 넣어주기만 하면 된다. 그러면 요청 큐가 알아서 웹서버에 요청하고 응답까지 받아준다. 사용자는 그저 응답을 받을 수 있도록 지정된 메서드를 만들어두기만 하면 응답이 왔을 때 그 메서드가 자동으로 호출된다.

* 또한 Volley 라이브러리의 가장 큰 장점은 스레드를 신경쓰지 않아도 된다는 것이다. 용청 큐가 내부에서 스레드를 만들어 웹서버에 요청하고 응답받는 과정을 진행하는데, 응답을 처리할 수 있는 메서드를 호출할 때는 메인 스레드에서 처리할 수 있도록 만들기 때문이다.

* 그렇기에 스레드를 사용할 필요도 없고, 화면에 결과를 표시할 때 사용할 핸들러로 필요가 없다.

## 실습

* Volley를 사용해 웹 요청과 응답을 처리하는 과정을 살펴보기

### build.gradle(Module:app)

* Volley는 외부 라이브러리이므로 build.grale파일에 정보를 추가해야 사용할 수 있다.

* dependencies 중괄호 안에 라이브러리 정보를 추가하고 [Sync Now]링크를 클릭하면 자동으로 라이브러리가 추가된다.

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.request"
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

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.android.volley:volley:1.1.0'//이 부분을 추가
}
```

### 매니페스트 파일

* INTERNET 권한과 usesCleartextTraffic 속성을 추가한다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.request">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:usesCleartextTraffic="true"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.SampleRequest">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### 메인 액티비티 소스파일

* XML 레이아웃은 '웹으로 요청하기'에서 사용한 것을 그대로 사용한다.

```java
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });

        //RequestQueue 객체 생성하기
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest(){
        String url=editText.getText().toString();

        //요청을 보내기 위한 StringRequest 객체 생성하기
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                println("응답-> "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                println("에러-> "+error.getMessage());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄.");
    }

    public void println(String data){
        textView.append(data+"\n");
    }
}
```

* 사용자가 버튼을 클릭했을 때 요청 객체를만들고 요청 큐를 넣어준다. 이때 요청 큐는 한 번만 만들어 계속 사용할 수 있기 때문에 static 키워드로 클래스 변수를 선언한 후 할당한다. 실제 앱을 만들때는 ```Application``` 클래스 안에 넣어두거나 별도의 클래스를 하나 만들어서 넣을 수 있다.

* 요청 큐를 만들 때는 ```Volley.newRequestQueue()``` 메서드를 사용할 수 있다. 이 코드는 ```onCreate()``` 메서드 안에 넣어준다.

* 요청 객체는 StringRequest 클래스로 만들 수 있다. StringRequest는 문자열을 주고받기 위해 사용하는 요청 객체이며 Volley 라이브러리 안에는 이외에도 여러 가지 유형의 요청 객체가 들어있다.

* 요청 객체를 new 연산자로 만들 때는 네 개의 파라미터로 전달할 수 있다.

* 첫 번째 파라미터로는 요청 방식을 지정한다. 이 때 ```GET()```또는 ```POST()``` 메서드를 전달한다.

* 두 번째 파라미터로는 웹사이트 주소를 전달한다.

* 세 번째 파라미터로는 응답받을 리스너 객체를 전달한다. 이 리스너의 ```onResponse()``` 메서드는 응답을 받았을 때 자동으로 호출된다.

* 네 번째 파라미터로는 에러가 발생했을 때 호출될 리스너 객체를 전달한다. 여기서는 GET 방식을 사용했지만, POST방식을 사용하면서 요청 파라미터를 넣고자 한다면, ```getParams()``` 메서드에서 반환하는 HashMap 객체에 파라미터값들을 넣어주면 된다.

* 요청 객체를 만들었다면 이 객체는 요청 큐에 넣어준다. 요청 큐의 ```add()``` 메서드로 요청 객체를 넣으면 요청 큐가 자동으로 요청과 응답 과정을 진행한다.

* 요청 객체는 cache 매커니즘을 지원하는데 만약 이전 응답 결과를 사용하지 겠다면 ```setShouldCache()``` 메서드를 사용해서 cache를 사용하지 않도록 설정하면 된다.
