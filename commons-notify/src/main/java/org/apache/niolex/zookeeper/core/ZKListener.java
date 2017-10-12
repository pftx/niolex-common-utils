/**
 * ZKListener.java
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
package org.apache.niolex.zookeeper.core;

import java.util.List;

/**
 * The data or children change listener interface.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
public interface ZKListener {

    /**
     * Data changed in zookeeper.
     *
     * @param data the new data
     */
    public void onDataChange(byte[] data);

    /**
     * Children changed in zookeeper.
     *
     * @param list the new children list
     */
    public void onChildrenChange(List<String> list);

}
