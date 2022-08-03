# GCD 합

## 출처

* https://www.acmicpc.net/problem/9613

## 문제

* 양의 정수 n개가 주어졌을 때, 가능한 모든 쌍의 GCD의 합을 구하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 테스트 케이스의 개수 t (1 ≤ t ≤ 100)이 주어진다. 각 테스트 케이스는 한 줄로 이루어져 있다. 각 테스트 케이스는 수의 개수 n (1 < n ≤ 100)가 주어지고, 다음에는 n개의 수가 주어진다. 입력으로 주어지는 수는 1,000,000을 넘지 않는다.

## 출력

* 각 테스트 케이스마다 가능한 모든 쌍의 GCD의 합을 출력한다.

## 입력 예제 1

```
3
4 10 20 30 40
3 7 5 12
3 125 15 25
```

## 출력 예제 1

```
70
3
35
```

## 풀이

* GCD 알고리즘을 각 테스트 케이스에서 입력받은 모든 두 쌍의 수에 대하여 구한 다음 더하면 된다. 이때 GCD 함수는 재귀함수를 활용하여 설계한다.

## 소스코드

```kotlin
package recursion

import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.*

object GCD {
    @JvmStatic
    fun main(args: Array<String>) {
        val bw = BufferedWriter(OutputStreamWriter(System.`out`))
        val sc = Scanner(System.`in`)
        val t = sc.nextInt()
        for (tc in 0 until t) {
            val n = sc.nextInt()
            val arr = IntArray(n)
            for (i in 0 until n) {
                arr[i] = sc.nextInt()
            }
            var sum: Long = 0
            for (i in 0 until n) {
                for (j in i until n) {
                    if (i != j) {
                        sum += gcd(arr[i], arr[j]).toLong()
                    }
                }
            }
            bw.write("${sum}\n")
        }

        bw.flush()
        bw.close()
    }

    fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }
}
```
