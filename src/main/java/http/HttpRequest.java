package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

/*
 * 요청 데이터를 처리하는 로직을 별도의 클래스로 분리한다.
 * HttpRequest : 클라이언트 요청 데이터를 읽은 후 각 데이터를 사용하기 좋은 형태로 분리하는 역할을 한다
 * ->데이터를 파싱하는 작업과 사용하는 부분을 분리
 * */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine; // http 메시지 첫 줄 : method, url, querystring
    private RequestParams requestParams = new RequestParams();
    private HttpHeaders httpHeaders; // http 헤더


    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        /**
         * 첫번째 http 메시지 줄을 통해 작업 가능한 명령들을 클래스로 분리함함
        * method, path 입력
         * */
        requestLine = new RequestLine(createRequestLine(br));
        requestParams.addQueryString(requestLine.getQueryString());

        httpHeaders = processHeaders(br);

        /**
         * 나머지 응답을 읽어와서
         * header map에 넣는다
         * */
        requestParams.addBody(IOUtils.readData(br, httpHeaders.getContentLength()));

    }

    private String createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return line;
    }

    private HttpHeaders processHeaders(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line;
        while (!(line = br.readLine()).equals("")) {
            headers.add(line);
        }
        return headers;
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return httpHeaders.getHeader(name);
    }

    public String getParameter(String name) {
        return requestParams.getParameter(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine=" + requestLine +
                ", requestParams=" + requestParams +
                ", httpHeaders=" + httpHeaders +
                '}';
    }
}
