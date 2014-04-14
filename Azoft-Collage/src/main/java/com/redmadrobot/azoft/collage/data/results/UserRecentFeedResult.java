package com.redmadrobot.azoft.collage.data.results;

import com.redmadrobot.azoft.collage.data.Post;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Date: 4/9/2014
 * Time: 9:31 AM
 *
 * @author MiG35
 */
public class UserRecentFeedResult implements Serializable {

	private static final long serialVersionUID = 4293075867718694860L;

	private final String mUserId;
	private final List<Post> mPosts;

	public UserRecentFeedResult(final String userId, final List<Post> posts) {
		if (null == posts) {
			throw new IllegalArgumentException("posts can't be null");
		}
		mUserId = userId;
		mPosts = Collections.unmodifiableList(posts);
	}

	public String getUserId() {
		return mUserId;
	}

	public List<Post> getPosts() {
		return mPosts;
	}
}