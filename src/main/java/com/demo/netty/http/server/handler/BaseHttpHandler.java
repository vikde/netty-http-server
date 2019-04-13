package com.demo.netty.http.server.handler;

import com.demo.netty.http.server.common.model.CustomHttpRequest;
import com.demo.netty.http.server.common.model.CustomHttpResponse;

/**
 * @author vikde
 * @date 2019/04/13
 */
public abstract class BaseHttpHandler {
    /**
     * 处理客户端发送过来的请求
     *
     * @param request 请求对象
     * @return 返回对象
     */
    public abstract CustomHttpResponse handle(CustomHttpRequest request);
}
