/**
 * Record.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.commons.storage;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-2
 */
public class Record {

    public static enum Type {
        WHOLE, FIRST, MIDDLE, LAST;
    }

    public static final int MAX_SIZE = 0x3fffffff;

    private int length;
    private byte[] data;
    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }
    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }
    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

}
