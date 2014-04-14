package com.redmadrobot.azoft.collage.loaders;

import android.content.Context;
import com.mig35.loaderlib.exceptions.NoNetworkException;
import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.redmadrobot.azoft.collage.data.Image;
import com.redmadrobot.azoft.collage.data.Post;
import com.redmadrobot.azoft.collage.data.results.UserRecentFeedResult;
import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import com.redmadrobot.azoft.collage.exceptions.NotAllowedException;
import com.redmadrobot.azoft.collage.server.RetrofitHelper;
import com.redmadrobot.azoft.collage.server.ServerConnector;
import com.redmadrobot.azoft.collage.server.responces.UserRecentFeedResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Will load last user's images posts. Has tries and feeds count parameters as static in this class.
 * <p/>
 * Date: 4/9/2014
 * Time: 9:29 AM
 *
 * @author MiG35
 */
public class UserImageFeedLoader extends DataAsyncTaskLibLoader<UserRecentFeedResult> {

	private static final int TRIES_COUNT = 3;
	private static final int FEED_COUNT = 100;
	private static final String IMAGE_POST_TYPE_STRING = "image";

	private final String mUserId;

	public UserImageFeedLoader(final Context context, final String userId) {
		super(context);

		mUserId = userId;
	}

	@Override
	protected UserRecentFeedResult performLoad() throws Exception {
		final List<Post> posts = new ArrayList<Post>();
		loadPosts(posts);
		Collections.sort(posts);
		return new UserRecentFeedResult(mUserId, posts);
	}

	private void loadPosts(final List<Post> posts) throws Exception {
		String currentMaxId = null;

		do {
			final UserRecentFeedResponse response = getUserRecentFeedResponse(currentMaxId);

			final UserRecentFeedResponse.Pagination pagination = response.getPagination();
			final List<UserRecentFeedResponse.PostItem> postItems = response.getPostItems();
			if (null != postItems) {
				for (final UserRecentFeedResponse.PostItem postItem : postItems) {
					if (IMAGE_POST_TYPE_STRING.equals(postItem.getType())) {
						final Post post = convertToPost(postItem);
						if (null != post && posts.size() < FEED_COUNT) {
							posts.add(post);
						}
					}
				}
			}
			currentMaxId = null == pagination ? null : pagination.getNextMaxId();
		} while (posts.size() < FEED_COUNT && null != currentMaxId);
	}

	private UserRecentFeedResponse getUserRecentFeedResponse(final String currentMaxId) throws Exception {
		Exception exception = new InternalServerException("Response is null");
		// because some times Internet is not good we should try to load this data some times, because this data is important.
		for (int i = 0; i < TRIES_COUNT; ++i) {
			try {
				final UserRecentFeedResponse response;
				if (null == currentMaxId) {
					response = ServerConnector.getInstagramService().getUserRecentFeed(mUserId, FEED_COUNT);
				}
				else {
					response = ServerConnector.getInstagramService().getUserRecentFeed(mUserId, FEED_COUNT, currentMaxId);
				}
				if (null != response) {
					return response;
				}
			}
			catch (final NoNetworkException e) {
				exception = e;
			}
			catch (final InternalServerException e) {
				exception = e;
				// we should check is we don't have access to view user's posts. if so, we shouldn't try to load again
				if (RetrofitHelper.isAccessDeniedException(e)) {
					throw new NotAllowedException(e);
				}
			}
		}
		// we have an error. throw it!
		throw exception;
	}

	private Post convertToPost(final UserRecentFeedResponse.PostItem postItem) {
		final UserRecentFeedResponse.PostImages postItemImages = postItem.getPostImages();
		if (null == postItemImages || !postItemImages.hasAllImages() || !postItemImages.hasDataInAllImages()) {
			// we need only images posts
			return null;
		}
		final int likesCount = null == postItem.getLikes() ? 0 : postItem.getLikes().getCount();

		return new Post(postItem.getId(), postItem.getType(), likesCount, convertToImage(postItemImages.getThumbnailImage()),
				convertToImage(postItemImages.getLowResolutionImage()), convertToImage(postItemImages.getStandardResolutionImage()));
	}

	private Image convertToImage(final UserRecentFeedResponse.PostImage postImage) {
		if (null == postImage) {
			return null;
		}

		return new Image(postImage.getWidth(), postImage.getHeight(), postImage.getUrl());
	}
}