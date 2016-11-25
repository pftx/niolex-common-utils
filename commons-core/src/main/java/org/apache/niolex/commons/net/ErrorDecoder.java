/**
 * ErrorDecoder.java
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
import org.apache.niolex.commons.net.RESTException.ErrorInfo;

/**
 * The interface used by {@link RESTClient} to decode error informations returned by RESTful server.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Nov 24, 2016
 */
public interface ErrorDecoder {

    /**
     * Decode the response body into an exception.
     * 
     * @param respCode the HTTP response code
     * @param respBody the HTTP response body
     * @return the decoded exception
     * @throws IOException if I/O related error occurred
     */
    public Exception decode(int respCode, byte[] respBody) throws IOException;

    /**
     * The default error decoder suitable for decode Spring Boot generated error info.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 3.0.1
     * @since Nov 24, 2016
     */
    public static class RESTDecoder implements ErrorDecoder {

        /**
         * The global instance to be used by all REST clients.
         */
        public static final RESTDecoder INSTANCE = new RESTDecoder();

        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.commons.net.ErrorDecoder#decode(int, byte[])
         */
        @Override
        public Exception decode(int respCode, byte[] respBody) throws IOException {
            ErrorInfo errorInfo = JacksonUtil.bin2Obj(respBody, ErrorInfo.class);
            return new RESTException(errorInfo);
        }

    }

}
