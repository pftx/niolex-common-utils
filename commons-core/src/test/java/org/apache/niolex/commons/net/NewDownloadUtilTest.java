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

import static org.apache.niolex.commons.net.DownloadUtil.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.net.NetException;
import org.apache.niolex.commons.net.DownloadUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class NewDownloadUtilTest {

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

    final static String URL = "http://service.baidu.com/static/images/nav.jpg";
    final static String FTP = "ftp://ftp:ftp@ftp.speed.hinet.net/test_001m.zip";
    final static String SMALL = NewDownloadUtilTest.class.getResource("Small.txt").toExternalForm();
    final static String JAR = Logger.class.getResource("Logger.class").toExternalForm();

    @Test(expected=NetException.class)
    public void testUnusualDownloadSmallFile() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[5]);
        try {
            unusualDownload(URL, in, 512, true);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_SMALL);
            throw e;
        }
    }

    @Test(expected=NetException.class)
    public void testUnusualDownloadLargeFile() throws Exception {
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("nav.jpg.txt", getClass());
        InputStream in = new ByteArrayInputStream(local);
        try {
            unusualDownload(URL, in, 512, true);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_LARGE);
            throw e;
        }
    }

    @Test
    public void testDownload() throws NetException {
        byte[] data = downloadFile(URL);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("nav.jpg.txt", getClass());
        assertArrayEquals(local, data);
    }

    @Test
    public void testDownloadJar() throws NetException {
        byte[] data = downloadFile(JAR);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("Logger.class", Logger.class);
        assertArrayEquals(local, data);
    }

    @Test
    public void testDownloadFtp() throws NetException {
        if (SystemUtil.getSystemProperty("download.ftp") != null) {
            return;
        }
        byte[] data = downloadFile(FTP);
        assertEquals(1049902, data.length);
    }

    @Test(expected=NetException.class)
    public void testDownloadTooLarge() throws NetException {
        try {
            downloadFile(URL, 54495);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_LARGE);
            throw e;
        }
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
            assertEquals(e.getCode(), NetException.ExCode.INVALID_SERVER_RESPONSE);
            throw e;
        }
    }

    @Test
    public void testDownloadFile() throws NetException {
        byte[] data = downloadFile("http://code.jquery.com/jquery-1.10.1.min.js", 93064);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("jquery-1.10.1.min.js.txt", getClass());
        assertArrayEquals(local, data);
    }

    @Test
    public void testUseCache() {
        byte[] buf1 = getByteBuffer(true);
        byte[] buf2 = getByteBuffer(true);
        byte[] buf3 = getByteBuffer(false);
        assertTrue(buf1 == buf2);
        assertTrue(buf1 != buf3);
    }

    @Test
    public final void testIsTextFileTypeBlank() throws Exception, Throwable {
        assertFalse(DownloadUtil.isTextFileType(""));
        assertFalse(DownloadUtil.isTextFileType(null));
        assertFalse(DownloadUtil.isTextFileType("  "));
    }

    @Test
    public final void testIsTextFileType() throws Exception, Throwable {
        assertFalse(DownloadUtil.isTextFileType("dijfewaoifjaw"));
        assertTrue(DownloadUtil.isTextFileType("/hone/text"));
        assertTrue(DownloadUtil.isTextFileType("jaxa/html"));
        assertTrue(DownloadUtil.isTextFileType("application/json"));
    }

}
