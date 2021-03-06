/**
 * CoverClassTest.java
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
package org.apache.niolex.commons.codec;

import org.apache.niolex.commons.coder.KeyUtil;
import org.apache.niolex.commons.compress.GZipUtil;
import org.apache.niolex.commons.compress.ZLibUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.internal.IgnoreException;
import org.apache.niolex.commons.internal.Synchronized;
import org.apache.niolex.commons.net.DownloadUtil;
import org.apache.niolex.commons.util.Runner;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-12
 */
public class CoverClassTest {

    @Test
    public void testCoverAll() {
        new Base16Util(){};
        new Base64Util(){};
        new IntegerUtil(){};
        new MD5Util(){};
        new SHAUtil(){};
        new CipherUtil(){};
        new RSAHelper(){};
        new RSAUtil(){};
        // --
        new Runner(){};
        new KeyUtil(){};
        new FileUtil(){};
        // --
        new IgnoreException();
        new Synchronized();
        // --
        new ZLibUtil(){};
        new GZipUtil(){};
        new ZLibUtil(){};
        new FileUtil(){};
        // --
        new DownloadUtil(){};
        System.out.println("All covered.");
    }

}
