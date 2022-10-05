# A → B

## 출처 

* https://www.acmicpc.net/problem/16953

## 문제

* 정수 A를 B로 바꾸려고 한다. 가능한 연산은 다음과 같은 두 가지이다.

```
2를 곱한다.
1을 수의 가장 오른쪽에 추가한다. 
```

* A를 B로 바꾸는데 필요한 연산의 최솟값을 구해보자.

## 입력

* 첫째 줄에 A, B (1 ≤ A < B ≤ 10^9)가 주어진다.

## 출력

* A를 B로 바꾸는데 필요한 연산의 최솟값에 1을 더한 값을 출력한다. 만들 수 없는 경우에는 -1을 출력한다.

## 입력 예제 1

```
2 162
```

## 출력 예제 1

```
5
```

## 입력 예제 2

```
4 42
```

## 출력 예제 2

```
-1
```

## 입력 예제 3

```
100 40021
```

## 출력 예제 3

```
5
```

## 풀이

* 하나의 숫자에서 두 가지 갈래로 흩어지고 그 갈랫길 중 목표노드에 도착하는 데 걸린 최소 간선 수를 구하는 bfs 문제와 동일하다.

* 그러기 위해선 노드 정보와 간선의 수를 인자로 하는 데이터 클래스를 생성하고 이 데이터클래스를 자료형으로 삼는 큐를 생성한다.

* 먼저 초기 입력값 A와 연산횟수 0을 queue에 입력한다. 그리고 queue가 비어있지 않는 동안에 아래의 과정을 반복한다.

* 먼저, 큐에 있는 맨 위의 값(데이터클래스)을 꺼낸다. 그 후, 그 데이터 클래스에서 연산자 횟수를 1 증가시킨 값을 변수에 넣는다.

* 그 후, 변수 2개를 만들어 하나는 A에 2를 곱한 값을, 다른 하나에는 A뒤에 1을 붙인 값을 저장한다. 그리고 각 변수에 대하여 해당 변수가 B와 동일하면 해당 연산자 횟수를 리턴하고, B보다 작으면 해당 값과 연산자 횟수를 큐에 넣는다. 

## 소스코드

```kotlin
package graphtheory.graphtraversal.bfs

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

data class Pair(var value:Long, var opCount:Long)//데이터 클래스(피연산자, 연산 횟수)

fun main() = with(BufferedReader(InputStreamReader(System.`in`))){
    val st = StringTokenizer(readLine())

    var A = st.nextToken().toLong()//정수 A(1 이상 B 미만의 자연수)
    val B = st.nextToken().toLong()//A에서 바꾸기 위한 정수 B(A 초과 10^9 이하의 자연수)

    //1. 연산 결과값 == B 이면, 탐색 성공
    //2. 연산 결과값 < B 이면, Queue에 추가
    //3. 연산 결과값 > B 이면, 버림.

    //bfs 알고리즘
    fun bfs(alpha:Long, omega:Long):Long{
        var queue: Queue<Pair>
        queue = LinkedList<Pair>()

        queue.add(Pair(alpha,0))
        while(queue.isNotEmpty()){

            var p = queue.remove()
            var opCount = p.opCount + 1//연산 횟수 1 증가

            var opValue1 = p.value * 2//첫 번째 연산: 곱
            //1번 케이스
            if(opValue1 == B){
                return opCount + 1
            }
            //2번 케이스
            else if(opValue1 < B){
                queue.add(Pair(opValue1,opCount))
            }

            var opValue2 = p.value*10 + 1//두 번째 연산: 뒤에 1 추가

            if(opValue2 == B){
                return opCount + 1
            }
            //2번 케이스
            else if(opValue2 < B) {
                queue.add(Pair(opValue2, opCount))
            }
        }
        return -1
    }


    println("${bfs(A,B)}")

    close()
}
```
