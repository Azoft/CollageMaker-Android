package com.redmadrobot.azoft.collage.exceptions;

/**
 * Indicates that our app is not allowed to access some data.
 * <p/>
 * Date: 4/9/2014
 * Time: 11:16 AM
 *
 * @author MiG35
 */
public class NotAllowedException extends InternalServerException {

	private static final long serialVersionUID = -4873834503540566594L;

	public NotAllowedException() {
	}

	public NotAllowedException(final String detailMessage) {
		super(detailMessage);
	}

	public NotAllowedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotAllowedException(final Throwable cause) {
		super(cause);
	}
}