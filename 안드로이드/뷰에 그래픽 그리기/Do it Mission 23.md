# 페인트보드 앱의 설정 기능 만들기

* 페인트보드 앱에 CAP을 설정할 수 있는 기능을 만든다.

* 손가락으로 터치하여 선을 그릴 때 선이 가질 수 있는 속성중의 하나이다.

## 조건

* 페인트보드 앱은 위쪽에 버튼이 있고 아래쪽에 손가락으로 선을 그릴 수 있는 패널이 있다. 위쪽에 있는 버튼들의 아래쪽에 레이아웃을 추가하고 그 안에 CAP 스타일을 선택할 수 있는 라디오 버튼들을 배치한다.

* CAP 스타일을 표시하는 라디오 버튼을 선택하면 선을 그리는 Pain 객체에 선택한 CAP 스타일이 설정되게 한다.

* CAP 스타일을 변경한 후 손가락을 터치해 선을 그리면 설정한 선의 속성으로 그려지도록 한다.

## 참고

* 선의 속성은 여러가직 ㅏ있는데, 선의 속성을 모두 넣고 싶다면 별도의 설정 화면을 만들 수도 있다.

## 소스코드

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
        android:id="@+id/toolsLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="색상" />

        <Button
            android:id="@+id/button2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="굵기" />

        <Button
            android:id="@+id/button3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="지우기" />

        <Button
            android:id="@+id/button4"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="취소" />

    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        >

        <RadioGroup
            android:id="@+id/radioGroup"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <RadioButton
                android:id="@+id/radioButton"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                android:text="BUTT"
                android:textSize="18sp" />

            <RadioButton
                android:id="@+id/radioButton2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                android:text="ROUND"
                android:textSize="18sp" />

            <RadioButton
                android:id="@+id/radioButton3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                android:text="SQUARE"
                android:textSize="18sp" />
        </RadioGroup>
    </LinearLayout>

    <LinearLayout
        android:id="@+id/boardLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="horizontal"></LinearLayout>
</LinearLayout>
```
### 페인트보드 클래스

```java
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class PaintBoard extends View {
    public boolean changed=false;

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    float lastX;
    float lastY;

    Path mPath=new Path();

    float mCurveEndY;
    float mCurveEndX;

    int mInvalidateExtraBorder=10;

    static final float TOUCH_TOLERANCE=8;

    private static final boolean RENDERING_ANTIALIAS = true;
    private static final boolean DITHER_FLAG = true;

    private int mCertainColor = 0xFF000000;
    private float mStrokeWidth = 2.0f;

    //생성자
    public PaintBoard(Context context){
        super(context);

        mPaint=new Paint();
        mPaint.setAntiAlias(RENDERING_ANTIALIAS);
        mPaint.setColor(mCertainColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);

        this.lastX=-1;
        this.lastY=-1;
    }

    //CAP 설정하는 메서드
    public void setCAP(Paint.Cap cap){
        mPaint.setStrokeCap(cap);
    }

    //백그라운드 칠하기
    public void drawBackground(Canvas canvas){
        if (canvas != null) {//만약 캔버스 값이 null이 아닌 경우, 즉 보드에 선이 있을 경우
            canvas.drawColor(Color.WHITE);//캔버스를 흰색으로 칠한다. 즉 달리말하면 해당 선들을 지워버린다.
        }
    }

    //새로운 이미지 생성
    public void newImage(int width, int height) {
        Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);

        mBitmap = img;
        mCanvas = canvas;

        drawBackground(mCanvas);

        changed = false;
        invalidate();
    }

    //onSizeChanged 메서드
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w > 0 && h > 0) {
            newImage(w, h);
        }
    }

    //비트맵 그리기
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }


    //터치 이벤트 처리
    public boolean onTouchEvent(MotionEvent event){
        int action=event.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                changed=true;

                Rect rect=touchUp(event,false);
                if(rect!=null){
                    invalidate(rect);
                }

                mPath.rewind();

                return true;
                case MotionEvent.ACTION_DOWN:
                    rect=touchDown(event);
                    if(rect!=null){
                        invalidate(rect);
                    }

                    return true;
            case MotionEvent.ACTION_MOVE:
                rect=touchMove(event);
                if(rect!=null){
                    invalidate(rect);
                }

                return true;
        }
        return false;
    }

    private Rect touchDown(MotionEvent event){
        float x=event.getX();
        float y=event.getY();

        lastX=x;
        lastY=y;

        Rect mInvalidRect=new Rect();
        mPath.moveTo(x,y);

        final int border=mInvalidateExtraBorder;
        mInvalidRect.set((int)x-border,(int)y-border,(int)x+border,(int)y+border);
        mCurveEndX=x;
        mCurveEndY=y;

        mCanvas.drawPath(mPath,mPaint);

        return mInvalidRect;
    }

    private Rect touchMove(MotionEvent event){
        Rect rect=processMove(event);

        return rect;
    }

    private Rect touchUp(MotionEvent event,boolean cancel){
        Rect rect=processMove(event);

        return rect;
    }

    private Rect processMove(MotionEvent event){
        final float x=event.getX();
        final float y=event.getY();

        final float dx=Math.abs(x-lastX);
        final float dy=Math.abs(y-lastY);

        Rect mInvalidRect=new Rect();
        if(dx>=TOUCH_TOLERANCE || dy>=TOUCH_TOLERANCE){
            final int border=mInvalidateExtraBorder;
            mInvalidRect.set((int)mCurveEndX-border,(int)mCurveEndY-border,(int)mCurveEndX+border,(int)mCurveEndY+border);

            float cx=mCurveEndX=(x+lastX)/2;
            float cy=mCurveEndY=(y+lastY)/2;

            mPath.quadTo(lastX,lastY,cx,cy);

            mInvalidRect.union((int)lastX-border,(int)lastY-border,(int)lastX+border,(int)lastY+border);
            mInvalidRect.union((int)cx-border,(int)cy-border,(int)cx+border,(int)cy+border);

            lastX=x;
            lastY=y;

            mCanvas.drawPath(mPath,mPaint);
        }
        return mInvalidRect;
    }

    //페인트보드 정보 업데이트
    public void updatePaintProperty(int color, int size) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(size);
    }
}
```

### 메인 액티비티 클래스

```java
package org.techtown.mission23;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    //도구 레이아웃 버튼들
    Button button;
    Button button2;
    Button button3;
    Button button4;

    PaintBoard board;

    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;

    int mColor = 0xff000000;
    int mSize = 2;
    int oldColor;
    int oldSize;
    boolean eraserSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout boardLayout=findViewById(R.id.boardLayout);
        LinearLayout toolsLayout=findViewById(R.id.toolsLayout);

        radioGroup=findViewById(R.id.radioGroup);//라디오 그룹 참조

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);//레이아웃 파라미터 설정

        //보드 생성 및 파라미터 설정 후 보드 레이아웃에 추가
        board=new PaintBoard(this);//보드 생성
        board.setLayoutParams(params);//레이아웃 파라미터 설정
        board.setPadding(2,2,2,2);//패딩 설정
        boardLayout.addView(board);//보드 레이아웃에 페인트보드 추가하기

        //색상 버튼 클릭시 이벤트 처리
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력창 띄우기
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("색상 입력");
                alert.setMessage("색상을 입력하세요.");

                final EditText newColor = new EditText(MainActivity.this);
                newColor.setHint("R,G,B,BL 중 하나를 입력하세요");
                alert.setView(newColor);

                //예 버튼 클릭시
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String color = newColor.getText().toString();
                        switch(color){
                            case "R":
                                mColor=Color.RED;
                                break;
                            case "G":
                                mColor=Color.GREEN;
                                break;
                            case "B":
                                mColor=Color.BLUE;
                                break;
                            case "BL":
                                mColor=Color.BLACK;
                                break;
                        }
                        board.updatePaintProperty(mColor,mSize);
                    }
                });

                alert.setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //굵기 버튼 클릭시 이벤트 처리
        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력창 띄우기
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("펜의 굵기 입력");
                alert.setMessage("펜의 굵기를 입력하세요.");

                final EditText newSize = new EditText(MainActivity.this);
                newSize.setHint("십진수 정수로 입력하세요(ex:30)");
                alert.setView(newSize);

                //예 버튼 클릭시
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String size = newSize.getText().toString();
                        mSize=Integer.parseInt(size);
                        board.updatePaintProperty(mColor,mSize);
                    }
                });

                alert.setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //지우개 버튼 클릭시 이벤트 처리
        button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eraserSelected=!eraserSelected;
                if(eraserSelected){
                    //나머지 버튼 비활성화
                    button.setEnabled(false);
                    button2.setEnabled(false);
                    button4.setEnabled(false);

                    //화면 갱신하기
                    button.invalidate();
                    button2.invalidate();
                    button4.invalidate();

                    //이전 정보(색깔, 크기)들에 현재 정보 값들을 저장
                    oldColor=mColor;
                    oldSize=mSize;

                    //현재 정보값들을 새로 초기화
                    mColor= Color.WHITE;//흰색인 이유는 선들을 지우는 효과를 주기 위해서이다.
                    mSize=15;

                    board.updatePaintProperty(mColor,mSize);//현재 정보들로 보드 갱신
                    button3.setText("그리기");
                }
                else{
                    //나머지 버튼 활성화
                    button.setEnabled(true);
                    button2.setEnabled(true);
                    button4.setEnabled(true);

                    //화면 갱신하기
                    button.invalidate();
                    button2.invalidate();
                    button4.invalidate();

                    //현재 정보값들을 예전 정보값들로 초기화
                    mColor=oldColor;
                    mSize=oldSize;

                    board.updatePaintProperty(mColor,mSize);//현재 정보들로 보드 갱신
                    button3.setText("지우기");
                }
            }
        });

        //취소 버튼 클릭시 이벤트 처리
        button4=findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.drawBackground(board.mCanvas);

            }
        });

        //BUTT 라디오 버튼 클릭시 이벤트 처리
        radioButton=findViewById(R.id.radioButton);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setCAP(Paint.Cap.BUTT);
            }
        });

        //ROUND 라디오 버튼 클릭시 이벤트 처리
        radioButton2=findViewById(R.id.radioButton2);
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setCAP(Paint.Cap.ROUND);
            }
        });

        //SQUARE 라디오 버튼 클릭시 이벤트 처리
        radioButton3=findViewById(R.id.radioButton3);
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setCAP(Paint.Cap.SQUARE);
            }
        });

    }
}
```
