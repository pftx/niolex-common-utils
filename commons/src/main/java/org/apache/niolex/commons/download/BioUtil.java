package org.apache.niolex.commons.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.niolex.commons.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BioUtil {
	private static final Logger log = LoggerFactory.getLogger(BioUtil.class);

	private static final int BUFFER_SIZE = 1024 * 16;
	private static final int MATERIAL_SIZE = 1024 * 30;
	private static final int CONNECT_TIMEOUT = Constants.DOWNLOAD_CONNECT_TIMEOUT;
	private static final int READ_TIMEOUT = Constants.DOWNLOAD_READ_TIMEOUT;
	private static final int MAX_SIZE = Constants.DOWNLOAD_MAX_FILE_SIZE;
	private static final int MIN_SIZE = Constants.DOWNLOAD_MIN_FILE_SIZE;

	private static final ThreadLocal<byte[]> bufferCache = new ThreadLocal<byte[]>();

	/**
	 * 
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
		if (log.isDebugEnabled())
			log.debug("Start to download file [" + strUrl + "].");
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
			byte[] byteBuf = bufferCache.get();
			if (byteBuf == null) {
				byteBuf = new byte[BUFFER_SIZE];
				bufferCache.set(byteBuf);
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
			if (log.isDebugEnabled())
				log.debug("Succeeded to download file [" + strUrl + "] size " + ret.length);
			return ret;
		} catch (IOException e) {
			String msg = "Failed to download file " + strUrl + " msg=" + e.getMessage();
			throw new DownloadException(DownloadException.ExCode.IOEXCEPTION, msg);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// 关闭过程中的异常我们不关心
				log.info("Exception while close url - " + e.getMessage());
			}
		}
	}

	public static boolean isTextFileType(String contentType) {
		if (contentType == null || contentType.length() == 0)
			return false;
		contentType = contentType.toLowerCase();
		if (contentType.contains("text") || contentType.contains("xml") || contentType.contains("html"))
			return true;
		return false;
	}

	
	private BioUtil() {
		// Utility类，禁止实例化
	}
}
