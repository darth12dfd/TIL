#  앨범의 사진을 애니메이션으로 보여주기

* 단말의 앨범에 있는 사진을 가져와서 하나씩 보여주는 기능을 만들어보기.

* 각각의 사진이 보일 때는 우측에서 좌측으로 애니메이션이 적용되도록 한다.

## 조건

* 단말의 앨범에 들어있는 사진 정보를 가져와서 화면에 보여준다.

* 화면에는 두 개의 사진이 한 번에 보이도록 하고 두 개의 사진이 들어 있는 패널은 애니메이션을 적용해서 다음 패널로 넘어가도록 한다.

* 화면의 위쪽에는 '현재 사진의 순서/사진의 전체 개수' 정보를 표시한다.

* 하나의 사진 정보에는 왼쪽에 이미지, 오른쪽에 날짜가 표시되도록 한다.

* 5초마다 사진 정보가 바뀌도록 애니메이션을 설정한다.

## 참고

* 단말의 앨범에 들어있는 사진은 내용 제공자를 사용해서 가져올 수 있다.

* 사진 썸네일은 패널 위에 보이도록 만들 수 있다.

## 소스코드

* 앱 외부에서 데이터를 안전하게 가져오기 위한 위험권한 설정, 비트맵 객체, 스레드와 핸들러, 그리고 내용 제공자 등의 내용을 숙지해야 한다.

### 메인 액티비티 XML 레이아웃

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
        android:gravity="right"
        android:text="0 개"
        android:textSize="30sp" />

    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    </FrameLayout>
</LinearLayout>
```

### 사진 아이템 XML 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>
<LinearLayout android:orientation="horizontal"
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.cardview.widget.CardView
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        app:cardUseCompatPadding="true"
        app:cardElevation="5dp"
        app:cardCornerRadius="10dp"
        app:cardBackgroundColor="#FFFFFFFF">

        <LinearLayout
            android:orientation="horizontal"
            android:layout_height="wrap_content"
            android:layout_width="match_parent">

            <ImageView
                android:layout_height="80dp"
                android:layout_width="80dp"
                android:padding="5dp"
                android:id="@+id/imageView"/>
            <LinearLayout
                android:orientation="vertical"
                android:layout_height="match_parent"
                android:layout_width="match_parent"
                android:layout_marginLeft="10dp">

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:gravity="center_horizontal|center_vertical"
                    android:hint="날짜"
                    android:textSize="30sp" />
            </LinearLayout>
        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### 이미지 정보 클래스

```java
package org.techtown.mission18;

public class ImageInfo {

    String path;
    String dateAdded;

    public ImageInfo(String path, String dateAdded) {
        this.path = path;
        this.dateAdded = dateAdded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "path='" + path + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }

}
```

### 사진 아이템 뷰 클래스

```java
package org.techtown.mission18;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PictureItemView extends LinearLayout {
    ImageView imageView;
    TextView textView;

    BitmapFactory.Options options = new BitmapFactory.Options();

    public PictureItemView(Context context) {
        super(context);
        init(context);
    }

    public PictureItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//인플레이션 진행하기
        inflater.inflate(R.layout.picture_item,this,true);

        textView=findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView);

        options.inSampleSize=12;
    }

    public void setImage(int resId){
        imageView.setImageResource(resId);
    }

    public void setDate(String date){
        textView.setText(date);
    }

    public void setImage(String path){
        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        imageView.setImageBitmap(bitmap);
    }

}

```

### 좌측 이동 애니메이션 XML 리소스

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

### 메인 액티비티 클래스

```java
package org.techtown.mission18;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    FrameLayout container;//카드뷰들을 담아둘 컨테이너
    TextView textView;//사진 개수 정보 저장할 변수

    ArrayList<ImageInfo> pictures;//ImageInfo를 저장할 ArrayList

    //버튼
    Button button1;
    Button button2;

    //애니메이션
    Animation translateIn;

    int selected=0;//여태까지 선택된 뷰의 수
    int picture_count=0;//총 사진의 수

    PictureItemView view;//카드뷰

    Handler handler=new Handler();//핸들러
    boolean isRunning=true;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container=findViewById(R.id.container);
        textView=findViewById(R.id.textView);

        view=new PictureItemView(this);
        view.setDate("일시");
        container.addView(view);

        translateIn=AnimationUtils.loadAnimation(this,R.anim.translate_in);

        button1=findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictures=queryAllPictures();

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

        AutoPermissions.Companion.loadAllPermissions(this,101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this, "permissions denied : " + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this, "permissions granted : " + strings.length, Toast.LENGTH_LONG).show();
    }

    class AnimationThread extends Thread{
        public void run(){
            isRunning=true;
            while (isRunning) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageInfo info=pictures.get(selected);

                        view.setDate(info.getDateAdded());
                        view.setImage(info.getPath());

                        view.startAnimation(translateIn);

                        selected++;
                        if(selected>picture_count){
                            selected=0;
                        }
                        textView.setText(selected+"/"+picture_count+"개");
                    }
                });

                try {
                    Thread.sleep(5000);
                }catch (Exception e){

                }
            }
        }
    }

    private ArrayList<ImageInfo> queryAllPictures() {
        ArrayList<ImageInfo> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DATE_ADDED };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnDataIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDateIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);

        while (cursor.moveToNext()) {
            String path = cursor.getString(columnDataIndex);
            String outDate = cursor.getString(columnDateIndex);
            String addedDate = dateFormat.format(new Date(new Long(outDate).longValue() * 1000L));

            if (!TextUtils.isEmpty(path)) {
                ImageInfo info = new ImageInfo(path, addedDate);
                result.add(info);
            }

            picture_count++;
        }

        textView.setText(selected + " / " + picture_count + " 개");
        Log.d("MainActivity", "Picture count : " + picture_count);

        for (ImageInfo info : result) {
            Log.d("MainActivity", info.toString());
        }

        return result;
    }
}
```
