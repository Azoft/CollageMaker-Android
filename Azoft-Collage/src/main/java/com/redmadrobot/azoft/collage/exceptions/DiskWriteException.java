package com.redmadrobot.azoft.collage.exceptions;

/**
 * Indicates some disk write/read errors.
 * <p/>
 * Date: 4/9/2014
 * Time: 12:29 PM
 *
 * @author MiG35
 */
public class DiskWriteException extends Exception {

	private static final long serialVersionUID = -7962369698722370033L;

	public DiskWriteException() {
	}

	public DiskWriteException(final Throwable throwable) {
		super(throwable);
	}
}