#  큰 수 A+B

## 출처

* https://www.acmicpc.net/problem/10757

## 문제

* 두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 A와 B가 주어진다. (0 < A,B < 10^10000)

## 출력

* 첫째 줄에 A+B를 출력한다.

## 입력 예제 1

```
9223372036854775807 9223372036854775808
```

## 출력 예제 1

```
18446744073709551615
```

## 풀이

* 케이스를 입력받아 정수로 변환한 후, 이를 더하면 되는 간단한 문제이지만, A,B의 값의 범위가 상당히 크기 때문에 통상적인 정수형변환으론 NumberFormatException이 발생하게 된다.

* 그렇기에 Long타입이 아닌 BigInteger 타입으로 형변환을 하면 된다.

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val st=StringTokenizer(readLine())

    val A=st.nextToken().toBigInteger()
    val B=st.nextToken().toBigInteger()

    println(A+B)
}
```
