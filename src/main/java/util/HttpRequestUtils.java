package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import static java.lang.Integer.parseInt;
import static util.IOUtils.readData;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);
    /**
     * @param queryString은
     *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키
     *            값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    // getUrl
    public static String getUrl(String line) {
        String url = line.split(" ")[1];
        log.debug("url : {}", url);
        return url;
    }

    /**
     * getHttpHeader
     * http 요청 정보의 header 부분을 추출하는 메소드
     * */
    public static void getHttpHeader(BufferedReader br) throws IOException {
        String line = br.readLine();

        // line이 null 값인 경우에 대한 예외 처리도 해야한다.
        // 그렇지 않을 경우 무한 루프에 빠진당
        if (line == null) {
            return;
        }

        // HTTP 요청 정보 Header 부분을 출력한다.
        while(!"".equals(line)) {
            log.debug("HTTP request Header info : {}",line);
            line = br.readLine();
        };

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

        return user;
    }


    // getRequestURL
    public static String getRequestURL(String url) {
        return url.substring(0, url.indexOf("?"));
    }

    // getQueryString
    public static String getQueryString(String url) {
        return url.substring(url.indexOf("?")+1);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
