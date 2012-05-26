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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-9-13$
 * 
 */
public abstract class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final int MATERIAL_SIZE = 1024 * 30;
    
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
            log.error("Error occured while format the file content into String - " + e.getMessage());
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
            log.warn("Error occured while read file [" + pathname + "] message - " + e.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ie) {
                log.info("Failed to close file " + pathname + " error message - " + ie.getMessage());
            }
        }
        return null;
    }
    
    public static boolean setCharacterFileContentToFileSystem(String pathname, String content, String charsetName) {
        try {
            return setBinaryFileContentToFileSystem(pathname, content.getBytes(charsetName));
        } catch (Exception e) {
            log.error("Error occured while format the file content into String - " + e.getMessage());
        }
        return false;
    }
    
    public static boolean setBinaryFileContentToFileSystem(String pathname, byte[] raw) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(pathname);
            out.write(raw);
            return true;
        } catch (Exception e) {
            log.warn("Error occured while read file [" + pathname + "] message - " + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ie) {
                log.info("Failed to close file " + pathname + " error message - " + ie.getMessage());
            }
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
    public static String getCharacterFileContentFromClassPath(String pathname, String encoding) {
        String result = "";
        try {
            result = new String(getBinaryFileContentFromClassPath(pathname), encoding);
        } catch (Exception e) {
            log.error("Error occured while format the file content into String - " + e.getMessage());
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
    public static byte[] getBinaryFileContentFromClassPath(String pathname) {
        InputStream in = null;
        try {
            byte[] raw = new byte[10240];
            in = FileUtil.class.getResourceAsStream(pathname);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(MATERIAL_SIZE);
            int count;
            while ((count = in.read(raw)) > 0) {
                bos.write(raw, 0, count);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            log.warn("Error occured while read file [" + pathname + "] message - " + e.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ie) {
                log.info("Failed to close file " + pathname + " error message - " + ie.getMessage());
            }
        }
        return null;
    }
    
    
}
