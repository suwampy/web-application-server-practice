package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    /**
     * makeUserList
     * 사용자 목록을 출력하는 HTML 동적으로 생성
     * */
    public static StringBuilder makeUserList(){
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border ='1'>");
        for (User u : users) {
            sb.append("<tr>");
            sb.append("<td>" + u.getUserId() + "</td>");
            sb.append("<td>" + u.getName() + "</td>");
            sb.append("<td>" + u.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb;
    }


    public static int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

   /**
    * 요구사항 6 - 사용자 목록 출력 / 로그인 확인하기
    * */
    public static boolean isLogin(String line) {
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
        String value = cookies.get("logined");

        if (value == null) {
            return false;
        }

        return Boolean.parseBoolean(value);
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
