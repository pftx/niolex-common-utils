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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.niolex.commons.file.FileChannelUtil;
import org.apache.niolex.commons.test.Check;

/**
 * One volume is a disk file, stores pages in it. One page have 16K as the minimum size.
 * We store the page size as a meta data into the volume header.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-2
 */
public class Volume {

    /**
     * The volume header size. Volume header is 32bit, used 7 bits for now.
     * xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx
     * ^^^^ The volume header magic code.
     *      ^^^ The page size of this volume.
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

    /**
     * The page size mask.
     */
    private static final int VOL_PAGE_SIZE_MASK = 0xe000000;

    /**
     * The page size shift bits.
     */
    private static final int VOL_PAGE_SIZE_SHIFT = 25;

    private final String fileName;
    private Page.Size pageSize;
    private RandomAccessFile file;
    private FileChannel channel;
    private boolean initialized;

    /**
     * Construct a new volume by the specified file name.
     *
     * @param fileName the file name
     */
    public Volume(String fileName) {
        super();
        this.fileName = fileName;
    }

    /**
     * Initialize this volume. If it's an old volume, read page size from file, else initialize a new volume
     * with the specified page size.
     *
     * @param pageSize the page size
     * @throws IOException if I/O error occurs
     */
    public synchronized void initialize(Page.Size pageSize) throws IOException {
        if (initialized) return;

        file = new RandomAccessFile(fileName, "rwd");
        channel = file.getChannel();

        if (file.length() > 4) {
            readHeader();
        } else {
            writeHeader(pageSize);
        }
    }

    /**
     * Initialize this volume, read volume header from the volume file. If it's a new volume, we will
     * throw {@link IllegalStateException}
     *
     * @throws IllegalStateException if it's a new volume
     * @throws IOException if I/O error occurs
     */
    public synchronized void initialize() throws IOException {
        if (initialized) return;

        file = new RandomAccessFile(fileName, "rwd");
        channel = file.getChannel();

        if (file.length() > 4) {
            readHeader();
        } else {
            throw new IllegalStateException("Please init the new volume with page size.");
        }
    }

    /**
     * Read volume file header from the volume.
     * 
     * @throws IOException if I/O error occurs
     */
    private void readHeader() throws IOException {
        int header = file.readInt();
        Check.isTrue((header & VOL_HEADER_MASK) == VOL_HEADER_MARK,
                "This is not a valid volume file.");

        int size = (header & VOL_PAGE_SIZE_MASK) >> VOL_PAGE_SIZE_SHIFT;
        pageSize = Page.Size.decode(size);
        initialized = true;
    }

    /**
     * Write volume file header to the volume.
     *
     * @param s the page size
     * @throws IOException if I/O error occurs
     */
    private void writeHeader(Page.Size s) throws IOException {
        int header = Page.Size.encode(s) << VOL_PAGE_SIZE_SHIFT | VOL_HEADER_MARK;
        file.writeInt(header);

        pageSize = s;
        initialized = true;
    }

    /**
     * Close the internal file of this volume.
     *
     * @throws IOException if I/O error occurs
     */
    public synchronized void close() throws IOException {
        if (file != null) {
            channel.close();
            file.close();
            channel = null;
            file = null;
        }

        initialized = false;
    }

    /**
     * Get the page which contains this address.
     * <br>
     * <b>This method is not synchronized, users must use some technique to avoid concurrent read
     * and write on the same page.</b>
     *
     * @param address the page address
     * @return the page with data read from the volume
     * @throws IOException if I/O error occurs
     */
    public Page readPage(long address) throws IOException {
        return readPage(new Page(pageSize, address));
    }

    /**
     * Get the page which contains this address.
     * <br>
     * <b>This method is not synchronized, users must use some technique to avoid concurrent read
     * and write on the same page.</b>
     *
     * @param p the page to be fulfilled with data from volume
     * @return the page with data read from the volume
     * @throws IOException if I/O error occurs
     */
    public Page readPage(Page p) throws IOException {
        Check.isTrue(initialized);
        final long addr = p.getAddress() + VOL_HEADER_SIZE;

        byte[] data = p.getBuf();
        Check.eq(data.length, pageSize.size(), "Invalid page size.");

        int dataPos = FileChannelUtil.readFromPosition(channel, ByteBuffer.wrap(data), addr);
        if (dataPos != data.length) {
            throw new EOFException("File maybe corrupted, pos - " + addr);
        }

        p.setFileName(fileName);
        return p;
    }

    /**
     * Write the page data into disk.
     * <br>
     * <b>This method is not synchronized, users must use some technique to avoid concurrent read
     * and write on the same page.</b>
     *
     * @param p the page to be written
     * @throws IOException if I/O error occurs
     */
    public void writePage(Page p) throws IOException {
        Check.isTrue(initialized);

        byte[] data = p.getBuf();
        Check.eq(data.length, pageSize.size(), "Invalid page size.");

        final long addr = p.getAddress() + VOL_HEADER_SIZE;
        int dataPos = FileChannelUtil.writeToPosition(channel, ByteBuffer.wrap(data), addr);
        if (dataPos != data.length) {
            throw new IOException("File maybe corrupted, pos - " + addr);
        }
    }

    /**
     * @return the page size of this volume
     */
    public Page.Size getPageSize() {
        return pageSize;
    }

}
