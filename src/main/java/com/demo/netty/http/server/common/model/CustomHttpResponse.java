package com.demo.netty.http.server.common.model;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class CustomHttpResponse {
    private HttpResponseStatus httpResponseStatus = HttpResponseStatus.OK;
    private String message = "success";
    private byte[] content;

    public HttpResponseStatus getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public void setHttpResponseStatus(HttpResponseStatus httpResponseStatus) {
        this.httpResponseStatus = httpResponseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
