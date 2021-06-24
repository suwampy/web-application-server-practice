package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 응답 데이터를 처리하는 로직을 별도의 클래스로 분리한다
 * HTML,CSS,js 파일을 읽어 반환하는 부분과
 * 302 상태 코드를 처리하는 부분이 가능해야 함
 * 쿠키 추가와 같이 HTTP 헤더에 임의의 값을 추가할 수 있어야 함
 */
public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);


    // outputStream
    private DataOutputStream dos = null;

    // 응답 헤더 정보
    private Map<String, String> headers = new HashMap<String, String>();

    public HttpResponse(OutputStream out) throws Exception {
        dos = new DataOutputStream(out);
    }


    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Forward : 건네주기
     * * Client가 Server에 Resource를 요청합니다.
     * * Server는 Web Container(Tomcat, Jboss등)에 의해 LoginServlet에서 HelloServlet로 forward하게 됩니다
     * * Server는 최종적으로 HelloServlet의 결과를 응답하게 됩니다.
     * * Client입장에서는 한번의 요청으로 결과물을 받아볼 수 있습니다.
     * * 객체를 재사용하거나 공유해야한다면 Forward를 사용하는 것이 좋습니다.
     */
    public void forward(String path) {
        try {
            log.debug("forward path : {}" , path);
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

            // css, js 처리
            if (path.endsWith(".css")) {
                headers.put("Content-Type", "text/css");
            } else if (path.endsWith(".js")) {
                headers.put("Content-Type", "application/javascript");
            } else {
                headers.put("Content-Type", "text/html;charset=utf-8");
            }
            headers.put("Content-Length", body.length + "");
            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", contents.length + "");
        response200Header(contents.length);
        responseBody(contents);
    }

    /**
     * response200Header
     */
    public void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 302 redirect
     * * Client가 Server에 Resource를 요청합니다.
     * * Server는 상태값 3XX와 함께 Redirect 주소[Location]를 같이 보내게 됩니다.
     * * Client는 새로운 주소값으로 다시 Resource를 요청합니다.
     * * Server는 새로운 Resource를 응답합니다.
     * * URL의 변화여부가 필요하다면 Redirect를 사용하는 것이 좋습니다.
     */
    public void sendRedirect(String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: " + path + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void responseBody(byte[] b) {
        try {
            dos.write(b, 0, b.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void processHeaders() {
        try {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

