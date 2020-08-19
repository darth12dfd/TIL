import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/*
 문제: 피보나치 함수
 목적: N이 주어졌을 때, fibonacci(N)을 호출했을 때, 0과 1이 각각 몇 번 출력되는지 구하는 프로그램을 작성하시오.
 예시: fibonacci(3)을 호출하면 다음과 같은 일이 일어난다.
        fibonacci(3)은 fibonacci(2)와 fibonacci(1) (첫 번째 호출)을 호출한다.
        fibonacci(2)는 fibonacci(1) (두 번째 호출)과 fibonacci(0)을 호출한다.
        두 번째 호출한 fibonacci(1)은 1을 출력하고 1을 리턴한다.
        fibonacci(0)은 0을 출력하고, 0을 리턴한다.
        fibonacci(2)는 fibonacci(1)과 fibonacci(0)의 결과를 얻고, 1을 리턴한다.
        첫 번째 호출한 fibonacci(1)은 1을 출력하고, 1을 리턴한다.
        fibonacci(3)은 fibonacci(2)와 fibonacci(1)의 결과를 얻고, 2를 리턴한다.
 입력: 첫째 줄에 테스트 케이스의 개수 T가 주어진다. 각 테스트 케이스는 한 줄로 이루어져 있고, N이 주어진다. N은 40보다 작거나 같은 자연수 또는 0이다.
 출력: 각 테스트 케이스마다 0이 출력되는 횟수와 1이 출력되는 횟수를 공백으로 구분해서 출력한다.
 알고리즘 분류: 다이나믹 프로그래밍
 */

fun main(args:Array<String>) {
    val br=BufferedReader(InputStreamReader(System.`in`))
    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    val case=br.readLine().toInt()

    for(i in 0 until case){
        val number=br.readLine().toInt()
        val zero=Array<Int>(number+1,{0})
        val one=Array<Int>(number+1,{0})
        for(j in 0 until number+1){
            if(j==0){
                zero[j]++
            }
            else if(j==1){
                one[j]++
            }
            else{
                zero[j]=zero[j-1]+zero[j-2]
                one[j]=one[j-1]+one[j-2]
            }
        }
        bw.write("${zero[number]} ${one[number]}\n")
    }
    bw.flush()
    br.close()
    bw.close()
}
