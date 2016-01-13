package com.azoft.azoft.collage.data.results;

import com.azoft.azoft.collage.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 4/8/2014
 * Time: 1:04 PM
 *
 * @author MiG35
 */
public class UsersSearchResult implements Serializable {

    private static final long serialVersionUID = -7231458361668713668L;

    private final String mNickName;
    private final List<User> mUsers;

    public UsersSearchResult(final String nickName, final List<User> users) {
        mNickName = nickName;
        if (null == users) {
            mUsers = Collections.emptyList();
        } else {
            mUsers = new ArrayList<>(users);
        }
    }

    public String getNickName() {
        return mNickName;
    }

    public List<User> getUsers() {
        return mUsers;
    }
}