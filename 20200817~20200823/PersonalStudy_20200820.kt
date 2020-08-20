import java.io.BufferedReader
import java.io.InputStreamReader
/*
    문제: 수 10개를 입력받은 뒤, 이를 42로 나눈 나머지를 구한다. 그 다음 서로 다른 값이 몇 개 있는지 출력하는 프로그램을 작성하시오.
    입력: 첫째 줄부터 열번째 줄 까지 숫자가 한 줄에 하나씩 주어진다. 이 숫자는 1,000보다 작거나 같고, 음이 아닌 정수이다.
    출력: 첫째 줄에, 42로 나누었을 때, 서로 다른 나머지가 몇 개 있는지 출력한다.
    알고리즘: 수학/사칙연산
    활용한 것: with함수와 expression 활용. 아래의 코드에서 main함수를 expression으로 표현해서 with함수로 설정하고, 여기서 BufferedReader타입의 인수를 revceiver매개변수로 받는다.
    그리고 이를 bloock 매개변수가 가리키는 함수에 바로 넣어 호출한다. 그렇게 함으로서 코드 중복을 줄이고 실행시간을 줄일 수 있다.
 */
fun main(args:Array<String>)=with(BufferedReader(InputStreamReader(System.`in`))){
    val set= mutableSetOf<Int>()
    for(i in 1..10){
        val number=this.readLine().toInt()
        set.add(number%42)
    }
    println(set.size)
}
