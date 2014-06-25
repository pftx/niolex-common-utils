/**
 * FileUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.niolex.commons.stream.StreamUtil;
import org.apache.niolex.commons.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get(Set) file contents from(to) local disk.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-9-13$
 */
public abstract class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);


    /**
     * Store binary data into local disk.<br>
     * Shortcut of {@link #setBinaryFileContentToFileSystem(String, byte[])}
     *
     * @param pathname the file path name
     * @param raw the data to store
     * @return true if file store success
     */
    public static boolean setBin(String pathname, byte[] raw) {
        return setBinaryFileContentToFileSystem(pathname, raw);
    }

    /**
     * Store binary data into local disk.
     *
     * @param pathname the file path name
     * @param raw the data to store
     * @return true if file store success
     */
    public static boolean setBinaryFileContentToFileSystem(String pathname, byte[] raw) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(pathname);
            out.write(raw);
            out.flush();
            return true;
        } catch (IOException e) {
            LOG.warn("Error occured while store content to file [{}] reason - {}", pathname, e.toString());
            return false;
        } finally {
            StreamUtil.closeStream(out);
        }
    }

    /**
     * Store the string into local file system.<br>
     * Shortcut of {@link #setCharacterFileContentToFileSystem(String, String, Charset)}
     *
     * @param pathname the file path and name
     * @param content the string to store
     * @param charset the charset to use
     * @return true if file store success
     */
    public static boolean setStr(String pathname, String content, Charset charset) {
        return setCharacterFileContentToFileSystem(pathname, content, charset);
    }

    /**
     * Store the string into local file system.
     *
     * @param pathname the file path and name
     * @param content the string to store
     * @param charset the charset to use
     * @return true if file store success
     */
    public static boolean setCharacterFileContentToFileSystem(String pathname, String content, Charset charset) {
        return setBinaryFileContentToFileSystem(pathname, content.getBytes(charset));
    }

    /**
     * Read the file content into a byte array. The file must less than 10MB.<br>
     * Shortcut of {@link #getBinaryFileContentFromFileSystem(String)}
     *
     * @param pathname
     *            The file path and file name.
     * @return The file content as byte array.
     * @throws IllegalStateException If file larger than 10 MB.
     */
    public static byte[] getBin(String pathname) {
        return getBinaryFileContentFromFileSystem(pathname);
    }

    /**
     * Read the file content into a byte array. The file must less than 10MB.
     *
     * @param pathname
     *            The file path and file name.
     * @return The file content as byte array.
     * @throws IllegalStateException If file larger than 10 MB.
     */
    public static byte[] getBinaryFileContentFromFileSystem(String pathname) {
        return getBinaryFileContentFromFileSystem(pathname, Const.M * 10);
    }

    /**
     * Read the file content into a byte array.
     *
     * @param pathname
     *            The file path and file name.
     * @param maxSize The max file size.
     * @return The file content as byte array.
     * @throws IllegalStateException If file too large.
     */
    public static byte[] getBinaryFileContentFromFileSystem(String pathname, final int maxSize) {
        InputStream in = null;
        try {
            File file = new File(pathname);
            final int size = (int) file.length();
            if (size > maxSize) {
                throw new IllegalStateException("File too large. max " + maxSize + ", file size " + size);
            }
            byte[] raw = new byte[size];
            in = new FileInputStream(file);
            StreamUtil.readData(in, raw);
            return raw;
        } catch (IOException e) {
            LOG.warn("Error occured while read file [{}] from filesystem; reason - {}", pathname, e.toString());
            return null;
        } finally {
            StreamUtil.closeStream(in);
        }
    }

    /**
     * Read the file content into a byte array.
     *
     * @param pathname
     *            The file path and file name.
     * @return The file content as byte array, or null if encounter exception.
     */
    public static <T> byte[] getBinaryFileContentFromClassPath(String pathname, Class<T> cls) {
    	try {
    	    InputStream in = cls.getResourceAsStream(pathname);
    	    if (in != null) {
    	        ByteArrayOutputStream out = new ByteArrayOutputStream(10240);
    	        StreamUtil.transferAndClose(in, out, 4096);
    	        return out.toByteArray();
    	    }
    	} catch (Exception e) {
    		LOG.warn("Error occured while read file [{}] from class [{}]; reason - {}", pathname, cls, e.toString());
        }
        return null;
    }

    /**
     * Read The File Content into a string.<br>
     * Shortcut of {@link #getCharacterFileContentFromFileSystem(String, Charset)}
     *
     * @param pathname
     *            The file path and file name.
     * @param encoding
     *            The file encoding.
     * @return The file content as string.
     */
    public static String getStr(String pathname, Charset encoding) {
        return getCharacterFileContentFromFileSystem(pathname, encoding);
    }

    /**
     * Read The File Content into a string.
     *
     * @param pathname
     *            The file path and file name.
     * @param encoding
     *            The file encoding.
     * @return The file content as string.
     */
    public static String getCharacterFileContentFromFileSystem(String pathname, Charset encoding) {
        byte[] data = getBinaryFileContentFromFileSystem(pathname);
        return data == null ? null : new String(data, encoding);
    }

    /**
     * Read The File Content into a string.
     *
     * @param pathname
     *            The file path and file name.
     * @param encoding
     *            The file encoding.
     * @return The file content as string.
     */
    public static <T> String getCharacterFileContentFromClassPath(String pathname, Class<T> cls, Charset encoding) {
        byte[] data = getBinaryFileContentFromClassPath(pathname, cls);
        return data == null ? null : new String(data, encoding);
    }

}
