# 두 종류의 버튼 모양 만들기

## 개요

* 두 개의 버튼을 화면에 추가하고 버튼 모양을 각각 다르게 보이도록 만들어 보시오

## 조건

* 화면에 두 개의 버튼을 배치한다.

* 첫 번째 버튼의 모양은 가장자리에 경계선만 보이도록 하고 경계선과 글자색이 동일하도록 만든다.

* 두 번째 버튼의 모양은 배경색이 있고 모서리는 약간 둥글며 글자가 하얀색이 되도록 만든다.

## 참고할 점

* 드로어블 객체를 만들어 버튼의 배경으로 설정하면 버튼의 모양을 만들 수 있다.

* 드로어블을 XML로 정의할 때 버튼의 모양이 결정된다. 

## 풀이

* 각 버튼에 대하여 드로어블 객체를 만들어 참조한다(android:background 속성 값으로). 또한 버튼의 모양과 배경색상을 결정해야 하므로 드로어블은 둘 다 셰이프 드로어블을 사용한다.

### 버튼 1

* 버튼 1의 경우는 배경색을 흰색으로 하고, 경계선의 색깔을 글자의 색깔과 동일하게 만든다.

* 셰이프 드로어블에서 <shape>속성 안에 있는 태그 들 중 테두리 속성을 지정할 수 있는 태그는 ```<stroke>```태그다. ```<stroke>```태그 내에서 color속성 값을 버튼의 글자 색과 동일한 값으로 설정한다.

* 셰이프 드로어블에서 <shape>속성 안에 있는 태그 들 중 도향 안쪽을 지정할 수 있는 태그는 ```<solid>```태그다. ```<solid>````태그 내에서 color속상 값을 흰 색으로 설정한다.

* 또한 <shape> 태그 내에서 shape 속성으로 도형의 형태를 결정한다. 여기서는 rectangle(직사각형)로 설정한다.

### 버튼 2

* 버튼 2에서 각을 동글동글하게 만들기 위해서는 셰이프 드로어블에서 ```<corners>```태그 안에 있는 ```radius```속성 값으로 정의할 수 있다. radius값이 커질수록 도형은 원과 점점 닮아져 간다.

## 소스코드

### 액티비티 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="80dp"
        android:text="버튼 1"
        android:textColor="#FF8C00"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:background="@drawable/button1_drawable"/>

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:text="버튼 2"
        android:textColor="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.500"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/button"
        android:background="@drawable/button2_drawable"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 버튼 1 드로어블

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <size android:width="150dp" android:height="50dp"/>
    <stroke
        android:width="1dp"
        android:color="#FF8C00"/>
    <solid
        android:color="#FFFFFF"/>
</shape>
```

### 버튼 2 드로어블

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners
        android:radius="5dp"/>
    <size android:width="150dp" android:height="50dp"/>
    <solid
        android:color="#FF8C00" />
</shape>
```
