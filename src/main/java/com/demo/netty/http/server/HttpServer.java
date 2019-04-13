package com.demo.netty.http.server;

import com.demo.netty.http.server.common.model.HttpServerConfig;
import com.demo.netty.http.server.handler.BaseHttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Http 服务
 *
 * @author vikde
 * @date 2019/04/13
 */
public class HttpServer extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    private HttpServerConfig httpServerConfig;
    private BaseHttpHandler httpHandler;

    public HttpServer(HttpServerConfig httpServerConfig, BaseHttpHandler httpHandler) {
        this.setName(HttpServer.class.getSimpleName());
        this.httpServerConfig = httpServerConfig;
        this.httpHandler = httpHandler;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(httpServerConfig.getBossThreadCount());
        EventLoopGroup workerGroup = new NioEventLoopGroup(httpServerConfig.getWorkerThreadCount());
        final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(httpServerConfig.getBusinessThreadCount(), new DefaultThreadFactory("businessGroup"));
        try {
            SslContext sslContext = null;
            if (httpServerConfig.getIsEnableSsl()) {
                //自签证书
                if (httpServerConfig.getIsSelfSignedCertificate()) {
                    SelfSignedCertificate ssc = new SelfSignedCertificate(httpServerConfig.getSelfSignedCertificateDomain());
                    sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).sslProvider(SslProvider.OPENSSL).build();
                }
                //签名证书
                else {
                    InputStream keyCertChainInputStream = new FileInputStream(httpServerConfig.getSslKeyCertChainFile());
                    InputStream keyInputStream = new FileInputStream(httpServerConfig.getSslKeyFile());
                    sslContext = SslContextBuilder.forServer(keyCertChainInputStream, keyInputStream, httpServerConfig.getSslKeyPassword()).sslProvider(SslProvider.OPENSSL).build();
                    keyCertChainInputStream.close();
                    keyInputStream.close();
                }
            }
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, httpServerConfig.getSoBackLog());
            bootstrap.childOption(ChannelOption.TCP_NODELAY, httpServerConfig.getTcpNoDelay());
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            final SslContext finalSslContext = sslContext;
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    if (httpServerConfig.getIsEnableSsl()) {
                        SSLEngine sslEngine;
                        if (finalSslContext != null) {
                            sslEngine = finalSslContext.newEngine(channel.alloc());
                            sslEngine.setUseClientMode(false);
                            sslEngine.setNeedClientAuth(false);
                            pipeline.addLast(new SslHandler(sslEngine));
                        }
                    }
                    pipeline.addLast(new LoggingHandler(httpServerConfig.getLogLevel()));
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpServerExpectContinueHandler());
                    pipeline.addLast(new HttpContentCompressor(httpServerConfig.getCompressionLevel()));
                    pipeline.addLast(new HttpObjectAggregator(httpServerConfig.getMaxContentLength()));
                    // 超时检测放到解码器后可以保证是解码成功才记录正常数据的超时判断，防止垃圾数据长连接在线
                    pipeline.addLast(new IdleStateHandler(httpServerConfig.getReaderIdleTime(), httpServerConfig.getWriterIdleTime(), 0, TimeUnit.SECONDS));
                    pipeline.addLast(businessGroup, new HttpServerHandler(httpHandler, httpServerConfig));
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(httpServerConfig.getPort()).sync();
            LOGGER.info("Http server is running at {}.", httpServerConfig.getPort());
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("Http server start error", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            businessGroup.shutdownGracefully();
        }
    }
}
