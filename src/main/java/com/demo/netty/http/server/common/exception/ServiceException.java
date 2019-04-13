package com.demo.netty.http.server.common.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 服务异常类
 * 没有堆栈信息
 *
 * @author vikde
 * @date 2019/04/13
 */
public class ServiceException extends Exception {
    private HttpResponseStatus httpResponseStatus;

    public ServiceException(HttpResponseStatus httpResponseStatus) {
        this(httpResponseStatus, httpResponseStatus.reasonPhrase());
    }

    public ServiceException(HttpResponseStatus httpResponseStatus, String message) {
        super(message, null, false, false);
        this.httpResponseStatus = httpResponseStatus;
    }

    public ServiceException(HttpResponseStatus httpResponseStatus, String message, boolean enableSuppression, boolean writableStackTrace) {
        super(message, null, enableSuppression, writableStackTrace);
        this.httpResponseStatus = httpResponseStatus;
    }

    public ServiceException(HttpResponseStatus httpResponseStatus, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpResponseStatus = httpResponseStatus;
    }

    public HttpResponseStatus getHttpResponseStatus() {
        return httpResponseStatus;
    }
}
