# netty-http-server

基于netty的http服务简单实现，支持https，项目仅用于示例

## 环境依赖
java 8+

gradle 4.0+


## 测试
1、实现BaseHttpHandler处理客户端的请求
```
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
```

2、设置参数并启动
```
public class HttpServerTest {
    public static void main(String[] args) {
        //参数设置
        HttpServerConfig config = new HttpServerConfig();
        config.setPort(8080);
        
        HelloHttpHandler handler = new HelloHttpHandler();
        HttpServer httpServer = new HttpServer(config, handler);
        httpServer.start();
    }
}
```

3、示例
```
GET http://127.0.0.1:8080/hello?name=zhangsan

HTTP/1.1 200 OK
server: netty
content-type: text/plain
connection: keep-alive

hello zhangsan
```