# N과 M (1)

## 출처

* https://www.acmicpc.net/problem/15649

## 문제

* 자연수 N과 M이 주어졌을 때, 아래 조건을 만족하는 길이가 M인 수열을 모두 구하는 프로그램을 작성하시오.

```1부터 N까지 자연수 중에서 중복 없이 M개를 고른 수열```

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
1 2
1 3
1 4
2 1
2 3
2 4
3 1
3 2
3 4
4 1
4 2
4 3
```

## 입력 예제 3

```4 4```

## 출력 예제 3

```
1 2 3 4
1 2 4 3
1 3 2 4
1 3 4 2
1 4 2 3
1 4 3 2
2 1 3 4
2 1 4 3
2 3 1 4
2 3 4 1
2 4 1 3
2 4 3 1
3 1 2 4
3 1 4 2
3 2 1 4
3 2 4 1
3 4 1 2
3 4 2 1
4 1 2 3
4 1 3 2
4 2 1 3
4 2 3 1
4 3 1 2
4 3 2 1
```

## 풀이

* 백트래킹 알고리즘은 조건에 맞는 모든 조합의 수를 찾는 것이다. 여기서 조건은 중복되는 수가 없다는 것과, 그 조합을 이루는 수의 갯수가 M개 이어야 한다는 것이다.

* dfs 알고리즘과 재귀함수를 이용한다. 이 때 dfs의 깊이가 m과 동일할 때 결과값을 출력하도록 한다. 그렇지 않은 경우, 깊이를 계속 1 증가시키면서 노드를 방문한다.

* 재귀함수를 빠져나온 후, 직전에 방문한 노드의 정보를 false로 바꿔준다. 이는, 이 1부터 N까지의 자연수 중에서, M개의 수를 순서대로 고르는 모든 경우를 출력하라고 했기 때문이다. 만약 방문한 노드의 정보를 false로 변경하지 않는다면, 그 수가 올바르게 출력되지 않는다.)

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val sc=StringTokenizer(readLine())
    val n=sc.nextToken().toInt()
    val m=sc.nextToken().toInt()

    var visit=BooleanArray(n,{false})//방문한 노드 저장할 배열
    var path=IntArray(m,{0})
    dfs(n,m,0,visit,path)
    close()
}

fun dfs(n:Int, m:Int, depth:Int,visited:BooleanArray,path:IntArray){
    if(depth==m){//만약 m만큼의 길이가 되었을 경우, 해당 길이의 배열을 출력한다.
        for(i in 0 until m){
            print("${path[i]} ")
        }
        println()
        return
    }

    for(i in 0 until n){
        //해당 노드를 방문하지 않았을 경우(즉, 중복을 허락하지 않는다.)
        if(visited[i]==false){//이 경우가 유망한 경우이다.
            visited[i]=true//해당 노드를 방문 상태로 변경
            path[depth]=i+1//방문 노드 배열에 해당 노드를 저장
            dfs(n,m,depth+1,visited, path)//깊이를 1 늘린 상태로 재귀함수 호출
            visited[i]=false//재귀 탈출시, 마지막 노드의 방문 정보를 지운다.
        }
    }
}
```
