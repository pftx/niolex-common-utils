package org.apache.niolex.commons.rpc;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.niolex.commons.codec.Base64Util;

import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.exception.ExceptionHandler;

/**
 * mcpack2远程调用代理类，相对于基类添加了向Header中加入属性的功能；
 * 当Header中包含username和password时，自动添加Basic认证信息；
 * 当Header中包含tokenname和tokensize是，自动添加随机token信息。
 * 
 */
public class HeaderMcpackRpcProxy extends McpackRpcProxy {

    // 用于放置header中需要添加的属性信息
    private Map<String, String> headerMap;

    public HeaderMcpackRpcProxy(String url, String encoding, ExceptionHandler exp) {
        super(url, encoding, exp);
    }

    @Override
    protected void sendRequest(byte[] reqBytes, URLConnection connection) {
        if (null != headerMap && !headerMap.isEmpty()) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        super.sendRequest(reqBytes, connection);
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
        if (null == headerMap || headerMap.isEmpty()) {
            return;
        }
        String username = headerMap.get("username");
        String password = headerMap.get("password");
        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
            headerMap.put("Authorization", authHeader(username, password));
            headerMap.remove("username");
            headerMap.remove("password");
        }
        String tokenname = headerMap.get("tokenname");
        String tokensize = headerMap.get("tokensize");
        if (!StringUtils.isBlank(tokenname) && !StringUtils.isBlank(tokensize)) {
            headerMap.put(tokenname, genToken(Integer.parseInt(tokensize)));
            headerMap.remove("tokenname");
            headerMap.remove("tokensize");
        }
    }

    public static String authHeader(String username, String password) {
        String authString = username + ":" + password;
        String auth = null;
        try {
            auth = "Basic " + Base64Util.byteToBase64(authString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Basic Auth Header not set: " + e.toString());
        }
        return auth;
    }
    
    public static String genToken(int length) {
        String tempId = "";
        int curLen = 0;
        while (curLen < length) {
            tempId = tempId + Long.toHexString((long) (Math.random() * 1000000000000000L))
                    + Long.toHexString(System.nanoTime());
            curLen = tempId.length();
        }

        return tempId.substring(0, length);
    }
}
