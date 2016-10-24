/**
 * CoderException.java
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
package org.apache.niolex.commons.coder;

/**
 * The base exception thrown from the Coder framework.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.0
 * @since Oct 21, 2016
 */
public class CoderException extends RuntimeException {

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = -3531761449607920967L;

    public CoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoderException(String message) {
        super(message);
    }

}
