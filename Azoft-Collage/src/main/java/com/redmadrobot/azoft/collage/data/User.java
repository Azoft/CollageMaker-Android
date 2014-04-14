package com.redmadrobot.azoft.collage.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Date: 4/8/2014
 * Time: 12:38 PM
 *
 * @author MiG35
 */
public class User implements Serializable {

	private static final long serialVersionUID = 8758285803290081341L;

	@SerializedName("username")
	private String mNickName;
	@SerializedName("first_name")
	private String mFirstName;
	@SerializedName("last_name")
	private String mLastName;
	@SerializedName("profile_picture")
	private String mProfilePicture;
	@SerializedName("id")
	private String mId;

	public String getNickName() {
		return mNickName;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public String getProfilePicture() {
		return mProfilePicture;
	}

	public String getId() {
		return mId;
	}
}