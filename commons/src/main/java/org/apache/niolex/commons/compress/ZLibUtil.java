/**
 * ZLibUtils.java
 *
 * Copyright 2011 @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipInputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.JavaType;

/**
 * ZLib压缩工具
 * 
 * @author @author@ (@author-email@)
 * @version 1.0
 * @since 1.0
 */
public abstract class ZLibUtil {

    /**
     * 压缩
     * 
     * @param data
     *            待压缩数据
     * @return byte[] 压缩后的数据
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream zout = new DeflaterOutputStream(out);
        try {
            zout.write(data);
            zout.close();
            return out.toByteArray();
        } finally {
            zout.close();
            out.close();
        }
    }

    /**
     * 解压缩
     * 
     * @param data
     *            待压缩的数据
     * @return byte[] 解压缩后的数据
     * @throws IOException
     */
    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InflaterOutputStream zos = new InflaterOutputStream(bos);
        try {
            zos.write(data);
            zos.close();
            return bos.toByteArray();
        } finally {
            zos.close();
            bos.close();
        }
    }

    /**
     * 压缩字符串
     * 
     * @param str
     * @return
     * @throws IOException
     */
    public static final byte[] compressString(String str) throws IOException {
        byte[] data = str.getBytes("UTF-8");
        return compress(data);
    }

    /**
     * 解压缩字符串
     * 
     * @param data
     * @return
     * @throws IOException
     */
    public static final String decompressString(byte[] data) throws IOException {
        data = decompress(data);
        return new String(data, "UTF-8");
    }

    /**
     * 解压缩对象
     * 
     * @param <T>
     * @param data
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T readObj(byte[] data, Class<T> valueType) throws JsonParseException, JsonMappingException,
            IOException {
        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(data));
        try {
            zin.getNextEntry();
            return JacksonUtil.readObj(zin, valueType);
        } finally {
            zin.closeEntry();
            zin.close();
        }
    }

    /**
     * 解压缩对象
     * 
     * @param <T>
     * @param data
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(byte[] data, JavaType valueType) throws JsonParseException, JsonMappingException,
            IOException {
        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(data));
        try {
            zin.getNextEntry();
            return (T) JacksonUtil.readObj(zin, valueType);
        } finally {
            zin.closeEntry();
            zin.close();
        }
    }
}