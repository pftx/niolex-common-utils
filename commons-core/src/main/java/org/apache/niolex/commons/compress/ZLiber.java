/**
 * ZLiber.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.commons.compress;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import org.apache.niolex.commons.stream.StreamUtil;

/**
 * The compressor using ZLib as the compress method.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-8
 */
public class ZLiber extends AbstractCompressor {

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#compress(byte[])
     */
    @Override
    public byte[] compress(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream zout = new DeflaterOutputStream(out);
        StreamUtil.writeAndCloseIgnoreException(zout, data);
        return out.toByteArray();
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#decompress(byte[])
     */
    @Override
    public byte[] decompress(byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InflaterOutputStream zos = new InflaterOutputStream(bos);
        StreamUtil.writeAndCloseIgnoreException(zos, data);
        return bos.toByteArray();
    }

}
