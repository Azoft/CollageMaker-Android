package com.azoft.azoft.collage.server;

import com.azoft.azoft.collage.server.responces.SearchUserResponse;
import com.azoft.azoft.collage.server.responces.UserRecentFeedResponse;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Date: 4/8/2014
 * Time: 2:28 PM
 *
 * @author MiG35
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface InstagramService {

    @GET("/users/search")
    SearchUserResponse searchUser(@Query("q") final String query, @Query("access_token") final String accessToken) throws Exception;

    @GET("/users/{user-id}/media/recent")
    UserRecentFeedResponse getUserRecentFeed(@Path("user-id") final String userId, @Query("count") final int count, @Query("access_token") final String accessToken) throws Exception;

    @GET("/users/{user-id}/media/recent")
    UserRecentFeedResponse getUserRecentFeed(@Path("user-id") final String userId, @Query("count") final int count,
                                             @Query("max_id") final String maxId, @Query("access_token") final String accessToken) throws Exception;
}