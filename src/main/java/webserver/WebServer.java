package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8888;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // ServerSokect에 사용자 요청이 발생하는 순간 클라이언트와 연결을 담당하는 Socket을
                // RequestHandler에 전달하면서새로운 스레드를 실행하는 방식으로
                // 멀티 스레드 프로그래밍을 지원
                // (RequestHandler 클래스는 Thread를 상속받음)
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start(); //Thread의 start 메소드 실행
            }
        }
    }
}
