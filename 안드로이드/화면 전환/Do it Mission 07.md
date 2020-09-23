# 로그인 화면과 메뉴 화면 전환하기

* 대부분의 업무용 앱에서 필요한 로그인 화면과 메뉴 화면을 간단하게 만들고 두 화면 간을 전환하면서 토스트로 메시지를 띄워주도록 만들어보자

## 조건

* 로그인 화면과 메뉴 화면 각각을 액티비티로 만든다

* 로그인 화면에는 하나의 버튼이 들어가도록 한다.

* 메뉴 화면에는 세 개의 버튼이 들어가돌고 하고 각각 '고객 관리' 매출 관리', '상품 관리'라는 이름으로 표시한다.

* 로그인 화면의 버튼을 누르면 메뉴 화면으로 이동한다.

* 메뉴 화면의 버튼 중에서 하나를 누르면 로그인 화면으로 돌아온 후 선택된 메뉴의 이름을 토스트 메시지로 보여준다.

## 참고

* 각 화면은 액티비티로 만들고 화면 간 전환시에는 ```startActivityForResult()``` 메서드를 사용한다.

## 소스코드

### 메인 액티비티

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
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="로그인"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스 파일

```java
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_MENU=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"로그인 버튼 클릭",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
                startActivityForResult(intent,REQUEST_MENU);
            }
        });

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
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="고객 관리"
        app:layout_constraintBottom_toTopOf="@+id/button2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="매출 관리"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="상품 관리"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/button2" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스 파일

```java
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    public static final int REQUEST_LOGIN=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //버튼 1 클릭시
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"고객 관리 클릭",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(intent,REQUEST_LOGIN);
            }
        });

        //버튼 2 클릭시
        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //버튼 3 클릭시
        Button button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
```
