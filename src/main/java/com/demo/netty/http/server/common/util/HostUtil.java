package com.demo.netty.http.server.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class HostUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostUtil.class);
    private static String ip = "";
    private static List<String> ipList;

    static {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            ipList = new LinkedList<>();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ia = ni.getInetAddresses();
                while (ia.hasMoreElements()) {
                    InetAddress inetAddress = ia.nextElement();
                    String tempIp = inetAddress.getHostAddress();
                    if (tempIp != null && !"127.0.0.1".equalsIgnoreCase(tempIp) && tempIp.split("\\.").length == 4) {
                        ipList.add(tempIp);
                    }
                }
            }
            //10>172>192
            Collections.sort(ipList);
            ipList.add("127.0.0.1");
            if (ipList.size() > 0) {
                ip = ipList.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("解析ip异常", e);
        }
    }

    public static String getIp() {
        return ip;
    }

    public static List<String> getIpList() {
        return ipList;
    }
}
