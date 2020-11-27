# 책 정보 저장 기능 만들기

* 사용자가책 정보를 입력하면 데이터베이스에 저장하는 기능을 만들기

* 사용자가 입력한 데이터는 앱을 종료하고 다시 실행했을 때도 그대로 단말에 저장되어 있어야 한다.

## 조건

* 책 정보를 저장할 수 있는 화면을 액티비티로 만든다.

* 책 정보는 제목, 저자, 내용을 입력할 수 있도록 한다.

* 저장 버튼을 누르면 데이터베이스에 저장되도록 한다.

* 정상적으로 저장되었다면, 간단한 토스트 메시지를 띄워준다.

## 참고

* 데이터베이스와 테이블은 미리 만들어져 있어야 한다.

* 그리고 앱이 실행될 때 데이터베이스 열기가 미리 진행되어야 한다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity" >

    <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:hint="제목"
        android:inputType="textPersonName" />

    <EditText
        android:id="@+id/editText2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:hint=" 저자"
        android:inputType="textPersonName" />

    <EditText
        android:id="@+id/editText3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:hint="내용"
        android:inputType="textPersonName" />

    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="저장" />
</LinearLayout>
```

### 안드로이드 매니페스트 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.mission21">

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DoItMission21">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### build.gradle(Module:app) 파일

```gradle
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "org.techtown.mission21"
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

### 책 데이터베이스 클래스

```java
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookDatabase {
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

### 메인 액티비티 클래스

```java
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissionsListener;
import com.pedro.library.AutoPermissions;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private static final String TAG="MainActivity";

    BookDatabase database;

    EditText editText;
    EditText editText2;
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);

        //데이터 베이스 열기
        if(database!=null){
            database.close();
            database=null;
        }

        database=BookDatabase.getInstance(this);
        boolean isOpen=database.open();
        if(isOpen){
            Log.d(TAG,"Book database is open");
        }
        else{
            Log.d(TAG,"Book database is open");
        }


        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=editText.getText().toString();
                String author=editText2.getText().toString();
                String contents=editText3.getText().toString();

                database.insertRecord(name,author,contents);
                Toast.makeText(getApplicationContext(),"책 정보를 추가했습니다.",Toast.LENGTH_LONG).show();

            }
        });
        
        AutoPermissions.Companion.loadAllPermissions(this,101);//위험 권한 자동 부여 요청하기
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

    protected void onDestroy() {
        //데이터 베이스 닫기
        if (database != null) {
            database.close();
            database = null;
        }
        super.onDestroy();
    }
}
```
