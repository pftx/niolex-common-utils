package org.apache.niolex.commons.net;

import static org.apache.niolex.commons.net.DownloadUtil.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.net.NetException;
import org.apache.niolex.commons.net.NetException.ExCode;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;
import org.apache.niolex.commons.util.Const;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

public class DownloadUtilTest {

    static StopWatch sw = new StopWatch(10);
    private Stop start;

    @BeforeClass
    public static void setUP() {
        sw.begin(true);
    }

    @AfterClass
    public static void summary() {
        sw.done();
        sw.print();
    }

    @Before
    public void before() {
        start = sw.start();
    }

    @After
    public void after() {
        start.stop();
    }

	@Test
	public final void testDownloadFileNormal() throws Exception, Throwable {
	    if (SystemUtil.defined("download", "download.http")) return;
		byte[] con = downloadFile("http://img01.51jobcdn.com/im/2009/logo/logo2009.gif");
		System.out.println("SL " + con.length);
		Assert.assertTrue(2800 < con.length);
		Assert.assertTrue(2900 > con.length);
	}

	@Test
	public final void testDownloadFileOK() throws Exception, Throwable {
	    if (SystemUtil.defined("download", "download.http")) return;
	    byte[] con = downloadFile("http://file.ipinyou.com.cn/material/1336702985302-b/index.html#2288|17680|1336702985302",
	            10000, 10000, 10000, false);
	    Assert.assertEquals(7299, con.length);
	}

	final static String JAR = Logger.class.getResource("Logger.class").toExternalForm();

	@Test
	public final void testDownloadJarOK() throws Exception, Throwable {
	    byte[] data = downloadFile(JAR, 50 * Const.K);
        byte[] local = FileUtil.getBinaryFileContentFromClassPath("Logger.class", Logger.class);
        assertArrayEquals(local, data);
	}

	@Test(expected=NetException.class)
	public final void testDownloadFileTooFast() throws Exception, Throwable {
		try {
			downloadFile("http://search.maven.org/#search%7Cga%7C1%7Cprotobuf-java",
							1000, 50, 7990180, false);
		} catch (NetException et) {
			System.out.println("MSG " + et.getMessage());
			assertEquals(et.getCode(), NetException.ExCode.IOEXCEPTION);
			throw et;
		}
	}

	@Test(expected=NetException.class)
	public final void testDownloadFileInvalidServerResponse() throws Exception, Throwable {
		try {
			downloadFile("http://www.renren.com/go/act/sale/8wangyi190x300.php?refpos=",
					10000, 10000, 1000, false);
		} catch (NetException et) {
			assertEquals(et.getCode(), NetException.ExCode.INVALID_SERVER_RESPONSE);
			throw et;
		}
	}
	
	@Test(expected=NetException.class)
	public final void testDownloadFile404() throws Exception, Throwable {
	    try {
	        downloadFile("https://floatingsun.net/articles/thrift-vs-protocol-buffers",
	                10000, 10000, 1000, false);
	    } catch (NetException et) {
	        assertEquals(et.getCode(), NetException.ExCode.IOEXCEPTION);
	        throw et;
	    }
	}

	@Test(expected=NetException.class)
	public final void testDownloadFileTooLarge() throws NetException {
	    try {
			downloadFile("http://jebe.xnimg.cn/20120428/10/936d29a4-7e8c-46cb-962e-cefb1b72b93b.jpg",
					10000, 10000, 1000, false);
		} catch (NetException et) {
			assertEquals(et.getCode(), NetException.ExCode.FILE_TOO_LARGE);
			throw et;
		}
	}

	// ------------------------------------------------------------------------------------------
	// TEST OTHER FUNCTIONS
	// ------------------------------------------------------------------------------------------

	class InternalInputStream extends ByteArrayInputStream {
	    final int size;
	    int cnt;

        /**
         * Constructor
         */
        public InternalInputStream(int size) {
            super(new byte[1]);
            this.size = size;
            this.cnt = 0;
        }

        /**
         * This is the override of super method.
         * @see java.io.ByteArrayInputStream#read(byte[], int, int)
         */
        @Override
        public synchronized int read(byte[] b, int off, int len) {
            if (cnt >= size) return -1;
            cnt += len;
            return cnt < size ? len : size + len - cnt;
        }

	}

    @Test
    public void testUnusualDownload() throws Exception {
        byte[] abc = unusualDownload(new InternalInputStream(34), 1, 55, "aa/bb", null);
        assertEquals(abc.length, 34);
    }

    @Test(expected=NetException.class)
    public void testUnusualDownloadSmallFile() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[5]);
        try {
            unusualDownload(in, 10, 512, "aa/bb", true);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_SMALL);
            throw e;
        }
    }

    @Test(expected=NetException.class)
    public void testUnusualDownloadTooLarge() throws Exception {
        try {
            byte[] abc = unusualDownload(new InternalInputStream(9340), 1, 5055, "aa/bb", false);
            assertEquals(abc.length, 34);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_LARGE);
            throw e;
        }
    }

    @Test(expected=NetException.class)
    public void testUnusualDownloadSuperLarge() throws Exception {
        try {
            byte[] abc = unusualDownload(new InternalInputStream(934000), 3, 17 * Const.K, "aa/bb", false);
            assertEquals(abc.length, 34);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.FILE_TOO_LARGE);
            throw e;
        }
    }

    @Test
    public void testUnusualDownloadLargeButWeWant() throws Exception {
        final int size = 33 * Const.K;
        byte[] abc = unusualDownload(new InternalInputStream(size), 5, size, "aa/bb", false);
        assertEquals(abc.length, size);
    }

    class InternalHttpURLConnection extends HttpURLConnection {

        /**
         * Constructor
         * @param u
         */
        protected InternalHttpURLConnection(int code) {
            super(null);
            this.responseCode = code;
            this.responseMessage = "Msg " + code;
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
        }
    }

    @Test
    public void testValidateHttpCode200() throws Exception {
        HttpURLConnection con = new InternalHttpURLConnection(200);
        validateHttpCode("not yet implemented", con);
    }

    @Test
    public void testValidateHttpCode203() throws Exception {
        HttpURLConnection con = new InternalHttpURLConnection(203);
        validateHttpCode("not yet implemented", con);
    }

    @Test
    public void testValidateHttpCode301() throws Exception {
        HttpURLConnection con = new InternalHttpURLConnection(301);
        validateHttpCode("not yet implemented", con);
    }

    @Test
    public void testValidateHttpCode302() throws Exception {
        HttpURLConnection con = new InternalHttpURLConnection(302);
        validateHttpCode("not yet implemented", con);
    }

    @Test
    public void testValidateHttpCode303() throws Exception {
        HttpURLConnection con = new InternalHttpURLConnection(303);
        try {
            validateHttpCode("http://dd.ku.cn", con);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.INVALID_SERVER_RESPONSE);
            assertEquals("INVALID_SERVER_RESPONSE: File http://dd.ku.cn invalid response 303 [Msg 303]", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateHttpCode403() throws IOException {
        HttpURLConnection con = new InternalHttpURLConnection(403);
        try {
            validateHttpCode("http://dd.ku.cn", con);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.INVALID_SERVER_RESPONSE);
            assertEquals("INVALID_SERVER_RESPONSE: File http://dd.ku.cn invalid response 403 [Msg 403]", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateContentLength() throws Exception {
        validateContentLength("not yet implemented", 200, 5, 210);
        validateContentLength("not yet implemented", -1, 5, 210);
        validateContentLength("not yet implemented", 210, 5, 210);
    }

    @Test
    public void testValidateContentLengthTooSmall() {
        try {
            validateContentLength("not yet implemented", 9, 10, 210);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.FILE_TOO_SMALL);
            assertEquals("FILE_TOO_SMALL: File not yet implemented content size [9] too small, it indicates error.", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateContentLengthTooSmall2() {
        try {
            validateContentLength("not yet implemented", 0, 5, 210);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.FILE_TOO_SMALL);
            assertEquals("FILE_TOO_SMALL: File not yet implemented content size [0] too small, it indicates error.", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateContentLengthTooLarge() {
        try {
            validateContentLength("not yet implemented", 211, 5, 210);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.FILE_TOO_LARGE);
            assertEquals("FILE_TOO_LARGE: File not yet implemented content size [211], max allowed [210] too large; download stoped.", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateContentLengthTooLarge2() {
        try {
            validateContentLength("not yet implemented", 21100, 5, 234);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.FILE_TOO_LARGE);
            assertEquals("FILE_TOO_LARGE: File not yet implemented content size [21100], max allowed [234] too large; download stoped.", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testSetUseThreadLocalCache() throws Exception {
        setUseThreadLocalCache(true);
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
    public void testIsTextFileType() {
        assertTrue(isTextFileType("local/txt"));
        assertTrue(isTextFileType("remote/xml"));
    }

    @Test
    public final void testIsTextFileTypeBlank() throws Exception, Throwable {
        assertFalse(isTextFileType(""));
        assertFalse(isTextFileType(null));
        assertFalse(isTextFileType("  "));
    }

    @Test
    public final void testIsTextFileTypeNega() throws Exception, Throwable {
        assertFalse(isTextFileType("dijfewaoifjaw"));
        assertTrue(isTextFileType("/hone/text"));
        assertTrue(isTextFileType("jaxa/html"));
        assertTrue(isTextFileType("application/json"));
    }

}
