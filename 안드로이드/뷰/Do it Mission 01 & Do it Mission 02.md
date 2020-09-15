# 도전 01

## 목표

* 화면 아래 쪽에 두 개의 버튼 추가하기

## 순서

* 1. 제약 레이아웃을 사용해 화면을 구성한다.
* 2. 화면의 아래쪽에 가이드라인을 배치한다.
* 3. 가이드라인의 위쪽에 버튼을 추가한 후 가이드라인과 연결한다. 그리고 적절한 간격으로 띄워준다.
* 4. 가이드라인의 위쪽에 버튼을 하나 더 추가한 후 이전에 추가했던 버튼 및 가이드라인과 연결한다. 그리고 적절하게 띄워준다.

## 참고

* 연결선은 부모 레이아웃의 벽면이나 다른 뷰의 연결점과 연결하여 만들 수 있음
* 가이드라인을 만들면 가이드라인과 연결하여 연결선을 만들 수 있음

## 소스코드

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline"
        android:layout_width="wrap_content"
        android:layout_height="0dp"
        android:orientation="horizontal"
        app:layout_constraintGuide_begin="700dp" />

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="44dp"
        android:layout_marginLeft="44dp"
        android:layout_marginTop="600dp"
        android:text="Button"
        app:layout_constraintBottom_toTopOf="@+id/guideline"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="100dp"
        android:layout_marginLeft="100dp"
        android:layout_marginTop="600dp"
        android:text="Button"
        app:layout_constraintBottom_toTopOf="@+id/guideline"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@+id/button3"
        app:layout_constraintTop_toTopOf="parent" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

# 도전 02

## 목표

* 위, 아래, 중앙의 공간을 차지하는 전형적인 화면 구성하기

## 순서

* 1. 제약 레이아웃을 사용해 화면을 구성한다.
* 2. 화면의 위쪽에 버튼을 추가하고 가로 방향으로 꽉 차도록 만든다.
* 3. 화면의 아래쪽에 버튼을 추가하고 가로 바얗ㅇ으로 꽉 차도록 만든다.
* 4. 화면의 가운데에 버튼을 추가학 ㅗ위쪽 버튼과 아래쪽 버튼 사이의 중앙 공간을 꽉 채우도록 만든다.

## 참고

* 상단 버튼은 위쪽과 좌, 우의 연결점을부모 레이아웃과 연결한다.
* 하단 버튼은 아래쪽과 좌, 우의 연결점을 부모 레이아웃과 연결한다.
* 가운데 버튼의 위쪽 연결점은 상단 버튼의 아래쪽 연결점과 연결한다.
* 가운데 버튼의 아래쪽 연결점은 하단 버튼의 위쪽 연결점과 연결한다.

## 소스코드
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/button"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button2"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"
        android:text="Button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <Button
        android:id="@+id/button5"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="Button"
        app:layout_constraintBottom_toTopOf="@+id/button2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/button"
        app:layout_constraintVertical_bias="0.499" />
</androidx.constraintlayout.widget.ConstraintLayout>
```
