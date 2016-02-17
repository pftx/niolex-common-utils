/**
 * HTTPResult.java
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

import static org.apache.niolex.commons.net.HTTPUtil.inferCharset;

import java.util.List;
import java.util.Map;

/**
 * The Java Bean to store the HTTP request result.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-2-4
 */
public class HTTPResult {

    private final int respCode;
    private final Map<String, List<String>> respHeaders;
    private final byte[] respBody;
    private String respBodyStr;
    private final HTTPClient client;

    /**
     * Construct a new HTTP result.
     *
     * @param respCode the HTTP response code
     * @param respHeaders the HTTP response headers
     * @param respBody the HTTP response body as bytes
     * @param client the HTTP client
     */
    public HTTPResult(int respCode, Map<String, List<String>> respHeaders, byte[] respBody, HTTPClient client) {
        super();
        this.respCode = respCode;
        this.respHeaders = respHeaders;
        this.respBody = respBody;
        this.client = client;
    }

    /**
     * @return the HTTP response code
     */
    public int getRespCode() {
        return respCode;
    }

    /**
     * @return the HTTP response headers
     */
    public Map<String, List<String>> getRespHeaders() {
        return respHeaders;
    }

    /**
     * Get the HTTP response Header by the specified key.
     *
     * @param key the Header key
     * @return the Header values
     */
    public List<String> getHeader(String key) {
        return respHeaders.get(key);
    }

    /**
     * @return the HTTP response body as bytes
     */
    public byte[] getRespBody() {
        return respBody;
    }

    /**
     * @return the HTTP response body as string
     */
    public String getRespBodyStr() {
        if (respBodyStr == null) {
            respBodyStr = new String(respBody, inferCharset(respHeaders, respBody));
        }
        return respBodyStr;
    }

    /**
     * @return the HTTP client
     */
    public HTTPClient client() {
        return client;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s Body Size [%d]", respHeaders.get(null), respBody == null ? -1 : respBody.length);
    }

}
