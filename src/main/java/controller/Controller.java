package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

public interface Controller {
    // 각 요청과 응답에 대한 처리를 담당
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
