# 책 정보 저장과 조회 기능 만들기

* 탭으로 구성된 화면을 만들고 첫 번째 탭 화면에서는 책 정보를 저장, 두 번째 탭 화면에서는 책 정보를 조회할 수 있도록 한다.

## 조건

* 탭 모양의 화면을 구성하고 두 개의 탭 버튼을 추가한다.

* 첫 번째 탭에서는 책 정보를 저장할 수 있도록 한다. 이 화면은 Do it Mission 21의 미션과 동일하다.

* 두 번째 탭에서는 책 정보를 조회하여 리스트로 보여줄 수 있도록 한다. 리싸이클러뷰의 각 아이ㅏ템에는 책 제목과 저자가 표시된다.

* 책 정보를 저장하거나 조회할 때 모두 데이터베이스를 사용한다. 즉, 책 정보를 저장할 때는 데이터베이스에 저장하고 조회할 때는 데이터베이스에 저장된 데이터를 가져와서 보여준다.

## 참고

* 데이터베이스와 테이블은 미리 만들어져 있어야 한다.

* 앱이 실행될 때 데이터베이스 열기가 미리 진행되어야 한다.

* 책 정보를 저장할 대는 INSERT SQL 문을 사용하고 책 정보를 조회할 때는 SELECT SQL 문을 사용한다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity" >

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_height="match_parent"
        android:layout_width="match_parent">
        <com.google.android.material.appbar.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">
            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@color/colorPrimaryDark"
                android:elevation="1dp"
                android:theme="@style/ThemeOverlay.AppCompat.Dark">

                <TextView
                    android:id="@+id/titleText"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="책 정보 입력 및 조회"
                    android:textAppearance="@style/Base.TextAppearance.Widget.AppCompat.Toolbar.Title"/>

            </androidx.appcompat.widget.Toolbar>

            <com.google.android.material.tabs.TabLayout
                android:id="@+id/tabs"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@android:color/background_light"
                android:elevation="1dp"
                app:tabGravity="fill"
                app:tabMode="fixed"
                app:tabSelectedTextColor="@color/colorAccent"
                app:tabTextColor="@color/colorPrimary" />
        </com.google.android.material.appbar.AppBarLayout>

        <FrameLayout
            android:id="@+id/container"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"></FrameLayout>
    </androidx.coordinatorlayout.widget.CoordinatorLayout>
</RelativeLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.mission22;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnDatabaseCallback, AutoPermissionsListener {
    private static final String TAG = "MainActivity";
    Toolbar toolbar;

    InputFragment inputFragment;
    SearchFragment searchFragment;

    BookDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        inputFragment=new InputFragment();
        searchFragment=new SearchFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,inputFragment).commit();

        TabLayout tabs=findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("입력"));
        tabs.addTab(tabs.newTab().setText("조회"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : " + position);

                Fragment selected=null;
                if(position==0){
                    selected=inputFragment;
                }
                else if(position==1){
                    selected=searchFragment;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.d(TAG, "Book database is open.");
        } else {
            Log.d(TAG, "Book database is not open.");
        }

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }


    protected void onDestroy() {
        // close database
        if (database != null) {
            database.close();
            database = null;
        }

        super.onDestroy();
    }

    @Override
    public void insert(String name, String author, String contents) {
        database.insertRecord(name, author, contents);
        Toast.makeText(getApplicationContext(), "책 정보를 추가했습니다.", Toast.LENGTH_LONG).show();
    }

    @Override
    public ArrayList<Book> selectAll() {
        ArrayList<Book> result = database.selectAll();
        Toast.makeText(getApplicationContext(), "책 정보를 조회했습니다.", Toast.LENGTH_LONG).show();

        return result;
    }
}
```

### 데이터 입력 프래그먼트 XML 레이아웃

```xml
<RelativeLayout
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <EditText
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:padding="20dp"
        android:textColor="#000000"
        android:textSize="22sp"
        android:hint="제목"
        android:layout_alignParentTop="true"
        android:id="@+id/editText"/>

    <EditText
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:padding="20dp"
        android:textColor="#000000"
        android:textSize="22sp"
        android:hint="저자"
        android:id="@+id/editText2"
        android:layout_below="@+id/editText"/>

    <EditText
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:padding="20dp"
        android:textColor="#000000"
        android:textSize="22sp"
        android:hint="내용"
        android:id="@+id/editText3"
        android:layout_below="@+id/editText2"
        android:gravity="left|top"
        android:layout_above="@+id/button"/>

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:id="@+id/button"
        android:text="저장"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:layout_margin="10dp"/>

</RelativeLayout>
```

### 데이터 입력 프래그먼트 클래스

```java
package org.techtown.mission22;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {//데이터베이스에 책 정보를 저장하기 위한 프래그먼트
    //에디트 텍스트 변수
    EditText editText;
    EditText editText2;
    EditText editText3;

    OnDatabaseCallback callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        callback = (OnDatabaseCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup) inflater.inflate(R.layout.input_fragment,container,false);

        editText = (EditText) rootView.findViewById(R.id.editText);
        editText2 = (EditText) rootView.findViewById(R.id.editText2);
        editText3 = (EditText) rootView.findViewById(R.id.editText3);

        Button button=(Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String author = editText2.getText().toString();
                String contents = editText3.getText().toString();

                callback.insert(name, author, contents);

                Toast.makeText(getContext(), "책 정보를 추가했습니다.", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
```

### 데이터 조회 프래그먼트 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    android:background="#ffbfa1"
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@+id/button" />

    <Button
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:id="@+id/button"
        android:text="새로고침"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:layout_margin="10dp"/>

</RelativeLayout>
```

### 데이터 조회 프래그먼트 클래스

```java
package org.techtown.mission22;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {//데이터베이스의 책 정보를 조회하기 위한 조회 창을 구성하기 위한 프래그먼트
    RecyclerView recyclerView;
    BookAdapter adapter;

    OnDatabaseCallback callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        callback = (OnDatabaseCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.search_fragment,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        ArrayList<Book> result = callback.selectAll();
        adapter.setItems(result);

        adapter.setOnItemClickListener(new OnBookItemClickListener() {
            @Override
            public void onItemClick(BookAdapter.ViewHolder holder, View view, int position) {
                Book item = adapter.getItem(position);

                Toast.makeText(getContext(), "아이템 선택됨 : " + item.getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Book> result = callback.selectAll();
                adapter.setItems(result);
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }
}
```

### 책 클래스

```java
package org.techtown.mission22;

public class Book {
    String title;
    String author;
    String contents;

    public Book(String title, String author, String contents) {
        this.title = title;
        this.author = author;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
```

### 책 어댑터 클래스

```java
package org.techtown.mission22;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements OnBookItemClickListener{
    ArrayList<Book> items=new ArrayList<Book>();
    OnBookItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.book_item,parent,false);//인플레이션을 통해 뷰 객체 만들기

        return new ViewHolder(itemView,this);
    }

    public void setOnItemClickListener(OnBookItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Book item){
        items.add(item);
    }

    public void setItems(ArrayList<Book> items){
        this.items=items;
    }

    public Book getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Book item){
        items.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView, final OnBookItemClickListener listener){
            super(itemView);

            textView=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {//아이템뷰에 OnClickListener 설정하가ㅣ
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(listener!=null){
                        listener.onItemClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(Book item){
            textView.setText(item.getTitle());
            textView2.setText(item.getAuthor());
        }
    }

}
```

### 책 데이터베이스 클래스

```java
package org.techtown.mission22;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BookDatabase {//책 정보를 저장할 데이터베이스 클래스
    private static final String TAG = "BookDatabase";


    private static BookDatabase database;//싱글톤 인스턴스

    public static String DATABASE_NAME = "book.db";//데이터 베이스 이름
    public static String TABLE_BOOK_INFO = "BOOK_INFO";//테이블 이름
    public static int DATABASE_VERSION = 1;//데이터베이스 버전


    private DatabaseHelper dbHelper;//헬퍼 클래스 정의
    private SQLiteDatabase db;//데이터베이스 객체

    private Context context;//컨텍스트 객체

    //생성자
    private BookDatabase(Context context) {
        this.context = context;
    }


    public static BookDatabase getInstance(Context context) {
        if (database == null) {
            database = new BookDatabase(context);
        }

        return database;
    }

    //데이터베이스 열기
    public boolean open() {
        println("opening database [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    //데이터 베이스 닫기
    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        db.close();
        database = null;
    }

    //입력받은 SQL을 통해서 raw query를 실행한다.
    //결과를 fetch한 이후 커서를 닫는다.
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    //헬퍼 클래스
    private class DatabaseHelper extends SQLiteOpenHelper {
        //생성자
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //데이터베이스가 생성됬을 경우
        public void onCreate(SQLiteDatabase _db) {
            // TABLE_BOOK_INFO
            println("creating table [" + TABLE_BOOK_INFO + "].");

            //기존에 있던 테이블 삭제
            String DROP_SQL = "drop table if exists " + TABLE_BOOK_INFO;
            try {
                _db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            // 테이블 생성
            String CREATE_SQL = "create table " + TABLE_BOOK_INFO + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  NAME TEXT, "
                    + "  AUTHOR TEXT, "
                    + "  CONTENTS TEXT, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                _db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            // 5개의 레코드 삽입
            insertRecord(_db, "Do it! 안드로이드 앱 프로그래밍", "정재곤", "안드로이드 기본서로 이지스퍼블리싱 출판사에서 출판했습니다.");
            insertRecord(_db, "Programming Android", "Mednieks, Zigurd", "Oreilly Associates Inc에서 출판했습니다.");
            insertRecord(_db, "모바일 프로그래밍", "이병옥,최성민 공저", "에이콘출판사에서 출판했습니다.");
            insertRecord(_db, "시작하세요! 안드로이드 게임 프로그래밍", "마리오 제흐너 저", "위키북스에서 출판했습니다.");
            insertRecord(_db, "실전! 안드로이드 시스템 프로그래밍 완전정복", "박선호,오영환 공저", "DW Wave에서 출판했습니다.");

        }

        //데이터베이스가 열렸을 경우
        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + DATABASE_NAME + "].");

        }

        //데이터베이스가 갱신됐을 경우
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

            if (oldVersion < 2) {   // version 1

            }

        }

        //데이터베이스에 레코드가 삽입됬을 경우
        private void insertRecord(SQLiteDatabase _db, String name, String author, String contents) {
            try {
                _db.execSQL( "insert into " + TABLE_BOOK_INFO + "(NAME, AUTHOR, CONTENTS) values ('" + name + "', '" + author + "', '" + contents + "');" );
            } catch(Exception ex) {
                Log.e(TAG, "Exception in executing insert SQL.", ex);
            }
        }

    }

    public ArrayList<Book> selectAll() {
        ArrayList<Book> result = new ArrayList<Book>();

        try {
            Cursor cursor = db.rawQuery("select NAME, AUTHOR, CONTENTS from " + TABLE_BOOK_INFO, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                String author = cursor.getString(1);
                String contents = cursor.getString(2);

                Book book = new Book(name, author, contents);
                result.add(book);
            }

        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }

        return result;
    }

    public void insertRecord(String name, String author, String contents) {
        try {
            db.execSQL( "insert into " + TABLE_BOOK_INFO + "(NAME, AUTHOR, CONTENTS) values ('" + name + "', '" + author + "', '" + contents + "');" );
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }
}
```

### 책 정보 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
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
                android:padding="5dp"
                app:srcCompat="@android:drawable/ic_input_get" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_margin="5dp"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="제목"
                    android:textSize="20sp" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="저자"
                    android:textColor="#FF0000FF"
                    android:textSize="15sp" />
            </LinearLayout>
        </LinearLayout>
    </androidx.cardview.widget.CardView>

</LinearLayout>
```

### 책 아이템 클릭 리스너 인터페이스

```java
package org.techtown.mission22;

import android.view.View;

public interface OnBookItemClickListener {
    public void onItemClick(BookAdapter.ViewHolder holder, View view,int position);
}
```

### 데이터베이스 콜백 리스너 인터페이스

```java
package org.techtown.mission22;

import java.util.ArrayList;

public interface OnDatabaseCallback {
    public void insert(String name, String author, String contents);
    public ArrayList<Book> selectAll();
}
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission22">

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoitMission22">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### build.gradle(Module:app)

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.mission22"
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
        maven { url 'https://jitpack.io' }
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
