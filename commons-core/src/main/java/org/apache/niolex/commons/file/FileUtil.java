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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.niolex.commons.stream.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get file content from local disk.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-9-13$
 *
 */
public abstract class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Read The File Content into a string.
     *
     * @param pathname
     *            The file path and file name.
     * @param encoding
     *            The file encoding.
     * @return The file content as string.
     */
    public static String getCharacterFileContentFromFileSystem(String pathname, String encoding) {
        String result = "";
        try {
            result = new String(getBinaryFileContentFromFileSystem(pathname), encoding);
        } catch (Exception e) {
            LOG.error("Error occured while format the file content into String - " + e.toString());
        }
        return result;
    }

    /**
     * Read the file content into a byte array.
     *
     * @param pathname
     *            The file path and file name.
     * @return The file content as byte array.
     */
    public static byte[] getBinaryFileContentFromFileSystem(String pathname) {
        InputStream in = null;
        try {
            File file = new File(pathname);
            byte[] raw = new byte[(int) file.length()];
            in = new FileInputStream(file);
            in.read(raw);
            return raw;
        } catch (Exception e) {
            LOG.warn("Error occured while read file [" + pathname + "] message - " + e.toString());
        } finally {
        	StreamUtil.closeStream(in);
        }
        return null;
    }

    public static boolean setCharacterFileContentToFileSystem(String pathname, String content, String charsetName) {
        try {
            return setBinaryFileContentToFileSystem(pathname, content.getBytes(charsetName));
        } catch (Exception e) {
            LOG.error("Error occured while store character content to file - " + e.toString());
        }
        return false;
    }

    public static boolean setBinaryFileContentToFileSystem(String pathname, byte[] raw) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(pathname);
            out.write(raw);
            out.flush();
            return true;
        } catch (Exception e) {
            LOG.warn("Error occured while store content to file [" + pathname + "] message - " + e.toString());
        } finally {
        	StreamUtil.closeStream(out);
        }
        return false;
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
    public static <T> String getCharacterFileContentFromClassPath(String pathname, Class<T> cls, String encoding) {
        String result = "";
        try {
            result = new String(getBinaryFileContentFromClassPath(pathname, cls), encoding);
        } catch (Exception e) {
            LOG.error("Error occured while format the file content into String - " + e.toString());
        }
        return result;
    }

    /**
     * Read the file content into a byte array.
     *
     * @param pathname
     *            The file path and file name.
     * @return The file content as byte array.
     */
    public static <T> byte[] getBinaryFileContentFromClassPath(String pathname, Class<T> cls) {
    	String apth = cls.getResource(pathname).toExternalForm().substring(6);
        return getBinaryFileContentFromFileSystem(apth);
    }


    /**
     * Make Directories (including parent directories) if not exist.
     *
     * @param pathname
     * @return true if Directory exist or successfully created, false otherwise.
     */
    public static final boolean mkdirsIfAbsent(String pathname) {
        File file = new File(pathname);
        if (!file.exists()) {
        	return file.mkdirs();
        }
        return true;
    }
}
