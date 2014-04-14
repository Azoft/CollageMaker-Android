package com.redmadrobot.azoft.collage.ui.fragments;

import android.app.Activity;
import com.mig35.loaderlib.ui.LoaderFragment;
import com.redmadrobot.azoft.collage.ui.activities.phone.CollageActivity;

/**
 * Base fragment. Can be used <strong>only</strong> with CollageActivity.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:51 PM
 *
 * @author MiG35
 */
public abstract class ActionBarLoaderFragment extends LoaderFragment {

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof CollageActivity)) {
			throw new IllegalStateException("This fragment should be used only with CollageActivity");
		}
	}

	public CollageActivity getActionBarActivity() {
		return (CollageActivity) getActivity();
	}
}