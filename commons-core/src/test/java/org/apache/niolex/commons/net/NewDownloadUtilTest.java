/**
 * NewDownloadUtilTest.java
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
package org.apache.niolex.commons.net;

import static org.junit.Assert.*;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the unusual download, use FTP.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class NewDownloadUtilTest extends DownloadUtil {

    static StopWatch sw = new StopWatch(10);
    private Stop start;

    @BeforeClass
    public static void setUP() {
        sw.begin(false);
    }

    @AfterClass
    public static void summary() {
        sw.done();
        sw.print();
    }

    @Before
    public void before() {
        start = sw.start();
        setUseThreadLocalCache(true);
    }

    @After
    public void after() {
        start.stop();
    }

    final static String URL = "http://fe.bdimg.com/tangram/2.0.2.5.js";
    final static String FTP = "ftp://ftp.sjtu.edu.cn/centos/RPM-GPG-KEY-CentOS-7";
    final static String SMALL = NewDownloadUtilTest.class.getResource("Small.txt").toExternalForm();

    @Test
    public void testDownloadOK() throws NetException {
        byte[] data = downloadFile(URL);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("2.0.2.5.js.txt", getClass());
        assertArrayEquals(local, data);
    }

    @Test
    public void testDownloadFile() throws NetException {
        if (SystemUtil.defined("download", "download.http")) return;
        byte[] data = downloadFile("http://www.10086.cn/images/quickbg.gif", 9624);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("quickbg.gif.txt", getClass());
        assertArrayEquals(local, data);
    }

    @Test
    public void testDownloadFtp() throws NetException {
        if (SystemUtil.defined("download", "download.ftp")) return;
        byte[] data = downloadFile(FTP, 30000, 30000, 1049902, false);
        assertEquals(1690, data.length);
    }

    @Test(expected=NetException.class)
    public final void testDownloadTooSmall() throws Exception {
        try {
            downloadFile(SMALL, 100, 200, 123, true);
        } catch (NetException et) {
            assertEquals(et.getCode(), NetException.ExCode.FILE_TOO_SMALL);
            throw et;
        }
    }

    @Test(expected=NetException.class)
    public void testDownloadIOException() throws NetException {
        try {
            downloadFile("http://code.jquery.com/jquery-1.10.1.min.js", 100, 200, 93064, true);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.IOEXCEPTION);
            throw e;
        }
    }

    @Test(expected=NetException.class)
    public void testDownloadInvalidResponse() throws NetException {
        try {
            downloadFile("http://mirror.bit.edu.cn/archlinux/readme", 54495);
        } catch (NetException e) {
            assertEquals(NetException.ExCode.INVALID_SERVER_RESPONSE, e.getCode());
            throw e;
        }
    }

}
