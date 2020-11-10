# 입력 화면의 애니메이션

* 고객 정보 입력 화면을 만들고 이 화면이 보이거나 사라질 때 애니메이션이 동작하도록 하기

## 조건

* 새로운 액티비티를 추가하고 고객 정보 입력이 가능하도록 만든다. 고객 정보 입력 화면에서는 이름과 나이, 전화번호를 입력받도록 만든다.

* MainActivity에서는 [입력 화면으로] 버튼을 누르면 고객 정보 입력 화면이 보이도록 한다. 이 과정에서 오른쪽에서 왼쪽으로 나타나는 애니메이션을 적용한다.

* 고객 정보 입력 화면에서 [저장]버튼을 누르면 MainActivity로 돌아오도록 한다. 이 과정에서도 애니메이션을 적용한다.

## 참고

* 화면 전체에 애니메이션을 적용할 수 있다.

## 소스코드

* 다른 액티비티를 호출할 때 애니메이션 효과를 주기 위해, ```overridePendingTransition``` 메서드를 사용한다.

### 메인 액티비티

#### XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/first_page"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="90dp"
        android:text="입력 화면으로"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스 파일

```java
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CustomerInfo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.entry,R.anim.exit);
            }
        });
    }
}
```

### 고객 정보 입력 액티비티

#### XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".CustomerInfo">

    <EditText
        android:id="@+id/editText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:ems="10"
        android:hint="이름"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/editText2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:ems="10"
        android:hint="나이"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editText" />

    <EditText
        android:id="@+id/editText3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:ems="10"
        android:hint="생년월일"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editText2" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="저장"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 소스파일

```java
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerInfo extends AppCompatActivity {
    Button button;
    EditText nameInput;
    EditText ageInput;
    EditText birthInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        nameInput=findViewById(R.id.editText);
        ageInput=findViewById(R.id.editText2);
        birthInput=findViewById(R.id.editText3);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = nameInput.getText().toString();
                String ageStr = ageInput.getText().toString();
                String birthStr = birthInput.getText().toString();
                Toast.makeText(getApplicationContext(), "이름 : " + nameStr + ", 나이 : " + ageStr + ", 생년월일 : " + birthStr, Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.entry_back,R.anim.exit_back);
            }
        });
    }
}
```

### 애니메이션 리소스 파일

* ```overridePendingTransition``` 메서드의 파라미터로 쓸 애니메이션 리소스 파일들이다. 

* 이 메서드에는 두 개의 파라미터를 필요로 하는데, 첫 번째 파라미터로는 보여질 화면의 애니메이션을 두 번째 파라미터로는 종료될 화면의 애니메이션을 지정한다.

#### [입력화면으로]버튼 클릭시 고객 관리 액티비티 호출 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/bounce_interpolator">
    <translate
        android:fromXDelta="100%" android:toXDelta="0%"
        android:fromYDelta="0%" android:toYDelta="0%"
        android:duration="1500"/>
</set>
```

#### [입력화면으로]버튼 클릭시 메인 화면 종료 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/bounce_interpolator">
    <translate
        android:fromXDelta="0%" android:toXDelta="-100%"
        android:fromYDelta="0%" android:toYDelta="0%"
        android:duration="1500"/>
</set>
```

#### [저장]버튼 클릭시 고객관리 화면 종료 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/bounce_interpolator">
    <translate
        android:fromXDelta="0%" android:toXDelta="100%"
        android:fromYDelta="0%" android:toYDelta="0%"
        android:duration="1500"/>
</set>
```

#### [저장]버튼 클릭시 메인 화면 재호출 애니메이션

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/bounce_interpolator">
    <translate
        android:fromXDelta="-100%" android:toXDelta="0%"
        android:fromYDelta="0%" android:toYDelta="0%"
        android:duration="1500"/>
</set>
```
