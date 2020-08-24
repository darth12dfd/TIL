import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Math.min
import java.util.*
/*
    문제 출처: https://www.acmicpc.net/problem/1149
    알고리즘 유형: 다이나믹 프로그래밍
 */
fun main(args:Array<String>)=with(BufferedReader(InputStreamReader(System.`in`))){
    val house_count=readLine().toInt()
    var list=Array(house_count,{IntArray(3,{0})})//입력 값을 저장할 이차원 배열
    var cost= Array(house_count,{IntArray(3,{0})})//각 경우별 비용을 저장할 리스트

    //list에 입력
    for(i in 0 until house_count){
        val st=StringTokenizer(readLine())
        for(j in 0 until 3){
            list[i][j]=st.nextToken().toInt()
        }
    }
    
    //cost 배열을 채운다. 이때, cost 배열의 각 값은 각 경우에 따른 최소 비용의 값이다. 이를테면, cost[1][0]=list[1][0]+min(cost[0][1],cost[0][2])가 된다.
    for(i in 0 until house_count){
        for(j in 0 until 3){
            cost[i][j]=list[i][j]//cost배열의 값은 기본적으로 list에 입력받은 값들이 먼저 더해진다.
            if(i!=0){//만약 이 집이 처음 집이 아닌 경우, 이전 집의 색깔도 고려해야 한다.
                if(j==0){
                    cost[i][j]+=min(cost[i-1][j+1],cost[i-1][j+2])//만약 빨간 색의 비용을 구하는 것이면, 이전 집의 각 색깔에 대한 최소 비용 값들을 비교해서 더 작은 값을 더해준다.
                }
                else if(j==1){
                    cost[i][j]+=min(cost[i-1][j-1],cost[i-1][j+1])//초록 색의 비용을 구하는 경우
                }
                else{
                    cost[i][j]+=min(cost[i-1][j-2],cost[i-1][j-1])//파란 색의 비용을 구하는 경우
                }
            }
        }
    }
    println(cost[house_count-1].min())
}
