package util;

import java.util.Map;


import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertEquals(parameters.get("password"), null);

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertEquals(parameters.get("password"), "password2");
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertEquals(parameters.isEmpty(), true);

        parameters = HttpRequestUtils.parseQueryString("");
        assertEquals(parameters.isEmpty(),true);

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertEquals(parameters.isEmpty(),true);
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(parameters.get("userId"), "javajigi");
        assertEquals(parameters.get("password"), null);
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertEquals(parameters.get("logined"), "true");
        assertEquals(parameters.get("JSessionId"), "1234");
        assertEquals(parameters.get("session"), null);
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertEquals(pair, new Pair("userId", "javajigi"));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertEquals(pair, null);
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertEquals(pair, new Pair("Content-Length", "59"));
    }
}
