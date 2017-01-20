/**
 * SeriException.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.seri;

/**
 * The exception thrown when problems occurred in serialization or de-serialization.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-7
 */
public class SeriException extends RuntimeException {

    /**
     * This is the serial version uuid.
     */
    private static final long serialVersionUID = 4658800212413782384L;

    public SeriException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeriException(String message) {
        super(message);
    }

}
