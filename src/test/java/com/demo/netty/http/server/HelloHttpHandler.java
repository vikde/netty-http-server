package com.demo.netty.http.server;

import com.demo.netty.http.server.common.model.CustomHttpRequest;
import com.demo.netty.http.server.common.model.CustomHttpResponse;
import com.demo.netty.http.server.handler.BaseHttpHandler;
import io.netty.handler.codec.http.HttpMethod;

import java.nio.charset.StandardCharsets;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class HelloHttpHandler extends BaseHttpHandler {
    @Override
    public CustomHttpResponse handle(CustomHttpRequest request) {
        CustomHttpResponse customHttpResponse = new CustomHttpResponse();

        String path = request.getPath();
        //hello处理
        if ("/hello".equals(path) && request.getHttpMethod() == HttpMethod.GET) {
            String name = request.getParamMap().get("name");
            customHttpResponse.setContent(("hello " + name).getBytes(StandardCharsets.UTF_8));
        }
        return customHttpResponse;
    }
}
