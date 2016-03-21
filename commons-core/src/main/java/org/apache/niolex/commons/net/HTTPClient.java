/**
 * HTTPClient.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.net;

import static org.apache.niolex.commons.net.HTTPMethod.*;
import static org.apache.niolex.commons.net.HTTPUtil.*;
import static org.apache.niolex.commons.util.DateTimeUtil.SECOND;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.bean.Triple;
import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This HTTP client class is used to help user control cookie and authentication. It is backed by HTTPUtil.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-1-28
 */
public class HTTPClient {
    private static final Logger LOG = LoggerFactory.getLogger(HTTPClient.class);

    /**
     * The HTTP request headers.
     */
    private final Map<String, String> reqHeaders = new HashMap<String, String>(REQ_HEADER);

    /**
     *  The end point to be used as the base when generate URL.
     */
    private final String endPoint;

    /**
     * The charset to be used to send request.
     */
    private final String charset;

    private int endPointDirDepth;
    private int connectTimeout = 6 * SECOND;
    private int readTimeout = 6 * SECOND;
    private String cookie;
    private String referer;
    private String authorization;

    /**
     * Construct a HTTPClient.
     *
     * @param endPoint the end point to be used as the base when generate URL
     * @param charset the charset to be used to send request
     */
    public HTTPClient(String endPoint, String charset) {
        super();
        this.endPoint = processEndPoint(endPoint);
        this.charset = charset;
    }

    /**
     * Construct a HTTPClient.
     *
     * @param endPoint the end point to be used as the base when generate URL
     * @param charset the charset to be used to send request
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     */
    public HTTPClient(String endPoint, String charset, int connectTimeout, int readTimeout) {
        super();
        this.endPoint = processEndPoint(endPoint);
        this.charset = charset;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * Use the HTTP Basic authentication (BA) implementation to enforce access controls to web resources.
     *
     * @param userName the BA user name
     * @param password the BA password
     */
    public void authorization(String userName, String password) {
        authorization = "Basic " + Base64Util.byteToBase64(StringUtil.strToUtf8Byte(userName + ":" + password));
        reqHeaders.put("Authorization", authorization);
    }

    /**
     * Do a HTTP get request.
     *
     * @param path the request path relative to the end point
     * @param params the request parameters
     * @return the HTTP result
     * @throws NetException if necessary
     */
    public HTTPResult get(String path, Map<String, String> params) throws NetException {
        Triple<Integer, Map<String, List<String>>, byte[]> res = doHTTP(generateURL(path), params, null, charset,
                reqHeaders, connectTimeout, readTimeout, GET);
        processCookie(res.y);

        return new HTTPResult(res.x, res.y, res.z, this);
    }

    /**
     * Do a HTTP post request.
     *
     * @param path the request path relative to the end point
     * @param params the request parameters
     * @return the HTTP result
     * @throws NetException if necessary
     */
    public HTTPResult post(String path, Map<String, String> params) throws NetException {
        Triple<Integer, Map<String, List<String>>, byte[]> res = doHTTP(generateURL(path), params, null, charset,
                reqHeaders, connectTimeout, readTimeout, POST);
        processCookie(res.y);

        return new HTTPResult(res.x, res.y, res.z, this);
    }

    /**
     * Do a HTTP post request.
     *
     * @param path the request path relative to the end point
     * @param json the request parameters in JSON format
     * @return the HTTP result
     * @throws NetException if necessary
     */
    public HTTPResult post(String path, String json) throws NetException {
        Triple<Integer, Map<String, List<String>>, byte[]> res = doHTTP(generateURL(path), null, json, charset,
                reqHeaders, connectTimeout, readTimeout, POST);
        processCookie(res.y);

        return new HTTPResult(res.x, res.y, res.z, this);
    }

    /**
     * Process the server Set-Cookie header and set the Cookie header for next request.
     *
     * @param respHeaders the HTTP server response headers
     */
    protected void processCookie(Map<String, List<String>> respHeaders) {
        List<String> cookies = respHeaders.get("Set-Cookie");

        if (cookies == null) {
            cookies = respHeaders.get("set-cookie");
        }

        if (CollectionUtil.isEmpty(cookies))
            return;

        for (String c : cookies) {
            // Every Set-Cookie Header is like this:
            // Set-Cookie:xxx=xxx; expires=Fri, 05-Feb-16 03:25:48 GMT; domain=www.baidu.com; path=/
            c = c.substring(0, c.indexOf(';'));

            if (cookie == null) cookie = c;
            else cookie = cookie + "; " + c;
        }

        LOG.debug("The new cookie: {}", cookie);
        reqHeaders.put("Cookie", cookie);
    }

    /**
     * Generate a valid URL according to the path.
     * We process relative path here too.
     *
     * @param path the relative path
     * @return the generated URL
     */
    protected String generateURL(String path) {
        String[] tokens = StringUtil.split(path, "/", false);

        if (tokens.length == 0) {
            return endPoint;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(endPoint);
        int indent = endPointDirDepth;
        for (int i = 0; i < tokens.length; ++i) {
            String item = tokens[i];
            if (item.equals(".")) continue;
            if (item.equals("..")) {
                if (indent == 0)
                    continue;

                --indent;
                sb.setLength(sb.lastIndexOf("/"));
            } else {
                ++indent;
                sb.append('/').append(item);
            }
        }

        reqHeaders.put("Referer", referer);
        referer = sb.toString();

        LOG.debug("The original path: {}, the generated URL: {}", path, referer);
        return referer;
    }

    /**
     * Process the end point, make sure it's valid.
     *
     * @param enp the end point
     * @return the processed end point
     */
    protected String processEndPoint(String enp) {
        URL u;
        try {
            u = new URL(enp);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The end point is invalid. " + enp);
        }

        String c = u.getProtocol();
        if (!(c.equals("https") || c.equals("http"))) {
            throw new IllegalArgumentException("The end point is not using HTTP protocol. " + enp);
        }

        if (u.getQuery() != null) {
            throw new IllegalArgumentException("The end point can not have query part. " + enp);
        }
        if (u.getRef() != null) {
            throw new IllegalArgumentException("The end point can not have ref part. " + enp);
        }

        endPointDirDepth = 0;
        String p = u.getPath();

        for (int i = 0, end = p.length() -1; i < end; ++i) {
            if (p.charAt(i) == '/') ++endPointDirDepth;
        }

        enp = u.toExternalForm();

        if (enp.endsWith("/")) enp = enp.substring(0, enp.length() - 1);

        referer = enp;
        LOG.debug("The processed end point: {}", referer);
        return enp;
    }

}
