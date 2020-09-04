# 두 개의 이미지뷰에 이미지 번갈아 보여주기

## 개요

* 두 개의 이미지뷰를 한 화면에 보여주고 하나의 이미지를 두 개의 이미지뷰에서 번갈아 보여주도록 만들어 보세요

## 순서

* 화면을 위와 아래 두 영역으로 나누고 그 영역에 각각 이미지뷰를 배치합니다.

* 각각의 이미지뷰는 스크롤이 생길 수 있도록 합니다.

* 상단의 이미지뷰에 하나의 이미지를 보이도록 합니다.

* 두 개의 이미지뷰 사이에 버튼을 하나 만들고 그 버튼을 누르면 상단의 이미지가 하단으로 옮겨져 보이고, 다시 누르면 상단으로 다시 옮겨지는 기능을 추가합니다.

## 참고

* 이미지를 화면에 보여죽 ㅣ위해서는 이미지뷰를 사용하며, 태그는 ```<ImageView>```를 사용합니다.

* 스크롤을 만들고 싶다면 뷰를 ```<ScrollView>```로 감싸준다.

## 소스코드

* xml 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity" >

    //상단 이미지

    <HorizontalScrollView
        android:id="@+id/horScrollView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity=""
        android:layout_weight="5">

        <ScrollView
            android:id="@+id/scrollView"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <ImageView
                android:id="@+id/imageview"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:srcCompat="@drawable/image01"/>
        </ScrollView>
    </HorizontalScrollView>
    //버튼
    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="이미지 뷰 바꾸기"
        android:layout_gravity="center"
        android:onClick="onButtonClicked"
        />

    //하단 이미지
    <HorizontalScrollView
        android:id="@+id/horScrollView2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:layout_weight="5">

        <ScrollView
            android:id="@+id/scrollView2"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <ImageView
                android:id="@+id/imageview2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:srcCompat="@drawable/image01"/>
        </ScrollView>
    </HorizontalScrollView>
</LinearLayout>
```

* java 파일

```java
package org.techtown.doitpractice03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    ScrollView scrollView;
    ImageView imageView;

    ScrollView scrollView2;
    ImageView imageView2;

    int imageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView=findViewById(R.id.scrollView);
        imageView=findViewById(R.id.imageview);
        scrollView.setHorizontalScrollBarEnabled(true);
        imageView.setVisibility(View.VISIBLE);

        scrollView2=findViewById(R.id.scrollView2);
        imageView2=findViewById(R.id.imageview2);
        scrollView2.setHorizontalScrollBarEnabled(true);
        imageView2.setVisibility(View.INVISIBLE);
    }

    public void onButtonClicked(View v){
        changeImageView();
    }

    private void changeImageView(){
        if(imageIndex==0){
            imageView.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageIndex=1;
        }
        else if(imageIndex==1){
            imageView.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageIndex=0;
        }
    }
}
```
