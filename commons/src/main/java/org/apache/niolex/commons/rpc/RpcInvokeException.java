/**
 * NoConnectionException.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.rpc;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-15$
 * 
 */
public class RpcInvokeException extends RuntimeException {

    /**
     * long
     */
    private static final long serialVersionUID = -7943641579068156198L;

    /**
     * 
     */
    public RpcInvokeException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public RpcInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public RpcInvokeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public RpcInvokeException(Throwable cause) {
        super(cause);
    }
    
}
