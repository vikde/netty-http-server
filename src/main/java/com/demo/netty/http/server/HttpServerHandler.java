package com.demo.netty.http.server;

import com.demo.netty.http.server.common.exception.ServiceException;
import com.demo.netty.http.server.common.model.CustomHttpRequest;
import com.demo.netty.http.server.common.model.CustomHttpResponse;
import com.demo.netty.http.server.common.model.HttpServerConfig;
import com.demo.netty.http.server.common.util.GzipUtil;
import com.demo.netty.http.server.handler.BaseHttpHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private BaseHttpHandler httpHandler;
    private HttpServerConfig httpServerConfig;

    HttpServerHandler(BaseHttpHandler httpHandler, HttpServerConfig httpServerConfig) {
        this.httpHandler = httpHandler;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        CustomHttpRequest httpRequest = new CustomHttpRequest(request);
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = socketAddress.getAddress().getHostAddress();
        httpRequest.setClientIp(clientIp);
        CustomHttpResponse httpResponse;
        try {
            //处理
            httpResponse = httpHandler.handle(httpRequest);
            if (httpResponse == null) {
                throw new ServiceException(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            httpResponse = new CustomHttpResponse();
            httpResponse.setMessage(ex.getMessage());
            httpResponse.setHttpResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        //响应信息
        byte[] contentBytes = httpResponse.getContent();
        if (contentBytes == null) {
            contentBytes = "".getBytes(CharsetUtil.UTF_8);
        }
        write(ctx, httpResponse.getHttpResponseStatus(), keepAlive, contentBytes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //io异常直接关闭
        if ((cause instanceof IOException)) {
            ctx.channel().close();
            return;
        }
        //如果客户端还在，就发送异常消息
        if (ctx.channel().isActive()) {
            HttpResponseStatus httpResponseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            write(ctx, httpResponseStatus, false, httpResponseStatus.reasonPhrase().getBytes(CharsetUtil.UTF_8));
            return;
        }
        //最后关闭通道
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //超时断开连接
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 写数据
     */
    private void write(ChannelHandlerContext ctx, HttpResponseStatus httpResponseStatus, boolean keepAlive, byte[] bytes) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(GzipUtil.compress(bytes));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, byteBuf);
        response.headers().set(HttpHeaderNames.SERVER, httpServerConfig.getServerName());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, keepAlive ? HttpHeaderValues.KEEP_ALIVE : HttpHeaderValues.CLOSE);
        response.headers().set(HttpHeaderNames.CONTENT_ENCODING, HttpHeaderValues.GZIP);
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (!keepAlive) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
