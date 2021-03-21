# BABBA

## 출처

* https://www.acmicpc.net/problem/9625

## 문제

* 상근이는 길을 걷다가 신기한 기계를 발견했다. 기계는 매우 매우 큰 화면과 버튼 하나로 이루어져 있다.

* 기계를 발견했을 때, 화면에는 A만 표시되어져 있었다. 버튼을 누르니 글자가 B로 변했다. 한 번 더 누르니 BA로 바뀌고, 그 다음에는 BAB, 그리고 BABBA로 바뀌었다. 상근이는 화면의 모든 B는 BA로 바뀌고, A는 B로 바뀐다는 사실을 알게되었다.

* 버튼을 K번 눌렀을 때, 화면에 A와 B의 개수는 몇 개가 될까?

## 입력

* 첫째 줄에 K (1 ≤ K ≤ 45)가 주어진다.

## 출력

* 첫째 줄에 A의 개수와 B의 개수를 공백으로 구분해 출력한다.

## 입력 예제 1

```
1
```

## 출력 예제 1

```
0 1
```

## 풀이

* 문제를 읽다보니, 피보나치 수열으로 풀어도 되겠다는 생각이 들었다.

```
1단계 :0 1
2단계 :1 1
3단계 :1 2
4단계 :2 3
5단계 :3 5
6단계 :5 8
```

* A와 B의 갯수를 int 변수로 받았는데, 값이 덮여버리지 않도록 임시 변수를 선언하는 방식이 유효하게 먹혔다. 마치 정렬에서도 A와 B의 순서를 바꿀때 임시 변수를 통해 값을 바꾸듯이.

## 소스코드

```kotlin
package dynamicprogramming

import java.io.BufferedReader
import java.io.InputStreamReader

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val K=readLine().toInt()

    var A=1//A는 B로 B는 AB로
    var B=0

    var count=0
    var tmp=0

    while(count!=K){
        tmp=A
        A=B
        B+=tmp

        count++
    }
    println("$A $B")
    close()
}
```
