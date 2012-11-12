/**
 * DownloadUtil.java
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
package org.apache.niolex.commons.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.niolex.commons.stream.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Download file from HTTP, FTP URL
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 *
 */
public abstract class DownloadUtil {
	private static final Logger LOG = LoggerFactory.getLogger(DownloadUtil.class);
	private static final ThreadLocal<byte[]> BUFFER_CACHE = new ThreadLocal<byte[]>();

	private static final int BUFFER_SIZE = 1024 * 16;
	private static final int MATERIAL_SIZE = 1024 * 30;
	private static final int CONNECT_TIMEOUT = 6000;
	private static final int READ_TIMEOUT = 6000;
	private static final int MAX_SIZE = 1024 * 10240;
	private static final int MIN_SIZE = 10;


	/**
	 * Get the file content from class path, relative to the place <code>cls</code> locates.
	 *
	 * @param res
	 * @param cls
	 * @return the download bytes
	 * @throws DownloadException
	 */
	public static <T> byte[] getClassPathResource(String res, Class<T> cls) throws DownloadException {
		return downloadFile(cls.getResource(res).toExternalForm());
	}

	/**
	 * Download the file pointed by the url and return the content as byte array.
	 *
	 * @param strUrl
	 *            The Url to be downloaded.
	 * @return The file content as byte array.
	 * @throws DownloadException
	 */
	public static byte[] downloadFile(String strUrl) throws DownloadException {
		return downloadFile(strUrl, CONNECT_TIMEOUT, READ_TIMEOUT, MAX_SIZE);
	}

	/**
	 * Download the file pointed by the url and return the content as byte array.
	 *
	 * @param strUrl
	 *            The Url to be downloaded.
	 * @param connectTimeout
	 *            Connect timeout in milliseconds.
	 * @param readTimeout
	 *            Read timeout in milliseconds.
	 * @param maxFileSize
	 *            Max file size in BYTE.
	 * @return The file content as byte array.
	 * @throws DownloadException
	 */
	public static byte[] downloadFile(String strUrl, int connectTimeout, int readTimeout, int maxFileSize)
			throws DownloadException {
		if (LOG.isDebugEnabled())
			LOG.debug("Start to download file [" + strUrl + "].");
		InputStream in = null;
		try {
			URL url = new URL(strUrl); // 得到文件的URL地址
			URLConnection ucon = url.openConnection();
			ucon.setConnectTimeout(connectTimeout);
			ucon.setReadTimeout(readTimeout);
			ucon.connect();
			if (ucon.getContentLength() > maxFileSize) {
				String msg = "File " + strUrl + " size [" + ucon.getContentLength() + "] too large, download stoped.";
				throw new DownloadException(DownloadException.ExCode.FILE_TOO_LARGE, msg);
			}
			if (ucon instanceof HttpURLConnection) {
			    HttpURLConnection httpCon = (HttpURLConnection)ucon;
			    if (httpCon.getResponseCode() > 399) {
			        String msg = "Failed to download file " + strUrl + " server response " + httpCon.getResponseMessage();
			        throw new DownloadException(DownloadException.ExCode.INVALID_SERVER_RESPONSE, msg);
			    }
			}
			in = ucon.getInputStream(); // 得到文件输入流
			byte[] byteBuf = BUFFER_CACHE.get();
			if (byteBuf == null) {
				byteBuf = new byte[BUFFER_SIZE];
				BUFFER_CACHE.set(byteBuf);
			}
			byte[] ret = null;
			int count, total = 0;
			// 首先使用直接缓冲法，避免开更多的对象
			while ((count = in.read(byteBuf, total, BUFFER_SIZE - total)) > 0) {
			    total += count;
			    if (total + 124 >= BUFFER_SIZE)
			        break;
			}
			if (total < BUFFER_SIZE - 124) {
				ret = ArrayUtils.subarray(byteBuf, 0, total);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(MATERIAL_SIZE);
				count = total;
				total = 0;
				do {
					bos.write(byteBuf, 0, count);
					total += count;
					if (total > maxFileSize) {
						String msg = "File " + strUrl + " size exceed [" + maxFileSize + "] download stoped.";
						throw new DownloadException(DownloadException.ExCode.FILE_TOO_LARGE, msg);
					}
				} while ((count = in.read(byteBuf)) > 0);
				ret = bos.toByteArray();
			}
			if (ret.length < MIN_SIZE) {
			    String msg = "File " + strUrl + " size [" + maxFileSize + "] too small.";
			    throw new DownloadException(DownloadException.ExCode.FILE_TOO_SMALL, msg);
			}
			if (LOG.isDebugEnabled())
				LOG.debug("Succeeded to download file [" + strUrl + "] size " + ret.length);
			return ret;
		} catch (IOException e) {
			String msg = "Failed to download file " + strUrl + " msg=" + e.getMessage();
			throw new DownloadException(DownloadException.ExCode.IOEXCEPTION, msg);
		} finally {
			// 关闭过程中的异常我们不关心
			StreamUtil.closeStream(in);
		}
	}

	public static boolean isTextFileType(String contentType) {
		if (contentType == null || contentType.length() == 0)
			return false;
		contentType = contentType.toLowerCase();
		if (contentType.contains("text") || contentType.contains("xml")
				|| contentType.contains("html") || contentType.contains("txt"))
			return true;
		return false;
	}


	private DownloadUtil() {
		// Utility类，禁止实例化
	}
}
