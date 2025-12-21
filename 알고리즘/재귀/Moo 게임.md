# Moo 게임

## 출처

* https://www.acmicpc.net/problem/5904

## 문제

* Moo는 술자리에서 즐겁게 할 수 있는 게임이다. 이 게임은 Moo수열을 각 사람이 하나씩 순서대로 외치면 되는 게임이다.

* Moo 수열은 길이가 무한대이며, 다음과 같이 생겼다. 

```
m o o m o o o m o o m o o o o m o o m o o o m o o m o o o o o 
```

* Moo 수열은 다음과 같은 방법으로 재귀적으로 만들 수 있다. 먼저, S(0)을 길이가 3인 수열 "m o o"이라고 하자. 1보다 크거나 같은 모든 k에 대해서, S(k)는 S(k-1)과 o가 k+2개인 수열 "m o ... o" 와 S(k-1)을 합쳐서 만들 수 있다.

```
S(0) = "m o o"
S(1) = "m o o m o o o m o o"
S(2) = "m o o m o o o m o o m o o o o m o o m o o o m o o"
```

* 위와 같은 식으로 만들면, 길이가 무한대인 문자열을 만들 수 있으며, 그 수열을 Moo 수열이라고 한다.

* N이 주어졌을 때, Moo 수열의 N번째 글자를 구하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 N (1 ≤ N ≤ 109)이 주어진다.

## 출력

* N번째 글자를 출력한다.

## 입력 예제 1

```
11
```

## 출력 예제 1

```
m
```

## 풀이

* 문자열을 다음처럼 정의

  - S(0) = "moo" (길이 3)

  - S(k) = S(k-1) + 중간 문자열 + S(k-1)

  - 중간 문자열 = "m" + "o"가 (k+2)개 → 길이 = (k+3)

* 따라서 길이 L(k)는

  - L(0) = 3

  - L(k) = 2 * L(k-1) + (k+3)

* N번째 문자를 찾는 규칙 (k단계에서)

```
왼쪽 길이 = L(k-1)
가운데 길이 = (k+3)
```

  1. N ≤ L(k-1) → 왼쪽 S(k-1)로 이동

  2. N > L(k-1) + (k+3) → 오른쪽 S(k-1)로 이동 (N을 그만큼 빼고)

  3. 그 사이면 “가운데 문자열”:

  - 가운데의 첫 글자면 'm'

  - 나머지는 모두 'o'

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val n = br.readLine().trim().toLong()  // N은 최대 1e9 정도까지 가므로 Long 사용

    // 1) L(k)를 구해서, L(k) >= N이 되는 최소 k를 찾는다.
    //    L(0)=3, L(k)=2*L(k-1)+(k+3)
    val lengths = ArrayList<Long>()
    lengths.add(3L) // L(0)

    var k = 0
    while (lengths[k] < n) {
        val next = lengths[k] * 2 + (k + 4).toLong()
        // 이유: (k+1) 단계의 가운데 길이는 (k+1)+3 = k+4
        lengths.add(next)
        k++
    }

    // 2) 재귀(또는 반복)로 n번째 문자를 추적한다.
    val answer = findChar(n, k, lengths)
    print(answer)
}

/**
 * n: 찾고 싶은 위치(1-indexed)
 * k: 현재 단계
 * lengths[k] = L(k)
 */
private fun findChar(n: Long, k: Int, lengths: List<Long>): Char {
    // S(0) = "moo"
    if (k == 0) {
        // n은 1~3
        return if (n == 1L) 'm' else 'o'
    }

    val leftLen = lengths[k - 1]          // 왼쪽 S(k-1)의 길이
    val middleLen = (k + 3).toLong()      // 가운데 "m" + "o"(k+2)개 => 총 k+3

    return when {
        // 1) n이 왼쪽 구간에 있으면 그대로 (k-1)로 내려감
        n <= leftLen -> findChar(n, k - 1, lengths)

        // 2) n이 가운데 구간에 있으면 가운데 문자열에서 판단
        n <= leftLen + middleLen -> {
            // 가운데 구간의 첫 글자만 'm', 나머지는 전부 'o'
            if (n == leftLen + 1) 'm' else 'o'
        }

        // 3) n이 오른쪽 구간이면,
        //    오른쪽은 다시 S(k-1)이므로 앞의 (leftLen + middleLen) 만큼 위치를 당겨서 내려감
        else -> findChar(n - (leftLen + middleLen), k - 1, lengths)
    }
}

```
