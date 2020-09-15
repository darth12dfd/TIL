import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Integer.max

//https://www.acmicpc.net/problem/2579

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val stairs=readLine().toInt()
    val stair_cost= IntArray(stairs+1,{0}) //각 층별 비용을 저장한 배열

    var max_cost=IntArray(stairs+1,{0}) //각 층에서 최대 비용을 저장할 배열


    //층별 비용 입력
    for(i in 1 until stair_cost.size){
        stair_cost[i]=readLine().toInt()
    }

    /*
        i=1일 때, max_cost[1]=stair_cost[1]
        i=2일 때, max_cost[2]=stair_cost[1]+stair_cost[2]
        그 외, max_cost[i]=max(max_cost[i-2],max_cost[i-3]+stair_cost[i-1])+stair_cost[i]
     */
    for(i in 1 until max_cost.size){

        if(i==1){
            max_cost[i]=stair_cost[i]
        }
        else if(i==2){
            max_cost[i]=stair_cost[i-1]+stair_cost[i]
        }
        else{
            max_cost[i]=max(max_cost[i-2],max_cost[i-3]+stair_cost[i-1])+stair_cost[i]
        }
    }

    println(max_cost[stairs])
}
