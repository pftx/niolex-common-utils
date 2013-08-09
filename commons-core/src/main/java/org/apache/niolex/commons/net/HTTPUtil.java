/**
 * HTTPUtil.java
 *
 * Copyright 2013 the original author or authors.
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

import static org.apache.niolex.commons.util.Const.K;
import static org.apache.niolex.commons.util.DateTimeUtil.SECOND;
import static org.apache.niolex.commons.net.DownloadUtil.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.commons.internal.Finally;
import org.apache.niolex.commons.stream.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

/**
 * This is a simple HTTP util for GET and POST requests.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-15
 */
public abstract class HTTPUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HTTPUtil.class);

    private static final int CONNECT_TIMEOUT = 6 * SECOND;
    private static final int READ_TIMEOUT = 6 * SECOND;
    private static final int MAX_BODY_SIZE = 100 * K;
    private static final Map<String, String> REQ_HEADER = ImmutableMap.of("DNT", "1",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0",
            "Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3",
            "Cache-Control", "no-cache");

    /**
     * Do a HTTP get request.
     *
     * @param url the request URL
     * @return the response body as string
     * @throws NetException
     */
    public static final String get(String url) throws NetException {
        return get(url, null);
    }

    /**
     * Do a HTTP get request.
     *
     * @param url the request URL
     * @param params the request parameters
     * @return the response body as string
     * @throws NetException
     */
    public static final String get(String url, Map<String, String> params) throws NetException {
        return doHTTP(url, params, true);
    }

    /**
     * Do a HTTP post request.
     *
     * @param url the request URL
     * @param params the request parameters
     * @return the response body as string
     * @throws NetException
     */
    public static final String post(String url, Map<String, String> params) throws NetException {
        return doHTTP(url, params, false);
    }

    /**
     * Do a HTTP request.
     *
     * @param url the request URL
     * @param params the request parameters
     * @param useGet whether do we use the HTTP GET method
     * @return the response body as string
     * @throws NetException
     */
    public static final String doHTTP(String url, Map<String, String> params, boolean useGet) throws NetException {
        Pair<Map<String, List<String>>, byte[]> res = doHTTP(url, params, REQ_HEADER, CONNECT_TIMEOUT, READ_TIMEOUT, useGet);
        return new String(res.b, inferCharset(res.a, res.b));
    }

    /**
     * Infer the charset from HTTP response headers, or from the body
     * @param headers the HTTP response headers
     * @param body the HTTP response body
     * @return the inferred charset
     */
    public static final Charset inferCharset(Map<String, List<String>> headers, byte[] body) {
        // Case 1. Infer charset from headers.
        String charSet = null;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if ("Content-Type".equalsIgnoreCase(entry.getKey())) {
                List<String> list = entry.getValue();
                if (!CollectionUtil.isEmpty(list)) {
                    String s = list.get(0);
                    int idx = s.indexOf("charset=");
                    if (idx > 0) {
                        charSet = s.substring(idx + 8).trim();
                    }
                }
                break;
            }
        }
        // Case 2. Infer charset from body.
        if (charSet == null) {
            String s = StringUtil.asciiByteToStr(Arrays.copyOfRange(body, 0, 400));
            int idx = s.indexOf("charset=");
            if (idx > 0) {
                idx += 8;
                char ch = s.charAt(idx);
                if (ch == '\'' || ch == '"') ++idx;
                final int start = idx;
                for (; idx < 400; ++idx) {
                    ch = s.charAt(idx);
                    if (ch == '\'' || ch == '"') {
                        break;
                    }
                }
                charSet = s.substring(start, idx);
            }
        }
        // Case 3. Use default.
        if (charSet == null) {
            return StringUtil.UTF_8;
        }
        return Charset.forName(charSet);
    }

    /**
     * Do the HTTP request.
     *
     * @param strUrl the request URL
     * @param params the request parameters
     * @param headers the request headers
     * @param connectTimeout the connection timeout
     * @param readTimeout the data read timeout
     * @param useGet whether do we use the HTTP GET method
     * @return the response pair; a is response header map, b is response body
     * @throws NetException
     */
    public static final Pair<Map<String, List<String>>, byte[]> doHTTP(String strUrl, Map<String, String> params,
            Map<String, String> headers, int connectTimeout, int readTimeout, boolean useGet) throws NetException {
        LOG.debug("Start HTTP request to [{}], C{}R{}.", strUrl, connectTimeout, readTimeout);
        InputStream in = null;
        try {
            // 1. For get, we pass parameters in URL; for post, we save it in reqBytes.
            byte[] reqBytes = null;
            if (!CollectionUtil.isEmpty(params)) {
                if (useGet) {
                    strUrl = strUrl + '?' + prepareWwwFormUrlEncoded(params);
                } else {
                    reqBytes = StringUtil.strToAsciiByte(prepareWwwFormUrlEncoded(params));
                }
            }
            URL url = new URL(strUrl); // We use Java URL to do the HTTP request.
            URLConnection ucon = url.openConnection();
            ucon.setConnectTimeout(connectTimeout);
            ucon.setReadTimeout(readTimeout);
            // 2. validate to Connection type.
            if (!(ucon instanceof HttpURLConnection)) {
                throw new NetException(NetException.ExCode.INVALID_URL_TYPE, "The request is not in HTTP protocol.");
            }
            final HttpURLConnection httpCon = (HttpURLConnection) ucon;
            // 3. We add all the request headers.
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpCon.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 4. For get or no parameter, we do not output data; for post, we pass parameters in Body.
            if (reqBytes == null) {
                httpCon.setDoOutput(false);
            } else {
                httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestProperty("Content-Length", Integer.toString(reqBytes.length));
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
            }
            httpCon.setDoInput(true);
            httpCon.connect();
            // 5. do output if needed.
            if (reqBytes != null) {
                Finally.writeAndClose(httpCon.getOutputStream(), reqBytes);
            }
            // 6. Get the input stream.
            in = httpCon.getInputStream();
            final int contentLength = httpCon.getContentLength();
            validateHttpCode(strUrl, httpCon);
            byte[] ret = null;
            // 7. Read response byte array according to the strategy.
            if (contentLength > 0) {
                ret = commonDownload(contentLength, in);
            } else {
                ret = unusualDownload(strUrl, in, MAX_BODY_SIZE, true);
            }
            // 8. Parse the response headers.
            LOG.debug("Succeeded to execute HTTP request to [{}], response size {}.", strUrl, ret.length);
            return Pair.create(httpCon.getHeaderFields(), ret);
        } catch (NetException e) {
            LOG.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            String msg = "Failed to execute HTTP request to [" + strUrl + "], msg=" + e.getMessage();
            LOG.warn(msg);
            throw new NetException(NetException.ExCode.IOEXCEPTION, msg, e);
        } finally {
            // Close the input stream.
            StreamUtil.closeStream(in);
        }
    }

    /**
     * Prepare the application/x-www-form-urlencoded HTTP parameters.
     *
     * @param params the parameters
     * @return the prepared parameters in String format
     */
    public static String prepareWwwFormUrlEncoded(Map<String, String> params) {
        if (CollectionUtil.isEmpty(params)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                sb.append(URLEncoder.encode(entry.getKey(), CharEncoding.UTF_8));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), CharEncoding.UTF_8));
                sb.append('&');
            } catch (Exception e) {/*Can not Happen.*/}
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
