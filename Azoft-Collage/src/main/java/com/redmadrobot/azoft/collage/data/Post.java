package com.redmadrobot.azoft.collage.data;

import java.io.Serializable;

/**
 * Date: 4/7/2014
 * Time: 5:29 PM
 *
 * @author MiG35
 */
public class Post implements Serializable, Comparable<Post> {

	private static final long serialVersionUID = -506449273621003026L;

	private final String mId;

	private final String mType;
	private final int mLikesCount;

	private final Image mThumbnailImage;
	private final Image mLowResolutionImage;
	private final Image mStandardResolutionImage;

	public Post(final String id, final String type, final int likesCount, final Image thumbnailImage, final Image lowResolutionImage,
			final Image standardResolutionImage) {
		mId = id;
		mType = type;
		mLikesCount = likesCount;
		mThumbnailImage = thumbnailImage;
		mLowResolutionImage = lowResolutionImage;
		mStandardResolutionImage = standardResolutionImage;
	}

	public String getId() {
		return mId;
	}

	public String getType() {
		return mType;
	}

	public int getLikesCount() {
		return mLikesCount;
	}

	public Image getThumbnailImage() {
		return mThumbnailImage;
	}

	public Image getLowResolutionImage() {
		return mLowResolutionImage;
	}

	public Image getStandardResolutionImage() {
		return mStandardResolutionImage;
	}

	@Override
	public int compareTo(final Post another) {
		return mLikesCount < another.mLikesCount ? 1 : -1;
	}
}