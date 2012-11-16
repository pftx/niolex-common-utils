package org.apache.niolex.commons.download;

import static org.junit.Assert.*;

import org.junit.Assert;

import org.junit.Test;

public class DownloadUtilTest {

	@Test
	public final void testGetClassPathResource() throws Exception, Throwable {
		String str = new String(DownloadUtil.getClassPathResource("../file/Data.txt", DownloadUtilTest.class),
				"utf-8");
		System.out.println("SL " + str.length());
		Assert.assertEquals(1783, str.length());
		assertTrue(str.startsWith("HOW-TOs, samples"));
	}

	@Test
	public final void testDownloadFileString() throws Exception, Throwable {
		byte[] con = DownloadUtil
				.downloadFile("http://mat1.gtimg.com/www/mb/images/nloginBg110617.jpg",
						10000, 20000, 1230000);
		System.out.println("SL " + con.length);
		Assert.assertEquals(69661, con.length);
	}

	@Test(expected=DownloadException.class)
	public final void testDownloadFileStringFast() throws Exception, Throwable {
		try {
			byte[] con = DownloadUtil
					.downloadFile("http://apache.etoak.com/tomcat/tomcat-6/v6.0.35/bin/apache-tomcat-6.0.35.zip",
							1000, 50, 7990180);
			System.out.println("SL " + con.length);
			Assert.assertEquals(136013, con.length);
		} catch (DownloadException et) {
			System.out.println("MSG " + et.getMessage());
			assertEquals(et.getCode(), DownloadException.ExCode.IOEXCEPTION);
			et = new DownloadException(et.getCode());
			et = new DownloadException(et.getCode(), et.toString(), et);

			throw et;
		}
	}

	@Test
	public final void testDownloadFileStringIntIntInt() throws Exception, Throwable {
		byte[] con = DownloadUtil
				.downloadFile("http://file.ipinyou.com.cn/material/1336702985302-b/index.html#2288|17680|1336702985302",
						10000, 10000, 10000);
		System.out.println("SL " + con.length);
		Assert.assertEquals(7299, con.length);
	}

	@Test(expected=DownloadException.class)
	public final void testDownloadFileString3() throws Exception, Throwable {
		try {
			byte[] con = DownloadUtil
					.downloadFile("http://www.renren.com/go/act/sale/8wangyi190x300.php?refpos=",
							10000, 10000, 1000);
			System.out.println("SL " + con.length);
			Assert.assertEquals(7299, con.length);
		} catch (DownloadException et) {
			assertEquals(et.getCode(), DownloadException.ExCode.INVALID_SERVER_RESPONSE);
			throw et;
		}
	}

	@Test(expected=DownloadException.class)
	public final void testDownloadFileString4() throws Exception, Throwable {
		try {
			byte[] con = DownloadUtil
					.downloadFile("http://jebe.xnimg.cn/20120428/10/936d29a4-7e8c-46cb-962e-cefb1b72b93b.jpg",
							10000, 10000, 1000);
			System.out.println("SL " + con.length);
			Assert.assertEquals(7299, con.length);
		} catch (DownloadException et) {
			assertEquals(et.getCode(), DownloadException.ExCode.FILE_TOO_LARGE);
			throw et;
		}
	}

	@Test
	public final void testIsTextFileType() throws Exception, Throwable {
		assertFalse(DownloadUtil.isTextFileType("dijfewaoifjaw"));
		assertTrue(DownloadUtil.isTextFileType("/hone/text"));
		assertTrue(DownloadUtil.isTextFileType("jaxa/html"));
	}

	@Test
	public void testDownloadUtil() throws Exception {
		assertTrue(DownloadUtil.isTextFileType("local/txt"));
		assertTrue(DownloadUtil.isTextFileType("remote/xml"));
	}

}
