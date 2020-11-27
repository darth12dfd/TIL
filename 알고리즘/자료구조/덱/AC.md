# AC

## 출처

* https://www.acmicpc.net/problem/5430

## 문제

* 선영이는 주말에 할 일이 없어서 새로운 언어 AC를 만들었다. AC는 정수 배열에 연산을 하기 위해 만든 언어이다. 이 언어에는 두 가지 함수 R(뒤집기)과 D(버리기)가 있다.

* 함수 R은 배열에 있는 숫자의 순서를 뒤집는 함수이고, D는 첫 번째 숫자를 버리는 함수이다. 배열이 비어있는데 D를 사용한 경우에는 에러가 발생한다.

* 함수는 조합해서 한 번에 사용할 수 있다. 예를 들어, "AB"는 A를 수행한 다음에 바로 이어서 B를 수행하는 함수이다. 예를 들어, "RDD"는 배열을 뒤집은 다음 처음 두 숫자를 버리는 함수이다.

* 배열의 초기값과 수행할 함수가 주어졌을 때, 최종 결과를 구하는 프로그램을 작성하시오.

## 입력

* 첫째 줄에 테스트 케이스의 개수 T가 주어진다. T는 최대 100이다.

* 각 테스트 케이스의 첫째 줄에는 수행할 함수 p가 주어진다. p의 길이는 1보다 크거나 같고, 100,000보다 작거나 같다.

* 다음 줄에는 배열에 들어있는 수의 개수 n이 주어진다. (0 ≤ n ≤ 100,000)

* 다음 줄에는 [x1,...,xn]과 같은 형태로 배열에 들어있는 수가 주어진다. (1 ≤ xi ≤ 100)

* 전체 테스트 케이스에 주어지는 p의 길이의 합과 n의 합은 70만을 넘지 않는다.

## 출력

* 각 테스트 케이스에 대해서, 입력으로 주어진 정수 배열에 함수를 수행한 결과를 출력한다. 만약, 에러가 발생한 경우에는 error를 출력한다.

## 입력 예제 1

```
4
RDD
4
[1,2,3,4]
DD
1
[42]
RRD
6
[1,1,2,3,5,8]
D
0
[]
```

## 출력 예제 1

```
[2,1]
error
[1,2,3,5,8]
error
```

## 풀이

* 처음엔 R이 입력 받았을 때, ```reverse()``` 메서드를 사용하는 방법을 썼다. 하지만, 이는 시간복잡도를 지나치게 늘리기 때문에 다른 방법을 고민해야 했다.

* 그러기 위해서, boolean 타입의 두 변수를 만들었다. 하나는 이것이 뒤집어 졌는가를 저장할 변수, 다른 하나는 현재 상황이 에러인지를 저장할 변수이다.

* 그런 다음 수행할 함수 p라는 문자열의 길이 만큼 반복문을 돌려서 각 함수마다 정해진 명령을 실행하도록 한다. 만약 R이 입력됬을 경우, isReverse 변수에 저장된 값을 기존의 저장된 값의 반대 값으로 저장한다.

* D가 입력 됬을 경우는, 먼저 입력받은 list가 비어있는지를 검사해서 비어있을 경우 isError 변수에 true를 저장하고 p에 대한 반복문을 탈출한다. 비어있지 않은 경우, isReverse변수를 조사해서 true값인 경우는, list의 마지막 값을 빼내고(pollLast), false 인 경우, 맨 앞의 값을 뺀다ㅣ(pollFirst)

* 그리고 해당 케이스에 대하여 명령문을 모두 분석한 다음, isError변수를 조사해서 그 값이 true이면, error를 출력하고, 그렇지 않은 경우는, isReverse 변수의 값에 따라 list에 있는 값들을 앞에서 빼거나 뒤에서 빼는 것을 출력한다.

## 소스코드

```kotlin
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main()=with(BufferedReader(InputStreamReader(System.`in`))){
    val case=readLine().toInt()
    val bw=BufferedWriter(OutputStreamWriter(System.`out`))

    for(i in 1..case){
        val order=readLine()//명령어 입력
        var isReverse=false
        var isError=false

        val size=readLine().toInt()//배열의 크기

        val st=StringTokenizer(readLine(),"[],")

        var list=LinkedList<Int>()

        for(i in 0 until size){
            list.add(st.nextToken().toInt())
        }

        for(j in 0 until order.length){
            if(order[j]=='R'){
                isReverse=!isReverse
            }
            else if(order[j]=='D'){
                if(list.isEmpty()){
                    isError=true
                    break
                }
                if(isReverse){
                    list.pollLast()
                }
                else{
                    list.pollFirst()
                }
            }
        }

        if(isError){
            bw.write("error\n")
        }
        else{
            bw.write("[")
            while(list.isNotEmpty()){
                if(isReverse){
                    bw.write("${list.pollLast()}")
                }
                else{
                    bw.write("${list.pollFirst()}")
                }
                if(list.size>0){
                    bw.write(",")
                }
            }
            bw.write("]\n")
        }
    }

    bw.flush()
    bw.close()
    close()
}
```
