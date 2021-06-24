package webserver;

import java.io.*;
import java.net.Socket;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import controller.Controller;
import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            HttpRequest req = new HttpRequest(in);
            HttpResponse res = new HttpResponse(out);

            /**
             * controller
             * */
            Controller c = RequestMapping.getController(req.getPath());

            if (c == null) {
                String path = getDefaultPath(req.getPath());
                res.forward(path);
            } else {
                c.service(req, res);
                log.debug("c:{}",c);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path){
        if (path.equals("/")){
            return "/index.html";
        }

        return path;
    }


}
