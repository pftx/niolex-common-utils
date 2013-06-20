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

import static java.net.HttpURLConnection.*;
import static org.apache.niolex.commons.util.Const.*;
import static org.apache.niolex.commons.util.DateTimeUtil.SECOND;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.niolex.commons.codec.IntegerUtil;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.stream.StreamUtil;
import org.apache.niolex.commons.test.Check;
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

	private static final int CONNECT_TIMEOUT = 6 * SECOND;
	private static final int READ_TIMEOUT = 6 * SECOND;
	// ---
	private static final int BUFFER_SIZE = 16 * K;
	private static final int MATERIAL_SIZE = 30 * K;
	private static final int MAX_SIZE = 10 * M;
	private static final int MIN_SIZE = 10;

	/**
	 * Set the flag whether we use thread local cache for less byte array creation.
	 * This is useful for large amount of file download.
	 */
	private static boolean useThreadLocalCache = true;

	/**
	 * Set the flag whether we use thread local cache for less byte array creation.
     * This is useful for large amount of file download.
     *
	 * @param useCache the new flag
	 */
	public static void setUseThreadLocalCache(boolean useCache) {
	    useThreadLocalCache = useCache;
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
		return downloadFile(strUrl, CONNECT_TIMEOUT, READ_TIMEOUT, MAX_SIZE, null);
	}

	/**
	 * Download the file pointed by the url and return the content as byte array.
	 *
	 * @param strUrl
	 *            The Url to be downloaded.
	 * @param maxFileSize
     *            Max file size in BYTE.
	 * @return The file content as byte array.
	 * @throws DownloadException
	 */
	public static byte[] downloadFile(String strUrl, int maxFileSize) throws DownloadException {
	    return downloadFile(strUrl, CONNECT_TIMEOUT, READ_TIMEOUT, maxFileSize, null);
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
	 * @param useCache Whether we use thread local cache or not.
	 * @return The file content as byte array.
	 * @throws DownloadException
	 */
	public static byte[] downloadFile(String strUrl, int connectTimeout, int readTimeout, int maxFileSize,
	        Boolean useCache) throws DownloadException {
		LOG.debug("Start to download file [{}], C{}R{}M{}.", strUrl, connectTimeout, readTimeout, maxFileSize);
		InputStream in = null;
		try {
			URL url = new URL(strUrl); // We use Java URL to download file.
			URLConnection ucon = url.openConnection();
			ucon.setConnectTimeout(connectTimeout);
			ucon.setReadTimeout(readTimeout);
			ucon.setDoOutput(false);
			ucon.setDoInput(true);
			ucon.connect();
			final int contentLength = ucon.getContentLength();
			validateContentLength(strUrl, contentLength, maxFileSize);
			if (ucon instanceof HttpURLConnection) {
			    validateHttpCode(strUrl, (HttpURLConnection) ucon);
			}
			in = ucon.getInputStream(); // Get the input stream.
			byte[] ret = null;
			// Create the byte array buffer according to the strategy.
			if (contentLength > 0) {
			    ret = commonDownload(contentLength, in);
            } else {
                ret = unusualDownload(strUrl, in, maxFileSize, useCache);
            }
			LOG.debug("Succeeded to download file [{}] size {}.", strUrl, ret.length);
			return ret;
		} catch (DownloadException e) {
		    LOG.info(e.getMessage());
		    throw e;
		} catch (Exception e) {
		    String msg = "Failed to download file " + strUrl + " msg=" + e.getMessage();
		    LOG.warn(msg);
	        throw new DownloadException(DownloadException.ExCode.IOEXCEPTION, msg, e);
		} finally {
			// Close the input stream.
			StreamUtil.closeStream(in);
		}
	}

	/**
	 * The common process of download a size known file.
	 *
	 * @param size the file size.
	 * @param in the input stream.
	 * @return the file content.
	 * @throws IOException
	 */
	public static byte[] commonDownload(final int size, InputStream in) throws IOException {
	    byte[] byteBuf = new byte[size];
	    int count, total = 0;
        // Start to download file.
        while ((count = in.read(byteBuf, total, size - total)) > 0) {
            total += count;
        }
        Check.eq(total, size, "The downloaded File is not completed.");
        return byteBuf;
	}

	/**
	 * The unusual process of download a size unknown file.
	 *
	 * @param strUrl The Url to be downloaded.
	 * @param in the input stream.
	 * @param maxFileSize Max file size in BYTE.
	 * @param useCache Whether we use thread local cache or not.
	 * @return the file content.
	 * @throws IOException
	 * @throws DownloadException
	 */
	public static byte[] unusualDownload(String strUrl, InputStream in, int maxFileSize, Boolean useCache)
	        throws IOException, DownloadException {
	    byte[] byteBuf = getByteBuffer(useCache == null ? useThreadLocalCache : useCache);
	    byte[] ret = null;
        int count, total = 0;
        final int size = byteBuf.length;
        // Start to download file.
        while ((count = in.read(byteBuf, total, size - total)) > 0) {
            total += count;
        }
        if (count == -1) {
            // Case 1. File is ready
            ret = ArrayUtils.subarray(byteBuf, 0, total);
        } else {
            // Case 2. We still need read more data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(MATERIAL_SIZE);
            count = total;
            total = 0;
            do {
                bos.write(byteBuf, 0, count);
                total += count;
                if (total > maxFileSize) {
                    throw new DownloadException(DownloadException.ExCode.FILE_TOO_LARGE, "File " +
                            strUrl + " size exceed [" + maxFileSize + "] download stoped.");
                }
            } while ((count = in.read(byteBuf)) > 0);
            ret = bos.toByteArray();
        }
        if (ret.length < MIN_SIZE) {
            throw new DownloadException(DownloadException.ExCode.FILE_TOO_SMALL, "File " + strUrl +
                    " content size [" + ret.length + "] too small, it indicates error.");
        }
        return ret;
	}

	/**
	 * Validate the content length.
	 *
	 * @param strUrl The Url to be downloaded.
	 * @param contentLength The content Length from HTTP header.
	 * @param maxFileSize Max file size in BYTE.
	 * @throws DownloadException
	 */
	public static void validateContentLength(final String strUrl, final int contentLength, final int maxFileSize) throws DownloadException {
	    if (contentLength > maxFileSize) {
            throw new DownloadException(DownloadException.ExCode.FILE_TOO_LARGE, "File " + strUrl +
                    " content size [" + contentLength + "], max allowed [" + maxFileSize + "] too large; download stoped.");
        }
        if (contentLength != -1 && contentLength < MIN_SIZE) {
            throw new DownloadException(DownloadException.ExCode.FILE_TOO_SMALL, "File " + strUrl +
                    " content size [" + contentLength + "] too small, it indicates error.");
        }
	}

	/**
	 * Validate the HTTP code.
	 *
	 * @param strUrl The Url to be downloaded.
	 * @param httpCon The HTTP connection.
	 * @throws DownloadException
	 * @throws IOException
	 */
	public static void validateHttpCode(final String strUrl, HttpURLConnection httpCon) throws DownloadException, IOException {
        if (!IntegerUtil.isIn(httpCon.getResponseCode(), HTTP_OK, HTTP_NOT_AUTHORITATIVE,
                HTTP_MOVED_PERM, HTTP_MOVED_TEMP)) {
            throw new DownloadException(DownloadException.ExCode.INVALID_SERVER_RESPONSE, "File " + strUrl + " invalid response [" + httpCon.getResponseMessage() + "]");
        }
	}

	/**
	 * Get the byte buffer according to user strategy.
	 *
	 * @param useCache Whether we use thread local cache or not.
	 * @return the byte buffer
	 */
	public static byte[] getByteBuffer(boolean useCache) {
	    if (useCache) {
	        byte[] byteBuf = BUFFER_CACHE.get();
            if (byteBuf == null) {
                byteBuf = new byte[BUFFER_SIZE];
                BUFFER_CACHE.set(byteBuf);
            }
            return byteBuf;
        } else {
            return new byte[BUFFER_SIZE];
        }
	}

	/**
	 * Test the content type is text or not.
	 *
	 * @param contentType the content type
	 * @return true if we think it's text
	 */
	public static boolean isTextFileType(String contentType) {
		if (StringUtils.isBlank(contentType))
			return false;
		contentType = contentType.toLowerCase();
		if (StringUtil.containsAny(contentType, "text", "xml", "json", "html", "txt"))
			return true;
		return false;
	}

}
