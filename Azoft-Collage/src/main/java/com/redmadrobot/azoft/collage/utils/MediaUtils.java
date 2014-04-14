package com.redmadrobot.azoft.collage.utils;

import android.graphics.Bitmap;
import com.redmadrobot.azoft.collage.exceptions.DiskWriteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Helper class to store bitmap as file with provider access.
 * <p/>
 * Date: 4/10/2014
 * Time: 3:24 PM
 *
 * @author MiG35
 */
public final class MediaUtils {

	private static final String STORAGE_FILE_PATH = "mediadata";

	private static final String STORAGE_AUTHORITY = "com.redmadrobot.azoft.collage.contentproviders.media";
	private static final String STORAGE_AUTHORITY_PATH = "images";

	private MediaUtils() {
	}

	public static String insertImage(final Bitmap source, final String title) throws DiskWriteException {
		final File storageFile = getStoreFile(title);
		if (null == storageFile) {
			return null;
		}
		try {
			final OutputStream imageOut = new FileOutputStream(storageFile);
			try {
				source.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
			}
			finally {
				imageOut.close();
			}
			return "content://" + STORAGE_AUTHORITY + '/' + STORAGE_AUTHORITY_PATH + '/' + title;
		}
		catch (final IOException e) {
			throw new DiskWriteException(e);
		}
	}

	static File getStoreFile(final String title) {
		final File internalFilesDir = CommonUtils.getInternalFilesDir();
		if (null == internalFilesDir) {
			return null;
		}
		final File mediaDataDir = new File(internalFilesDir, STORAGE_FILE_PATH);
		if (!mediaDataDir.exists() && !mediaDataDir.mkdir()) {
			return null;
		}
		return new File(mediaDataDir, title);
	}
}