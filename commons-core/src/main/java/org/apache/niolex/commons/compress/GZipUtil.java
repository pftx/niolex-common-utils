/**
 * ZipUtil.java
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
package org.apache.niolex.commons.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.internal.Finally;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.JavaType;

/**
 * 利用GZIP算法进行数据压缩的工具类。
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-3-28$
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
        Finally.writeAndClose(zout, data);
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
        Finally.transferAndClose(zin, out, 10240);
        return out.toByteArray();
    }

    /**
     * 压缩字符串
     *
     * @param str 准备压缩的字符串
     * @return 压缩后的二进制数组
     * @throws IOException
     */
    public static final byte[] compressString(String str) throws IOException {
        return compress(str.getBytes(StringUtil.UTF_8));
    }

    /**
     * 解压缩字符串
     *
     * @param data 准备解压的数据
     * @return 解压后的字符串
     * @throws IOException
     */
    public static final String decompressString(byte[] data) throws IOException {
        return new String(decompress(data), StringUtil.UTF_8);
    }

    /**
     * 压缩对象，使用Json作为内部表现形式
     *
     * @param value
     * @return 压缩后的数据
     * @throws IOException
     */
    public static byte[] compressObj(Object value) throws IOException {
        return compress(JacksonUtil.obj2bin(value));
    }

    /**
     * 解压缩对象，使用Json作为内部表现形式
     *
     * @param <T>
     * @param data
     * @param valueType
     * @return 解压后的对象
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T decompressObj(byte[] data, Class<T> valueType) throws JsonParseException, JsonMappingException,
            IOException {
        return JacksonUtil.bin2Obj(decompress(data), valueType);
    }

    /**
     * 解压缩对象，使用Json作为内部表现形式
     *
     * @param <T>
     * @param data
     * @param valueType
     * @return 解压后的对象
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T decompressObj(byte[] data, JavaType valueType) throws JsonParseException, JsonMappingException,
            IOException {
        return JacksonUtil.bin2Obj(decompress(data), valueType);
    }

}
