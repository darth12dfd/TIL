import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/*
    출처: https://www.acmicpc.net/problem/2667
    알고리즘 유형: 너비 우선 탐색
 */

data class digInfo(var vertical:Int, var horizontal:Int, var complex_id:Int)

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val tile=readLine().toInt()

    var complex=Array(tile,{IntArray(tile,{0})})//집들을 나타내는 배열
    var complex_id_list=Array(tile,{IntArray(tile,{0})})//단지 번호 까지 나타낸 배열
    var complex_count=0//단지 수

    var visited_list=Array(tile,{BooleanArray(tile,{false})})//방문 노드 저장할 2차원 배열


    //배열 입력
    for(i in 0 until tile){
        val string=readLine()
        for(j in 0 until tile){
            complex[i][j]=string[j]-'0'
        }
    }

    //complex_count가 증가되는 조건을 충족 시키면서 bfs함수를 호출한다.
    /*
        증가 조건:
        1). complex[i][j]가 0이 아니어야 한다.
        2). 여태까지 방문한 노드들 중에서, 이웃한 노드가 없어야 한다.
     */
    for(i in 0 until tile){
        for(j in 0 until tile){
            if(complex[i][j]!=0 && !visited_list[i][j]){
                bfs(i,j,complex_count,complex_id_list,complex,visited_list)//bfs를 호출한다.
                complex_count++//단지 수를 1 증가 시킨다.
            }
        }
    }

    println(complex_count)//단지의 수 출력

    var complex_id=IntArray(complex_count,{0})


    //단지별 가구 수 입력
    for(i in 0 until tile) {
        for (j in 0 until tile) {
            if (complex_id_list[i][j] != 0) {
                val tmp = complex_id_list[i][j] - 1
                complex_id[tmp]++
            }
        }
    }

    complex_id.sort()//단지별 가구수 오름차순 정렬
    for(i in 0 until complex_count){
        println(complex_id[i])
    }
}

fun bfs(vertical:Int, horizontal:Int,complex_count:Int, complex_id_list:Array<IntArray>, complex:Array<IntArray>, visited_list:Array<BooleanArray>){
    var current_complex_id=complex_count+1//현재 단지의 수
    val queue: Queue<digInfo> = LinkedList<digInfo>()// 큐를 생성
    visited_list[vertical][horizontal]=true//방문 리스트에 현재 노드 방문 표기
    complex_id_list[vertical][horizontal]=current_complex_id//해당 단지의 번호를 저장한다.
    queue.add(digInfo(vertical,horizontal,current_complex_id))//방문한 노드를 큐에 저장한다.

    //큐가 빌때까지 반복
    while(queue.isNotEmpty()){
        //가장 먼저 방문한 좌표 정보를 저장
        current_complex_id=queue.element().complex_id
        var current_vertical=queue.element().vertical
        var current_horizontal=queue.element().horizontal
        queue.poll()//그 좌표를 큐에서 삭제


        //만약 현재 노드를 방문했다면, 그 노드와 인접한 노드를 방문한다.
        /*
            방문 조건:
            1). complex[i][j]가 0이 아니어야 하고
            2). complex[first_vertical][first_horizontal]과 이웃해야 한다.
            3). complex[i][j]를 방문한 적이 없어야 한다.
         */
        for(i in 0 until complex.size){
            for(j in 0 until complex[i].size){
                val nx= Math.abs(current_horizontal - j)
                val ny= Math.abs(current_vertical - i)
                if(nx+ny==1 && complex[i][j]!=0 && !visited_list[i][j]){
                    visited_list[i][j]=true//방문 리스트에 현재 노드 방문 표기
                    complex_id_list[i][j]=current_complex_id//해당 단지의 번호를 저장한다.
                    queue.add(digInfo(i,j,current_complex_id))//방문한 노드를 큐에 저장한다.
                }
            }
        }
    }
}
