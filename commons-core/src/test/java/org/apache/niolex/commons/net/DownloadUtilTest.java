package org.apache.niolex.commons.net;

import static org.apache.niolex.commons.net.DownloadUtil.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.niolex.commons.net.NetException;
import org.apache.niolex.commons.net.DownloadUtil;
import org.apache.niolex.commons.net.NetException.ExCode;
import org.junit.Assert;
import org.junit.Test;

public class DownloadUtilTest {

	@Test
	public final void testDownloadFileNormal() throws Exception, Throwable {
		byte[] con = downloadFile("http://mat1.gtimg.com/www/mb/images/nloginBg110617.jpg",
						10000, 20000, 1230000, false);
		System.out.println("SL " + con.length);
		Assert.assertTrue(62000 < con.length);
		Assert.assertTrue(70000 > con.length);
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

	@Test
	public final void testDownloadFileOK() throws Exception, Throwable {
		byte[] con = downloadFile("http://file.ipinyou.com.cn/material/1336702985302-b/index.html#2288|17680|1336702985302",
						10000, 10000, 10000, false);
		Assert.assertEquals(7299, con.length);
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

    @Test
    public void testIsTextFileType() {
        assertTrue(DownloadUtil.isTextFileType("local/txt"));
        assertTrue(DownloadUtil.isTextFileType("remote/xml"));
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
            assertEquals("INVALID_SERVER_RESPONSE: File http://dd.ku.cn invalid response [Msg 303]", e.getMessage());
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
            assertEquals("INVALID_SERVER_RESPONSE: File http://dd.ku.cn invalid response [Msg 403]", e.getMessage());
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testValidateContentLength() throws Exception {
        validateContentLength("not yet implemented", 200, 210);
        validateContentLength("not yet implemented", -1, 210);
        validateContentLength("not yet implemented", 210, 210);
    }

    @Test
    public void testValidateContentLengthTooSmall() {
        try {
            validateContentLength("not yet implemented", 9, 210);
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
            validateContentLength("not yet implemented", 0, 210);
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
            validateContentLength("not yet implemented", 211, 210);
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
            validateContentLength("not yet implemented", 21100, 234);
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.FILE_TOO_LARGE);
            assertEquals("FILE_TOO_LARGE: File not yet implemented content size [21100], max allowed [234] too large; download stoped.", e.getMessage());
            return;
        }
        assertTrue(false);
    }

}
