/**
 * RESTResult.java
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

import org.apache.niolex.commons.compress.JacksonUtil;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The Java Bean to store the REST result.
 * 
 * @param <T> the REST response class type
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Aug 31, 2016
 */
public class RESTResult<T> {

    private final HTTPResult result;
    private final int respCode;
    private final T response;
    private final Exception error;

    /**
     * Construct a new RESTResult.
     * 
     * @param result the HTTP result
     * @param clazz the REST response class
     * @param errorDecoder the error decoder to be used
     * @throws IOException if tailed to parse the response JSON into the specified class type
     */
    public RESTResult(HTTPResult result, Class<T> clazz, ErrorDecoder errorDecoder) throws IOException {
        super();
        this.result = result;
        this.respCode = result.getRespCode();
        if (respCode >= 200 && respCode < 300) {
            this.response = JacksonUtil.bin2Obj(result.getRespBody(), clazz);
        } else {
            this.response = null;
        }

        if (respCode >= 400 && errorDecoder != null) {
            error = errorDecoder.decode(respCode, result.getRespBody());
        } else {
            error = null;
        }
    }

    /**
     * Construct a new RESTResult.
     * 
     * @param result the HTTP result
     * @param typeRef the REST response type reference
     * @param errorDecoder the error decoder to be used
     * @throws IOException if tailed to parse the response JSON into the specified class type
     */
    public RESTResult(HTTPResult result, TypeReference<T> typeRef, ErrorDecoder errorDecoder) throws IOException {
        super();
        this.result = result;
        this.respCode = result.getRespCode();
        if (respCode >= 200 && respCode < 300) {
            this.response = JacksonUtil.bin2Obj(result.getRespBody(), typeRef);
        } else {
            this.response = null;
        }

        if (respCode >= 400 && errorDecoder != null) {
            error = errorDecoder.decode(respCode, result.getRespBody());
        } else {
            error = null;
        }
    }

    /**
     * @return the HTTP result
     */
    public HTTPResult getResult() {
        return result;
    }

    /**
     * @return the HTTP response code
     */
    public int getRespCode() {
        return respCode;
    }

    /**
     * @return the REST response or null if error occurred.
     */
    public T getResponse() {
        return response;
    }

    /**
     * @return the exception returned from RESTful server if there is any.
     */
    public Exception getError() {
        return error;
    }

}
