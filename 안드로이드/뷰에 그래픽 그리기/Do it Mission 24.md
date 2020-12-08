# 빨간 사각형을 터치해서 움직이기

* 패널에 빨간색 사각형을 만든 후 손가락으로 터치하여 드래그하면 사각형이 손가락과 함께 움직이도록 만들기

## 조건

* 화면에는 패널 하나가 보이도록 한 다음 이 패널 안에 빨간색 사각형을 하나 그려넣는다.

* 화면이 처음 띄워졌을 때 빨간 사각형이 보이는 위치는 화면의 가운데 도는 좌측 상단으로 지정한다.

* 손가락을 터치하여 움직이면 빨간색 사각형이 손가락의 움직임에 따라서 이동하도록 만든다.

## 참고

* 손가락으로 터치하여 선을 그렸던 것과 같은 원리로 소스코드를 구성하면 된다.

## 소스코드

### 직사각형 뷰 클래스

```java
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RectangleView extends View {
    private Paint paint;//빨간색 사각형을 그릴 페인트 객체
    private Paint basePaint;//빨간색 사각형을 지울 페인트 객체

    //빨간색 사각형과 관련된 정보
    int rectWidth = 200;//너비
    int rectHeight = 200;//높이
    int curX = 100;//사각형의 현재 X 좌표 값
    int curY = 100;//사각형의 현재 Y 좌표 값

    //사각형뷰의 생성자
    public RectangleView(Context context) {
        super(context);

        init(context);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }
    
    //생성자 호출 시 동시 선언되는 초기화 함수
    private void init(Context context) {
        //빨간색 직사각형 페인트 객체 생성
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        //하얀색 직사각형 페인트 객체 생성
        basePaint = new Paint();
        basePaint.setStyle(Paint.Style.FILL);
        basePaint.setColor(Color.WHITE);
    }

    //그리기 메서드
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), basePaint);//하얀색 페인트 객체로 화면 전체를 하얀색으로 세팅한다.
        canvas.drawRect(curX, curY, curX + rectWidth, curY + rectHeight, paint);//그 다음 터치 이벤트가 발생한 X,Y 좌표값에 빨간색 사각형을 세팅한다. 
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {//터치후 손가락을 뗐을 때, 그 이벤트가 발생한 X,Y 좌표값을 저장
            curX = (int) event.getX();
            curY = (int) event.getY();

            invalidate();
        } else if (action == MotionEvent.ACTION_MOVE) {//터치후 드래그 중일 때, 그 이벤트가 발생한 X,Y 좌표값을 저장
            curX = (int) event.getX();
            curY = (int) event.getY();

            invalidate();
        } else if (action == MotionEvent.ACTION_UP) {

        }

        return true;
    }

}
```

### 메인 액티비티 클래스

```java
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RectangleView view=new RectangleView(this);
        setContentView(view);
    }
}
```
