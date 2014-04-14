package com.redmadrobot.azoft.collage.loaders;

import android.content.Context;
import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.redmadrobot.azoft.collage.data.User;
import com.redmadrobot.azoft.collage.data.results.UserDataResult;
import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import com.redmadrobot.azoft.collage.server.RetrofitHelper;
import com.redmadrobot.azoft.collage.server.ServerConnector;
import com.redmadrobot.azoft.collage.server.responces.UserRecentFeedResponse;

import java.util.List;

/**
 * Date: 4/11/2014
 * Time: 3:54 PM
 *
 * @author MiG35
 */
public class UserDataLoader extends DataAsyncTaskLibLoader<UserDataResult> {

	private final User mUser;

	public UserDataLoader(final Context context, final User user) {
		super(context);

		mUser = user;
	}

	@Override
	protected UserDataResult performLoad() throws Exception {
		try {
			final UserRecentFeedResponse response = ServerConnector.getInstagramService().getUserRecentFeed(mUser.getId(), 1);
			if (null == response) {
				throw new InternalServerException("Response is null");
			}
			final List<UserRecentFeedResponse.PostItem> postItems = response.getPostItems();
			if (null == postItems || postItems.isEmpty()) {
				return UserDataResult.getHasNoData(mUser);
			}
			else {
				return UserDataResult.getHasData(mUser);
			}
		}
		catch (final InternalServerException e) {
			// we should check is we don't have access to view user's posts. if so, we shouldn't try to load again
			if (RetrofitHelper.isAccessDeniedException(e)) {
				return UserDataResult.getNotAllowed(mUser);
			}
			throw e;
		}
	}
}
