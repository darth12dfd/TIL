import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Collections

//출처: https://www.acmicpc.net/problem/2751

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val size=readLine().toInt()

    var list=ArrayList<Int>()

    for(i in 0 until size){
        list.add(i,readLine().toInt())
    }

    Collections.sort(list)
    for(i in 0 until size){
        println(list[i])
    }

}
