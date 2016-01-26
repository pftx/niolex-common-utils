/**
 * HTTPMethod.java
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

/**
 * The HTTP Method.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-1-26
 */
public enum HTTPMethod {
    GET(true), POST(false), HEAD(true), OPTIONS(true), PUT(true), DELETE(true), TRACE(true);

    private final boolean passParametersInURL;

    HTTPMethod(boolean passParametersInURL) {
        this.passParametersInURL = passParametersInURL;
    }

    public boolean passParametersInURL() {
        return passParametersInURL;
    }
}
