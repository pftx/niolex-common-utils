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

        /**
         * Create a new Size.
         *
         * @param size the int size
         */
        private Size(int size) {
            this.size = size;
        }

        /**
         * @return the int size
         */
        public int size() {
            return size;
        }

    }

    private String fileName;
    private int address;
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
    public Page(Size size, int address) {
        super();
        this.size = size;
        this.buf = new byte[size.size()];
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
    public int getAddress() {
        return address;
    }

    /**
     * Set the address of this page. The address will be aligned by the page size.
     *
     * @param address the address to set
     */
    public void setAddress(int address) {
        this.address = (address / size.size()) * size.size();
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
