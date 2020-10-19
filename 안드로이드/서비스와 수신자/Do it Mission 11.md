# 서비스 실행하고 화면에 보여주기

* 버튼을 눌렀을 때 서비스를 실행하고 서비스에서 보내오는 글자를 화면에 보여주기

## 조건

* 화면에 버튼 하나와 입력상자 그리고 텍스트뷰를 배치한다.

* 버튼을 누르면 입력상자의 글자를 가져와 서비스를 실행하면서 보내준다.

* 서비스에서는 다시 MainActivity 화면으로 받은 글자를 보내준다.

* MainActivity 화면에서는 서비스로부터 받은 텍스트를 화면의 텍스트뷰에 표시한다.

## 참고

* 입력상자의 글자를 바로 텍스트뷰에 보여주는 것이 아니라 서비스로 보냈다가 다시 받아서 보여주도록 하기

## 소스코드

### 메인 액티비티에서 서비스로 에디트 텍스트에 있는 글귀를 인텐트에 넣어서 보내기

```java
public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. EditText에 있는 글귀를 서비스로 보내기
                String text=editText.getText().toString();//1.1 텍스트뷰에 있는 값을 가져오기

                //1.2 인텐트 객체 만들고 부가 데이터를 넣는다.
                Intent intent=new Intent(getApplicationContext(),MyService.class);
                intent.putExtra("command","show");
                intent.putExtra("text",text);

                startService(intent);//1.3 해당 인텐트 객체를 보내면서 서비스를 시작한다.
            }
        });
중략...
```

### 액티비티에서 받은 인텐트(글귀)를 서비스에서 다시 액티비티로 보내기

```java
package org.techtown.mission11;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //2. 서비스에서 텍스트 뷰로 인텐트 보내기
        if(intent==null){
            return Service.START_STICKY;
        }
        else{
            //2.1 인텐트에서 부가 데이터 가져오기
            String command=intent.getStringExtra("command");
            String text=intent.getStringExtra("text");

            //2.2 액티비티를 띄우기 위한 인텐트 객체 만들기
            Intent showIntent=new Intent(getApplicationContext(),MainActivity.class);

            //2.3 인텐트에 플래그 추가
            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showIntent.putExtra("command","show");
            showIntent.putExtra("text",text);

            startActivity(showIntent);//2.4 액티비티 시작하기
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
```

### 서비스에서 받은 인텐트(글귀)를 텍스트뷰에 보여주기

```java
중략...
        //3. 서비스에서 받은 인텐트를 텍스트뷰에 보여주기
        textView=findViewById(R.id.textView);

        //3.1 액티비티가 새로 만들어질 때 전달된 인텐트 처리하기
        Intent passedIntent=getIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //3.1 액티비티가 이미 만들어져 있을 때 전달된 인텐트 처리하기
        processIntent(intent);
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent){
        if(intent != null){
            //3.2 인텐트에서 부가데이터를 받기
            String text=intent.getStringExtra("text");

            textView.setText(text);//3.3 받은 부가데이터를 textView에 보여주기
        }
    }
}
```
