package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Map;

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

            // http request 정보를 log로 찍기
            // getHttpHeader(br);


            /**
             * 3.4.3.2 요구사항 2 - GET 방식으로 회원가입 하기
             * */
            // User user = createUserGet(url);

            /**
             * 3.4.3.3 요구사항 3 - POST 방식으로 회원가입하기
             * */
            if (url.contains("/user/create")){
                User user = createUserPost(br);
                /**
                 * 3.4.3.4 요구사항 4 - 302 status code 적용
                 * */
                response302Header(new DataOutputStream(out));
            }


            // 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다
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


    /**
     * 요구사항 3 - POST 방식으로 회원가입 하기
     * */
    public User createUserPost(BufferedReader br) throws IOException {
        String params = getHttpContents(br);
        User user = makeUser(params);
        return user;
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
