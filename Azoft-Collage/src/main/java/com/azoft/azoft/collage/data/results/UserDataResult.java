package com.azoft.azoft.collage.data.results;

import com.azoft.azoft.collage.data.User;

/**
 * Date: 4/11/2014
 * Time: 3:55 PM
 *
 * @author MiG35
 */
public class UserDataResult {

    private final User mUser;
    private final boolean mDataViewAllowed;
    private final boolean mHasImages;

    private UserDataResult(final User user, final boolean dataViewAllowed, final boolean hasImages) {
        mUser = user;
        mDataViewAllowed = dataViewAllowed;
        mHasImages = hasImages;
    }

    public User getUser() {
        return mUser;
    }

    public boolean isDataViewAllowed() {
        return mDataViewAllowed;
    }

    public boolean isHasImages() {
        return mHasImages;
    }

    public static UserDataResult getNotAllowed(final User user) {
        return new UserDataResult(user, false, false);
    }

    public static UserDataResult getHasNoData(final User user) {
        return new UserDataResult(user, true, false);
    }

    public static UserDataResult getHasData(final User user) {
        return new UserDataResult(user, true, true);
    }
}