# N 번째 큰수

## 출처

* https://www.acmicpc.net/problem/2075

## 문제

* N×N의 표에 수 N2개 채워져 있다. 채워진 수에는 한 가지 특징이 있는데, 모든 수는 자신의 한 칸 위에 있는 수보다 크다는 것이다. N=5일 때의 예를 보자.

| 12 | 7	| 9	|  15 |	5 |
| ---- | ---- | ---- | ---- | ---- |
| 13 | 8	| 11	| 19	| 6 |
| 21 | 10	| 26	| 31	| 16 |
| 48	| 14 | 28 |	35 | 25 |
| 52 | 20 |	32 | 41 |	49 |

* 이러한 표가 주어졌을 때, N번째 큰 수를 찾는 프로그램을 작성하시오. 표에 채워진 수는 모두 다르다.

## 입력

* 첫째 줄에 N(1 ≤ N ≤ 1,500)이 주어진다. 다음 N개의 줄에는 각 줄마다 N개의 수가 주어진다. 표에 적힌 수는 -10억보다 크거나 같고, 10억보다 작거나 같은 정수이다.

## 출력

* 첫째 줄에 N번째 큰 수를 출력한다.

## 입력 예제 1

```
5
12 7 9 15 5
13 8 11 19 6
21 10 26 31 16
48 14 28 35 25
52 20 32 41 49
```

## 출력 예제 1

```
35
```

## 소스코드

```kotlin
package datastructure.priorityqueue

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main() = with(BufferedReader(InputStreamReader(System.`in`))){
    val N = readLine().toInt()

    val pq = PriorityQueue<Int>()//우선순위 큐 생성

    /*
        우선순위 큐를 사용해서 N번째 큰 수를 구한다.
        이때 우선순위 큐에 N*N 개의 원소를 넣어도 되지만,
        우선순위 큐의 사이즈를 N만큼 유지하면서 삽입/삭제를 반복한다. 이리함녀 마지막에 남는 peek()값이 N번째 로 큰 값이 된다.

     */

    val st = StringTokenizer(readLine())
    // 맨 위에 입력받은 줄의 숫자들은 그대로 우선순위 큐에 넣는다.
    for(i in 0 until N){

        pq.offer(st.nextToken().toInt())
    }

    //그 아랫줄 부터 입력받은 값과 우선순위 큐에 입력된 값중 가장 먼저 나오는 값을 비교해서
    //만약 입력받은 값이 pq.peek() 값보다 큰 경우, pq.peek()값을 빼고, 입력받은 값을 우선순위 큐에 삽입한다.
    for(i in 1 until N){
        val st = StringTokenizer(readLine())
        for(j in 0 until N){
            var temp = st.nextToken().toInt()

            if(pq.peek() < temp){
                pq.poll()
                pq.offer(temp)
            }
        }
    }

    println(pq.poll())

    close()
}
```
