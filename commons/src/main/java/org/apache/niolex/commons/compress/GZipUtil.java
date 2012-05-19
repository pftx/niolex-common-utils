/**
 * ZipUtil.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.JavaType;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-3-28$
 * 
 */
public abstract class GZipUtil {

    /**
     * 压缩
     * 
     * @param data
     *            待压缩数据
     * @return byte[] 压缩后的数据
     * @throws IOException
     */
    public static final byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream zout = new GZIPOutputStream(out);
        try {
            zout.write(data);
        } finally {
            zout.close();
        }
        return out.toByteArray();
    }

    /**
     * 解压缩
     * 
     * @param data
     *            待压缩的数据
     * @return byte[] 解压缩后的数据
     * @throws IOException
     */
    public static final byte[] decompress(byte[] data) throws IOException {
        GZIPInputStream zin = new GZIPInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            data = new byte[10240];
            int len;
            while ((len = zin.read(data)) != -1) {
                out.write(data, 0, len);
            }
            return out.toByteArray();
        } finally {
            zin.close();
            out.close();
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
        GZIPInputStream zin = new GZIPInputStream(new ByteArrayInputStream(data));
        try {
            return JacksonUtil.readObj(zin, valueType);
        } finally {
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
        GZIPInputStream zin = new GZIPInputStream(new ByteArrayInputStream(data));
        try {
            return (T) JacksonUtil.readObj(zin, valueType);
        } finally {
            zin.close();
        }
    }

    /**
     * 压缩对象
     * 
     * @param value
     * @return
     * @throws IOException
     */
    public static byte[] writeObj(Object value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream zout = new GZIPOutputStream(out);
        try {
            JacksonUtil.writeObj(zout, value);
        } finally {
            zout.close();
        }
        return out.toByteArray();
    }
}
