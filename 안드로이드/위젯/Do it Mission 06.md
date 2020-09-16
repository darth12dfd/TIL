# 시크바와 프로그레스바 보여주기

## 개요

* 시크바와 프로그레스바를 표시하고 시크바의 값을 바꾸었을 때 프로그레스바의 값도 바뀌도록 만들어 보세요

## 조건

* 화면에 시크바와 프로그레스바, 그리고 입력상자를 배치한다.

* 시크바의 값을 바꾸면 프로그레스바의 값도 바뀌도록 한다.

* 시크바의 값을 바꾸었을 때 그 값이 입력상자에 표시되도록 한다.

* 프로그레스바는 막대형을 사용한다.

## 참고

* 시크바의 값이 바귈 때 그 값을 알려주는 콜백 메서드를 사용한다.

* 시크바의 값이 바뀔 때 그 값을 프로그레스바와 입력상자에 설정한다.

## 소스코드

### XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <EditText
        android:id="@+id/editText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:ems="10"
        android:inputType="text"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ProgressBar
        android:id="@+id/progressBar"
        style="?android:attr/progressBarStyleHorizontal"
        android:layout_width="0dp"
        android:layout_height="100dp"
        android:layout_marginBottom="100dp"
        android:max="100"
        app:layout_constraintBottom_toTopOf="@+id/editText"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <SeekBar
        android:id="@+id/seekBar"
        android:layout_width="0dp"
        android:layout_height="100dp"
        android:max="100"
        app:layout_constraintBottom_toTopOf="@+id/progressBar"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.473" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 메인 액티비티

```java
package org.techtown.doitmission06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);//입력상자
        progressBar=findViewById(R.id.progressBar);//프로그레스 바
        SeekBar seekBar=findViewById(R.id.seekBar);//시크바

        //사용자가 시크바를 조작할 때의 데이터 활용
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editText.setText(String.valueOf(seekBar.getProgress()));//입력상자의 text 속성에다가 시크바의 progress값을 넣는다. 이 때, String형태로 타입을 바꾼다.
                progressBar.setProgress(seekBar.getProgress());//프로그레스바의 progress 속성에다가 시크바의 progress값을 넣는다.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
```
