/**
 * ZLibUtilTest.java
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

import java.io.IOException;

import junit.framework.Assert;

import org.apache.niolex.commons.compress.ZLibUtil;
import org.apache.niolex.commons.download.BioUtil;
import org.apache.niolex.commons.download.DownloadException;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-6-28$
 * 
 */
public class ZLibUtilTest {
    
    static byte[] data = null;
    static {
        try {
            data = BioUtil.downloadFile(ZLibUtilTest.class.getResource("/com/baidu/api/core/compress/Data.txt").toExternalForm());
        } catch (DownloadException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void doSmoke() throws IOException {
        String in = "我是中国人好高骛远、南辕北辙、缘木求鱼、刻舟求剑→亡羊补牢→鞍前马后、承上启下、与时俱进、荣辱与共！";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = ZLibUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = ZLibUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }
    
    @Test
    public void doSmoke2() throws IOException {
        String in = "中国是儒家,中国人是儒家思想,因为中国崇尚祖先的教化,保守,专制,封建,不可以向权威和上级提出挑战,而是要顺从,提倡循规蹈矩,老成温厚.";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = ZLibUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = ZLibUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }
    
    @Test
    public void doSmoke3() throws IOException {
        System.out.println("Original size => " + data.length);
        byte[] q = ZLibUtil.compress(data);
        System.out.println("Compressed size => " + q.length);
        Assert.assertEquals(data.length, 17991);
        Assert.assertEquals(q.length, 7521);
        
    }
}
