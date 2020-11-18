# JSON 데이터 다루기

* JSON은 JavaScript Object Notation으로 자바스크립트 객체 포맷을 데이터를 주고받을 때 사용할 수 있도록 문자열로 표현한 것을 말한다.

* 그렇기에 자바스크립트 객체 포맷과 거의 동일하다. 속성의 이름과 문자열에 큰따옴표를 사용한다는 정도의 차이만 존재한다.

## JSON 데이터 포멧

* 중괄호를 사용해서 객체를 만들 수 있다.

* 중괄호 안에 여러개의 속성이 있는데 각각의 속성은 콤마(,)로 구분되며, 각각의 속성 자체는 속성 이름과 속성 값이 콜론(:)기호로 구분되어 한 쌍을 이룬다.

* 콜론 뒤에 값이 들어가는데, 기본 자료형(문자열, 숫자)이 올 수도 있고, 다시 중괄호로 싸인 객체가 들어올 수 도 있다.

* 문자열 값에는 큰따옴표를 붙이고 숫자에는 붙이지 않는다. 배열을 사용하려면 대괄호를 사용하고 그 안에 쉼표로 구분된 배열 원소들이 들어간다.

## JSON 결과물 처리

* Gson은 자바스크립트에서 처럼 JSON 문자열을 객체로 변환할 수 있도록 한다. 즉, JSON 문자열이 자바 객체로 만들어질 수 있다.

* Volley를 사용해서 웹서버로부터 JSON 응답을 받았다면 Gson을 이용해서 자바 객체로 바꾸고 그 객체 안에 들어있는 데이터를 접근하여 사용할 수 있다.

## 실습

* 먼저 build.gradle(Module:app)파일을 열고 라이브러리를 추가한다.

```gradle
implementation 'com.google.code.gson:gson:2.8.5'
```

* Gson은 JSON 문자열을 자바 객체로 바꿔주는데 자바는 객체를 만들 때 클래스를 먼저 저으이하는 과정을 거치므로 JSON 문자열을 자바 객체로 바꿀때도 클래스를 먼저 정의한다.

* 클래스의 이름은 MovieList로 하고 boxOfficeResult라는 이름의 변수를 선언한다. 이 때 앞으로 나오는 모든 변수들은 영화 정보 JSON 응답의 포맷에 맞추어 정의한다.

```java
public class MovieList {
    MovieListResult boxOfficeResult;
}
```

* 또한 변수의 자료형은 JSON 문자열에서 속성의 자료형과 같아야 한다.

* JSON 문자열에서 boxOfficeResult 속성의 값이 객체이므로 이 객체를 변환하여 담아둘 클래스를 MovieListResult라는 이름으로 추가한다.

```java
import java.util.ArrayList;

public class MovieListResult {
    String boxofficeType;
    String showRange;

    ArrayList<Movie> dailyBoxOfficeList= new ArrayList<Movie>();

}
```

* JSON 문자열의 속성중 dailyBoxOfficeList 의 값이 배열이므로 이를 ArrayList 자료형으로 변환해주낟. 그리고 그 배열안에 다시 객체가 들어가므로 해당 객체들을 위한 Movie 클래스를 만들어 준다.

```java
public class Movie {

    String rnum;
    String rank;
    String rankInten;
    String rankOldAndNew;
    String movieCd;
    String movieNm;
    String salesAmt;
    String salesShare;
    String salesInten;
    String salesChange;
    String salesAcc;
    String audiCnt;
    String audiInten;
    String audiChange;
    String audiAcc;
    String scrnCnt;
    String showCnt;
}
```

* 그 다음 응답을 확인했던 ```onResponse()``` 메서드에 ```processResponse()``` 메서드를 정의하고 추가한다. 이 때 ```processResponse()``` 메서드의 파라미터로는 응답문자열을 전달한다.

* 새로 정의한 ```processResponse()``` 메서드에서는 Gson 객체를 이용해 응답 문자열을 MovieList 객체로 변환한다.
바
* Gson 객체에는 ```fromJson()``` 메서드가 있어서 문자열을 자바 객체로 바꾼다. 첫 번째 파라미터는 문자열이고 두 번째 파라미터는 어떤 클래스인지를 지정하기 위한 클래스 객체이다.

```java
public void processResponse(String response){
        Gson gson=new Gson();
        MovieList movieList=gson.fromJson(response,MovieList.class);//JSON 문자열을 MovieList 객체로 변환하기
        println("영화 정보의 수: "+movieList.boxOfficeResult.dailyBoxOfficeList.size());
    }
```

* 아래로 쭉 내리면 조회된 영화 정보 개수가 표시된다.
