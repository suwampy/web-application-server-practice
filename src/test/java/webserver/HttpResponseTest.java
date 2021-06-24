package webserver;

import http.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    /*
    * 응답을 보낼 때 HTML, CSS, 자바스크립트 파일을 직접 읽어 응답으로 보내는 메소드
    * */
    @Test
    public void responseForward() throws Exception {
        // Http_Forward.txt 결과는 응답 body에 index.html이 포함되어있어야한다
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));

    }

    /*
    * 다른  URL로 리다이렉트하는 메소드
    * **/
    @Test
    public void resposeRedirect() throws Exception {
        // Http_Redirect.txt 결과는
        // 응답 header에 Location 정보가 /index.html로 포함되어있어야 한다

    }

    @Test
    public void responseCookies() throws Exception {
        // Http_Cookie.txt 결과는 응답 header에
        // Set-Cookie 값으로
        // logined-true가 포함되어있어야한다
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}

