# Z

## 출처

* https://www.acmicpc.net/problem/1074

## 문제

* 한수는 크기가 2^N × 2^N인 2차원 배열을 Z모양으로 탐색하려고 한다. 예를 들어, 2×2배열을 왼쪽 위칸, 오른쪽 위칸, 왼쪽 아래칸, 오른쪽 아래칸 순서대로 방문하면 Z모양이다.

* N > 1인 경우, 배열을 크기가 2^(N-1) × 2^(N-1)로 4등분 한 후에 재귀적으로 순서대로 방문한다.

* N이 주어졌을 때, r행 c열을 몇 번째로 방문하는지 출력하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 정수 N, r, c가 주어진다.

## 출력

* r행 c열을 몇 번째로 방문했는지 출력한다.

## 제한

* 1 ≤ N ≤ 15
* 0 ≤ r, c < 2^N

## 입력 예제 1

```
2 3 1
```

## 출력 예제 1

```
11
```

## 입력 예제 2

```
3 7 7
```

## 출력 예제 2

```
63
```

## 풀이

* 재귀함수를 활용한 분할정복 문제.
 
* 가장 먼저 크기가 2^N * 2^N 인 행렬을 사분할 한 뒤 r과 c가 어느 사분면에 위채해 있는지를 구하면 된다. 그리고 이동한 횟수를 저장할 변수 cnt를 선언한다.

* 그 다음 재귀함수를 정의하고 매개변수로 한 변의 사이즈 size와 타겟 위치 인덱스인 r, c를 넘겨받는다.

* r과 c가 1사분면에 속한다면, 아무데도 방문하지 않은 것이므로 cnt를 건드리지 않고, 재귀함수에 현재 size의 절반, r, c를 넘겨준다. 

* r과 c가 2사분면에 속한다면, 앞에서 1사분면을 방문해야하므로 count에 (size x size)/4를 더한다. (한 사분면의 크기: 전체 배열 크기의 4등분) 그리고 재귀함수에 현재 size의 절반, 2사분면에서의 r,c 상대위치 r, c-size/2를 넘겨준다.

* r과 c가 3사분면에 속한다면, 앞에서 1,2사분면을 방문해야하므로 count에 (size x size)/4 * 2를 더한다. 그리고 재귀함수에 현재 size의 절반, 3사분면에서의 r,c 상대위치 r-size/2, c를 넘겨준다.

* r과 c가 4사분면에 속한다면, 앞에서 1,2,3사분면을 방문해야하므로 count에 (size x size)/4 * 3를 더한다. 그리고 재귀함수에 현재 size의 절반, 4사분면에서의 r,c 상대위치 r-size/2, c-size/2를 넘겨준다.

* 위를 반복하다 만일 size 가 1이 되면 함수를 종료한다.

* cnt값을 출력한다.

## 소스코드

```kotlin
package divideandconquer

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main () = with(BufferedReader(InputStreamReader(System.`in`))){
    val st = StringTokenizer(readLine())

    val N = st.nextToken().toInt()//2^N * 2^N인 2차원 배열(1 이상 15 이하의 자연수)

    val r = st.nextToken().toInt()//도착해야 하는 행(0 이상 2^N 이하의 자연수)
    val c = st.nextToken().toInt()//도착해야 하는 열(0 이상 2^N 이하의 자연수)

    val size = Math.pow(2.0,N.toDouble()).toInt()

    var cnt = 0//이동 횟수를 저장할 변수

    fun find(size:Int,r:Int,c:Int){
        if(size == 1){//만약 행렬의 크기가 1이라면 함수를 리턴
            return
        }

        //행렬 기준 1사분면에 대하여 탐색
        if(r < size/2 && c < size/2 ){
            find(size/2,r,c)//사이즈를 2로 줄인 다음 재귀 함수 호출
        }
        //행렬 기준 2사분면에 대하여 탐색
        else if(r < size/2 && c >= size/2 ){
            cnt += (size * size/4) //4분면 기준으로 행렬 너비를 4로 나눈 값을 더함
            find(size/2,r,c-size/2)//사이즈를 2로 줄인 다음 c에다가 해당 값을 줄인 값을 인자로하는 재귀 함수 호출
        }
        //행렬 기준 3사분면에 대하여 탐색
        else if(r >=size/2 && c< size/2){
            cnt += (size * size / 4) * 2//4분면 기준으로 행렬 너비를 4로 나눈 값에 2를 곱한 값을 더함
            find(size/2,r-size/2,c)//사이즈를 2로 줄인 다음 r에다가 해당 값을 줄인 값을 인자로 하는 재귀 함수 호출
        }
        //행렬 기준 4사분면에 대하여 탐색
        else{
            cnt += (size* size/4) * 3 //4분면 기준으로 행렬 너비를 4로 나눈 값에 3을 곱한 값을 더함
            find(size/2,r-size/2,c-size/2)//사이즈를 2로 줄인 다음 해당 값을 r값과 c값에서 뺀 값들을 인자로 하는 재귀함수 호출
        }
    }

    find(size,r,c)
    println(cnt)//이동 횟수 출력

    close()
}
```
