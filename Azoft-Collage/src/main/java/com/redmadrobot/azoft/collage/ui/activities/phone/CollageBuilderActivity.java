package com.redmadrobot.azoft.collage.ui.activities.phone;

import android.support.v4.app.Fragment;
import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.data.User;
import com.redmadrobot.azoft.collage.ui.fragments.CollageBuilderFragment;

import java.io.Serializable;

/**
 * Date: 4/8/2014
 * Time: 6:38 PM
 *
 * @author MiG35
 */
public class CollageBuilderActivity extends SingleFragmentActivity {

	public static final String EXTRA_USER = "com.redmadrobot.azoft.collage.ui.activities.phone.CollageBuilderActivity.EXTRA_USER";
	public static final String EXTRA_KOLAJ = "com.redmadrobot.azoft.collage.ui.activities.phone.CollageBuilderActivity.EXTRA_KOLAJ";

	@Override
	protected Fragment createFragment() {
		final Serializable user = getIntent().getSerializableExtra(EXTRA_USER);
		final Serializable collage = getIntent().getSerializableExtra(EXTRA_KOLAJ);
		if (user instanceof User && collage instanceof Collage) {
			return CollageBuilderFragment.getInstance((User) user, (Collage) collage);
		}
		return null;
	}
}