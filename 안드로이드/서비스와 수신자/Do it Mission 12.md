# 서비스에서 수신자로 메시지 보내기

* 서비스에서 보낸 메시지를 액티비티 안에서 등록한 브로드캐스트 수신자를 이용해 받도록 만들기

## 조건

* 화면에 버튼 하나와 입력상자 그리고 텍스트뷰를 배치한다.

* 버튼을 누르면 입력상자의 글자를 가져와 서비스를 실행하면서 보내준다.

* 서비스에서는 다시 브로드캐스팅을 이요앻 글자를 보내준다.

* MaiActivity 화면에서는 브로드캐스트 수신자를 통해 글자를 전달받는다.

* 수신자를 통해 전달받은 글자를 화면에 있는 텍스트뷰에 표시한다.

## 참고

* 액티비티 안에서 브로드캐스트 수신자를 등록할 수 있다.

* 액티비티 안의 수신자에서 메시지를 수신하면 그 메시지를 액티비티 안의 텍스트뷰에 표시할 수 있다.

## 소스코드

### 서비스 파일

```kotlin
package org.techtown.mission12

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent==null){
            return Service.START_STICKY
        }
        else{
            val text=intent.getStringExtra("text")//액티비티에서 받은 인텐트에서 text를 추출
            val newIntent=Intent()//액티비티로 전달한 인텐트 생성

            newIntent.setAction("org.techtown.broadcast.SHOW")
            newIntent.putExtra("text",text)
            sendBroadcast(newIntent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
```

### 메인 액티비티 소스파일

````kotlin
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var editText: EditText? =null
    var textView: TextView? =null
    var receiver: MyReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText=findViewById(R.id.editText)
        textView=findViewById(R.id.textView)

        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var text=editText!!.text.toString()

            val intent= Intent(this, MyService::class.java)
            intent.putExtra("text",text)//부가 데이터 넣기
            
            startService(intent)//서비스 시작
        }

        //브로드캐스트 수신자 생성
        receiver=MyReceiver()
        val filter=IntentFilter()
        filter.addAction("org.techtown.broadcast.SHOW")
        registerReceiver(receiver, filter)

    }

    inner class MyReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                processIntent(intent)
            }
        }
    }

    private fun processIntent(intent:Intent){
        if(intent!=null){
            val text=intent.getStringExtra("text")!!
            if(text!=null){
                textView!!.text=text
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
```
