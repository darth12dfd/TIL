# 세 개 이상의 화면 만들어 전환하기

* 앱에서 사용될 수 있는 여러 화면을 구성하고 각 화면을 전환하면서 토스트로 메시지를 띄워주도록 만들어 보세요

## 조건

* 로그인 화면과 메뉴 화면 그리고 세 개의 서브 화면(고객 관리 화면, 매출 관리 화면, 상품 관리 화면)을 각각 액티비티로 만든다.

* 로그인 화면에는 두 개의 입력상자와 하나의 버튼이 들어가도록 한다.

* 메뉴 화면에는 네 개의 버튼이 들어가도록 하고 각각 고객관리, 매출관리, 상품관리, 로그인 이라는 이름으로 표시한다.

* 로그인 화면의 로그인 버튼을 누르면 메뉴 화면으로 이동한다. 만약 사용자 이름이나 비밀번호가 입력되어 있지 않은 상태에서 로그인 버튼을 누르면 토스트로 입력하라는 메시지를 보여주고 대기한다.

* 메뉴 화면의 버튼 중에서 하나를 누르면 해당 서브 화면으로 이동한다. 메뉴 화면에 있는 로그인 버튼을 누르면 로그인 화면으로 이동하고, 각 서브 화면에 있는 메뉴 버튼을 누르면 메뉴 화면으로 이동한다.

## 참고

* 각 화면은 액티비티로 만들고 startActivityForResult() 메서드로 새로 띄우거나 finish() 메서드를 사용해서 원래의 화면으로 돌아올 수 있게 한다.

* 그리고 어떤 화면으로부터 보내온 응답인지 모두 확인하여 토스트 메시지로 보여준다.

## 소스코드

### 로그인 액티비티

#### 레이아웃 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button"
        android:layout_width="73dp"
        android:layout_height="100dp"
        android:layout_marginTop="83dp"
        android:layout_marginEnd="65dp"
        android:layout_marginRight="65dp"
        android:text="로그인"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/editText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="46dp"
        android:layout_marginLeft="46dp"
        android:layout_marginTop="83dp"
        android:ems="10"
        android:hint="아이디 입력"
        android:inputType="textPersonName"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/editText2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="46dp"
        android:layout_marginLeft="46dp"
        android:layout_marginTop="10dp"
        android:ems="10"
        android:hint="비밀번호 입력"
        android:inputType="textPersonName"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editText" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스 파일

```java
package org.techtown.doitmission08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(username.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(),"아이디 혹은 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivityForResult(intent, REQUEST_CODE_MENU);
                }
            }
        });

        usernameInput = (EditText) findViewById(R.id.editText);
        passwordInput = (EditText) findViewById(R.id.editText2);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_MENU) {
            if (intent != null) {
                String menu = intent.getStringExtra("menu");
                String message = intent.getStringExtra("message");

                Toast.makeText(getBaseContext(), "result code : " + resultCode + ", menu : " + menu + ", message : " + message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
```

### 메뉴 액티비티

#### 레이아웃 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MenuActivity">

    <Button
        android:id="@+id/client_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="200dp"
        android:text="고객 관리"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.501"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/sales_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="매출 관리"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/product_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="200dp"
        android:text="상품 관리"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.501"
        app:layout_constraintStart_toStartOf="parent" />

    <Button
        android:id="@+id/back_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="70dp"
        android:text="로그인"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스 파일

```java
package org.techtown.doitmission08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CLIENT_MANAGE=201;
    public static final int REQUEST_CODE_SALES_MANAGE=202;
    public static final int REQUEST_CODE_PRODUCT_MANAGE=203;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //메인 화면에서 호출받은 것들 토스트 메시지로 확인 시키기
        Intent recievedIntent=getIntent();
        String id=recievedIntent.getStringExtra("username");
        String pw=recievedIntent.getStringExtra("password");
        Toast.makeText(this,"username : "+id+" password : "+pw,Toast.LENGTH_LONG).show();

        //로그인 버튼 클릭 이벤트 처리
        Button back_button=findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("message","result is OK!");
                setResult(RESULT_OK,intent);
                finish();
            }
        });


        //고객 관리 버튼 클릭 이벤트 처리
        Button client_manage_button=findViewById(R.id.client_button);
        client_manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ClientActivity.class);//고객 관리 화면 액티비티로 전환해출 인텐트 생성
                intent.putExtra("textTitle","고객 관리 화면");
                startActivityForResult(intent,REQUEST_CODE_CLIENT_MANAGE);
            }
        });

        //매출 관리 버튼 클릭 이벤트 처리
        Button sales_manage_button=findViewById(R.id.sales_button);
        sales_manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SalesActivity.class);//고객 관리 화면 액티비티로 전환해출 인텐트 생성
                intent.putExtra("textTitle","매출 관리 화면");
                startActivityForResult(intent,REQUEST_CODE_SALES_MANAGE);
            }
        });

        //상품 관리 버튼 클릭 이벤트 처리
        Button product_manage_button=findViewById(R.id.product_button);
        product_manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductActivity.class);//고객 관리 화면 액티비티로 전환해출 인텐트 생성
                intent.putExtra("textTitle","상품 관리 화면");
                startActivityForResult(intent,REQUEST_CODE_PRODUCT_MANAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {

            if (requestCode == REQUEST_CODE_CLIENT_MANAGE) {
                String message=intent.getStringExtra("message");
                if(message!=null) {
                    Toast.makeText(getBaseContext(), "고객 관리 응답 : "+message,Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode==REQUEST_CODE_SALES_MANAGE){
                String message=intent.getStringExtra("message");
                if(message!=null) {
                    Toast.makeText(getBaseContext(), "매출 관리 응답 : "+message,Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode==REQUEST_CODE_PRODUCT_MANAGE){
                String message=intent.getStringExtra("message");
                if(message!=null) {
                    Toast.makeText(getBaseContext(), "상품 관리 응답 : "+message,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
```

#### 세부 메뉴 액티비티

* 세부 액티비티 모두 소스 자체는 동일하므로 대표적인 경우 하나만 표시한다.

##### 레이아웃 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ClientActivity">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:text="고객 관리"
        android:textSize="30sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/back_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="메뉴"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

##### 소스 파일

```java
package org.techtown.doitmission08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        textView=findViewById(R.id.textView);
        //호출한 화면 명 호출
        Intent recievedIntent=getIntent();
        String textTitle=recievedIntent.getStringExtra("textTitle");
        Toast.makeText(this,"Title : "+textTitle,Toast.LENGTH_LONG).show();
        textView.setText(textTitle);

        Button back_button=findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("message","result is OK!");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
```
