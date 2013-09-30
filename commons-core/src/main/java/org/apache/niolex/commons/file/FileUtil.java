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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.niolex.commons.internal.Finally;
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
            StreamUtil.closeStream(out);
            return true;
        } catch (Exception e) {
            LOG.warn("Error occured while store content to file [{}] reason - {}", pathname, e.toString());
            StreamUtil.closeStream(out);
            return false;
        }
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
            final int SIZE = (int) file.length();
            if (SIZE > maxSize) {
                throw new IllegalStateException("File too large. max " + maxSize + ", file size " + SIZE);
            }
            byte[] raw = new byte[SIZE];
            in = new FileInputStream(file);
            int k = 0;
            while (k != SIZE) {
                k += in.read(raw, k, SIZE - k);
            }
            return raw;
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            LOG.warn("Error occured while read file [{}] from filesystem; reason - {}", pathname, e.toString());
        } finally {
            StreamUtil.closeStream(in);
        }
        return null;
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
    	        Finally.transferAndClose(in, out, 4096);
    	        return out.toByteArray();
    	    }
    	} catch (Exception e) {
    		LOG.warn("Error occured while read file [{}] from class [{}]; reason - {}", pathname, cls, e.toString());
        }
        return null;
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
        if (data != null) {
            return new String(data, encoding);
        }
        return null;
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
        if (data != null) {
            return new String(data, encoding);
        }
        return null;
    }

}
