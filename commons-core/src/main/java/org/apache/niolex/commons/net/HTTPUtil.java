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

import static java.net.HttpURLConnection.*;
import static org.apache.niolex.commons.net.DownloadUtil.*;
import static org.apache.niolex.commons.util.Const.M;
import static org.apache.niolex.commons.util.DateTimeUtil.SECOND;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.niolex.commons.bean.Triple;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.commons.stream.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

/**
 * This is a simple HTTP utility for GET and POST requests.
 *
 * Since 2016-1-26 with version 2.0:
 * I want to refactor this Utility, support HTTP post with JSON data, so some compatibility must be broken.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.0.0
 * @since 2013-7-15
 */
public abstract class HTTPUtil {

    /**
     * The demo request headers. User can use this as a base and add more headers into it.
     */
    public static final Map<String, String> REQ_HEADER = ImmutableMap.of("DNT", "1",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36",
            "Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3",
            "Cache-Control", "no-cache");

    private static final Logger LOG = LoggerFactory.getLogger(HTTPUtil.class);

    private static final int CONNECT_TIMEOUT = 6 * SECOND;
    private static final int READ_TIMEOUT = 6 * SECOND;
    private static final int MAX_BODY_SIZE = 15 * M;

    /**
     * Do a HTTP get request.
     *
     * @param url the request URL
     * @return the response body as string
     * @throws NetException if necessary
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
     * @throws NetException if necessary
     */
    public static final String get(String url, Map<String, String> params) throws NetException {
        return get(url, params, CharEncoding.UTF_8);
    }

    /**
     * Do a HTTP get request.
     *
     * @param url the request URL
     * @param params the request parameters
     * @param paramCharset the charset used to send the request parameters
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String get(String url, Map<String, String> params, String paramCharset) throws NetException {
        return doHTTP(url, params, null, paramCharset, HTTPMethod.GET);
    }

    /**
     * Do a HTTP post request.
     *
     * @param url the request URL
     * @param params the request parameters, will be encoded as application/x-www-form-urlencoded
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String post(String url, Map<String, String> params) throws NetException {
        return post(url, params, CharEncoding.UTF_8);
    }

    /**
     * Do a HTTP post request.
     *
     * @param url the request URL
     * @param params the request parameters, will be encoded as application/x-www-form-urlencoded
     * @param paramCharset the charset used to send the request parameters
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String post(String url, Map<String, String> params, String paramCharset) throws NetException {
        return doHTTP(url, params, null, paramCharset, HTTPMethod.POST);
    }

    /**
     * Do a HTTP post request.
     *
     * @param url the request URL
     * @param postBody the request body to be posted, will be sent as application/json
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String post(String url, String postBody) throws NetException {
        return post(url, postBody, CharEncoding.UTF_8);
    }

    /**
     * Do a HTTP post request.
     *
     * @param url the request URL
     * @param postBody the request body to be posted, will be sent as application/json
     * @param postCharset the charset used to send the post body
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String post(String url, String postBody, String postCharset) throws NetException {
        return doHTTP(url, null, postBody, postCharset, HTTPMethod.POST);
    }

    /**
     * Do a HTTP request.
     * <b>If you want to post data in the parameter postBody, you must leave parameter params as null.</b>
     *
     * @param url the request URL
     * @param params the request parameters
     * @param postBody the request body to be posted, will be encoded as application/json
     * @param reqCharset the charset used to send the request parameters
     * @param method the HTTP method to be used
     * @return the response body as string
     * @throws NetException if necessary
     */
    public static final String doHTTP(String url, Map<String, String> params, String postBody, String reqCharset,
            HTTPMethod method) throws NetException {
        Triple<Integer, Map<String, List<String>>, byte[]> res = doHTTP(url, params, postBody, reqCharset,
                REQ_HEADER, CONNECT_TIMEOUT, READ_TIMEOUT, method);
        if (res.z == null) {
            throw new NetException(NetException.ExCode.INVALID_SERVER_RESPONSE, "URL " + url + " response code "
                    + res.x + " [There is no data.]");
        }

        String rest = new String(res.z, inferCharset(res.y, res.z));

        if (res.x >= 400) {
            throw new NetException(NetException.ExCode.INVALID_SERVER_RESPONSE, "URL " + url + " response code "
                    + res.x + " [Response size " + rest.length() + "]");
        }
        return rest;
    }

    /**
     * Do the HTTP request.
     *
     * @param strUrl the request URL
     * @param connectTimeout the connection timeout
     * @param readTimeout the data read timeout
     * @param method the HTTP method to be used
     * @return the response triple: x is response code, y is response header map, z is response body
     * @throws NetException if necessary
     */
    public static final Triple<Integer, Map<String, List<String>>, byte[]> doHTTP(String strUrl, int connectTimeout,
            int readTimeout, HTTPMethod method) throws NetException {
        return doHTTP(strUrl, null, null, null, REQ_HEADER, connectTimeout, readTimeout, method);
    }

    /**
     * Do the HTTP request.
     * We will take care of resource release and HTTP-30X redirect to new location.
     * <b>If you want to post data in the parameter postBody, you must leave parameter params as null.</b>
     *
     * @param strUrl the request URL
     * @param params the request parameters
     * @param postBody the request body to be posted, will be sent as application/json
     * @param reqCharset the charset used to send the request parameters
     * @param reqHeaders the request headers
     * @param connectTimeout the connection timeout
     * @param readTimeout the data read timeout
     * @param method the HTTP method to be used
     * @return the response triple: x is response code, y is response header map, z is response body
     * @throws NetException if necessary
     */
    public static final Triple<Integer, Map<String, List<String>>, byte[]> doHTTP(String strUrl, Map<String, String> params,
            String postBody, String reqCharset, Map<String, String> reqHeaders, int connectTimeout, int readTimeout,
            HTTPMethod method) throws NetException {
        LOG.debug("Start HTTP {} request to [{}], C{}R{}.", method.name(), strUrl, connectTimeout, readTimeout);

        InputStream in = null;
        boolean inErrorStatus = true; // If this value is true, we disconnect the socket connection.
        HttpURLConnection httpCon = null;

        try {
            // 1. Prepare HTTP parameters.
            // For GET HEAD DELETE, we pass parameters in URL; for POST, we save it in reqBytes.
            byte[] reqBytes = null;
            String contentType = null;

            // We use UTF8 as the default charset.
            if (reqCharset == null) {
                reqCharset = CharEncoding.UTF_8;
            }

            // Process the <params> map.
            if (!CollectionUtil.isEmpty(params)) {
                if (method.passParametersInURL()) {
                    strUrl = strUrl + '?' + prepareWwwFormUrlEncoded(params, reqCharset);
                } else {
                    reqBytes = StringUtil.strToAsciiByte(prepareWwwFormUrlEncoded(params, reqCharset));
                    contentType = "application/x-www-form-urlencoded; charset=" + reqCharset;
                }
            }

            // Process the post body.
            if (!StringUtil.isBlank(postBody)) {
                if (method.passParametersInURL()) {
                    throw new IllegalArgumentException("You must use HTTP POST method when you set postBody.");
                }

                if (reqBytes != null) {
                    throw new IllegalArgumentException("You can not use both params and postBody together.");
                }

                // Translate postBody into bytes.
                reqBytes = postBody.getBytes(reqCharset);
                contentType = "application/json; charset=" + reqCharset;
            }
            URL url = new URL(strUrl); // We use Java URL to do the HTTP request.

            // 2. Create a connection and validate the connection type.
            // At this time, no real socket connection was established. Just an Java object was created.
            URLConnection ucon = url.openConnection();
            if (!(ucon instanceof HttpURLConnection)) {
                throw new NetException(NetException.ExCode.INVALID_URL_TYPE, "The request is not in HTTP protocol.");
            }
            httpCon = (HttpURLConnection) ucon;

            // 3. Add all the request headers and do proper configuration.
            if (reqHeaders != null) {
                for (Map.Entry<String, String> entry : reqHeaders.entrySet()) {
                    httpCon.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpCon.setConnectTimeout(connectTimeout);
            httpCon.setReadTimeout(readTimeout);

            // 4. For get or no parameter, we do not output data; for post, we pass parameters in Body.
            httpCon.setRequestMethod(method.name());

            if (reqBytes == null) {
                httpCon.setDoOutput(false);
            } else {
                httpCon.setRequestProperty("Content-Type", contentType);
                httpCon.setRequestProperty("Content-Length", Integer.toString(reqBytes.length));
                httpCon.setDoOutput(true);
            }
            httpCon.setDoInput(true);
            httpCon.setInstanceFollowRedirects(true);

            // Network connection created at this time.
            httpCon.connect();

            // 5. do output if needed.
            if (reqBytes != null) {
                StreamUtil.writeAndClose(httpCon.getOutputStream(), reqBytes);
            }

            // 6. Validate the response code. Do manual redirect here.
            final int httpResponseCode = httpCon.getResponseCode();
            if (httpResponseCode == HTTP_MOVED_PERM || httpResponseCode == HTTP_MOVED_TEMP) {
                // HttpURLConnection by design won't automatically redirect from HTTP to HTTPS (or vice versa).
                // We need to handle it manually.
                String location = httpCon.getHeaderField("Location");
                String cookies = httpCon.getHeaderField("Set-Cookie");

                Map<String, String> newHeaders = new HashMap<String, String>(reqHeaders);
                newHeaders.put("Referer", strUrl);
                newHeaders.put("Cookie", cookies);

                LOG.debug("Going to redirect to new URL [{}].", location);
                return doHTTP(location, null, null, null, newHeaders, connectTimeout, readTimeout, method);
            }

            // 7. Get the input stream. If error occurs, try the error stream.
            if (httpResponseCode >= 400) {
                in = httpCon.getErrorStream();
            } else {
                in = httpCon.getInputStream();
            }
            final int contentLength = httpCon.getContentLength();
            // Read response byte array according to the condition.
            byte[] ret = checkAndDownloadData(strUrl, contentLength, in);

            // 8. Prepare the result.
            inErrorStatus = false; // HTTP being processed correctly, HTTP connection can be reused.
            return Triple.create(httpResponseCode, httpCon.getHeaderFields(), ret);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (NetException e) {
            LOG.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            String msg = "Failed to execute HTTP request to [" + strUrl + "], msg=" + e.toString();
            LOG.warn(msg);
            throw new NetException(NetException.ExCode.IOEXCEPTION, msg, e);
        } finally {
            // Close the input stream.
            StreamUtil.closeStream(in);
            // Cleanup the HTTP connection.
            cleanupHttpURLConnection(inErrorStatus, httpCon);
        }
    }

    /**
     * Download data from the input stream, and check the data size if necessary.
     *
     * @param strUrl the Url to be downloaded
     * @param contentLength the content Length from HTTP header
     * @param in the input stream
     * @return the downloaded bytes
     * @throws NetException if necessary
     * @throws IOException if necessary
     */
    public static byte[] checkAndDownloadData(final String strUrl, final int contentLength, InputStream in)
            throws NetException, IOException {
        byte[] ret = null;

        // 7. Read response byte array according to the condition.
        if (in != null && contentLength != 0) {
            if (contentLength > 0) {
                ret = commonDownload(in, contentLength);
            } else {
                ret = unusualDownload(in, 0, MAX_BODY_SIZE, strUrl, Boolean.TRUE);
            }

            LOG.debug("Succeeded to execute HTTP request to [{}], response size {}.", strUrl, ret.length);
        }

        return ret;
    }

    /**
     * Cleanup the HTTP connection so that JVM can release the network resources.
     *
     * @param inErrorStatus whether we are in error status when using the HTTP connection
     * @param httpCon the http connection instance
     */
    public static void cleanupHttpURLConnection(boolean inErrorStatus, HttpURLConnection httpCon) {
        // If the stream is not completely consumed, the underlying socket connection can not be reused,
        // so we need to disconnect it for this case to release system resource.
        if (inErrorStatus && httpCon != null) {
            httpCon.disconnect();
        }
    }

    /**
     * Infer the charset from HTTP response headers, or from the body if needed.
     *
     * @param headers the HTTP response headers
     * @param body the HTTP response body
     * @return the inferred charset
     */
    public static final Charset inferCharset(Map<String, List<String>> headers, byte[] body) {
        // Case 1. Infer charset from headers.
        String rawCharSet = null;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if ("Content-Type".equalsIgnoreCase(entry.getKey())) {
                List<String> list = entry.getValue();
                if (!CollectionUtil.isEmpty(list)) {
                    String s = list.get(0);
                    int idx = s.toLowerCase().indexOf("charset");
                    if (idx > 0) {
                        rawCharSet = s.substring(idx + 7);
                    }
                }
                break;
            }
        }

        // Case 2. Infer charset from body.
        if (rawCharSet == null) {
            int length = body.length < 512 ? body.length : 512;
            String s = new String(body, 0, length, StringUtil.US_ASCII);

            int idx = s.toLowerCase().indexOf("charset");
            if (idx > 0) {
                idx += 7;
                int end = s.length() > idx + 20 ? idx + 20 : s.length();
                rawCharSet = s.substring(idx, end);
            }
        }

        // Case 3. Use default.
        if (rawCharSet == null) {
            return StringUtil.UTF_8;
        }

        return retrieveCharsetFromString(rawCharSet);
    }

    /**
     * Retrieve the charset from the string, we will remove ' " = etc from the string to
     * get the real charset name.
     *
     * @param rawCharSet the raw charset in string
     * @return the inferred charset
     */
    public static final Charset retrieveCharsetFromString(String rawCharSet) {
        int idx = 0, end = rawCharSet.length();

        while (idx < end) {
            char ch = rawCharSet.charAt(idx);
            if (ch == '\'' || ch == '"' || ch == ' ' || ch == '=') ++idx;
            else break;
        }
        final int start = idx;

        while (idx < end) {
            char ch = rawCharSet.charAt(idx);
            if (ch == '\'' || ch == '"' || ch == ' '|| ch == ';' || ch == '>') break;
            else ++idx;
        }
        return Charset.forName(rawCharSet.substring(start, idx));
    }

    /**
     * Prepare the application/x-www-form-urlencoded HTTP parameters.
     *
     * @param params the parameters
     * @param paramCharset the charset used to encode the parameters
     * @return the prepared parameters in String format
     * @throws IllegalArgumentException if the parameter charset is not supported
     */
    public static String prepareWwwFormUrlEncoded(Map<String, String> params, String paramCharset) {
        if (CollectionUtil.isEmpty(params)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                sb.append(URLEncoder.encode(entry.getKey(), paramCharset));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), paramCharset));
                sb.append('&');
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
