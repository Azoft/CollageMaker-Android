package com.redmadrobot.azoft.collage.data;

import java.io.File;
import java.io.Serializable;

/**
 * Contains image data as source and it's position inside region.
 * <p/>
 * Date: 4/9/2014
 * Time: 11:42 AM
 *
 * @author MiG35
 */
public class CollageRegionData implements Serializable {

	private static final long serialVersionUID = -7032708611705926844L;

	private final File mImageFile;
	private Float mImageLeft;
	private Float mImageTop;
	private float mImageScale;

	public CollageRegionData(final File imageFile) {
		if (null == imageFile) {
			throw new IllegalArgumentException("imageFile can't be null");
		}
		mImageFile = imageFile;
		mImageScale = 1f;
	}

	public File getImageFile() {
		return mImageFile;
	}

	public Float getImageLeft() {
		return mImageLeft;
	}

	public void setImageLeft(final float imageLeft) {
		mImageLeft = imageLeft;
	}

	public Float getImageTop() {
		return mImageTop;
	}

	public void setImageTop(final float imageTop) {
		mImageTop = imageTop;
	}

	public float getImageScale() {
		return mImageScale;
	}

	public void setImageScale(final float imageScale) {
		mImageScale = imageScale;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CollageRegionData)) {
			return false;
		}

		final CollageRegionData collageRegionData = (CollageRegionData) o;

		return mImageFile.equals(collageRegionData.mImageFile);
	}

	@SuppressWarnings("NonFinalFieldReferencedInHashCode")
	@Override
	public int hashCode() {
		return mImageFile.hashCode();
	}
}