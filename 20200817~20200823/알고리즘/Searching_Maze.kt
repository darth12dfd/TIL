import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Math.*
import java.util.*

/*
    출처:https://www.acmicpc.net/problem/2178
 */

data class Location(var vertical:Int,var horizontal:Int,var distance:Int)//좌표 정보를 저장할 데이터 클래스

fun main(args:Array<String>)=with(BufferedReader(InputStreamReader(System.`in`))){
    val order=readLine().split(' ')
    val vertical=order[0].toInt()//행 수
    val horizontal=order[1].toInt()//열 수
    
    var visited_list=Array(vertical,{BooleanArray(horizontal,{false})})//방문 노드 저장할 2차원 배열
    //보드 채우기
    var board=Array(vertical,{BooleanArray(horizontal,{false})})//보드 배열: 2차원 Boolean배열
    var distance=Array(vertical,{IntArray(horizontal,{0})})//각 노드별 거리를 저장할 2차원 배열
    for(i in 0 until vertical){
        val location=readLine()
        for(j in 0 until horizontal){
            val number=location[j]-'0'
            if(number==1){
                board[i][j]=true
            }
        }
    }
    //최소 경로 찾기
    bfs(0,0,board,visited_list,distance)//너비 우선 탐색으로 구한다.

    println(distance[vertical-1][horizontal-1])//최종적인 거리를 구한다.
}


fun bfs(vertical:Int,horizontal: Int,board:Array<BooleanArray>,visited:Array<BooleanArray>,distance_array:Array<IntArray>){
    var min_distance=999
    var distance=1
    val queue: Queue<Location> = LinkedList<Location>()
    visited[vertical][horizontal]=true//방문 정보 저장
    distance_array[vertical][horizontal]=distance
    queue.add(Location(vertical, horizontal, distance))//방문 노드를 큐에 저장
    
    //큐가 빌 때 까지 반복
    while(queue.isNotEmpty()){
        distance=queue.element().distance
        //가장 먼저 방문한 좌표 정보를 저장
        var current_vertical=queue.element().vertical
        var current_horizontal=queue.element().horizontal
        queue.poll()//가장 먼저 방문한 좌표 정보를 큐에서 삭제(FIFO)

        //만약 현재 노드를 방문했다면, 그 노드와 인접한 노드를 방문한다.
        for(i in 0 until board.size){
            for(j in 0 until board[0].size){
                /*1. 현재 노드에서 거리가 1이고,
                  2. 방문 가능하며
                  3. 방문하지 않은 경우
                 */
                val nx=abs(current_horizontal-j)
                val ny=abs(current_vertical-i)
                if(nx+ny==1 && board[i][j] && !visited[i][j]){
                    visited[i][j]=true
                    distance_array[i][j]=distance+1
                    queue.add(Location(i,j,distance+1))
                }
            }
        }
    }
}
