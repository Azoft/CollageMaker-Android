package com.redmadrobot.azoft.collage.exceptions;

/**
 * Indicates that this exception should be ignored by base activity exception handle.
 * <p/>
 * Date: 4/10/2014
 * Time: 1:02 PM
 *
 * @author MiG35
 */
public class NoHandleInBaseActivityException extends Exception {

	private static final long serialVersionUID = 2439717201384478829L;

	public NoHandleInBaseActivityException() {
	}

	public NoHandleInBaseActivityException(final String detailMessage) {
		super(detailMessage);
	}

	public NoHandleInBaseActivityException(final String detailMessage, final Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoHandleInBaseActivityException(final Throwable throwable) {
		super(throwable);
	}
}