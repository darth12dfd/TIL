# CCW 

## 출처

* https://www.acmicpc.net/problem/11758

## 문제

* 2차원 좌표 평면 위에 있는 점 3개 P1, P2, P3가 주어진다. P1, P2, P3를 순서대로 이은 선분이 어떤 방향을 이루고 있는지 구하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 P1의 (x1, y1), 둘째 줄에 P2의 (x2, y2), 셋째 줄에 P3의 (x3, y3)가 주어진다. (-10,000 ≤ x1, y1, x2, y2, x3, y3 ≤ 10,000) 모든 좌표는 정수이다. P1, P2, P3의 좌표는 서로 다르다.

## 출력

* P1, P2, P3를 순서대로 이은 선분이 반시계 방향을 나타내면 1, 시계 방향이면 -1, 일직선이면 0을 출력한다.

## 입력 예제 1

```
1 1
5 5
7 3
```

## 출력 예제 1

```
-1
```

## 입력 예제 2

```
1 1
3 3
5 5
```

## 출력 예제 2

```
0
```

## 입력 예제 3

```
1 1
7 3
5 5
```

## 출력 예제 3

```
1
```

## 풀이

* CCW 알고리즘을 코드로 구현하는 문제이다. 기본적인 로직은 신발끈 공식에서 사용했던 벡터 외적을 활용한다. 

* 세 점의 좌표(or 벡터)를 외적해서 그 값이 음수가 나오면 -1(시계방향)을, 0이 나오면 0(일직선)을 양수가 나오면 1(반시계 방향)을 출력한다.

* CCW 알고리즘에 대한 설명은 [링크](https://snowfleur.tistory.com/98)와 같다.

## 소스코드

```kotlin
package mathematics.geometry

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() = with(BufferedReader(InputStreamReader(System.`in`))){
    val bw = BufferedWriter(OutputStreamWriter(System.`out`))

    var st = StringTokenizer(readLine())
    val x1 = st.nextToken().toInt()
    val y1 = st.nextToken().toInt()

    st = StringTokenizer(readLine())
    val x2 = st.nextToken().toInt()
    val y2 = st.nextToken().toInt()

    st = StringTokenizer(readLine())
    val x3 = st.nextToken().toInt()
    val y3 = st.nextToken().toInt()

    //ccw 알고리즘 함수
    fun ccw(x1:Int,y1:Int,x2:Int,y2:Int,x3:Int,y3:Int):Int{
        //외적을 위한 내적 곱 수행
        val a = x1 * y2 + x2 * y3 + x3 * y1
        val b = y1 * x2 + y2 * x3 + y3 * x1

        //반시계 방향(외적 값이 양수인 경우)
        if(a -b > 0){
            return 1
        }
        //일직선(외적 값이 0인 경우)
        else if(a == b){
            return 0
        }
        //시계방향(외적 값이 음수인 경우)
        else{
            return -1
        }
    }

    bw.write("${ccw(x1, y1, x2, y2, x3, y3)}\n")

    bw.flush()
    bw.close()

    close()
}
```
