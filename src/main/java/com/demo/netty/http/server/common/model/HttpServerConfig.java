package com.demo.netty.http.server.common.model;

import io.netty.handler.logging.LogLevel;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class HttpServerConfig {
    /**
     * 端口
     */
    private int port = 80;
    /**
     * boss线程数量
     */
    private int bossThreadCount = Runtime.getRuntime().availableProcessors();
    /**
     * worker线程数量
     */
    private int workerThreadCount = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * 业务线程数量
     */
    private int businessThreadCount = Runtime.getRuntime().availableProcessors() * 4;
    /**
     * 连接超时断开时间 读 秒;0表示不设置超时时间;
     */
    private int readerIdleTime = 100;
    /**
     * 连接超时断开时间 写 秒;0表示不设置超时时间;
     */
    private int writerIdleTime = 100;
    /**
     * SO_BACKLOG
     */
    private int soBackLog = 1024;
    /**
     * TCP_NODELAY
     */
    private boolean tcpNoDelay = true;
    /**
     * netty日志级别
     */
    private LogLevel logLevel = LogLevel.DEBUG;
    /**
     * 压缩级别
     */
    private int compressionLevel = 6;
    /**
     * 请求最大长度
     */
    private int maxContentLength = 1024 * 1024;
    /**
     * 是否使用SSL证书
     */
    private boolean isEnableSsl = false;
    /**
     * 是否使用自签证书
     */
    private boolean isSelfSignedCertificate = true;
    /**
     * 自签证书使用的域名
     */
    private String selfSignedCertificateDomain = "example.com";
    /**
     * 钥匙链
     */
    private String sslKeyCertChainFile = "";
    /**
     * 密钥文件
     */
    private String sslKeyFile = "";
    /**
     * 密码
     */
    private String sslKeyPassword = "";
    /**
     * 服务名称
     */
    private String serverName = "netty";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThreadCount() {
        return bossThreadCount;
    }

    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public int getBusinessThreadCount() {
        return businessThreadCount;
    }

    public void setBusinessThreadCount(int businessThreadCount) {
        this.businessThreadCount = businessThreadCount;
    }

    public int getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public int getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(int writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public int getSoBackLog() {
        return soBackLog;
    }

    public void setSoBackLog(int soBackLog) {
        this.soBackLog = soBackLog;
    }

    public boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public boolean getIsEnableSsl() {
        return isEnableSsl;
    }

    public void setIsEnableSsl(boolean isEnableSsl) {
        this.isEnableSsl = isEnableSsl;
    }

    public boolean getIsSelfSignedCertificate() {
        return isSelfSignedCertificate;
    }

    public void setIsSelfSignedCertificate(boolean isSelfSignedCertificate) {
        this.isSelfSignedCertificate = isSelfSignedCertificate;
    }

    public String getSelfSignedCertificateDomain() {
        return selfSignedCertificateDomain;
    }

    public void setSelfSignedCertificateDomain(String selfSignedCertificateDomain) {
        this.selfSignedCertificateDomain = selfSignedCertificateDomain;
    }

    public String getSslKeyCertChainFile() {
        return sslKeyCertChainFile;
    }

    public void setSslKeyCertChainFile(String sslKeyCertChainFile) {
        this.sslKeyCertChainFile = sslKeyCertChainFile;
    }

    public String getSslKeyFile() {
        return sslKeyFile;
    }

    public void setSslKeyFile(String sslKeyFile) {
        this.sslKeyFile = sslKeyFile;
    }

    public String getSslKeyPassword() {
        return sslKeyPassword;
    }

    public void setSslKeyPassword(String sslKeyPassword) {
        this.sslKeyPassword = sslKeyPassword;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
