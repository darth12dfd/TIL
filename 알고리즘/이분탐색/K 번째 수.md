# K번째 수

## 출처

* https://www.acmicpc.net/problem/1300

## 문제

* 세준이는 크기가 N×N인 배열 A를 만들었다. 배열에 들어있는 수 A[i][j] = i×j 이다. 이 수를 일차원 배열 B에 넣으면 B의 크기는 N×N이 된다. B를 오름차순 정렬했을 때, B[k]를 구해보자.

* 배열 A와 B의 인덱스는 1부터 시작한다.

## 입력

* 첫째 줄에 배열의 크기 N이 주어진다. N은 105보다 작거나 같은 자연수이다. 둘째 줄에 k가 주어진다. k는 min(109, N2)보다 작거나 같은 자연수이다.

## 출력

* B[k]를 출력한다.

## 입력 예제 1

```
3
7
```

## 출력 예제 1

```
6
```

## 소스코드

```kotlin
package binarysearch

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    val n=readLine().toLong()
    val k=readLine().toInt()

    var left=1L
    var right=k.toLong()

    var result=0L

    while(left<=right){
        var mid:Long=(left+right)/2

        var count=0L

        for(i in 1..n){
            count+=Math.min(mid/i,n)
        }

        if(count>=k){
            result=mid
            right=mid-1
        }
        else{
            left=mid+1
        }
    }

    bw.write("$result")
    bw.flush()
    bw.close()
    close()
}
```
