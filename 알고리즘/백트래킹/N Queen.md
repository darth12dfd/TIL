# N-Queen

## 출처

* https://www.acmicpc.net/problem/9663

## 문제

* N-Queen 문제는 크기가 N × N인 체스판 위에 퀸 N개를 서로 공격할 수 없게 놓는 문제이다.

* N이 주어졌을 때, 퀸을 놓는 방법의 수를 구하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 N이 주어진다. (1 ≤ N < 15)

## 출력

* 첫째 줄에 퀸 N개를 서로 공격할 수 없게 놓는 경우의 수를 출력한다.

## 입력 예제

```8```

## 출력 예제

```92```

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val n=readLine().toInt()
    var case=0//조건 해당하는 모든 경우를 저장할 변수
    var col=IntArray(n,{0})

    fun queens(i:Int){

        fun promising(j:Int):Boolean{
            for(k in 0 until j){
                if(col[j]==col[k] || Math.abs(col[j]-col[k])==Math.abs(j-k)){
                    return false
                }
            }
            return true
        }

        if(i==n){
            case++
            return
        }
        else{
            for(a in 0 until n){
                col[i]=a
                if(promising(i)) {
                    queens(i + 1)
                }
            }
        }
    }

    queens(0)
    println(case)
    close()
}
```
