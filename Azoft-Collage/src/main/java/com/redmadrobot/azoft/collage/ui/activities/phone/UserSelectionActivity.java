package com.redmadrobot.azoft.collage.ui.activities.phone;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.redmadrobot.azoft.collage.data.User;
import com.redmadrobot.azoft.collage.ui.fragments.UserSelectionFragment;

public class UserSelectionActivity extends SingleFragmentActivity implements UserSelectionFragment.OnUserSelectedListener {

	public static final String RESULT_USER = "com.redmadrobot.azoft.collage.ui.activities.phone.UserSelectionActivity.RESULT_USER";

	@Override
	protected Fragment createFragment() {
		return new UserSelectionFragment();
	}

	@Override
	public void onUserSelected(final User user) {
		setResult(RESULT_OK, new Intent().putExtra(RESULT_USER, user));
		finish();
	}
}