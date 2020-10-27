# N과 M (4)

## 출처

* https://www.acmicpc.net/problem/15652

## 문제

* 자연수 N과 M이 주어졌을 때, 아래 조건을 만족하는 길이가 M인 수열을 모두 구하는 프로그램을 작성하시오.

```
1부터 N까지 자연수 중에서 M개를 고른 수열
같은 수를 여러 번 골라도 된다.
고른 수열은 비내림차순이어야 한다.(길이가 K인 수열 A가 A1 ≤ A2 ≤ ... ≤ AK-1 ≤ AK를 만족하면, 비내림차순이라고 한다.)
```

## 입력

* 첫째 줄에 자연수 N과 M이 주어진다. (1 ≤ M ≤ N ≤ 8)

## 출력

* 한 줄에 하나씩 문제의 조건을 만족하는 수열을 출력한다. 중복되는 수열을 여러 번 출력하면 안되며, 각 수열은 공백으로 구분해서 출력해야 한다.

* 수열은 사전 순으로 증가하는 순서로 출력해야 한다.

## 입력 예제 1

```3 1```

## 출력 예제 1

```
1
2
3
```

## 입력 예제 2

```4 2```

## 출력 예제 2

```
1 1
1 2
1 3
1 4
2 2
2 3
2 4
3 3
3 4
4 4
```

## 입력 예제 3

```3 3```

## 출력 예제 3

```
1 1 1
1 1 2
1 1 3
1 2 2
1 2 3
1 3 3
2 2 2
2 2 3
2 3 3
3 3 3
```

## 풀이

* N과 M(2) 문제와 N과 M(3) 문제를 적절히 합하면 풀 수 있다. 기존 N과 M(3) 문제에서 dfs 함수에 인덱스 정보를 입력하고 반복문의 시작 부분을 입력받은 인덱스 에서 시작하면 된다.

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val sc=StringTokenizer(readLine())
    val n=sc.nextToken().toInt()
    val m=sc.nextToken().toInt()

    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    var path=IntArray(m,{0})//경로를 저장할 배열

    fun dfs_result(n:Int, m:Int, depth:Int,index:Int){
        if(depth==m){//만약 m만큼의 길이가 되었을 경우, 해당 길이의 배열을 출력한다.
            for(i in 0 until m){
                bw.write("${path[i]} ")
            }
            bw.write("\n")
            return
        }

        for(i in index until n){
            //해당 노드를 방문하지 않았을 경우(즉, 중복을 허락하지 않는다.)
            path[depth]=i+1//방문 노드 배열에 해당 노드를 저장
            dfs_result(n,m,depth+1,i)//깊이를 1 늘린 상태로 재귀함수 호출
        }
    }
    dfs_result(n,m,0,0)
    bw.flush()
    bw.close()
    close()

}
```
