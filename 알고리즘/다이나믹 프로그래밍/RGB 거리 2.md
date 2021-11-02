# RGB 거리 2

## 출처

* https://www.acmicpc.net/problem/17404

## 문제

* RGB거리에는 집이 N개 있다. 거리는 선분으로 나타낼 수 있고, 1번 집부터 N번 집이 순서대로 있다.

* 집은 빨강, 초록, 파랑 중 하나의 색으로 칠해야 한다. 각각의 집을 빨강, 초록, 파랑으로 칠하는 비용이 주어졌을 때, 아래 규칙을 만족하면서 모든 집을 칠하는 비용의 최솟값을 구해보자.

```
1번 집의 색은 2번, N번 집의 색과 같지 않아야 한다.
N번 집의 색은 N-1번, 1번 집의 색과 같지 않아야 한다.
i(2 ≤ i ≤ N-1)번 집의 색은 i-1, i+1번 집의 색과 같지 않아야 한다.
```

## 입력

* 첫째 줄에 집의 수 N(2 ≤ N ≤ 1,000)이 주어진다. 둘째 줄부터 N개의 줄에는 각 집을 빨강, 초록, 파랑으로 칠하는 비용이 1번 집부터 한 줄에 하나씩 주어진다. 집을 칠하는 비용은 1,000보다 작거나 같은 자연수이다.

## 출력

* 첫째 줄에 모든 집을 칠하는 비용의 최솟값을 출력한다.

## 입력 예제 1

```
3
26 40 83
49 60 57
13 89 99
```

## 출력 예제 1

```
110
```

## 입력 예제 2

```
3
1 100 100
100 1 100
100 100 1
```

## 출력 예제 2

```
3
```

## 입력 예제 3

```
3
1 100 100
100 100 100
1 100 100
```

## 출력 예제 3

```
201
```

## 입력 예제 4

```
6
30 19 5
64 77 64
15 19 97
4 71 57
90 86 84
93 32 91
```

## 출력 예제 4

```
208
```

## 입력 예제 5

```
8
71 39 44
32 83 55
51 37 63
89 29 100
83 58 11
65 13 15
47 25 29
60 66 19
```

## 출력 예제 5

```
253
```

## 풀이

* dp[][]을 선언한다.

* 일단 이 문제에서 dp[i][j] = k가 있다면, 아래와 같은 의미를 가진다.-> 1~i번째 집까지 칠하고 i번째 집은 j색으로 칠했을 때의 최소비용 = k를 의미한다.

* 따라서 j는 0~2로 표현가능하게 선언하고 각각을 R, G, B를 의미하게 한다.

* 개요에서 말했듯이 첫 집과 마지막 집 또한 이웃이기 때문에 

```
첫 집 : RED 인 경우 ▶ 첫 집의 GREEN, BLUE의 DP값을 무한대로 설정한다.

첫 집 : GREEN 인 경우 ▶ 첫 집의 RED, BLUE의 DP값을 무한대로 설정한다.

첫 집 : BLUE 인 경우 ▶ 첫 집의 RED, GREEN의 DP값을 무한대로 설정한다.
```

* 위와 같이 3가지 경우로 나눠서 계산한 뒤

```
첫 집 : RED 인 경우 ▶ 마지막 집을 GREEN, BLUE로 칠한 DP값 중에 최솟값을 구한다.

첫 집 : GREEN 인 경우 ▶ 마지막 집을 RED, BLUE로 칠한 DP값 중에 최솟값을 구한다.

첫 집 : BLUE 인 경우 ▶ 마지막 집을 RED, GREEN로 칠한 DP값 중에 최솟값을 구한다.
```

## 소스코드

```kotlin
package dynamicprograming

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main() = with(BufferedReader(InputStreamReader(System.`in`))){
    val N = readLine().toInt()//집의 수

    val INF = 1000*1000//최댓값

    var house = Array(N + 1, { IntArray( 3 ) } )//house[i][j] => i번째 집을 j색으로 채색했을 때의 비용

    var dp = Array(N + 1, { IntArray( 3 ) } )//dp 2차원 배열(dp[i][j] = k는 1~i번째 집까지 칠하고 i번째 집은 j색(R,G,B)으로 칠했을 때의 최소비용 k를 의미한다.

    var answer = INF//정답을 저장할 변수

    //입력값 저장
    for(i in 1..N){
        val st = StringTokenizer(readLine())
        for(j in 0 until 3){
            house[i][j] = st.nextToken().toInt()
        }
    }

    //R,G,B 순으로 첫 집을 칠하는 경우
    for(k in 0 until 3){
        //R,G,B 순으로 칠하는 경우 각 색을 제외한 나머지는 INF로 초기화 해준다.
        for(i in 0 until 3){
            if(i == k){
                dp[1][i] = house[1][i]
            }
            else{
                dp[1][i] = INF
            }
        }

        //R,G,B 순으로 칠했을 때의 최소값을 구한다.
        for(i in 2..N){
            dp[i][0] = Math.min(dp[i-1][1], dp[i-1][2]) + house[i][0]
            dp[i][1] = Math.min(dp[i-1][0], dp[i-1][2]) + house[i][1]
            dp[i][2] = Math.min(dp[i-1][0], dp[i-1][1]) + house[i][2]
        }

        //최솟값을 구한다.
        for(i in 0 until 3){
            if(i != k){
                answer = Math.min(answer,dp[N][i])
            }
        }
    }

    println("$answer")

    close()
}
```
