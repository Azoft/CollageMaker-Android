package com.redmadrobot.azoft.collage.server;

import com.redmadrobot.azoft.collage.server.responces.SearchUserResponse;
import com.redmadrobot.azoft.collage.server.responces.UserRecentFeedResponse;
import retrofit.http.EncodedPath;
import retrofit.http.EncodedQuery;
import retrofit.http.GET;

/**
 * Date: 4/8/2014
 * Time: 2:28 PM
 *
 * @author MiG35
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface InstagramService {

	@GET("/users/search?client_id=" + ServerConnector.CLIENT_ID)
	SearchUserResponse searchUser(@EncodedQuery("q") final String query) throws Exception;

	@GET("/users/{user-id}/media/recent/?client_id=" + ServerConnector.CLIENT_ID)
	UserRecentFeedResponse getUserRecentFeed(@EncodedPath("user-id") final String userId, @EncodedQuery("count") final int count) throws Exception;

	@GET("/users/{user-id}/media/recent/?client_id=" + ServerConnector.CLIENT_ID)
	UserRecentFeedResponse getUserRecentFeed(@EncodedPath("user-id") final String userId, @EncodedQuery("count") final int count,
			@EncodedQuery("max_id") final String maxId) throws Exception;
}