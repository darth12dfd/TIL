# LCS

## 출처

* https://www.acmicpc.net/problem/9251

## 문제

* LCS(Longest Common Subsequence, 최장 공통 부분 수열)문제는 두 수열이 주어졌을 때, 모두의 부분 수열이 되는 수열 중 가장 긴 것을 찾는 문제이다.

* 예를 들어, ACAYKP와 CAPCAK의 LCS는 ACAK가 된다.

## 입력

* 첫째 줄과 둘째 줄에 두 문자열이 주어진다. 문자열은 알파벳 대문자로만 이루어져 있으며, 최대 1000글자로 이루어져 있다.

## 출력

* 첫째 줄에 입력으로 주어진 두 문자열의 LCS의 길이를 출력한다.

## 입력 예제

```
ACAYKP
CAPCAK
```

## 출력 예제

```4```

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.InputStreamReader

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    var str1=readLine()
    var str2=readLine()

    str1="0"+str1
    str2="0"+str2

    var dp=Array(str1.length,{Array(str2.length,{0})})

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

    print(dp[str1.lastIndex][str2.lastIndex])

    close()
}
```
