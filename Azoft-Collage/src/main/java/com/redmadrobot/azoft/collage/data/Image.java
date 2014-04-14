package com.redmadrobot.azoft.collage.data;

import java.io.Serializable;

/**
 * Date: 4/9/2014
 * Time: 9:40 AM
 *
 * @author MiG35
 */
public class Image implements Serializable {

	private static final long serialVersionUID = 1238987802396038801L;

	private final int mWidth;
	private final int mHeight;
	private final String mUrl;

	public Image(final int width, final int height, final String url) {
		mWidth = width;
		mHeight = height;
		mUrl = url;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public String getUrl() {
		return mUrl;
	}
}