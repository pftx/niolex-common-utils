/**
 * Page.java
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

import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.util.Const;

/**
 * Page is a lower level file management tool.
 * We read and write file in the unit of page.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-2
 */
public class Page {

    /**
     * The Page Size.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2014-1-2
     */
    public static enum Size implements Const {
        S16K(16 * K),
        S64K(64 * K),
        S256K(256 * K),
        S1M(1 * M),
        S4M(4 * M);

        /**
         * Decode int code into size.
         *
         * @param code the int code
         * @return the size
         */
        public static Size decode(int code) {
            switch (code) {
                case 1:
                    return S64K;
                case 2:
                    return S256K;
                case 3:
                    return S1M;
                case 4:
                    return S4M;
                default:
                case 0:
                    return S16K;
            }
        }

        /**
         * Encode size into int code.
         *
         * @param s the size
         * @return the int code
         */
        public static int encode(Size s) {
            switch (s) {
                case S64K:
                    return 1;
                case S256K:
                    return 2;
                case S1M:
                    return 3;
                case S4M:
                    return 4;
                case S16K:
                default:
                    return 0;
            }
        }

        private final int size;
        private final long sizeMask;

        /**
         * Create a new Size.
         *
         * @param size the int size
         */
        private Size(int size) {
            this.size = size;
            long t = size - 1;
            this.sizeMask = ~t;
        }

        /**
         * @return the int size
         */
        public int size() {
            return size;
        }

        /**
         * Mask the data address to the buffer head address.
         * 
         * @param addr the data address
         * @return the buffer head address
         */
        public long maskAddress(long addr) {
            return addr & sizeMask;
        }

    }

    private String fileName;
    private long address;
    private final Size size;
    private final byte[] buf;

    /**
     * Construct a new page.
     *
     * @param size the page size
     */
    public Page(Size size) {
        this(size, 0);
    }

    /**
     * Construct a new page with the specified size and address.
     *
     * @param size the page size
     * @param address the page address inside a volume
     */
    public Page(Size size, long address) {
        this(size, new byte[size.size()], address);
    }

    /**
     * Construct a new page with the specified size and buffer and address.
     *
     * @param size the page size
     * @param buffer the byte buffer used to store page data
     * @param address the page address inside a volume
     */
    public Page(Size size, byte[] buffer, long address) {
        super();
        Check.eq(size.size, buffer.length, "The buffer size not equals the page size.");
        this.size = size;
        this.buf = buffer;
        setAddress(address);
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the address
     */
    public long getAddress() {
        return address;
    }

    /**
     * Set the address of this page. The address will be aligned by the page size.
     *
     * @param address the address to set
     */
    public void setAddress(long address) {
        this.address = size.maskAddress(address);
    }

    /**
     * @return the page size
     */
    public Size getSize() {
        return size;
    }

    /**
     * @return the internal data buffer
     */
    public byte[] getBuf() {
        return buf;
    }

}
