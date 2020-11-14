# AsyncTask

* 핸들러를 사용하지 않고 좀 더 간단하게 스레드를 다룰 수 있는 방법도 있다.

* AsyncTask 클래스를 상속하여 새로운 클래스를 만들면 그 안에 스레드를 위한 코드와 UI 접근 코드를 한꺼번에 넣을 수 있다. 따라서, 스레드로 처리해야하는 코드를 하나의 AsyncTask 클래스로 정의할 수 있다는 장점이 생긴다.

* AsyncTask 객체를 만들고 ```execute()``` 메서드를 실행하면 이 객체는 정의된 백그라운드 작업을 수행하고 필요한 경우에 그 결과를 메인 스레드에서 실행하므로 UI 객체에 접근하는 데 문제가 없다.

* AsyncTask 클래스를 상속하여 새로운 클래스를 정의하면 그 내부에서 필요한 경우마다 콜백 메서드들이 자동으로 호출된다.

* ```doInBackground()``` 메서드에는 새로 만들어진 스레드에서 실행되어야할 코드들을 넣을 수 있다. 즉, 스레드에서 동작하는 것이다.

* ```onPreExecute()```, ```onProgressUpdate()```, ```onPostExecute()``` 메서드는 새로 만든 스레드가 아닌 메인 스레드에서 실행된다. 따라서 UI 객체에 자유롭게 접근할 수 있다.

* 결국 하나의 클래스 안에 스레드에서 동작해야 하는 작업과 그 작업 결과를 UI 객체에 반영하는 코드를 같이 구현할 수 있다.

* AsyncTask 객체의 ```cancel()``` 메서드를 호출하면 작업을 취소할 수 있는데, 이 메서드로 작업을 취소했을 때는 ```onCancelled()``` 메서드가 호출된다. 작업의 진행 상황을 확인하고 싶을 때는 AsyncTask 객체의 ```getStatus()``` 메서드를 사용할 수 있다.

* 위의 메서드를 호출했을 때 반환되는 AsyncTask.Status 객체는 상태를 표현하고 있으며, 각각의 상태는 PENDING(작업 시작 안함), RUNNING(실행 중), FINISHED(종료)로 구분된다.

## 실습

```java
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    BackgroundTask task;
    int value;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.progressBar);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task=new BackgroundTask();
                task.execute();
            }
        });

        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.cancel(true);
            }
        });
    }

    class BackgroundTask extends AsyncTask<Integer, Integer, Integer>{
        protected void onPreExecute(){
            value=0;
            progressBar.setProgress(value);
        }

        protected Integer doInBackground(Integer ... values){
            while(isCancelled() == false){
                value++;
                if(value>=100){
                    break;
                }
                else{
                    publishProgress(value);
                }

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){}
            }
            return value;
        }

        protected void onProgressUpdate(Integer ... values){
            progressBar.setProgress(values[0].intValue());
        }

        protected void onPostExecute(Integer result){
            progressBar.setProgress(0);
        }

        protected void onCancelled(){
            progressBar.setProgress(0);
        }
    }
}
```

* 백그라운드 작업을 수행할 클래스는 BackgroundTask라는 이름의 클래스로 정의하고 AsyncTask 클래스를 상속 받는다. 이 클래슨느 추상 클래스이므로 새로 정의한 클래스에서는 필요한 메서드를 다시 정의하여 사용한다.

* 이 때, AsyncTask 클래스를 상속하는 코드를 보면 <>안에 세 개의 자료형이 명시되어 있다. 이 자료형은 이 클래스를 상속하면서 재정의할 새로운 클래스의 메서드가 어떤 자료형의 파라미터를 가질것인지를 알려준다.

* <>안에 명시한 자료형은 그 순서에 따라 각각 ```doInBackground()```, ```onProgressUpdate()```, ```onPostExecute()``` 메서드의 각각의 파라미터를 결정한다.

* 먼저 ```onPreExecute()``` 메서드는 초기화 단계에서 사용되므로 값을 저장하기 위해 메인 액티비티에 정의한 value 변수의 값을 0으로 초기화하고 프로그레스바의 값도 0으로 만들어 준다.

* ```onInBackground()``` 메서드는 주된 작업을 실행하는데 사용되므로 while구문으로 value값을 1씩 증가시키도록 한다. 루프를 빠져나오는 조건은 작업이 취소되거나 value값이 100 이상일 때 이므로 루프는 100번 수행된다.

* 중간중간 진행상태를 UI에 업데이트하도록 만들기 위해 ```publishProgress()``` 메서드를 호출한다.

* ```onProgressUpdate()``` 메서드는 ```doInBackgroun()``` 메서드 안에서 ```publishProgress()``` 메서드가 호출될 때마다 자동으로 호출된다. 이 안에서는 프로그레스바의 값을 전달된 파라미터의 값으로 설정한다.
