# KCM Travle

## 출처

* https://www.acmicpc.net/problem/10217

## 문제

* 각고의 노력 끝에 찬민이는 2014 Google Code Jam World Finals에 진출하게 되었다. 구글에서 온 초대장을 받고 기뻐했던 것도 잠시, 찬찬히 읽어보던 찬민이는 중요한 사실을 알아차렸다. 최근의 대세에 힘입어 구글 역시 대기업답게 비용 감축에 열을 내고 있었던 것이다.

* 초대장 내용에 의하면 구글은 찬민에게 최대 M원까지의 비용만을 여행비로써 부담해주겠다고 한다. 인천에서 LA행 직항 한 번 끊어주는게 그렇게 힘드냐고 따지고도 싶었지만, 다가올 결승 대회를 생각하면 대회 외적인 곳에 마음을 쓰고 싶지 않은 것 또한 사실이었다. 그래서 찬민은 인천에서 LA까지 M원 이하로 사용하면서 도착할 수 있는 가장 빠른 길을 차선책으로 택하기로 하였다.

* 각 공항들간 티켓가격과 이동시간이 주어질 때, 찬민이 인천에서 LA로 갈 수 있는 가장 빠른 길을 찾아 찬민의 대회 참가를 도와주자.

## 입력

* 입력 파일의 첫 번째 줄에 테스트 케이스의 수를 의미하는 자연수 T가 주어진다. 그 다음에는 T개의 테스트 케이스가 주어진다.

* 각 테스트 케이스의 첫 줄에는 공항의 수 N (2 ≤ N ≤ 100), 총 지원비용 M (0 ≤ M ≤ 10,000), 티켓정보의 수 K (0 ≤ K ≤ 10,000)가 공백으로 구분되어 주어진다. 이어서 K개의 줄에 걸쳐 각 티켓의 출발공항 u, 도착공항 v (1 ≤ u, v ≤ N, u ≠ v), 비용 c (1 ≤ c ≤ M, 정수), 그리고 소요시간 d (1 ≤ d ≤ 1,000) 가 공백으로 구분되어 주어진다. 인천은 언제나 1번 도시이고, LA는 언제나 N번 도시이다

## 출력

* 각 테스트 케이스당 한 줄에 찬민이 LA에 도착하는 데 걸리는 가장 짧은 소요시간을 출력한다.

만약, LA에 도착할 수 없는 경우 "Poor KCM"을 출력한다.

## 입력 예제 1

```
2
3 100 3
1 2 1 1
2 3 1 1
1 3 3 30
4 10 4
1 2 5 3
2 3 5 4
3 4 1 5
1 3 10 6
```

## 출력 예제 1

```
2
Poor KCM
```

## 풀이

* 문제를 읽어보면 최단 시간을 구하는 문제이면서 동시에 티켓 가격은 조건을 만족해야 한다.

* 가중치가 1이 아니므로 DFS, BFS가 아닌 다익스트라, 벨만 포드, 플로이드 와샬을 사용해야 한다.

* 한 노드 ▶ 노드 이므로 다익스트라, 벨만 포드를 사용해야 한다.

* 음의 가중치는 없기 때문에 다익스트라를 사용해야 한다.

* 결국 다익스트라를 사용해야 하는데 이 문제는 다익스트라 + dp를 이용해서 풀이한다.

* 이때 dp[i][j] = k가 있다면, i노드까지 j비용으로 갈 수 있는 최소의 시간 = k 라고 생각하고 풀이에 들어간다.

* 우선순위 큐는 시간을 기준으로 집어넣도록 설정한다.

* 

## 소스코드

```kotlin
package graphtheory.dijkstra

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

data class AirLine(val index:Int, val time:Int, val cost:Int)//도착 노드 시간 비용을 저장할 배열

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val T=readLine().toInt()//테스트 케이스

    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    for(i in 1..T){
        var st=StringTokenizer(readLine())

        val N=st.nextToken().toInt()//공항의 수
        val M=st.nextToken().toInt()//총 지원 비용
        val K=st.nextToken().toInt()//티켓 정보의 주
        
        val MAX_VALUE=100*1000//한 간선의 최대값이다.

        var dp=Array(N+1,{IntArray(M+1,{MAX_VALUE})})//dp[i][j]= 시작 공항부터 i공항 까지 j의 비용을 소비하며 이동한 시간 중 최소시간

        //비행기 항로들을 저장할 어레이리스트
        var airLines:ArrayList<ArrayList<AirLine>>
        airLines= ArrayList()

        for(i in 0..N){
            airLines.add(ArrayList())
        }

        //티켓의 수만큼 반복문 돌리기
        for(j in 0 until K){
            st= StringTokenizer(readLine())

            val u=st.nextToken().toInt()//티켓의 출발 공항
            val v=st.nextToken().toInt()//티켓의 도착 공항
            val c=st.nextToken().toInt()//비용
            val d=st.nextToken().toInt()//소요시간

            airLines[u].add(AirLine(v,d,c))
        }

        fun dijkstras():Int{
            var queue=PriorityQueue<AirLine>(kotlin.Comparator { o1, o2 ->  o1.time-o2.time })//간선 가중치가 작은값을 우선으로 호출하는 우선순위 큐(가중치가 같으면 비용순으로 정렬)
            queue.add(AirLine(1,0,0))//큐에 시작 노드 하나 추가

            dp[1][0]=0

            while(queue.isNotEmpty()){
                val a=queue.poll()//하나 추출

                if(a.index == N){
                    break
                }

                if(dp[a.index][a.cost] < a.time){
                    continue
                }

                dp[a.index][a.cost] = a.time

                //해당 인덱스를 시작노드로 하는 모든 항로에 대하여 반복문 실행
                for(j in 0 until airLines[a.index].size){
                    val toAirplane=airLines[a.index].get(j)

                    val toNode=toAirplane.index
                    val toCost=a.cost + toAirplane.cost
                    val toTime=a.time + toAirplane.time

                    if(toCost > M){
                        continue
                    }

                    if(dp[toNode][toCost] > toTime){
                        //불필요한 푸쉬 방지위해 다음과 같이 값 설정
                        for(k in toCost..M){
                            if(dp[toNode][k] > toTime){
                                dp[toNode][k] = toTime
                            }
                        }

                        queue.add(AirLine(toNode,toTime,toCost))
                    }
                }
            }

            var result = MAX_VALUE

            for(j in 1..M){
                result = Math.min(result,dp[N][j])
            }

            return result
        }

        val result=dijkstras()

        if(result==MAX_VALUE){
            bw.write("Poor KCM\n")
        }

        else{
            bw.write("$result\n")
        }

    }

    bw.flush()
    bw.close()
    close()
}
```
