package org.apache.niolex.commons.download;

public class DownloadException extends Exception {
	private static final long serialVersionUID = -6315233292954959316L;
	private ExCode code;

	public DownloadException(ExCode code) {
		super();
		this.code = code;
	}

	public DownloadException(ExCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public DownloadException(ExCode code, String message) {
		super(message);
		this.code = code;
	}

	public static enum ExCode {
		FILE_TOO_LARGE, INVALID_FILETYPE, IOEXCEPTION, INVALID_SERVER_RESPONSE, FILE_TOO_SMALL
	}

	public ExCode getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return code.name() + ": " + super.toString();
	}
	
	@Override
	public String getMessage() {
		return code.name() + ": " + super.getMessage();
	}
}
