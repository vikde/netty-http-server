package com.demo.netty.http.server.common.model;

import com.demo.netty.http.server.common.util.HostUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class CustomHttpRequest {
    /**
     * 原始请求
     */
    private FullHttpRequest httpRequest;
    /**
     * 客户端ip
     */
    private String clientIp = "";
    /**
     * 服务端ip
     */
    private String serverIp = HostUtil.getIp();
    /**
     * 请求方法
     */
    private HttpMethod httpMethod;
    /**
     * 请求的路径
     */
    private String path;
    /**
     * get请求中的paramMap
     */
    private Map<String, String> paramMap;

    public CustomHttpRequest(FullHttpRequest request) {
        this.httpRequest = request;
        this.httpMethod = request.method();
        parseUri(request.uri());
    }

    /**
     * 返回请求uri的path,uri参数
     */
    private void parseUri(String uri) {
        path = "/";
        paramMap = new LinkedHashMap<>();
        String queryStr = "";
        if (uri != null && uri.length() > 0) {
            int index = uri.indexOf("?");
            if (index < 0) {
                path = uri;
            } else {
                path = uri.substring(0, index);
                if (index < uri.length() - 1) {
                    queryStr = uri.substring(index + 1);
                }
            }
        }
        if (!queryStr.isEmpty()) {
            String[] params = queryStr.split("&");
            for (String param : params) {
                if (param == null || param.length() < 2) {
                    continue;
                }
                int paramIndex = param.indexOf("=");
                if (paramIndex <= 0) {
                    continue;
                }
                String key = param.substring(0, paramIndex);
                String value = param.substring(paramIndex + 1);
                paramMap.put(key.trim(), value.trim());
            }
        }
    }

    public FullHttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}
