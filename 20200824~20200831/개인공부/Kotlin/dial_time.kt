/*
    출처: https://www.acmicpc.net/problem/5622
    핵심: when을 표현식으로 사용하기
 */

import java.io.BufferedReader
import java.io.InputStreamReader

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val dial=readLine()
    var total_time=0//총 시간
    for(i in 0 until dial.length){
        val number:Int = when(dial[i]){
            'A','B','C'->2
            'D','E','F'->3
            'G','H','I'->4
            'J','K','L'->5
            'M','N','O'->6
            'P','Q','R','S'->7
            'T','U','V'->8
            'W','X','Y','Z'->9
            else->1
        }
        total_time+=(number+1)
    }
    println(total_time)
}
