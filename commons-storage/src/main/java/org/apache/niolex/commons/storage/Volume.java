/**
 * Volume.java
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

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.niolex.commons.test.Check;

/**
 * One volume is a disk file, stores records in it.
 * The records are managed by page, and one page have 16K as the minimum size.
 * We store the page size as a meta data into the volume header.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-2
 */
public class Volume {

    /**
     * The volume file header size.
     */
    private static final int VOL_HEADER_SIZE = 4;

    /**
     * The volume file header mask.
     */
    private static final int VOL_HEADER_MASK = 0xff000000;

    /**
     * The volume file header mark.
     */
    private static final int VOL_HEADER_MARK = 0xb1000000;

    private final String fileName;
    private Page.Size pageSize;
    private RandomAccessFile file;
    private boolean initialized;

    /**
     * Construct a new volume with this file.
     *
     * @param fileName the file name
     */
    public Volume(String fileName) {
        super();
        this.fileName = fileName;
    }

    /**
     * Initialize this volume with file header.
     *
     * @throws IOException
     */
    public synchronized void initialize() throws IOException {
        Check.isTrue(!initialized);
        file = new RandomAccessFile(fileName, "rwd");
        if (file.length() > 0) {
            readHeader();
        } else {
            throw new IllegalStateException("Please init the volume with page size.");
        }
    }

    /**
     * Read file header.
     */
    private void readHeader() throws IOException {
        int header = file.readInt();
        Check.isTrue((header & VOL_HEADER_MASK) == VOL_HEADER_MARK,
                "This is not a valid volume file.");
        pageSize = Page.Size.decode(header & 0xf);
        initialized = true;
    }

    /**
     * Initialize this volume with page size.
     *
     * @param pageSize the page size
     * @throws IOException
     */
    public synchronized void initialize(Page.Size pageSize) throws IOException {
        Check.isTrue(!initialized);
        file = new RandomAccessFile(fileName, "rwd");
        if (file.length() > 0) {
            readHeader();
        } else {
            writeHeader(pageSize);
        }
    }

    /**
     * Write file header.
     *
     * @param s the page size
     */
    private void writeHeader(Page.Size s) throws IOException {
        int header = Page.Size.encode(s) | VOL_HEADER_MARK;
        file.writeInt(header);
    }

    /**
     * Close the internal file.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        file.close();
        initialized = false;
    }

    /**
     * Get the page start with this address.
     *
     * @param address the page address
     * @return the page
     * @throws IOException
     */
    public Page getPage(int address) throws IOException {
        return getPage(new Page(pageSize, address));
    }

    /**
     * Get the page start with this address.
     *
     * @param p the page contains the address
     * @return the page
     * @throws IOException
     */
    public synchronized Page getPage(Page p) throws IOException {
        Check.isTrue(initialized);
        final int addr = p.getAddress() + VOL_HEADER_SIZE;
        if (file.getFilePointer() != addr) {
            file.seek(addr);
        }
        byte[] data = p.getBuf();
        int dataPos = 0, length = data.length;
        int count = 0;
        while ((count = file.read(data, dataPos, length - dataPos)) >= 0) {
            dataPos += count;
            if (dataPos == length) {
                break;
            }
        }
        if (dataPos != length) {
            throw new EOFException("End of file reached, pos - " + file.getFilePointer());
        }
        p.setFileName(fileName);
        return p;
    }

    /**
     * Write the page data into disk.
     *
     * @param p the page to be written
     * @throws IOException
     */
    public synchronized void setPage(Page p) throws IOException {
        Check.isTrue(p.getSize() == pageSize);
        Check.isTrue(initialized);
        final int addr = p.getAddress() + VOL_HEADER_SIZE;
        if (file.getFilePointer() != addr) {
            file.seek(addr);
        }
        file.write(p.getBuf());
    }

}
