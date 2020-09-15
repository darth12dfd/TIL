# 블랙 잭

## 문제

* 카지노에서 제일 인기 있는 게임 블랙잭의 규칙은 상당히 쉽다. 카드의 합이 21을 넘지 않는 한도 내에서, 카드의 합을 최대한 크게 만드는 게임이다. 블랙잭은 카지노마다 다양한 규정이 있다. 한국 최고의 블랙잭 고수 김정인은 새로운 블랙잭 규칙을 만들어 상근, 창영이와 게임하려고 한다.
김정인 버전의 블랙잭에서 각 카드에는 양의 정수가 쓰여 있다. 그 다음, 딜러는 N장의 카드를 모두 숫자가 보이도록 바닥에 놓는다. 그런 후에 딜러는 숫자 M을 크게 외친다.
이제 플레이어는 제한된 시간 안에 N장의 카드 중에서 3장의 카드를 골라야 한다. 블랙잭 변형 게임이기 때문에, 플레이어가 고른 카드의 합은 M을 넘지 않으면서 M과 최대한 가깝게 만들어야 한다.
N장의 카드에 써져 있는 숫자가 주어졌을 때, M을 넘지 않으면서 M에 최대한 가까운 카드 3장의 합을 구해 출력하시오.

## 입력

* 첫째 줄에 카드의 개수 N(3 ≤ N ≤ 100)과 M(10 ≤ M ≤ 300,000)이 주어진다. 둘째 줄에는 카드에 쓰여 있는 수가 주어지며, 이 값은 100,000을 넘지 않는다.
합이 M을 넘지 않는 카드 3장을 찾을 수 있는 경우만 입력으로 주어진다.

## 출력

* 첫째 줄에 M을 넘지 않으면서 M에 최대한 가까운 카드 3장의 합을 출력한다.


## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/*
    출처: https://www.acmicpc.net/problem/2798
    알고리즘 분류: 브루트포스 알고리즘
 */

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val input=readLine().split(' ')
    val card_count=input[0].toInt()
    val maximum_value=input[1].toInt()
    var values=IntArray(card_count,{0})
    val st=StringTokenizer(readLine())

    for(i in 0 until card_count){
        values[i]=st.nextToken().toInt()
    }
    var value=0
    //브루트 포스 알고리즘
    /*
        3개의 반복문을 중첩함으로써, 배열에서 3개의 다른 값들을 선택하는 모든 경우의 수에 대해서, 그 값이 기존의 value값보다 크고 그것이 딜러가 설정한 최대값보다 작거나 같은 경우를 구한다.
     */
    for(i in 2 until card_count){
        for(j in 1 until i){
            for(k in 0 until j){
                if((values[i]+values[j]+values[k])<=maximum_value && value<(values[i]+values[j]+values[k])){
                    value=(values[i]+values[j]+values[k])
                }
            }
        }
    }
    println(value)
}
```

## 풀이

* N개의 숫자들 중 서로 다른 3개의 수를 선택해야 하므로, 먼저 3중 중첩 반복문을 통해서, N개의 수 중에서 3개의 수를 고르는 모든 경우를 추출한다.

* 그리고 그 모든 경우에 대하여 그 3개의 수의 합이 M보다 작거나 같거나 이전 경우의 3개의 수의 합보다 큰 경우를 모두 만족하는 value값을 찾는다.

* 그 value값을 출력한다.
