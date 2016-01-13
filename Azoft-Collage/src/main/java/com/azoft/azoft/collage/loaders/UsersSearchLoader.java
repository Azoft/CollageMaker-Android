package com.azoft.azoft.collage.loaders;

import android.content.Context;

import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.azoft.azoft.collage.data.results.UsersSearchResult;
import com.azoft.azoft.collage.exceptions.InternalServerException;
import com.azoft.azoft.collage.server.ServerConnector;
import com.azoft.azoft.collage.server.responces.SearchUserResponse;

/**
 * Will load users with parameter nickName
 * <p/>
 * Date: 4/8/2014
 * Time: 11:39 AM
 *
 * @author MiG35
 */
public class UsersSearchLoader extends DataAsyncTaskLibLoader<UsersSearchResult> {

    private final String mNickName;
    private final String mAccessToken;

    public UsersSearchLoader(final Context context, final String nickName, final String accessToken) {
        super(context);

        mNickName = nickName;
        mAccessToken = accessToken;
    }

    @Override
    protected UsersSearchResult performLoad() throws Exception {
        final SearchUserResponse response = ServerConnector.getInstagramService().searchUser(mNickName, mAccessToken);
        if (null == response) {
            throw new InternalServerException("Response is null");
        }
        return new UsersSearchResult(mNickName, response.getUsers());
    }
}