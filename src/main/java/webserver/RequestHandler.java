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

import static util.HttpRequestUtils.getUrl;
import static util.HttpRequestUtils.parseQueryString;

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
            httpRequestInfo(line, br);


            /**
             * 96p 요구사항 2 - GET 방식으로 회원가입 하기
             * */
            User user = createUserGet(url);

            // 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * httpRequestsInfo
     * http 요청 정보를 추출하는 메소드
     * */
   public void httpRequestInfo(String line, BufferedReader br) throws IOException {
        // line이 null 값인 경우에 대한 예외 처리도 해야한다.
        // 그렇지 않을 경우 무한 루프에 빠진당
        if (line == null) {
            return;
        }

        // HTTP 요청 정보 전체를 출력한다.
        while(!"".equals(line)) {
            log.info("HTTP request info : {}",line);
            line = br.readLine();
        };

    }

    /**
     * 요구사항 2 - GET 방식으로 회원가입하기
     * */
    public User createUserGet(String url) throws UnsupportedEncodingException {
        // todos : 리팩토링 필요할듯...?
        int index = url.indexOf("?");
        String requestPath = url.substring(0,index);
        String params = url.substring(index+1);
        Map<String, String> temp =  parseQueryString(params);

        String userId = temp.get("userId");
        String password = temp.get("password");
        String name = URLDecoder.decode(temp.get("name"),"UTF-8");
        String email = null;
        if (temp.get("email") != null){
            email = temp.get("email");
        }

        User user = new User(userId,password,name,email);

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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
