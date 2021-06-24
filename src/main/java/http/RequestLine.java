package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * request header의 시작 줄 (start-line)은 실행되어야 할 요청, 또는 요청 수행에 대한 성공 또는 실패가 기록된다
 * ex ) GET /index.html HTTP 1.1
 * */
public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger( RequestLine.class);

    private HttpMethod method; // 메소드
    private String path; // URL
    private String queryString;


    public RequestLine(String requestLine) {
        log.debug("request line :{}" , requestLine);
        /**
         * ex ) GET /index.html HTTP 1.1
         * tokens[0] = GET - 메소드
         * tokens[1] = /index.html - path
         * */
        String[] tokens = requestLine.split(" ");
        this.method = HttpMethod.valueOf(tokens[0]);

        String[] url = tokens[1].split("\\?");
        this.path = url[0];

        // 더 있을때 쿼리스트링으로
        if (url.length == 2) {
            this.queryString = url[1];
        }


    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}
