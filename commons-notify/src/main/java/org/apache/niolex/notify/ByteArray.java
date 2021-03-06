/**
 * ByteArray.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.notify;

import java.util.Arrays;

/**
 * Encapsulate raw byte array, to support use as hash map key. Implement {@link #hashCode()}
 * and {@link #equals(Object)} inside.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public class ByteArray {

    public final byte[] array;
    public final int hashCode;

    /**
     * The only constructor.
     *
     * @param array the byte array to be encapsulated
     */
    public ByteArray(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The parameter should not be null!");
        }
        this.array = array;
        this.hashCode = Arrays.hashCode(array);
    }

    /**
     * Override super method
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Override super method
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteArray) {
            ByteArray other = (ByteArray)obj;
            return hashCode == other.hashCode && Arrays.equals(array, other.array);
        }
        return false;
    }

}
