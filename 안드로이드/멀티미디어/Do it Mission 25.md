# 앨범 사진을 리스트로 보여주기

* 단말의 앨범에 있는 사진들을 가져와 리스트를 보여주는 기능을 만들어 보자. 앨범에 있는 사진들은 내용 제공자를 이용해 가져올 수 있다.

## 조건

* 앨범에 저장된 사진을 가져와 사진 리스트르 표시한다.

* 앨범에 들어있는 사진은 내용 제공자를 사용해 가져온다.

* 리싸이클러뷰의 한 아이템에는 사진 썸네일 이미지와 날짜를 표시한다.

* 리싸이클러뷰의 위쪽에는 전체 사진의 개수를 표시한다.

## 참고

* 단말의 사진 앱으로 사진을 찍으면 앨범에서 볼 수 있다.

* 앨범 앱을 사용하지 않고 내가 만든 앱에서 앨범에 저장된 사진을 보여줄 수 있다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <TextView
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:textSize="16dp"
        android:textColor="#000000"
        android:text="0 개"
        android:layout_alignParentTop="true"
        android:layout_alignParentRight="true"
        android:layout_margin="10dp"
        android:id="@+id/textView"/>

    <androidx.recyclerview.widget.RecyclerView
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:id="@+id/recyclerView"
        android:layout_above="@+id/button"
        android:layout_below="@+id/textView"/>

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:text="새로고침"
        android:layout_margin="10dp"
        android:id="@+id/button"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"/>

</RelativeLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.mission25;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    TextView textView;
    RecyclerView recyclerView;
    PhotoAdapter adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int pictureCount = 0;

    ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PhotoAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnPhotoItemClickListener() {
            @Override
            public void onItemClick(PhotoAdapter.ViewHolder holder, View view, int position) {
                Photo item = adapter.getItem(position);

                Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + item.getDisplayName(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Photo> result = queryAllPhotos();

                adapter.setItems(result);
                adapter.notifyDataSetChanged();
            }
        });

        ArrayList<Photo> result = queryAllPhotos();
        adapter.setItems(result);
        adapter.notifyDataSetChanged();

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    private ArrayList<Photo> queryAllPhotos() {
        ArrayList<Photo> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnDataIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnNameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int columnDateIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);

        pictureCount = 0;
        while (cursor.moveToNext()) {
            String path = cursor.getString(columnDataIndex);
            String displayName = cursor.getString(columnNameIndex);
            String outDate = cursor.getString(columnDateIndex);
            String addedDate = dateFormat.format(new Date(new Long(outDate).longValue() * 1000L));

            if (!TextUtils.isEmpty(path)) {
                Photo photo = new Photo(path, displayName, addedDate);
                result.add(photo);
            }

            pictureCount++;
        }

        textView.setText(pictureCount + " 개");
        Log.d("MainActivity", "Picture count : " + pictureCount);

        for (Photo info : result) {
            Log.d("MainActivity", info.toString());
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }

}
```

### 포토 클래스

```java
package org.techtown.mission25;

public class Photo {
    String path;
    String displayName;
    String dateAdded;

    public Photo(String path, String displayName, String dateAdded) {
        this.path = path;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "path='" + path + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}
```

### 포토 어댑터 클래스

```java
package org.techtown.mission25;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> implements OnPhotoItemClickListener {
    ArrayList<Photo> items = new ArrayList<Photo>();

    OnPhotoItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.photo_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        Photo item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Photo item) {
        items.add(item);
    }

    public void setItems(ArrayList<Photo> items) {
        this.items = items;
    }

    public Photo getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Photo item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnPhotoItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(PhotoAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ImageView imageView;

        BitmapFactory.Options options = new BitmapFactory.Options();

        public ViewHolder(View itemView, final OnPhotoItemClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);

            options.inSampleSize = 12;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Photo item) {
            textView.setText(item.getDateAdded());

            Bitmap bitmap = BitmapFactory.decodeFile(item.getPath(), options);
            imageView.setImageBitmap(bitmap);
        }

    }

}
```

### 포토 아이템 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
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
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <ImageView
                android:id="@+id/imageView"
                android:layout_width="80dp"
                android:layout_height="80dp"
                android:layout_weight="1"
                android:padding="5dp"
                tools:srcCompat="@tools:sample/avatars" />

            <TextView
                android:id="@+id/textView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom|right"
                android:layout_weight="1"
                android:gravity="right"
                android:text="날짜"
                android:textSize="24sp" />

        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest package="org.techtown.mission25" xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>


    <application android:theme="@style/AppTheme" android:supportsRtl="true" android:roundIcon="@mipmap/ic_launcher_round" android:label="@string/app_name" android:icon="@mipmap/ic_launcher" android:allowBackup="true">


    <activity android:name=".MainActivity">


    <intent-filter>

    <action android:name="android.intent.action.MAIN"/>

    <category android:name="android.intent.category.LAUNCHER"/>

</intent-filter>

</activity>

</application>

</manifest>
```

### 그래들 파일

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.mission25"
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

allprojects {
    repositories {
        maven { url 'https://jitpack.io'}
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.pedroSG94:AutoPermissions:1.0.3'
}
```
