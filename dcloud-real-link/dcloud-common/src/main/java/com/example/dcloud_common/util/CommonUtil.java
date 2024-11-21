package com.example.dcloud_common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.hash.Hashing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * @Description：Common Util
 * @Author： RainbowJier
 * @Data： 2024/8/13 21:21
 */

@Slf4j
public class CommonUtil {


    /**
     * Get ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;

        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {

                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {

                    // Obtain the IP address by NIC
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    ipAddress = inet.getHostAddress();
                }
            }

            // 对于通过多个代理的情况，第⼀个IP为客户端真实 IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * Get all request headers.
     */
    public static Map<String, String> getAllRequestHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> map = new HashMap<>();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();

            // Get request header value by name.
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * MD5 encryption.
     */
    public static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }

            return sb.toString().toUpperCase();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Get random verification code.
     */
    public static String getRandomCode(int length) {
        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            sb.append(sources.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }

    /**
     * Get current timestamp.
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Generate UUID.
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * Get string with random length.
     */
    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String getStringNumRandom(int length) {
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);

        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }

        return saltString.toString();
    }

    /**
     * Response Json data to fronted.
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {
        response.setContentType("application/json;charset=utf-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtil.objToJsonStr(obj));
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("Respond json data to the fronted exception: {}", e.getMessage());
        }
    }

    /**
     * murmur hash算法
     */
    public static long murmurHash32(String param) {
        return Hashing.murmur3_32().hashUnencodedChars(param).padToLong();
    }


    /**
     * URL增加前缀
     */
    public static String addUrlPrefix(String url) {
        return IDUtil.generateSnowFlakeID() + "&" + url;
    }

    /**
     * URL移除前缀
     */
    public static String removeUrlPrefix(String url) {
        return url.substring(url.indexOf("&") + 1);
    }

    /**
     * 如果短链码重复，则调用这个⽅法
     * url前缀编号+1，如果还是用雪花算法，则容易C和B端不⼀致，所以采⽤原先的id递增1
     */
    public static String addUrlPrefixVersion(String url) {
        // 前缀
        String result = url.substring(0, url.indexOf("&"));

        //原始地址
        String originalUrl = url.substring(url.indexOf("&") + 1);

        //新id编号
        Long newIdValue = Long.parseLong(result) + 1;

        return newIdValue + "&" + originalUrl;
    }


}
