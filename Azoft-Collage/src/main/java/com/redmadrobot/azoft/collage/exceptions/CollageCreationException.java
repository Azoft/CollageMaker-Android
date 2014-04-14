package com.redmadrobot.azoft.collage.exceptions;

/**
 * Special exception for inner collage creation logic fails.
 * <p/>
 * Date: 4/9/2014
 * Time: 6:28 PM
 *
 * @author MiG35
 */
public class CollageCreationException extends NoHandleInBaseActivityException {

	private static final long serialVersionUID = -8353694528536973759L;

	public CollageCreationException() {
	}

	public CollageCreationException(final String detailMessage) {
		super(detailMessage);
	}

	public CollageCreationException(final String detailMessage, final Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CollageCreationException(final Throwable throwable) {
		super(throwable);
	}
}