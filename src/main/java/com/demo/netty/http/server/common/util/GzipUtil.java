package com.demo.netty.http.server.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author vikde
 * @date 2019/04/13
 */
public class GzipUtil {
    private static final int BUFFER_SIZE = 1024;

    /**
     * 压缩
     */
    public static byte[] compress(byte[] data) {
        byte[] result = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(outputStream);
            int count = -1;
            byte[] buf = new byte[1024];
            while ((count = inputStream.read(buf, 0, BUFFER_SIZE)) != -1) {
                gzipOutputStream.write(buf, 0, count);
            }
            gzipOutputStream.finish();
            gzipOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzipOutputStream != null) {
                    gzipOutputStream.close();
                }
                result = outputStream.toByteArray();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 解压
     */
    public static byte[] decompress(byte[] data) {
        byte[] result = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = null;
        try {
            gzipInputStream = new GZIPInputStream(inputStream);
            int count = -1;
            byte[] buf = new byte[1024];
            while ((count = gzipInputStream.read(buf, 0, BUFFER_SIZE)) != -1) {
                outputStream.write(buf, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzipInputStream != null) {
                    gzipInputStream.close();
                }
                result = outputStream.toByteArray();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
