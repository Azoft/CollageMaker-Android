package com.azoft.azoft.collage.loaders;

import android.content.Context;

import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.azoft.azoft.collage.data.User;
import com.azoft.azoft.collage.data.results.UserDataResult;
import com.azoft.azoft.collage.exceptions.InternalServerException;
import com.azoft.azoft.collage.server.RetrofitHelper;
import com.azoft.azoft.collage.server.ServerConnector;
import com.azoft.azoft.collage.server.responces.UserRecentFeedResponse;

import java.util.List;

/**
 * Date: 4/11/2014
 * Time: 3:54 PM
 *
 * @author MiG35
 */
public class UserDataLoader extends DataAsyncTaskLibLoader<UserDataResult> {

    private final User mUser;
    private final String mAccessToken;

    public UserDataLoader(final Context context, final User user, final String accessToken) {
        super(context);

        mUser = user;
        mAccessToken = accessToken;
    }

    @Override
    protected UserDataResult performLoad() throws Exception {
        try {
            final UserRecentFeedResponse response = ServerConnector.getInstagramService().getUserRecentFeed(mUser.getId(), 1, mAccessToken);
            if (null == response) {
                throw new InternalServerException("Response is null");
            }
            final List<UserRecentFeedResponse.PostItem> postItems = response.getPostItems();
            if (null == postItems || postItems.isEmpty()) {
                return UserDataResult.getHasNoData(mUser);
            } else {
                return UserDataResult.getHasData(mUser);
            }
        } catch (final InternalServerException e) {
            // we should check is we don't have access to view user's posts. if so, we shouldn't try to load again
            if (RetrofitHelper.isAccessDeniedException(e)) {
                return UserDataResult.getNotAllowed(mUser);
            }
            throw e;
        }
    }
}
