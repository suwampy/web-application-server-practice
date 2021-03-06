package webserver;

import java.io.*;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;


import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpRequestUtils.*;
import static util.IOUtils.readData;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // InputStream : 클라이언트(웹 브라우저)에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
        // OutputStream : 서버에서 클라이언트에 응답을 보낼 때 전달되는 데이터를 담당하는 스트림
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            /**
             * 요구사항 1 - index.html 응답하기
             * http://localhost:8080/index.html로 접속했을 때
             * webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
             * */

            // InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // BufferedReader.readLine() 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
            // HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
            String line = br.readLine();
            String url = getUrl(line);

            /**
             * init ... 초기 생성값들인데 따로 뺄 수 있는방법업음??
             * */
            boolean setCookie = false; // cookie 셋팅값 : 로그인 실패, 성공 여부
            String defaultUrl = "/index.html"; // 리다이렉션할 기본 url
            boolean logined = false; // 로그인 여부.. .처음에 http 헤더의 cookie 값을 읽어와서 판별
            int contentLength = 0; //콘텐츠길이
            boolean cssFlag = false; //css

            // line이 null 값인 경우에 대한 예외 처리도 해야한다.
            // 그렇지 않을 경우 무한 루프에 빠진당
            if (line == null) {
                return;
            }

            while (!"".equals(line)) {
                line = br.readLine();

                // HTTP 요청 정보 Header 부분을 출력한다.
                log.debug("HTTP request Header info : {}",line);

                if (line.contains("Cookie")) {
                    logined = isLogin(line);
                }

                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);

            if (url.equals("/user/create")) {

                // 요구사항 2 - GET 방식으로 회원가입 하기
                // User user = createUserGet(url);

                // 요구사항 3 - POST 방식으로 회원가입하기
                User postUser = createUserPost(br, contentLength);

                /**
                 * 요구사항 5 - 로그인하기위해 회원가입할 때 생성한 User 깩체를
                 * DataBase.addUser() 메소드를 활용해 저장
                 * */
                DataBase.addUser(postUser);

                // 요구사항 4 - 302 status code 적용
                response302Header(dos, defaultUrl);

            }else if (url.equals("/user/login")) {
                // 요구사항 5 - 로그인하기
                // 아이디와 비밀번호가 같은지를 확인해 로그인 성공여부 체크
                User getUser = getUser(br, contentLength);

                if (getUser != null) {
                    setCookie = true;
                    response302HeaderLogin(dos, setCookie, defaultUrl);
                } else {
                    defaultUrl = "/user/login_failed.html";
                    response302HeaderLogin(dos, setCookie, defaultUrl);
                }

            }else if (url.equals("/user/list")) {
                /**
                 * 요구사항 6 - 사용자 목록 출력
                 * 접근하고 있는 사용자가 '로그인' 상태일 경우 (Cookie 값이 logined = true)
                 * /user/list로 접근했을 때 사용자 목록을 출력
                 * 만약 로그인 하징 ㅏㄶ은 상태라면 로그인 페이지로 이동
                 * */
                if (logined) {
                    log.debug("login");
                    // 사용자 목록을 출력하는 HTML 동적으로 생성한 후 응답으로 보냄
                    StringBuilder sb = makeUserList();
                    byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
                    response200Header(dos, body.length, cssFlag);
                    responseBody(dos, body);

                } else {
                    log.debug("not login");
                    defaultUrl = "/user/login.html";
                    response302HeaderLogin(dos, setCookie, defaultUrl);
                }

            }else if (url.contains(".css")) {
                log.debug("css setting");
                /**
                 * 요구사항 7 - css
                 * 응답 헤더의 Content-Type을 text/html로 보내면
                 * 브라우저는 HTML 파일로 인식하기 때문에 CSS가 정상적으로 동작하지 않는다
                 *
                 * CSS인 경우 응답헤더의 Content-Type을 text/css로 전송한다다
                 * * */
                cssFlag = true;
                response200(out, url, cssFlag);
            } else {
                response200(out, url, cssFlag);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 요구사항 2 - GET 방식으로 회원가입하기
     */
    public User createUserGet(String url) throws UnsupportedEncodingException {
        // todos : 리팩토링 필요할듯...?
        int index = url.indexOf("?");
        String requestPath = url.substring(0, index);
        String params = url.substring(index + 1);

        User user = makeUser(params);
        return user;
    }


    /**
     * 요구사항 3 - POST 방식으로 회원가입 하기
     */
    public User createUserPost(BufferedReader br, int contentLength) throws IOException {
        // POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
        // HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.
        // HTTP 본문에 전달되는 데이터는 GET 방식으로 데이터를 전달할 때의 이름= 값과 같다.

        // BufferedREader에서 본문 데이터는 util.IOUtils 클래스의 readData() 메소드를 활용한다.
        // 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.
        String params = readData(br, contentLength);
        User user = makeUser(params);
        return user;
    }


    // =============================== response ===============================
    // 200 응답
    private void response200(OutputStream out, String url, Boolean cssFlag) throws IOException {
        // 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length, cssFlag);
        responseBody(dos, body);

    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, Boolean cssFlag) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if (cssFlag) {
                dos.writeBytes("Content-Type: text/css\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 요구사항 4 - 302 status code 적용
     * <p>
     * 하이퍼텍스트 전송 프로토콜 (HTTP)의 302 Found 리다이렉트 상태 응답 코드는
     * 클라이언트가 요청한 리소스가 Location (en-US) 헤더에 주어진 URL에 일시적으로 이동되었음을 가리킨다.
     * 브라우저는 사용자를 이 URL의 페이지로 리다이렉트시키지만
     * 검색 엔진은 그 리소스가 일시적으로 이동되었다고 해서 그에 대한 링크를 갱신하지는 않는다
     * ('SEO 관점' 에서 말하자면, 링크 주스(Link Juice)가 새로운 URL로 보내지지는 않는다).
     */
    private void response302Header(DataOutputStream dos, String defaultUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + defaultUrl + " \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 요구사항 5 - 로그인하기
     * 로그인 성공시 HTTP 응답 헤더(response header)에
     * Set-Cookie를 추가해 로그인 성공 여부를 전달한다.
     *
     * 200에다가하랬는데 어케하지....ㅋㅋ
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


    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
