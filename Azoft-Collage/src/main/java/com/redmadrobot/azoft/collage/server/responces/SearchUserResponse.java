package com.redmadrobot.azoft.collage.server.responces;

import com.google.gson.annotations.SerializedName;
import com.redmadrobot.azoft.collage.data.User;

import java.util.List;

/**
 * Date: 4/8/2014
 * Time: 2:38 PM
 *
 * @author MiG35
 */
public class SearchUserResponse {

	@SerializedName("data")
	private List<User> mUsers;

	public List<User> getUsers() {
		return mUsers;
	}
}