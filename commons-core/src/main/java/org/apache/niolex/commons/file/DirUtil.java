/**
 * DirUtil.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.commons.file;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Directory related operations.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-22
 */
public class DirUtil {

    /**
     * Make Directories (including parent directories) if not exist.
     *
     * @param pathname the file path name
     * @return true if Directory exist or successfully created, false otherwise.
     */
    public static final boolean mkdirsIfAbsent(String pathname) {
        File file = new File(pathname);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * Check whether the pathname is a directory or not.
     *
     * @param pathname the file path name
     * @return true if it is a Directory, false if it's not or this pathname not exist.
     */
    public static final boolean isDir(String pathname) {
        File file = new File(pathname);
        if (!file.exists()) {
            return false;
        } else {
            return file.isDirectory();
        }
    }

    /**
     * Delete the directory or file represented by this pathname.
     *
     * @param pathname the pathname of the directory or file
     * @param recursive whether we should recursively delete children.
     * @return true if deleted or file not exist, false otherwise
     */
    public static final boolean delete(String pathname, boolean recursive) {
        File file = new File(pathname);
        return delete(file, recursive);
    }

    /**
     * Delete the directory or file represented by this file.
     *
     * @param file the file to be deleted
     * @param recursive whether we should recursively delete children.
     * @return true if deleted or file not exist, false otherwise
     */
    public static final boolean delete(File file, boolean recursive) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                return file.delete();
            }
            if (recursive) {
                for (File f: files) {
                    delete(f, true);
                }
                return file.delete();
            } else {
                return false;
            }
        } else {
            return file.delete();
        }
    }

}
