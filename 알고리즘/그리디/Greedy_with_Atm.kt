import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

//출처: https://www.acmicpc.net/problem/11399
//알고리즘: 탐욕 알고리즘, 정렬 알고리즘
//풀이: 결과적으로 계산하는 데 가장 적은 시간이 드는 사람이 먼저 계산하는 것이 총 계산시간을 최소화 시키는 방법이 된다.(탐욕 알고리즘)

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val people=readLine().toInt()
    var time= mutableListOf<Int>()
    val sc=StringTokenizer(readLine())

    for(i in 0 until people){
        time.add(i,sc.nextToken().toInt())
    }

    time.sort()

    var total_time=0

    for(i in 0 until people){
        for(j in 0 until i+1){
            total_time+=time[j]
        }

    }
    println(total_time)
}
