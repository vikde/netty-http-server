package com.demo.netty.http.server;

import com.demo.netty.http.server.common.model.HttpServerConfig;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class HttpServerTest {
    public static void main(String[] args) {
        //参数设置
        HttpServerConfig config = new HttpServerConfig();
        config.setPort(80);

        HelloHttpHandler handler = new HelloHttpHandler();
        HttpServer httpServer = new HttpServer(config, handler);
        httpServer.start();
    }
}