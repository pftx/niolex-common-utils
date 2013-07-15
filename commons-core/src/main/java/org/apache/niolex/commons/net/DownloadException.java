/**
 * DownloadException.java
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
package org.apache.niolex.commons.net;

/**
 * The Exception throws from the DownloadUtil
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 *
 */
public class DownloadException extends Exception {
	private static final long serialVersionUID = -6315233292954959316L;

	public static enum ExCode {
	    FILE_TOO_LARGE, FILE_TOO_SMALL, IOEXCEPTION, INVALID_SERVER_RESPONSE;
	}

	// The exception code
	private final ExCode code;

	public DownloadException(ExCode code, String message) {
	    super(message);
	    this.code = code;
	}

	public DownloadException(ExCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ExCode getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return code.name() + ": " + super.getMessage();
	}

}
