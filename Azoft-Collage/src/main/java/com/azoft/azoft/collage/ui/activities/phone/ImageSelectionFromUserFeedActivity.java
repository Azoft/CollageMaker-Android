package com.azoft.azoft.collage.ui.activities.phone;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.azoft.azoft.collage.data.Post;
import com.azoft.azoft.collage.data.User;
import com.azoft.azoft.collage.ui.fragments.ImageSelectionFromUserFeedFragment;

import java.io.Serializable;

/**
 * Date: 4/8/2014
 * Time: 7:03 PM
 *
 * @author MiG35
 */
public class ImageSelectionFromUserFeedActivity extends SingleFragmentActivity implements ImageSelectionFromUserFeedFragment.PostSelectionListener {

    public static final String RESULT_POST = "com.redmadrobot.azoft.collage.ui.fragments.ImageSelectionFromUserFeedFragment.RESULT_POST";

    public static final String EXTRA_USER = "com.redmadrobot.azoft.collage.ui.activities.phone.ImageSelectionFromUserFeedActivity.EXTRA_USER";

    @Override
    protected Fragment createFragment() {
        final Serializable user = getIntent().getSerializableExtra(EXTRA_USER);
        if (user instanceof User) {
            return ImageSelectionFromUserFeedFragment.getInstance((User) user);
        }
        return null;
    }

    @Override
    public void onPostSelected(final Post post) {
        final Intent intent = new Intent();
        intent.putExtra(RESULT_POST, post);
        setResult(RESULT_OK, intent);
        finish();
    }
}