## 1. 요구사항

- 질문 / 답변 게시판에 처음 접근하면 질문 목록을 볼 수 있다
- 질문 목록 화면에서 회원가입, 로그인, 로그아웃, 개인정보 수정이 가능하며 질문하기 화면으로 이동할 수 있다
- 회원가입 화면
- 로그인 화면
- 질문하기 화면 : 각 질문 제목을 클릭하면 각 질문의 상세보기 화면으로 이동
- 상세보기 화면 : 답변을 추가할 수 있고, 질문과 답변의 수정/삭제가 가능



## 2. 로컬 개발 환경 구축



## 3. 원격 서버에 배포

- 리눅스 기본 명령어 를 알아야한대...

  | 리눅스 명령어                     | 설명                                                         |
  | --------------------------------- | ------------------------------------------------------------ |
  | **pwd** (print working directory) | 현재 작업 중인 디렉토리 정보 출력                            |
  | **cd** (change directory)         | 경로를 이동할 때 사용한다.<br />- 주요 옵션<br />   * cd ~ : 어느 곳에든지 홈 디렉토리로 바로 이동<br />   * cd .. : 상위 디렉토리로 이동<br />   * cd /dir : 절대경로 dir로 이동할 경우 사용<br />   * cd - : 이동하기 바로 전의 디렉토리로 이동 |
  | **ls** (list segments)            | 현재 위치의 파일 목록을 조회한다.<br />- 주요 옵션<br />   * ls -l : 파일들의 상세정보를 나타냄<br />   * ls -a : 숨어있는 파일들도 표시함<br />   * ls -al : 다보기 |
  | **chmod**                         | 기존 파일 또는 디렉토리에 대한 접근 권한을 변경할 때 사용    |
  | **cp** (copy)                     | 파일을 복사하는 명령어                                       |
  | **rm** (remove)                   | 파일이나 디렉토리를 삭제할 때 사용하는 명령어                |
  | **mv** (move)                     | 파일을 이동하는 명령어                                       |
  | **ln** (Link)                     | 특정 파일에 대한 *심벌릭 링크*를 만듦<br />* *심벌릭 링크?*  단순히 원본파일을 가리키도록 링크만 시켜둔 것으로 MS의 윈도우시스템에서 흔히 사용하는 '바로가기' 같은 것 |
  | **ps** (process)                  | 현재 시스템에서 실행 중인 프로세스의 목록을 보여줌           |
  | **kil**l                          | 자원 제한으로 인해 멈춘 프로세스를 중지시킴                  |
  | **touch**                         | 파일의 용량이 0인 파일을 생성, 날짜 변경하는 명령어          |
  | **mkdir** (make directory)        | 새로운 디렉토리를 만들 때 사용하는 명령어                    |
  | **cat** (catenate)                | 파일 이름을 인자로 받아서 그 내용을 출력할 때 사용           |

- aws까지다루게된경위에대하여....ㅋㅋㅋㅋㅋ (나중에공부하자..ㅠㅠ) : 우분투로 EC2 인스턴스 생성

  - 클라우드 컴퓨팅 : 남의 컴퓨터를 빌려서 원격 제어를 통해서 사용하는 것..

  - 호스팅 : 인터넷에 연결된 컴퓨터 한 대 한 대를 빌려서 제공

  - AWS-EC2 (Elastic Compute Cloud) : 컴퓨터를 빌려주는 서비스~

  - 인스턴스 : 컴퓨터 한 대

  - 스토리지 : 저장 장치의 크기

  - 키 페어 선택 / 키 페어 생성 : 비밀번호 생성

  - 인스턴스 검사 오른쪽클릭 - 연결

  - 웹서버 설치하기 ->ec2 ip 주소를 통해서 웹서버에 접근할 수 있당

    - sudo apt update : 우분투 최신 업데이트
    - sudo apt install apache2 : 아파치 웹서버 설치

  - 퍼블릭 IPv4 주소 또는 퍼블릭 IPv4 DNS 로 접근할수잇당

  - 접근안됨 -> 방화벽 때문!

  - 보안

    - 인바운드 : 외부에서 ec2 인스턴스로 접근 -최소한으로 열어야함
    - 아웃바운드:ec2 인스턴스에서 바깥쪽으로 접근 -  다 열려있어야함

    - 보안 그룹 - 인바운드 규칙 편집 - 규칙 추가 - HTTP - 누구나 접속할 수 있게 0.0.0.0/0 로

  - 아파치 홈페이지로 이동~

  

### 3.1 요구사항

- HTTP 웹 서버를 물리적으로 떨어져 있는 원격 서버에 배포해 정상적으로 동작하는지 테스트
- HTTP 웹 서버 배포 작업은 root 계정이 아닌 배포를 담당할 새로운 계정으로 만들어 진행



### 3.2 힌트

 1. 계정 추가 및 sudo 권한 할당

 2. 각 계정별 UTF-8 인코딩 설정해 한글 이슈 해결

    - sudo locale-gen ko_KR.EUC-KR ko_KR.UTF-8

    - sudo dpkg-reconfigure locales

      >**이거따라하면됨ㅋㅋ**
      >
      >아래 명령어를 통해 현재 설정된 언어정보를 확인할 수 있습니다.
      >
      >
      >
      >locale
      >
      >
      >
      >
      >
      >한글팩을 설치해줍니다.
      >
      >
      >
      >apt-get -y install language-pack-ko
      >
      >
      >
      >
      >
      >한글 utf8 언어팩을 설치합니다.
      >
      >
      >
      >locale-gen ko_KR.UTF-8
      >
      >
      >
      >
      >
      >아래 명령어로 언어팩을 선택할 수 있습니다.
      >
      >
      >
      >dpkg-reconfigure locales
      >
      >
      >
      >"ko_KR.UTF-8 UTF-8" 에 해당하는 번호를 입력하고 엔터! (저는 290번이였네요. 혹시 다를수 있으니 확인하세요)
      >
      >
      >
      >"ko_KR.UTF-8" 을 선택합니다. (저는 3번과 4번에 똑같이 나타났습니다. 그냥 3번 눌렀습니다.)
      >
      >
      >
      >
      >
      >아래 명령어로 시스템 정보를 업데이트하면 적용됩니다.
      >
      >
      >
      >update-locale LANG=ko_KR.UTF-8 LC_MESSAGES=POSIX
      >
      >
      >
      >
      >
      >로그아웃 후 다시 로그인한 후(SSH 재접속) 다시 locale 명령어를 입력하면 적용됩니다!

3. JDK, 메이븐 설치

   >**JDK 설치**
   >
   >```
   >$ wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add - 
   >$ sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
   >$ sudo apt-get update; sudo apt-get install -y java-11-amazon-corretto-jdk
   >```
   >
   >
   >
   >**환경변수 설정**
   >
   >javac의 실제 위치를 찾기 위해 readlink 명령어를 사용하자 (readlink는 심볼릭 링크의 원본을 찾기 위한 명령어이다)
   >
   >readlink -f /usr/bin/javac 로 javac 위치 확인
   >
   >```
   >$ vim ~/.bashrc
   >$ export JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto/bin/javac
   >$ export PATH="$PATH:$JAVA_HOME/bin”
   >$ source ~/.bashrc   
   >```
   >
   >
   >
   >echo $JAVA_HOME 로 확인
   >
   >
   >
   >**메이븐 설치 **
   >
   >```
   >$ sudo apt install maven
   >$ mvn -v
   >$ apt list maven
   >```
   >
   >vi /etc/mavenrc 파일을 새로 생성하여
   >
   >
   >
   >JAVA_HOME=여러분들의 jdk가 들어있는 home 디렉토리
   >
   >
   >
   >를 입력하고 저장 후
   >
   >
   >
   >다시 mvn 명령어를 사용하면 된다.
   >
   >
   >
   >주의할점은 /bin 디렉토리는 포함하면 안되는 것 같다 딱 /bin 경로가 있는 root경로까지만 추가후 
   >
   >
   >
   >mvn 명령어를 실행 해보자

   

4. git설치

   >**깃 설치**
   >
   >```
   >$ sudo apt-get install git
   >$ sudo apt install git
   >
   >```

   > **깃 설정**
   >
   > ```
   > $ git config --global user.name [이름]
   > $ git config --global user.mail [메일 주소]
   > $ git clone [url 주소]
   > ```
   >
   > 

   > **메이븐 빌드 및 서버 실행**
   >
   > 1. 클론한 디렉토리 이동, **mvn clean package** 명령 실행
   > 2. 빌드 후 **java -cp target/classes:target/dependency/* webserver.WebServer 8800 &** 실행
   > 3. 로컬가동 **curl http://localhost:8800**  확인

5. 방화벽제거

6. 소스코드 재배포

   ```
   $ ps-ef | grep webserver
   $ kill -9 $PID
   ```

   
## 4. 웹서버 실습

HTTP 웹 서버의 핵심이 되는 코드는 webserver 패키지의 **WebServer**와 **RequestHandler** 클래스이다. 

- `WebServer` 클래스 : **웹 서버를 시작**하고, 사용자의 요청이 있을 경우 **사용자의 요청을 RequestHandler 클래스에 위임**하는 역할을 한다.

```java
public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8800;

    // 서버를 시작하는 메인 메소드
    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        // ServerSocket :  사용자 요청이 발생할 떄까지 대기 상태에 있도록 지원하는 역할을 담당
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // ServerSokect에 사용자 요청이 발생하는 순간
                // 클라이언트와 연결을 담당하는 Socket을
                // RequestHandler에 전달하면서
                // 새로운 스레드를 실행하는 방식으로
                // 멀티 스레드 프로그래밍을 지원
                // (RequestHandler 클래스는 Thread를 상속받음)
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start(); //Thread의 start 메소드 실행
            }
        }
    }
}

```

- `RequestHandler` 클래스 : Thread를 상속하고 있으며, **사용자의 요청에 대한 처리**와 **응답에 대한 처리**를 담당하는 가장 중심이 되는 클래스

  ```java
  public class RequestHandler extends Thread {
      private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  
      private Socket connection;
  
      public RequestHandler(Socket connectionSocket) {
          this.connection = connectionSocket;
      }
  
      public void run() {
          log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());
  
          try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
              // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
              DataOutputStream dos = new DataOutputStream(out);
              byte[] body = "Hello World".getBytes();
              response200Header(dos, body.length);
              responseBody(dos, body);
          } catch (IOException e) {
              log.error(e.getMessage());
          }
      }
  
      private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
          try {
              dos.writeBytes("HTTP/1.1 200 OK \r\n");
              dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
              dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
              dos.writeBytes("\r\n");
          } catch (IOException e) {
              log.error(e.getMessage());
          }
      }
  
      private void responseBody(DataOutputStream dos, byte[] body) {
          try {
              dos.write(body, 0, body.length);
              dos.flush();
          } catch (IOException e) {
              log.error(e.getMessage());
          }
      }
  }
  
  ```

  ![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbyNlXu%2Fbtq6w7UXfUc%2Fsux06qwJwkciw3x6CAxJ30%2Fimg.png)

## 실습 요구사항

### **1. index.html 응답하기**

| 힌트        | 요구사항                                                     |
| ----------- | ------------------------------------------------------------ |
| HTTP Header | GET /index.html HTTP/1.1<br />Connection : keep-alive<br />Accept : */* |
| 1단계       | - InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다.<br />   ** 구글에서 *java inputstream bufferedreader*로 검색 후 문제 해결<br />- BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.<br />- HTTP 요청 정보 전체를 출력한다.<br />   ** 헤더 마지막은 while (!"".equals(line)) {} 로 확인 가능<br />   ** line이 null값인 경우에 대한 예외 처리도 해야 함. 그렇지 않을 경우 무한 루프에 빠짐 (if (line == null)) {return;} |
| 2단계       | - HTTP 요청 정보의 첫 번째 라인에서 요청 URL(위 예의 경우 /index.html)을 추출<br />  ** String[] tokens = line.split(" "); 를 활용해 문자열을 분리할 수 있음<br />- 구현은 별도의 유틸 클래스를 만들고 단위 테스트를 만들어 진행하면 편함 |
| 3단계       | - 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 됨<br />- 구글에서 "java files readallbytes"로 검색해 파일 데이터를 byte[] 로 읽음<br />byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath()); |

**webserver/RequestHandler **

```java
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // InputStream : 클라이언트(웹 브라우저)에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
        // OutputStream : 서버에서 클라이언트에 응답을 보낼 때 전달되는 데이터를 담당하는 스트림
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            /**
             * 2021 - 06 - 04
             * 96p 요구사항 1 - index.html 응답하기
             * http://localhost:8080/index.html로 접속했을 때
             * webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
             * */

            // InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
            String line = br.readLine();

            // HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
            String url = getUrl(line);

            // line이 null 값인 경우에 대한 예외 처리도 해야한다.
            // 그렇지 않을 경우 무한 루프에 빠진당
            if (line == null) {
                return;
            }

            // HTTP 요청 정보 전체를 출력한다.
            while(!"".equals(line)) {
                log.info(line);
                line = br.readLine();
            };

            // 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getUrl(String line) {
        String url = line.split(" ")[1];
        return url;
    }
```



>### 요약정리
>
>1. **포트번호를 받아옴**
>
>2. **서버 소켓 생성**
>
>  ```java
>  try (ServerSocket listenSocket = new ServerSocket(port)) {}
>  ```
>
>3. **클라이언트가 대기될 때 까지 대기**
>
>  ```java
>  Socket connection;
>  while ((connection = listenSocket.accept()) != null) {}
>  ```
>
>4. **ServerSokect에 사용자 요청이 발생하는 순간 클라이언트와 연결을 담당하는 Socket을 RequestHandler에 전달하면서 새로운 스레드를 실행하는 방식으로 멀티 스레드 프로그래밍을 지원**  
>
>  ```java
>  while ((connection = listenSocket.accept()) != null) {
>      RequestHandler requestHandler = new RequestHandler(connection);
>      requestHandler.start();
>  }
>  ```
>
>5. **`RequestHandler` 는 Thread를 상속받은 클래스. ⬆에서 start() 메소드가 실행됐으니 RequestHandler 클래스의 `run() 메소드`가 실행된다.** [스레드 정리내용](https://github.com/suwampy/TIL/blob/main/java/Thread.md)
>
>6. **RequestHandler 클래스에서는 메인 메소드에서 생성된 socket 객체를 생성자에 주입받는다**
>
>  ```java
>  public RequestHandler(Socket connectionSocket) {
>      this.connection = connectionSocket;
>  }
>  ```
>
>7. **run 메소드에서는 `InputStream`과 `OutputStream`이 생성된다.**
>
>  - InputStream : 클라이언트(웹 브라우저)에서 서버로 `요청`을 보낼 때 전달되는 데이터를 담당하는 스트림
>  - OutputStream :서버에서 클라이언트에 `응답`을 보낼 때 전달되는 데이터를 담당하는 스트림
>
>  ```JAVA
>  try (InputStream in = connection.getInputStream();
>       OutputStream out = connection.getOutputStream()) {}
>  ```
>
>8. **첫 번째 요구사항 - index.html 응답하기**
>
>  8.1   InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다.
>
>  ```java
>  BufferedReader br = new BufferedReader(new InputStreamReader(in));
>  ```
>
>  8.2 BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
>
>  ```java
>  String line = br.readLine();
>  ```
>
>  8.3 HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
>
>  ```java
>  String url = getUrl(line);
>
>  private String get Url(String line) {
>      String line = line.split("")[1];
>      return url;
>  }
>  ```
>
>  8.4 line이 null 값인 경우에 대한 예외 처리도 해야한다.그렇지 않을경우 무한루프에빠짐
>
>  ```java
>  if (line == null){
>      return;
>  }
>  ```
>
>  8.5 HTTP 요청 정보 전체를 출력한다.
>
>  ```java
>  while(!"". equals(line)){
>      log.info("HTTP request info : {}:", line);
>      line = br.readLine();
>  }
>  ```
>
>  ***log에  http request 정보가 찍힌당....***
>
>  ![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbc7dJH%2Fbtq6zQSlBZF%2F7VN8D6nvrfRq7omA4xeCB1%2Fimg.png)
>
>  8.6 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달
>
>  ```java
>  DataOutputStream dos = new DataOutputStream(out);
>  byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
>  response200Header(dos, body.length);
>  responseBody(dos, body);
>  ```
>
>  [실습 코드](https://github.com/suwampy/web-application-server/blob/master/src/main/java/webserver/WebServer.java)

### 2. GET 방식으로 회원가입하기

'회원가입' 메뉴를 클릭하면 `http://localhost:8080/user/form.html` 로 이동하면서 회원가입 할 수 있다.

| 힌트         | 요구사항                                                     |
| ------------ | ------------------------------------------------------------ |
| HTTP Headerd | GET /user/create?userId=javajigi&password=password&name=JaeSung<br />HTTP/1.1 |
| 1단계        | - HTTP 요청의 첫 번째 라인에서 요청 URL을 추출한다.<br />- 요청 URL에서 접근 경로와 이름 = 값으로 전달되는 데이터를 추출해 User 클래스에 담는다.<br />- 구현은 가능하면 JUnit을 활용해 단위 테스트를 진행하면서 하면 좀 더 효과적으로 개발 가능하다.<br />- 이름 = 값 파싱은 util.HttpRequestUtills 클래스의 parseQueryString() 메소드를 활용한다.<br />- 요청 URL과 이름 값을 분리해야 한다.<br />String url = "/data=234";<br />int index = url.indexOf("?");<br />String requestPath = url.substring(0, index);<br />String params = url.substring(index+1); |

**webserver/RequestHander**

```java
public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
              connection.getPort());

    // InputStream : 클라이언트(웹 브라우저)에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
    // OutputStream : 서버에서 클라이언트에 응답을 보낼 때 전달되는 데이터를 담당하는 스트림
    try (InputStream in = connection.getInputStream();
         OutputStream out = connection.getOutputStream()) {
        // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        
        // InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
        String line = br.readLine();

        // HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
        String url = getUrl(line);

        // http request 정보를 log로 찍기
        // getHttpHeader(br);
        
        /**
         * 3.4.3.2 요구사항 2 - GET 방식으로 회원가입 하기
         * */
        if (url.contains("/user/create")){
        	User user = createUserGet(url);
        }

        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos,body.length);
        responseBody(dos, body);


    } catch (IOException e) {
        log.error(e.getMessage());
    }
}

/**
* 요구사항 2 - GET 방식으로 회원가입하기
* */
public User createUserGet(String url) throws UnsupportedEncodingException {
    // todos : 리팩토링 필요할듯...?
    int index = url.indexOf("?");
    String requestPath = url.substring(0,index);
    String params = url.substring(index+1);

    User user = makeUser(params);
    return user;
}

```

**util/HttpRequestUtils**

```java
/**
 * makeUser
 * param 값을 받아와서 user 객체를 생성하는 메소드
 * */
public static User makeUser(String params) throws UnsupportedEncodingException {
    Map<String, String> temp =  parseQueryString(params);

    String userId = temp.get("userId");
    String password = temp.get("password");
    String name = URLDecoder.decode(temp.get("name"),"UTF-8");
    String email = URLDecoder.decode(temp.get("email"),"UTF-8");

    User user = new User(userId,password,name,email);

    log.debug("makeUser : {}", user);
    return user;
}

```

>### 요약정리
>
>1. **요청받은 URL이 회원가입 URL이라면?**
>
>  ```java
>  if (url.contains("/user/create")){ }
>  ```
>
>2. **요청 URL에서 접근 경로와 이름 = 값으로 전달되는 데이터를 추출해 User 클래스에 담는다.**
>
>  ```java
>  if (url.contains("/user/create")){
>  	User user = createUserPost(br);
>  }
>
>  /**
>  * 요구사항 3 - GET 방식으로 회원가입 하기
>  * */
>  public User createUserGet(String url) throws UnsupportedEncodingException {
>      int index = url.indexOf("?");
>      String requestPath = url.substring(0,index);
>      String params = url.substring(index+1);
>
>      User user = makeUser(params);
>      return user;
>  }
>
>  ```
>
>3. **이름 = 값 파싱은 util.HttpRequestUtills 클래스의 parseQueryString() 메소드를 활용한다.**
>
>  ***util/HttpRequestUtils***
>
>  ```java
>  /**
>  * makeUser
>  * param 값을 받아와서 user 객체를 생성하는 메소드
>  * */
>  public static User makeUser(String params) throws UnsupportedEncodingException {
>      Map<String, String> temp =  parseQueryString(params);
>
>      String userId = temp.get("userId");
>      String password = temp.get("password");
>      String name = URLDecoder.decode(temp.get("name"),"UTF-8");
>      String email = URLDecoder.decode(temp.get("email"),"UTF-8");
>
>      User user = new User(userId,password,name,email);
>
>      log.debug("makeUser : {}", user);
>      return user;
>  }
>
>  ```
>
>  ![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fsuy02%2Fbtq6DDRYfJK%2F9EwbZkukmtAIxvEmiyq4sk%2Fimg.png)
>
>  => user 객체가 만들어진당~~~



### 3. POST 방식으로 회원가입 하기

| 힌트               | 요구사항                                                     |
| ------------------ | ------------------------------------------------------------ |
| HTTP Header와 Body | POST /user/create HTTP/1.1<br />Host : localhost:8080<br />Connection : keep-alive<br />Content-Length : 59<br />Content-Type : application/x-www-form-urlencoded<br />Accept : */*<br /><br />userId=javajigi&password=password&name=JaeSung |
| 1단계              | - POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.<br />- HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.<br />- HTTP 본문에 전달되는 데잍터는 GET 방식으로 데이터를 전달할 떄의 이름 = 값과 같다.<br />- BufferedReader에서  본문 데이터는 util.IOUtills 클래스의 readData() 메소드를 활용한다. 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.<br />- 회원가입시 입력한 모든 데이터를 추출해 User 객체를 생성한다. |

**webserver/RequestHander**

```java
public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
              connection.getPort());

    // InputStream : 클라이언트(웹 브라우저)에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
    // OutputStream : 서버에서 클라이언트에 응답을 보낼 때 전달되는 데이터를 담당하는 스트림
    try (InputStream in = connection.getInputStream();
         OutputStream out = connection.getOutputStream()) {
        // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        
        // InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
        String line = br.readLine();

        // HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
        String url = getUrl(line);

        // http request 정보를 log로 찍기
        // getHttpHeader(br);
        
        /**
         * 3.4.3.3 요구사항 3 - POST 방식으로 회원가입 하기
         * */
        if (url.contains("/user/create")){
        	User user = createUserPost(br);
        }

        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos,body.length);
        responseBody(dos, body);


    } catch (IOException e) {
        log.error(e.getMessage());
    }
}

/**
* 요구사항 2 - GET 방식으로 회원가입하기
* */
public User createUserGet(String url) throws UnsupportedEncodingException {
    String params = getHttpContents(br);
    User user = makeUser(params);
    return user;
}

```

**util/HttpRequestUtils**

```java
/**
* getHttpContents
* http 요청 정보의 contents 본문을 추출하는 메소드
* */
public static String getHttpContents(BufferedReader br) throws IOException {
    // POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
    // HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.
    // HTTP 본문에 전달되는 데이터는 GET 방식으로 데이터를 전달할 때의 이름= 값과 같다.
    int contentLength = getContentLength(br);

    // BufferedREader에서 본문 데이터는 util.IOUtils 클래스의 readData() 메소드를 활용한다.
    // 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.
    String readData = readData(br, contentLength);

    return readData;
}

/**
* getContentLength
* http 요청 정보의 header 부분에서 Content-Length 를 추출하는 메소드
* */
public static int getContentLength(BufferedReader br) throws IOException {
    int contentLength = 0;
    String line = br.readLine();

    while(!"".equals(line)) {
        String getKey = parseHeader(line).getKey();
        if (getKey.equals("Content-Length")){
            log.debug("Content-Length : {}", parseHeader(line).getValue());
            contentLength = parseInt(parseHeader(line).getValue());
        }

        line = br.readLine();
    }
    return contentLength;
}


/**
* makeUser
* param 값을 받아와서 user 객체를 생성하는 메소드
* */
public static User makeUser(String params) throws UnsupportedEncodingException {
    Map<String, String> temp =  parseQueryString(params);

    String userId = temp.get("userId");
    String password = temp.get("password");
    String name = URLDecoder.decode(temp.get("name"),"UTF-8");
    String email = URLDecoder.decode(temp.get("email"),"UTF-8");

    User user = new User(userId,password,name,email);

    log.debug("makeUser : {}", user);
    return user;
}
```

>### 요약정리
>
>1. **요청받은 URL이 회원가입 URL이라면?**
>
>  ```java
>  if (url.contains("/user/create")){ 
>  	User user = createUserPost(br);
>  }
>  ```
>
>2.   **POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.**  - HTTP 본문을 읽어오기 위해 `createUserPost` 메소드에 `BufferedReader` 객체인 br을 전달하자
>
>  ```java
>  public User createUserPost(BufferedReader br) throws IOException {
>      String params = getHttpContents(br);
>      User user = makeUser(params);
>      return user;
>  }
>  ```
>
>3. 간결한 코드를 위해 Http Contents를 받아오는 메소드 `getHttpContents`는 ***util/HttpRequestUtils*** 클래스에다가 static 메소드로 빼놧당...
>
>  ***util/HttpRequestUtils*** 
>
>  ```java
>  /**
>  * getHttpContents
>  * http 요청 정보의 contents 본문을 추출하는 메소드
>  * */
>  public static String getHttpContents(BufferedReader br) throws IOException {
>      // POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
>      // HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.
>      // HTTP 본문에 전달되는 데이터는 GET 방식으로 데이터를 전달할 때의 이름= 값과 같다.
>      int contentLength = getContentLength(br);
>
>      // BufferedREader에서 본문 데이터는 util.IOUtils 클래스의 readData() 메소드를 활용한다.
>      // 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.
>      String readData = readData(br, contentLength);
>
>      return readData;
>  }
>
>  ```
>
>  > ## ***HTTP 메시지?***
>  >
>  > HTTP 메시지는 서버와 클라이언트 간에 데이터가 교환되는 방식이다.
>  > 메시지 타입은 두 가지가 있다.
>  > `요청(*request)`은* 클라이언트가 서버로 전달해서 서버의 액션이 일어나게끔 하는 메시지고, 
>  > `응답(*response)`은 요청*에 대한 서버의 답변
>  >
>  > ## HTTP 응답
>  >
>  > 1. 시작줄(start-line) : 실행되어야 할 요청, 또는 요청 수행에 대한 성공 또는 실패가 기록
>  >    `POST / HTTP 1.1`
>  > 2. HTTP 헤더 : 요청에 대한 설명, 메시지 본문에 대한 설명
>  >    `Host: localhost:8080`
>  >    `User-Agent: Mozilla/5.9 ...`
>  >    `Accpet : text/html ...`
>  > 3. 빈줄
>  > 4. 요청과 관련된 내용(HTML 폼 콘텐츠 등), 응답과 관련된 문서가 들어감
>  >
>  > **HTTP 메시지의 시작 줄과 HTTP 헤더를 묶어서 head라고 부르며, HTTP 메시지의 payload는 body라고 부름 **
>  >
>  > ![](https://mdn.mozillademos.org/files/13823/HTTP_Response_Headers2.png)
>  >
>  > https://developer.mozilla.org/ko/docs/Web/HTTP/Messages
>
>4. 본문의 길이를 알기 위해` getContentLength` 메소드를 통해 http 요청정보의 header 부분에서 Content-Length를 추출한다
>
>  ***util/HttpRequestUtils*** 
>
>  ```java
>  /**
>  * getContentLength
>  * http 요청 정보의 header 부분에서 Content-Length 를 추출하는 메소드
>  * */
>  public static int getContentLength(BufferedReader br) throws IOException {
>      int contentLength = 0;
>      String line = br.readLine();
>
>      while(!"".equals(line)) {
>          String getKey = parseHeader(line).getKey();
>          if (getKey.equals("Content-Length")){
>              log.debug("Content-Length : {}", parseHeader(line).getValue());
>              contentLength = parseInt(parseHeader(line).getValue());
>          }
>
>          line = br.readLine();
>      }
>      return contentLength;
>  }
>  ```
>
>5. BufferedReader에서  본문 데이터는 util.IOUtills 클래스의 readData() 메소드를 활용한다. 
>
>  ```java
>  String readData = readData(br, contentLength);
>  ```
>
>6. 읽어온 data로 User 객체를 만든다
>
>  ```java
>  public User createUserPost(BufferedReader br) throws IOException {
>      String params = getHttpContents(br);
>      User user = makeUser(params);
>      return user;
>  }
>  ```
>
>![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FccIJPs%2Fbtq6zWFGlQE%2FWjABvZQTAYgKgcn2mELom0%2Fimg.png)
>
>=> user 객체가 만들어진당~~~



### 4. 302 status code 적용

회원가입을 완료했을때 /index.html으로 이동하자

| 힌트  | 요구사항                                                     |
| ----- | ------------------------------------------------------------ |
| 1단계 | - hTTP 응답 헤더의 status code를 200이 아니라 302 code를 사용한다.<br />- http://en.wikipedia.org/wiki/HTTP_302 참고 |

```java
if (url.contains("/user/create")){
    User user = createUserPost(br);
	// 3.4.3.4 요구사항 4 - 302 status code 적용
    response302Header(new DataOutputStream(out));
}
/**
* 요구사항 4 - 302 status code 적용
*
* 하이퍼텍스트 전송 프로토콜 (HTTP)의 302 Found 리다이렉트 상태 응답 코드는
* 클라이언트가 요청한 리소스가 Location (en-US) 헤더에 주어진 URL에 일시적으로 이동되었음을 가리킨다.
* 브라우저는 사용자를 이 URL의 페이지로 리다이렉트시키지만
* 검색 엔진은 그 리소스가 일시적으로 이동되었다고 해서 그에 대한 링크를 갱신하지는 않는다
* ('SEO 관점' 에서 말하자면, 링크 주스(Link Juice)가 새로운 URL로 보내지지는 않는다).
* */
private void response302Header(DataOutputStream dos) {
    try {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: /index.html");
        dos.writeBytes("\r\n");
        dos.flush();
    } catch (IOException e) {
        log.error(e.getMessage());
    }
}

```





### 5. 로그인하기

| 힌트  | 요구사항                                                     |
| ----- | ------------------------------------------------------------ |
| 1단계 | -  로그인 성공시 HTTP 응답 헤더에 Set - Cookie를 추가해 로그인 성공 여부를 전달한다.<br />- 응답 헤더의 예시<br />HTTP/1.1 200 OK<br />Content-Type : text/html<br />Set-COokie : logined=true |
|       | - 위와 같이 응답을 보내면 브라우저는 다음과 같이 HTTP 요청 헤더에 COokie 값으로 전달한다. 이렇게 전달받은 Cookie 값으로 로그인 유무를 판단한다.<br />- 다음 요청에 대한 요청 헤더 예시<br />GET /index.html HTTP/1.1<br />Host : localhost:8080<br />COnnection:keep-alive<br />Accept: */*<br />Cookie : logined=true |
| 2단계 | - 정상적으로 로그인 되었는지 확인하려면 앞 단계에서 회원가입한 데이터를 유지해야한다.<br />** 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메소드를 활용해 저장한다.<br />- 아이디와 비밀번호가 같은지를 확인해 로그인이 성공하면 응답 헤더의 Set-Cookie 값을 logined=true, 로그인이 실패할 경우 Set-Cookie 값을 logined=false로 설정한다.<br />- 응답 헤더에 Set-Cookie 값을 설정한 후 요청 헤더에 Cookie 값이 전달되는지 확인한다. |

**webserver/RequestHander**

```java
boolean setCookie = false; // cookie 셋팅값 : 로그인 실패, 성공 여부
public void run() {
    if (url.equals("/user/login")) {
        User getUser = getUser(br, contentLength);

        if (getUser != null) {
            setCookie = true;
            response302HeaderLogin(dos, setCookie, defaultUrl);
        } else {
            defaultUrl = "/user/login_failed.html";
            response302HeaderLogin(dos, setCookie, defaultUrl);
        }
    }
}
/**
* 요구사항 5 - 로그인하기
* 로그인 성공시 HTTP 응답 헤더(response header)에
* Set-Cookie를 추가해 로그인 성공 여부를 전달한다.
 * */
private void response302HeaderLogin(DataOutputStream dos, boolean setCookie, String defaultUrl) {
    try {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + defaultUrl + " \r\n");

        if (setCookie) {
            log.debug("set cookie true");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
        } else {
            log.debug("set cookie false");
            dos.writeBytes("Set-Cookie: logined=false \r\n");
        }

        dos.writeBytes("\r\n");
        dos.flush();
    } catch (IOException e) {
        log.error(e.getMessage());
    }
}

```

**util/HttpRequestUtils**

```java
/**
* getUser
* id와 pwd를 받아와 저장된 유저 정보를 받아오는 메소드
* */
public static User getUser(BufferedReader br, int contentLength) throws IOException {
    String body = IOUtils.readData(br, contentLength);
    Map<String, String> params = HttpRequestUtils.parseQueryString(body);
    User u = DataBase.findUserById(params.get("userId"));
    return DataBase.findUserById(params.get("userId"));
}

```

