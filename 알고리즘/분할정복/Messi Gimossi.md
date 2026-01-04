# Messi Gimossi

## 출처

* https://www.acmicpc.net/problem/17297

## 문제

* 메시는 축구 선수이다. 메시는 기분이 좋다.

```
messi(1): Messi

messi(2)​​: Messi Gimossi

messi(3)​​​​​​: Messi Gimossi Messi

messi(4): Messi Gimossi Messi Messi Gimossi

messi(5): Messi Gimossi Messi Messi Gimossi Messi Gimossi Messi
```

* 메시의 외침은 피보나치 수열과 유사하게 정의된다. messi(N)은 messi(N-1), 공백, messi(N-2)을 차례로 이어붙여서 만든 문자열이다.

* 욱제는 N이 충분히 클 때, messi(N)의 M번째 글자가 뭔지 궁금해졌다.

## 입력

* 정수 M이 주어진다. (1 ≤ M ≤ 230-1)

## 출력

* N이 충분히 클 때, messi(N)의 M번째 글자가 공백(' ')이 아닐 경우에는 그 글자를 출력한다.

* M번째 글자가 공백(' ')일 경우에는 Messi Messi Gimossi를 출력한다.

* 정답은 대소문자를 구분하므로 출력에 주의한다.

## 입력 예제 1

```
1
```

## 출력 예제 1

```
M
```

## 입력 예제 2

```
20
```

## 출력 예제 2

```
Messi Messi Gimossi
```

## 입력 예제 3

```
1073741823
```

## 출력 예제 3

```
G
```

## 풀이

* 대충 문자열이 아래와 같이 정의됨

```
messi(1) = "Messi"
messi(2) = "Messi Gimossi"
messi(N) = messi(N-1) + " " + messi(N-2)   (N ≥ 3)
```

* 매우 빠르게 길이가 커지기 때문에 전체 문자열을 만들 수는 없음

* 목표는 messi(N)의 M번째 문자가 무엇인지 구하는 것

* 단, 그 위치가 공백(‘ ’)이라면 "Messi Messi Gimossi"를 출력해야 함.

### 문자열 자체보다는 문자열 길이만 다루기

* 문자열이 너무 길어서 이걸 다 다루는건 문제가 있음. 그런고로 문자열의 길이를 DP를 활용하여 풀이함

* 길이는 피보나치와 거의 유사

```
len[1] = 5
len[2] = 13
len[i] = len[i−1] + 1 + len[i−2]
```

* 이때 여기서 +1은 두 문자열 사이의 공백.

### 분할정복

* 이제 messi(N)에서 M이 가리키는 문자가 어디에 있는지 찾아야함.

```
messi(i) =   messi(i−1)
           + " "
           + messi(i−2)
```

* M이 len[i−1]보다 작으면 → 왼쪽 부분

* M이 len[i−1] + 1과 같으면 → 공백

* 그렇지 않으면 → 오른쪽 부분 (M − (len[i−1] + 1))

* 계속 i를 줄여가면서 탐색합니다.

### 결론

*  문자열 길이만 저장 → DP

*  찾고 싶은 문자가 어디 위치에 속하는지 분할정복으로 판단.

## 소스코드

```kotlin
package divideandconquer

import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val M = reader.readLine().trim().toLong() // 원하는 위치 M

    // 길이 배열: len[0]은 messi(1), len[1]은 messi(2) 역할
    val lenList = mutableListOf<Long>()
    lenList.add(5)   // "Messi".length == 5
    lenList.add(13)  // "Messi Gimossi".length == 13

    // 필요한만큼 길이를 채워넣는다
    while (lenList.last() < M) {
        val size = lenList.size
        // messi(i) = messi(i-1) + 1 + messi(i-2)
        val nextLen = lenList[size - 1] + 1 + lenList[size - 2]
        lenList.add(nextLen)
    }

    // 분할정복 함수
    fun findChar(idx: Int, pos: Long): String {
        // idx: 현재 messi(idx+1) 를 의미
        // pos: 이 문자열의 pos번째 문자를 찾는다

        // 기저조건: 더이상 나눌 수 없으면 직접 답을 계산
        if (idx <= 1) {
            val baseStr = if (idx == 1) "Messi Gimossi" else "Messi"
            // idx == 1일 때: "Messi Gimossi", idx == 0 일 때: "Messi"
            return if (pos == baseStr.length + 1L) {
                "" // 공백일 경우 빈 문자열로 처리
            } else {
                baseStr[(pos - 1).toInt()].toString()
            }
        }

        val leftLen = lenList[idx - 1] // messi(idx)

        return when {
            pos <= leftLen -> findChar(idx - 1, pos)
            pos == leftLen + 1L -> " "   // 가운데 공백
            else -> findChar(idx - 2, pos - leftLen - 1L)
        }
    }

    // 가장 큰 인덱스
    val idx = lenList.lastIndex
    val result = findChar(idx, M)

    // 공백이면 요구된 출력, 아니면 실제 문자
    if (result == " ") {
        println("Messi Messi Gimossi")
    } else {
        println(result)
    }
}

```
