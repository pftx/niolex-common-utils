/**
 * RESTClient.java
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

import java.io.IOException;
import java.util.Map;

import org.apache.niolex.commons.compress.JacksonUtil;

/**
 * A convenient tool class to help you invoke the RESTful API.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Aug 31, 2016
 */
public class RESTClient extends HTTPClient {

    /**
     * The error decoder used to decode error informations returned from RESTful server.
     */
    private ErrorDecoder errorDecoder = ErrorDecoder.RESTDecoder.INSTANCE;

    /**
     * Construct a RESTClient.
     *
     * @param endPoint the end point to be used as the base when generate URL
     * @param charset the charset to be used to send request
     */
    public RESTClient(String endPoint, String charset) {
        super(endPoint, charset);
    }

    /**
     * Construct a RESTClient.
     *
     * @param endPoint the end point to be used as the base when generate URL
     * @param charset the charset to be used to send request
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     */
    public RESTClient(String endPoint, String charset, int connectTimeout, int readTimeout) {
        super(endPoint, charset, connectTimeout, readTimeout);
    }

    /**
     * Set the error decoder used by this REST client.
     * 
     * @param errorDecoder the error decoder to be used
     */
    public void setErrorDecoder(ErrorDecoder errorDecoder) {
        this.errorDecoder = errorDecoder;
    }

    /**
     * Using GET to invoke the RESTful API.
     * 
     * @param <T> the response class type
     * @param path the API URL relative path
     * @param clazz the response class type
     * @param params the request parameters
     * @return the REST result
     * @throws NetException if network related error occurred
     * @throws IOException if can not interpret the response as the specified class
     */
    public <T> RESTResult<T> get(String path, Class<T> clazz, Map<String, String> params) throws NetException, IOException {
        HTTPResult res = get(path, params);
        return new RESTResult<T>(res, clazz, errorDecoder);
    }

    /**
     * Using DELETE to invoke the RESTful API.
     * 
     * @param <T> the response class type
     * @param path the API URL relative path
     * @param clazz the response class type
     * @param params the request parameters
     * @return the REST result
     * @throws NetException if network related error occurred
     * @throws IOException if can not interpret the response as the specified class
     */
    public <T> RESTResult<T> delete(String path, Class<T> clazz, Map<String, String> params) throws NetException, IOException {
        HTTPResult res = delete(path, params);
        return new RESTResult<T>(res, clazz, errorDecoder);
    }

    /**
     * Using POST to invoke the RESTful API.
     * 
     * @param <T> the response class type
     * @param path the API URL relative path
     * @param clazz the response class type
     * @param requestBody the request body to be posted to server
     * @return the REST result
     * @throws NetException if network related error occurred
     * @throws IOException if can not interpret the response as the specified class
     */
    public <T> RESTResult<T> post(String path, Class<T> clazz, Object requestBody) throws NetException, IOException {
        HTTPResult res = post(path, obj2json(requestBody));
        return new RESTResult<T>(res, clazz, errorDecoder);
    }

    /**
     * Using PUT to invoke the RESTful API.
     * 
     * @param <T> the response class type
     * @param path the API URL relative path
     * @param clazz the response class type
     * @param requestBody the request body to be posted to server
     * @return the REST result
     * @throws NetException if network related error occurred
     * @throws IOException if can not interpret the response as the specified class
     */
    public <T> RESTResult<T> put(String path, Class<T> clazz, Object requestBody) throws NetException, IOException {
        HTTPResult res = put(path, obj2json(requestBody));
        return new RESTResult<T>(res, clazz, errorDecoder);
    }

    /**
     * Convert the specified object into JSON string.
     * 
     * @param o the object
     * @return the JSON string
     * @throws IOException if failed to generate JSON string
     */
    public String obj2json(Object o) throws IOException {
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            return o.toString();
        } else {
            return JacksonUtil.obj2Str(o);
        }
    }

}
