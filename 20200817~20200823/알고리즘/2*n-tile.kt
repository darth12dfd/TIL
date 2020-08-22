/*
    문제: 2×n 크기의 직사각형을 1×2, 2×1 타일로 채우는 방법의 수를 구하는 프로그램을 작성하시오.
    입력: 첫째 줄에 n이 주어진다.(1<=n<=1000)
    출력: 첫째 줄에 2*n 크기의 직사각형을 채우는 방법의 수를 10,007로 나눈 나머지를 출력한다.
    알고리즘 분류: 다이나믹 프로그래밍(Bottom-Up)=>점화식: d[n]=d[n-1]+d[n-2]
                (단 n>2 이상일 때만, d[0]=1,d[1]=1 이다.)
 */

import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args:Array<String>)=with(BufferedReader(InputStreamReader(System.`in`))){
    val number=this.readLine().toInt()
    var case=Array<Long>(number+1,{0})
    for(i in 1 until number+1){
        if(i==1){
            case[i]=1
        }
        else if(i==2){
            case[i]=2
        }
        else{
            case[i]=(case[i-2]+case[i-1])%10007
        }
    }
    println(case[number])
}
