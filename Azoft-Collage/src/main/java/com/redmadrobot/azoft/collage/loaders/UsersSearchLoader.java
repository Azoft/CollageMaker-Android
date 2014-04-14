package com.redmadrobot.azoft.collage.loaders;

import android.content.Context;
import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.redmadrobot.azoft.collage.data.results.UsersSearchResult;
import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import com.redmadrobot.azoft.collage.server.ServerConnector;
import com.redmadrobot.azoft.collage.server.responces.SearchUserResponse;

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

	public UsersSearchLoader(final Context context, final String nickName) {
		super(context);

		mNickName = nickName;
	}

	@Override
	protected UsersSearchResult performLoad() throws Exception {
		final SearchUserResponse response = ServerConnector.getInstagramService().searchUser(mNickName);
		if (null == response) {
			throw new InternalServerException("Response is null");
		}
		return new UsersSearchResult(mNickName, response.getUsers());
	}
}