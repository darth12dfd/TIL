# A+B(6)

## 출처

* https://www.acmicpc.net/problem/10953

## 문제

* 두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 테스트 케이스의 개수 T가 주어진다.

* 각 테스트 케이스는 한 줄로 이루어져 있으며, 각 줄에 A와 B가 주어진다. A와 B는 콤마(,)로 구분되어 있다. (0 < A, B < 10)

## 출력

* 각 테스트 케이스마다 A+B를 출력한다.

## 입력 예제 1

```
5
1,1
2,3
3,4
9,8
5,2
```

## 출력 예제 1

```
2
5
7
17
7
```

## 소스코드

```kotlin
package mathematics.arithmetic

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val T=readLine().toInt()

    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    for(i in 0 until T){
        val numbers=readLine().split(",")
        bw.write("${numbers[0].toInt()+numbers[1].toInt()}\n")
    }
    bw.flush()
    bw.close()
    close()
}
```
