package webserver;

import http.RequestLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLineTest {

    @Test
    public void create_method() {
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(line.getMethod()).isEqualTo("GET");
        assertThat(line.getPath()).isEqualTo("/index.html");
    }
}
