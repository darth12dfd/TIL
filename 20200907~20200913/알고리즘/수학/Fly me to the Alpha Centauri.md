# Fly me to the Alpha Centauri

## 출처

* https://www.acmicpc.net/problem/1011

## 문제

* 우현이는 어린 시절, 지구 외의 다른 행성에서도 인류들이 살아갈 수 있는 미래가 오리라 믿었다. 그리고 그가 지구라는 세상에 발을 내려 놓은 지 23년이 지난 지금, 세계 최연소 ASNA 우주 비행사가 되어 새로운 세계에 발을 내려 놓는 영광의 순간을 기다리고 있다.
그가 탑승하게 될 우주선은 Alpha Centauri라는 새로운 인류의 보금자리를 개척하기 위한 대규모 생활 유지 시스템을 탑재하고 있기 때문에, 그 크기와 질량이 엄청난 이유로 최신기술력을 총 동원하여 개발한 공간이동 장치를 탑재하였다. 하지만 이 공간이동 장치는 이동 거리를 급격하게 늘릴 경우 기계에 심각한 결함이 발생하는 단점이 있어서, 이전 작동시기에 k광년을 이동하였을 때는 k-1 , k 혹은 k+1 광년만을 다시 이동할 수 있다. 예를 들어, 이 장치를 처음 작동시킬 경우 -1 , 0 , 1 광년을 이론상 이동할 수 있으나 사실상 음수 혹은 0 거리만큼의 이동은 의미가 없으므로 1 광년을 이동할 수 있으며, 그 다음에는 0 , 1 , 2 광년을 이동할 수 있는 것이다. ( 여기서 다시 2광년을 이동한다면 다음 시기엔 1, 2, 3 광년을 이동할 수 있다. ) 김우현은 공간이동 장치 작동시의 에너지 소모가 크다는 점을 잘 알고 있기 때문에 x지점에서 y지점을 향해 최소한의 작동 횟수로 이동하려 한다. 하지만 y지점에 도착해서도 공간 이동장치의 안전성을 위하여 y지점에 도착하기 바로 직전의 이동거리는 반드시 1광년으로 하려 한다.
김우현을 위해 x지점부터 정확히 y지점으로 이동하는데 필요한 공간 이동 장치 작동 횟수의 최솟값을 구하는 프로그램을 작성하라.

## 입력

* 입력의 첫 줄에는 테스트케이스의 개수 T가 주어진다. 각각의 테스트 케이스에 대해 현재 위치 x 와 목표 위치 y 가 정수로 주어지며, x는 항상 y보다 작은 값을 갖는다. (0 ≤ x < y < 2의 31승)

## 출력

* 각 테스트 케이스에 대해 x지점으로부터 y지점까지 정확히 도달하는데 필요한 최소한의 공간이동 장치 작동 회수를 출력한다.

## 풀이

* 원할한 문제풀이를 위해 3 가지 임시 변수를 설정하고 들어간다.

```distance: Y좌표와 X좌표 사이의 거리```, ```count: 기계의 작동 횟수(구해야 하는 결과값)```, ```max: 한 번 작동할 때 마다 이동할 수 있는 최대 광년```

* 그리고 distance값에 따른 count값과 max 값을 찾아낸다.

```
distance(Y-X) count(결과값)  max
1                 1           1
2                 2           1
3                 3           1
4                 3           2
5                 4           2
6                 4           2
7                 5           2
8                 5           2
9                 5           3
10                6           3
11                6           3
12                6           3
13                7           3
14                7           3
15                7           3
16                7           4
17                8           4
18                8           4
19                8           4
20                8           4
21                9           4
22                9           4
23                9           4
24                9           4
.
.
.
중략
```

* 위의 표에서 여러 가지 규칙들을 발견할 수 있다. 먼저 max값의 경우 distance에 루트를 씌운 값을 내림한 값과 동일하다. 이를 식으로 나타내면 ```max=Math.floor(Math.sqrt(distance))``` 이다.

* 위의 표에서 알 수 있드싱 max값이 동일한 경우에 count값이 3가지 숫자로 나눠진다. 이 경우에 대해서 조사하면 답을 쉽게 구할 수 있다.

* 먼저 count값이 가장 작은 경우의 distance값을 보자 max값이 2인 경우에 대하여, count값이 가장 작은 distance는 4이다. 그리고, max값이 3인 경우 count값이 가장 작은 distacne는 9이다. 즉, 먼저 max값이 distance값의 제곱근인 경우가 있다.
그리고 그 때의 count값은 각각 3과 5이다. 두 식 모두, max 값에 2를 곱한 다음 1을 빼준 값과 동일하다. max값이 커져도 이 규칙은 동일하다. 이를 식으로 나타내면 다음과 같다.

```max*max=distance, count=2*max-1```

* 그 다음 경우를 살펴보자 max가 2인 경우, distance가 6일 때, count가 4이고 7일 때 5가 된다. 그리고 max값이 3인 경우, distance가 12일 때, count가 6이고, distance가 13일 때, count가 7이 된다.
distance와 max를 나눴을 때, 그 값에 따라 count값이 바뀌는 것을 확인 할 수 있다. 먼저 max가 2 일때, distance를 나눈 값이 max+1인 3보다 작거나 같은 경우, count값이 2*max값인 4가 된다.
그리고 max+1인 3보다 큰 경우, count값은 2*max+1인 5가 된다. 이를 식으로 나타내면 다음과 같다.

```distance/max<=max+1, count=2*max```

```distance/max>max+1, count=2*max+1```

* 위의 세가지 식으로 모든 경우에 대하여 count값을 구할 수 있다. 소스코드는 아래와 같다.

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val case=readLine().toInt()
    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    for(i in 1..case) {
        val sc = StringTokenizer(readLine())
        val x = sc.nextToken().toInt()//시작 좌표
        val y = sc.nextToken().toInt()//도착 좌표
        val distance = y - x//두 좌표 사이의 거리
        val max=Math.floor(Math.sqrt(distance.toDouble()))//한 번에 이동할 수 있는 최대 광년

        //1 번째 케이스, 만약 max값이 distance의 루트 값과 동일하다면 2*max-1이 count값이 된다.
        if(max==Math.sqrt(distance.toDouble())){
            bw.write("${(2*max-1).toInt()}\n")
        }
        //2 번째 케이스, 만약 distance 값을 max로 나눈 값이 max+1보다 작거나 같은 경우, 2*max 값이 count값이 된다.
        else if(max+1.0>=distance.toDouble()/max){
            bw.write("${(2*max).toInt()}\n")
        }
        //3 번째 케이스, 만약 distance 값을 max로 나눈 값이 max+1보다 큰 경우, 2*max+1이 count값이 된다.
        else{
            bw.write("${(2*max+1).toInt()}\n")
        }
    }
    bw.flush()
    bw.close()
}
```
