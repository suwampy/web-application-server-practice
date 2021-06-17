# HTTP 웹 서버 구현을 통해 HTTP 이해하기

3장의 실습 내용을 책 토대로 구현~



1. index.html 응답하기
2. GET 방식으로 회원가입하기
3. POST 방식으로 회원가입하기
4. 302 status code 적용하기
5. 로그인하기
6. CSS 적용하기

## 1. index.html 응답하기

### 클라이언트에서 서버로 전송하는 데이터가 어떻게 구성되어있는지?

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbCIzBw%2Fbtq7yz9rCFU%2FARkcE8rb3z1LppmQ8PAks0%2Fimg.png)

- **New Client Connect!** : 클라이언트로부터 2개의 요청이 발생 됨. 각 요청마다 클라이언트의 포트는 서로 다른 포트를 연결함. 서버는 각 요청에 대해 순차적으로 실행하는 것이 아니라 **동시에 각 요청에 대응하는 스레드를 생성해 동시에 실행 (Thread-0, Thread-1)**
- 각 요청에 대한 첫 번째 라인은  `GET /index.html HTTP/1.1` 와 같은 형태로 구성되어있음
- 첫 번째 라인을 제외한 나머지 요청 데이터는 **"<필드이름> : <필드값>"** 형태로 구성되 어있음
- 각 요청의 마지막은 빈 문자열("") 로 구성되어 있음

> ### HTTP 응답
>
> 1. 시작줄(start-line) : 실행되어야 할 요청, 또는 요청 수행에 대한 성공 또는 실패가 기록
>    `POST / HTTP 1.1`
> 2. HTTP 헤더 : 요청에 대한 설명, 메시지 본문에 대한 설명. **<필드 이름> : <필드 값>** 쌍으로 구성되어 있음
>    `Host: localhost:8080`
>    `User-Agent: Mozilla/5.9 ...`
>    `Accpet : text/html ...`
> 3. 빈줄
> 4. 요청과 관련된 내용(HTML 폼 콘텐츠 등), 응답과 관련된 문서가 들어감
>
> **HTTP 메시지의 시작 줄과 HTTP 헤더를 묶어서 head라고 부르며, HTTP 메시지의 payload는 body라고 부름 **
>
> ![](https://mdn.mozillademos.org/files/13823/HTTP_Response_Headers2.png)
>
> https://developer.mozilla.org/ko/docs/Web/HTTP/Messages



## 2. GET 방식으로 회원가입 하기

GET 방식으로 데이터를 입력하면 해당 데이터가 브라우저 URL 입력창에 표시된다

GET은 서버에 존재하는 데이터를 가져오는것



## 3. POST 방식으로 회원가입 하기

method가 GET에서 POST로 변경되면 요청 URI에 포함되어 있던 쿼리 스트링은 **HTTP 요청의 본문(body)를 통해 전달된다**. POST 방식으로 데이터를 전달하면서 헤더에 본문 데이터에 대한 길이가 **Content-Length**라는 필드 이름으로 전달된다.

POST는 서버에 요청을 보내 데이터 추가, 수정, 삭제와 같은 작업을 실행한다. 

데이터의 상태를 변경하는 작업을 담당한다.



## 4. 302 status code 적용

/user/create 요청을 받아 회원가입을 완료한 후 응답을 보낼 떄 클라이언트에게 /index.html로 이동하도록 할 수 있다. 이 때 사용하는 상태 코드가 **302 상태 코드** 이다.

/index.html로 이동하도록 응답을 보낼 때 사용하는 응답 헤더는 Location으로 응답을 보내면됨

```
HTTP/1.1 302 Found
LOcation: index.html
```

=> 클라이언트는 첫 라인의 상태코드를 확인한 후 => **302**라면 Location 값을 읽어 **서버에 재요청**을 보낸다



302 상태 코드를 활용해 페이지를 이동할 경우 요청과 응답이 한 번이 아니라 두 번 발생한다.

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FMlTyN%2Fbtq7ybAV8f5%2Fp9y4riCBM1h7VgkH755OD0%2Fimg.png)

>  **리다이렉트 방식**으로 페이지를 이동한다 => 내부적으로 **302 상태 코드**를 활용해 이동



- 대표적으로 사용되는 상태 코드
  - 2XX : 성공
  - 3XX : 리다이렉션
  - 4XX : 클라이언트 오류
  - 5XX : 서버 오류





## 5. 로그인하기

HTTP는 요청을 보내고 응답을 받으면 클라이언트와 서버 간의 연결을 끊는다.

이와 같이 클라이언트와 서버 간의 연결을 끊기 때문에 각 요청 사이에 상태를 공유할 수 없다 => **무상태 프로토콜**

HTTP가 무상태 프로토콜이기 때문에 서버는 클라이언트가 누구인지 식별할 수 있는 방법이 없다는 문제가 발생

=> 이를 위한 해결책 : **Cookie**

> ### HTTP가 쿠키를 지원하는 방법
>
> ➡ 서버에서 로그인 요청을 받음 
>
> ➡ 로그인 성공/실패 여부에 따라 **응답 헤더**에 **Set-Cookie**로 결과 값을 저장
>
> ➡ 클라이언트는 응답 헤더에 Set-Cookie가 존재할 경우 값을 읽어 서버에 보내는 **요청 헤더**의 **Cookie 헤더값으로 다시 전달**
>
> *HTTP는 헤더를 통해 공유할 데이터를 매번 다시 전송하는 방식으로 데이터를 공유*
>
> 



## 6. 사용자 목록 출력



## 7. css 지원

브라우저는 응답을 받은 후 Content-Type 헤더 값을 통해 응답 본문에 포함되어 있는 컨텐츠가 어떤 컨텐츠인지를 판단

이와 같ㅇ ㅣ데이터에 대한 정보를 포함하고 있는 헤더 정보들을 **메타데이터** 라고 부름

