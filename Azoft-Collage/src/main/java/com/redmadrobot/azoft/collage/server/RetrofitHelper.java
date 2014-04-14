package com.redmadrobot.azoft.collage.server;

import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Date: 4/11/2014
 * Time: 3:57 PM
 *
 * @author MiG35
 */
public final class RetrofitHelper {

	private static final int NOT_ALLOWED = 400;

	private RetrofitHelper() {
	}

	public static boolean isAccessDeniedException(final InternalServerException exception) {
		if (null == exception) {
			return false;
		}
		// we should check is we don't have access to view user's posts. if so, we shouldn't try to load again
		final Throwable cause = exception.getCause();
		if (cause instanceof RetrofitError) {
			final Response response = ((RetrofitError) cause).getResponse();
			if (null != response && response.getStatus() == NOT_ALLOWED) {
				return true;
			}
		}
		return false;
	}
}