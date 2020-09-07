# 실습 04

## 문제

* SMS 입력 화면 만들고 글자의 수 표시하기

* 화면 위쪽에 텍스트 입력상자, 아래쪽에 [전송]과 [닫기]버튼을 수평으로 배치하기

## 순서

* MS로 문자를 전송하는 화면은 위쪽에 텍스트 입력상자, 아래쪽에 [전송]과 [닫기]버튼을 수평으로 배치하도록 구성합니다.

* 텍스트 입력상자 바로 아래에 입력되는 글자의 바이트 수를 '10/80 바이트'와 같은 포맷으로 표시하되 우측 정렬로 하도록 하고 색상을 눈에 잘 듸는 다른 색으로 설정한다.

* 텍스트 입력상자에 입력되는 글자의 크기와 줄 간격을 조정하여 한 줄에 한글 8글자가 들어가도록 만든다.

* [전송] 버튼을 누르면 입력된 글자를 화면에 토스트로 표시하여 내용을 확인할 수 있도록 한다.

## 참고

* 화면에서 '10/80 바이트'로 된 글자 부분을 가장 위쪽으로 배치한다.

* 입력상자에 글자가 입력될 때마다 자동으로 호출되는 메서드를 사용한다.

## 소스코드

### xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    //입력상자 레이아웃
    <RelativeLayout
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:layout_above="@+id/buttonLayout"
        android:layout_alignParentTop="true"
        android:id="@+id/inputlayout">
        //텍스트 뷰
        <TextView
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:layout_alignParentTop="true"
            android:id="@+id/inputCount"
            android:textSize="18sp"
            android:layout_marginBottom="10dp"
            android:layout_marginTop="10dp"
            android:layout_marginRight="10dp"
            android:textColor="#ffff0000"
            android:text="0 / 80 바이트"
            android:layout_alignParentRight="true"/>

        //입력 상자
        <EditText
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            android:id="@+id/inputMessage"
            android:textSize="36sp"
            android:layout_marginRight="10dp"
            android:layout_gravity="center_vertical|center_horizontal"
            android:maxLength="80"
            android:layout_marginLeft="10dp"
            android:gravity="top"
            android:layout_below="@+id/inputCount"/>

        </RelativeLayout>
    //버튼 레이아웃
    <LinearLayout
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:id="@+id/buttonLayout"
        android:layout_alignParentBottom="true"
        android:orientation="vertical">

        <LinearLayout
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:layout_gravity="center_horizontal"
            android:orientation="horizontal">
            //전송 버튼
            <Button
                android:layout_height="wrap_content"
                android:layout_width="wrap_content"
                android:id="@+id/sendButton"
                android:textSize="18sp"
                android:text="전송"
                android:paddingRight="20dp"
                android:paddingLeft="20dp"
                android:layout_margin="20dp"/>
            //닫기 버튼
            <Button
                android:layout_height="wrap_content"
                android:layout_width="wrap_content"
                android:id="@+id/closeButton"
                android:textSize="18sp"
                android:text="닫기"
                android:paddingRight="20dp"
                android:paddingLeft="20dp"
                android:layout_margin="20dp"/>

        </LinearLayout>
    </LinearLayout>
</RelativeLayout>
```

### java

```java
package org.techtown.doitmission04;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    EditText inputMessage;
    TextView inputCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMessage = findViewById(R.id.inputMessage);
        inputCount = findViewById(R.id.inputCount);

        //전송 버튼 관련 이벤트 처리
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                Toast.makeText(getApplicationContext(), "전송할 메시지\n\n" + message, Toast.LENGTH_LONG).show();
            }
        });

        //닫기 버튼 관련 이벤트 처리
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //텍스트 뷰, 입력상자 관련 인터페이스 생성자 형성 및 텍스트 뷰 이벤트 처리
        TextWatcher watcher = new TextWatcher() {
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                byte[] bytes = null;
                try {
                    bytes = str.toString().getBytes("KSC5601");
                    int strCount = bytes.length;
                    inputCount.setText(strCount + " / 80바이트");
                } catch(UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable strEditable) {
                String str = strEditable.toString();
                try {
                    byte[] strBytes = str.getBytes("KSC5601");
                    if(strBytes.length > 80) {
                        strEditable.delete(strEditable.length()-2, strEditable.length()-1);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        //입력상자 이벤트 처리
        inputMessage.addTextChangedListener(watcher);
    }

}
```
