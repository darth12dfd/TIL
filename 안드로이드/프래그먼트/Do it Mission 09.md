# 고객 정보 입력 화면의 구성

* 고객 정보 입력 화면을 프래그먼트로 만들어보세요. 이 화면은 고객의 이름, 나이 생년월일을 입력받기 위한 것입니다.

## 조건

* 프래그먼트로 고객 정보 입력 화면을 만들어 액티비티에 넣어줍니다.

* 프래그먼트의 레이아웃에는 리니어 레이아웃이나 상대 레이아웃을 사용하고 그 안에 이름과 나이를 입력받는 입력상자, 생년월일을 표시하는 버튼 그리고 [저장] 버튼을 배치한다.

* 생년월일을 표시하는 버튼에는 오늘 날짜를 자동으로 표시하며 버튼을 누르면 [날짜 선택] 대화상자를 띄우고 날씨를 입력받아 표시한다. 이름을 넣을 수 있는 입력상자에는 문자열을, 나이를 입력받기 윟나 입력상자에는 숫자를 입력할 수 있도록 설정하여 적당한 키패드를 띄우도록 하고 나이는 세 자리가지만 입력할 수 있게 만든다.

* [저장]버튼을 누르면 토스트로 입력한 정보를 표시한다.

## 참고

* 이름과 나이를 입력받는 입력상자에 키 입력 유형을 설정한다.

* 적당한 키패드를 띄우려면 inputType 속성을 이용한다.

* 날짜를 설정하려면 ```DatePickerDialog``` 와 ```SimpleDateFormat```을 사용하면 된다.

## 소스코드

### 메인 액티비티

#### 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>

    <FrameLayout
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        xmlns:android="http://schemas.android.com/apk/res/android">

        <fragment
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            class="org.techtown.mission.MainFragment"
            android:id="@+id/mainFragment"/>

</FrameLayout>
```

#### 소스파일

```java
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

### 프래그먼트

#### 레이아웃 파일

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="20dp"
        android:layout_centerInParent="true"
        android:background="#aaffffff"
        >
        <LinearLayout
            android:id="@+id/contentsLayout"
            android:orientation="vertical"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentTop="true"
            android:layout_margin="4dp"
            >
            <LinearLayout
                android:orientation="horizontal"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                >
                <TextView
                    android:id="@+id/nameLabel"
                    android:text="  이      름  "
                    android:textColor="#ff222222"
                    android:textSize="16sp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    />
                <EditText
                    android:id="@+id/nameInput"
                    android:layout_width="120dp"
                    android:layout_height="wrap_content"
                    android:layout_alignBaseline="@id/nameLabel"
                    android:inputType="textPersonName"
                    />
            </LinearLayout>
            <LinearLayout
                android:orientation="horizontal"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                >
                <TextView
                    android:id="@+id/ageLabel"
                    android:text="  나      이  "
                    android:textColor="#ff222222"
                    android:textSize="16sp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    />
                <EditText
                    android:id="@+id/ageInput"
                    android:layout_width="120dp"
                    android:layout_height="wrap_content"
                    android:layout_alignBaseline="@id/ageLabel"
                    android:inputType="numberDecimal"
                    android:maxLength="3"
                    />
            </LinearLayout>
            <LinearLayout
                android:orientation="horizontal"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                >
                <TextView
                    android:id="@+id/birthLabel"
                    android:text="  생 년 월 일  "
                    android:textColor="#ff222222"
                    android:textSize="16sp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    />
            </LinearLayout>
            <LinearLayout
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                >
                <Button
                    android:id="@+id/birthButton"
                    android:layout_width="180dp"
                    android:layout_height="wrap_content"
                    android:layout_gravity="right"
                    android:text=""
                    />
            </LinearLayout>
        </LinearLayout>
        <Button
            android:id="@+id/saveButton"
            android:layout_width="120dp"
            android:layout_height="wrap_content"
            android:layout_below="@+id/contentsLayout"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="30dp"
            android:text="저 장"
            />
    </RelativeLayout>

</RelativeLayout>
```

#### 소스 파일

```java
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment {
    EditText nameInput;
    EditText ageInput;

    Button birthButton;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    Date selectedDate;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);


        nameInput = rootView.findViewById(R.id.nameInput);
        ageInput = rootView.findViewById(R.id.ageInput);

        birthButton = rootView.findViewById(R.id.birthButton);
        birthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog();
            }
        });

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nameStr = nameInput.getText().toString();
                String ageStr = ageInput.getText().toString();
                String birthStr = birthButton.getText().toString();

                Toast.makeText(getContext(), "이름 : " + nameStr + ", 나이 : " + ageStr + ", 생년월일 : " + birthStr, Toast.LENGTH_SHORT).show();
            }
        });


        // set selected date using current date
        Date curDate = new Date();
        setSelectedDate(curDate);

        return rootView;
    }

    private void showDateDialog() {
        String birthDateStr = birthButton.getText().toString();

        Calendar calendar = Calendar.getInstance();
        Date curBirthDate = new Date();
        try {
            curBirthDate = dateFormat.parse(birthDateStr);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        calendar.setTime(curBirthDate);

        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),  birthDateSetListener,  curYear, curMonth, curDay);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, monthOfYear);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date curDate = selectedCalendar.getTime();
            setSelectedDate(curDate);
        }
    };

    private void setSelectedDate(Date curDate) {
        selectedDate = curDate;

        String selectedDateStr = dateFormat.format(curDate);
        birthButton.setText(selectedDateStr);
    }

}
```
