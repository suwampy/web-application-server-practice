package webserver;

import java.io.*;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import static util.HttpRequestUtils.*;
import static util.IOUtils.readData;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    // 클라이언트와 서버가 연결되어있는 커넥션
   private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        //소켓이 열렸을 때
        // InputStream : 서버->클라이언트
        //               서버가 클라이언트에게 전송하는 데이터를 읽어옴
        // OutputStream : 클라이언트 ->서버
        //                클라이언트가 서버에게 데이터를 전송함
        // try(스트림;스트림;) {} : 자원을 다 사용했을 때 자동으로 close를 해줌
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = br.readLine();

            if (line ==null){
                return;
            }

            String url = HttpRequestUtils.getUrl(line);
            String method = HttpRequestUtils.getMethod(line);
            Map<String, String> headers = new HashMap<String, String>();
            while(!"".equals(line)) {
                log.debug("header : {}", line);
                /**
                 * 첫 번째 라인을 제외한 나머지 요청 데이터는 "<필드 이름> : <필드 값>" 형태로 구성되어 있다.
                 * 각 요청의 마지막은 빈 문자열("")로 구성되어 있다.
                 * */
                line = br.readLine();
                String[] headerTokens = line.split(": ");
                if (headerTokens.length == 2){
                    headers.put(headerTokens[0], headerTokens[1]);
                }
            }

            /**
             * 회원가입
            * */
            log.debug("method : {}" , method);
            log.debug("url : {}", url);
            if (url.contains("/create")) {
                Map <String, String> params = null;
                if (method.equals("GET")){
                    /**
                     * get 방식으로 회원가입
                     * */
                    int index = url.indexOf("?");
                    String queryString = url.substring(index +1);
                    params = HttpRequestUtils.parseQueryString(queryString);

                }else if(method.equals("POST")){
                    /**
                     * post 방식으로 회원가입
                     *
                     * POST /create HTTP/1.1 <= 요청 라인
                     * Host:localhost:8080   <= 요청 헤더
                     * Content-Length :59
                     * Content-Type:application/x-wwww-form-urlencoded
                     * Accept: ＊/＊
                     * (헤더와 본문 사이의 빈 공백 라인)
                     * userId=javajigi&password=password <= 요청 본문(HTTP Body)
                     *
                     * */
                    String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                    log.debug("Request Body : {}", requestBody);
                    params = HttpRequestUtils.parseQueryString(requestBody);
                }

                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User : {}",user);

                /* *
                 * 요구사항 4 - 302 코드를 활용한 redirect
                 * url = "/index.html"
                 * get/post 제출했을 때 데이터가 그대로 남아있다....
                 * 서블릿의 redirect 방식처럼 회원가입을 완료한 후 'index.html'로 이동해야 한다
                 * => HTTP 응답 헤더의 status codeㄱ를 200이 아니라 302 code를 이용
                 *
                 * 302 상태 코드를 활용해 페이지를 이동할 경우
                 * 요청과 응답이 한 번이 아니라 두 번 발생한다
                 * 302 상태 코드를 활용한 페이지 이동 방식은
                 * 많은 라이브러리와 프레임워크에서 리다이렉트 이동 방식으로 알려져 있다.
                 * 리다이렉트 이동 방식 => 내부적으로 302 상태 코드를 활용해 이동
                 *
                 * HTTP 는 서버에서 클라이언트로 응답을 보낼 떄
                 * 상태 코드를 활용해 요청에 대한 처리 상태를
                 * 클라이언트가 인식할 수 있도록 한다.
                 *
                 * 2XX : 성공
                 * 3XX : 리다이렉션. 클라이언트는 요청을 마치기 위해 추가 동작 필요
                 * 4XX : 요청 오류. 클라이언트에 오류가 잇음
                 * 5XX : 서버 오류. 서버가 유효한 요청을 명백하게 수행하지 못했음
                 *
                 * */

                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp"+url).toPath());
                response302Header(dos);
                responseBody(dos, body);

            } else if(url.contains("/login")){
                /**
                 * 요구사항 5 : 로그인
                 * */
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                log.debug("Request Body : {}", requestBody);
                Map <String, String> params = HttpRequestUtils.parseQueryString(requestBody);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));

            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp"+url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 Document Follows \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

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
