# LCS 2

## 출처

* https://www.acmicpc.net/problem/9252

## 문제

* LCS(Longest Common Subsequence, 최장 공통 부분 수열)문제는 두 수열이 주어졌을 때, 모두의 부분 수열이 되는 수열 중 가장 긴 것을 찾는 문제이다.

* 예를 들어, ACAYKP와 CAPCAK의 LCS는 ACAK가 된다.

## 입력

* 첫째 줄과 둘째 줄에 두 문자열이 주어진다. 문자열은 알파벳 대문자로만 이루어져 있으며, 최대 1000글자로 이루어져 있다.

## 출력

* 첫째 줄에 입력으로 주어진 두 문자열의 LCS의 길이를, 둘째 줄에 LCS를 출력한다.

* LCS가 여러 가지인 경우에는 아무거나 출력하고, LCS의 길이가 0인 경우에는 둘째 줄을 출력하지 않는다.

## 입력 예제 1

```
ACAYKP
CAPCAK
```

## 출력 예제 1

```
4
ACAK
```

## 풀이

* 여기서는 dp 배열로 이차원 배열을 사용하므로 역추적을 하는 과정에서 두 개의 변수를 고려해야 한다.

* 그렇기에 역추적하는 과정에서 dp[i][j]와 여러 값들(dp[i-1][j], d[[i][j-1], dp[i-1][j-1]) 등을 고려해야한다.

* 만약 dp[i][j]와 dp[i-1][j]와 동일하다면 i값을 1 줄여야 하고, dp[i][j]와 dp[i][j-1]의 값이 동일하다면 j값을 1 줄여야 하고, 둘 다 아닌 경우 dp[i][j] = dp[i-1][j-1] -1 이라는 뜻이므로, ㄴ두 번째 문자열에서 j번째 인덱스에 해당하는 값을 스택에 추가하고, i값과 j값을 1씩 줄여준다. 이 과정을 i값과 j값 둘다 1 보다 크거나 같은 경우에 대하여 반복하고 i와 j의 초깃값은 각 배열의 길이이다. 

## 소스코드

```kotlin
package dynamicprograming

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() = with(BufferedReader(InputStreamReader(System.`in`))){
    val bw = BufferedWriter(OutputStreamWriter(System.`out`))

    var str1 = readLine()//첫 번째 문장
    var str2 = readLine()//두 번째 문장

    str1="0"+str1
    str2="0"+str2

    //1. 최단거리 구하기
    var dp=Array( str1.length, { Array( str2.length, { 0 } ) } )

    for(i in 1 until str1.length){
        for(j in 1 until str2.length){
            if(str1[i]==str2[j]){
                dp[i][j]=dp[i-1][j-1]+1
            }
            else{
                dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1])
            }
        }
    }

    var result = dp[str1.lastIndex][str2.lastIndex]

    bw.write("$result\n")

    var i = str1.lastIndex
    var j = str2.lastIndex
    
    //2. 역추적
    var stack = Stack<String>()

    while(i >= 1 && j >= 1){
        //위와 같은 경우 위로 이동
        if(dp[i][j] == dp[i-1][j]) {
            i--
        }//왼쪽과 같은 경우 왼쪽으로 이동
        else if(dp[i][j] == dp[i][j-1]){
            j--
        }
        else {
            //대각선으로 같다면
            stack.push(str2.get(j).toString())//해당 값을 stack에 push
            i--;j--//i값과 j값을 1씩 줄인다.
        }
    }
    while(stack.isNotEmpty()){
        bw.write("${stack.pop()}")
    }

    bw.flush()
    bw.close()

    close()
}
```
