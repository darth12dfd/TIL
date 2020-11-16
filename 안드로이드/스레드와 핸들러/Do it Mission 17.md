# 패널을 번갈아가며 보여주기

* 고객 정보를 보여주는 두 개의 패널을 만들고 각 패널을 번갈아가면서 보여주도록 애니메이션을 적용하기

* 이 때 애니메이션은 우측에서 좌측으로 이동하도록 적용할 수 있다.

## 조건

* 고객 정보를 보여주는 패널 화면을 구성한다.

* 패널은 두 개를 만들고 한 번에 하나의 패널이 보이도록 한다.

* 패널 위쪽에는 고객 이름, 그 아래에는 연락처와 주소가 간단하게 보이도록 한다.

* 패널을 하나씩 보여주되 5초 간격으로 다음 패널이 보이도록 애니메이션을 적용한다. 애니메이션은 오른쪽에서 왼쪽으로 이동하는 애니메이션을 적용한다.

## 참고

* 오른쪽에서 왼쪽으로 움직이는 애니메이션 동작을 우선 res/anim 폴더 안에 xml 파일로 정의한 후 패널에 적용시킨다.

## 소스코드

### 애니메이션 리소스 파일

#### 퇴장 애니메이션

```xml
<?xml version="1.0" encoding="UTF-8"?>
<set android:interpolator="@android:anim/accelerate_decelerate_interpolator"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="2000"
        android:toXDelta="-100%p"
        android:fromXDelta="0%p"/>
</set>
```

#### 등장 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/accelerate_decelerate_interpolator">
    <translate
        android:duration="2000"
        android:toXDelta="0%p"
        android:fromXDelta="100%p"/>
</set>
```

### 메인 액티비티

#### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity" >

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="시작" />

        <Button
            android:id="@+id/button2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="종료" />
    </LinearLayout>

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="20dp"
        android:text="고객 정보"
        android:textSize="24sp" />

    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    </FrameLayout>
</LinearLayout>
```

#### 메인 액티비티 소스파일

```java
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    //카드뷰들을 담아둘 컨테이너
    FrameLayout container;

    //버튼
    Button button1;
    Button button2;

    //애니메이션
    Animation translateIn;
    Animation translateOut;

    int selected=1;

    //카드뷰
    CustomerItem view1;
    CustomerItem view2;

    Handler handler=new Handler();//핸들러

    boolean isRunning=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 아이템 뷰들을 컨테이너에 저장
        container=findViewById(R.id.container);

        view1=new CustomerItem(this);
        view1.setName("이규원");
        view1.setPhone("010-5226-9456");
        view1.setAddress("용인시");
        view1.setImageView(R.drawable.customer);
        container.addView(view1);

        view2=new CustomerItem(this);
        view2.setName("조인성");
        view2.setPhone("010-2901-3109");
        view2.setAddress("양주시");
        view2.setImageView(R.drawable.customer);
        container.addView(view2);

        //2. 애니메이션 로딩
        translateIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_in);
        translateOut=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_out);

        //3. 버튼
        button1=findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationThread thread=new AnimationThread();
                thread.start();
            }
        });

        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning=false;
            }
        });
    }

    class AnimationThread extends Thread{
        public void run(){
            isRunning=true;
            while (isRunning) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (selected == 0) {
                            view1.startAnimation(translateIn);
                            view2.startAnimation(translateOut);
                        }
                        else if(selected==1){
                            view1.startAnimation(translateOut);
                            view2.startAnimation(translateIn);
                        }
                    }
                });
                selected++;
                if(selected>1){
                    selected=0;
                }
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                }
            }
        }
    }
}
```

### 고객 관리 액티비티

#### 고객 정보 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardBackgroundColor="#FFFFFFFF"
        app:cardCornerRadius="10dp"
        app:cardElevation="5dp"
        app:cardUseCompatPadding="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <ImageView
                android:id="@+id/imageView"
                android:layout_width="80dp"
                android:layout_height="80dp"
                android:padding="5dp"
                tools:srcCompat="@drawable/customer" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginLeft="10dp"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:hint="이름"
                    android:textSize="30sp" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_alignParentRight="true"
                    android:layout_weight="1"
                    android:hint="전화번호"
                    android:textColor="#FF0000FF"
                    android:textSize="20sp" />

                <TextView
                    android:id="@+id/textView3"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_alignParentRight="true"
                    android:layout_weight="1"
                    android:hint="주소"
                    android:textColor="#FFFF0000"
                    android:textSize="20sp" />
            </LinearLayout>
        </LinearLayout>

    </androidx.cardview.widget.CardView>
</LinearLayout>
```

#### 고객 정보 소스파일

```java
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerItem extends LinearLayout {
    ImageView imageView;
    TextView textView1;
    TextView textView2;
    TextView textView3;


    public CustomerItem(Context context) {
        super(context);
        init(context);
    }

    public CustomerItem(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//인플레이션 지냏ㅇ하기
        inflater.inflate(R.layout.customer_item,this,true);

        imageView=findViewById(R.id.imageView);
        textView1=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        textView3=findViewById(R.id.textView3);
    }

    public void setName(String name){
        textView1.setText(name);
    }

    public void setPhone(String phone){
        textView2.setText(phone);
    }

    public void setAddress(String address){
        textView3.setText(address);
    }

    public void setImageView(int res){
        imageView.setImageResource(res);
    }
}

```
