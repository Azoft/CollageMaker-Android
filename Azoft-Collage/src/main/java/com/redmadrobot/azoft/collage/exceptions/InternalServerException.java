package com.redmadrobot.azoft.collage.exceptions;

/**
 * Some exception in protocol or in server communication.
 * <p/>
 * Date: 4/8/2014
 * Time: 2:49 PM
 *
 * @author MiG35
 */
public class InternalServerException extends Exception {

	private static final long serialVersionUID = 5180367710900570152L;

	public InternalServerException() {
	}

	public InternalServerException(final String detailMessage) {
		super(detailMessage);
	}

	public InternalServerException(final String detailMessage, final Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InternalServerException(final Throwable throwable) {
		super(throwable);
	}
}