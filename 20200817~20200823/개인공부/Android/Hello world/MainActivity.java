package org.techtown.hello;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//이 코드는 부모 클래승 있는 onCreate()함수를 호출한다. 즉, 화면에 메시지를 보여주는 기능과는 상관이 없다.
        setContentView(R.layout.activity_main);//setContentView():화면에 무엇을 보여줄 것인지를 설정해주는 함수
        //R.layout.activity_main은 화면의 구성 요소에 대한 정보(activity_main.xml파일을 가리킨다.)
    }

    //버튼 클릭시 메시지 출력
    public void onButton1Clicked(View v){
        Toast.makeText(this,"확인 1 버튼이 눌렸어요.",Toast.LENGTH_LONG).show();
    }

    //버튼 클릭시 네이버 접속
    public void onButton2Clicked(View v){
        Intent myIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"));
        startActivity(myIntent);
    }

    //버튼 클릭시 전화앱으로 연결
    public void onButton3Clicked(View v){
        Intent myIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("tel:010-1000-1000"));
        startActivity(myIntent);
    }
}
