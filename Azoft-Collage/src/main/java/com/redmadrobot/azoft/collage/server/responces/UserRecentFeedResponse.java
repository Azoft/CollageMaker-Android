package com.redmadrobot.azoft.collage.server.responces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Date: 4/9/2014
 * Time: 9:34 AM
 *
 * @author MiG35
 */
public class UserRecentFeedResponse {

	@SerializedName("pagination")
	private Pagination mPagination;
	@SerializedName("data")
	private List<PostItem> mPostItems;

	public Pagination getPagination() {
		return mPagination;
	}

	public List<PostItem> getPostItems() {
		return mPostItems;
	}

	public static class Pagination {

		@SerializedName("next_url")
		private String mNextUrl;
		@SerializedName("next_max_id")
		private String mNextMaxId;

		public String getNextUrl() {
			return mNextUrl;
		}

		public String getNextMaxId() {
			return mNextMaxId;
		}
	}

	public static class PostItem {

		@SerializedName("type")
		private String mType;
		@SerializedName("id")
		private String mId;
		@SerializedName("likes")
		private Likes mLikes;
		@SerializedName("images")
		private PostImages mPostImages;

		public String getType() {
			return mType;
		}

		public String getId() {
			return mId;
		}

		public PostImages getPostImages() {
			return mPostImages;
		}

		public Likes getLikes() {
			return mLikes;
		}
	}

	public static class Likes {

		@SerializedName("count")
		private int mCount;

		public int getCount() {
			return mCount;
		}
	}

	public static class PostImages {

		@SerializedName("low_resolution")
		private PostImage mLowResolutionImage;
		@SerializedName("thumbnail")
		private PostImage mThumbnailImage;
		@SerializedName("standard_resolution")
		private PostImage mStandardResolutionImage;

		public PostImage getLowResolutionImage() {
			return mLowResolutionImage;
		}

		public PostImage getThumbnailImage() {
			return mThumbnailImage;
		}

		public PostImage getStandardResolutionImage() {
			return mStandardResolutionImage;
		}

		public boolean hasAllImages() {
			return null != mLowResolutionImage && null != mThumbnailImage && null != mStandardResolutionImage;
		}

		public boolean hasDataInAllImages() {
			return mLowResolutionImage.hasData() && mThumbnailImage.hasData() && mStandardResolutionImage.hasData();
		}
	}

	public static class PostImage {

		@SerializedName("url")
		private String mUrl;
		@SerializedName("width")
		private int mWidth;
		@SerializedName("height")
		private int mHeight;

		public String getUrl() {
			return mUrl;
		}

		public int getWidth() {
			return mWidth;
		}

		public int getHeight() {
			return mHeight;
		}

		public boolean hasData() {
			return 0 != mWidth && 0 != mHeight && null != mUrl;
		}
	}
}