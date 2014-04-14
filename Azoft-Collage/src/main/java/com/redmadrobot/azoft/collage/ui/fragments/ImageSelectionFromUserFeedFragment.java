package com.redmadrobot.azoft.collage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.Post;
import com.redmadrobot.azoft.collage.data.User;
import com.redmadrobot.azoft.collage.data.results.UserRecentFeedResult;
import com.redmadrobot.azoft.collage.loaders.UserImageFeedLoader;
import com.redmadrobot.azoft.collage.ui.adapters.UserFeedAdapter;

/**
 * Will load and show user's image feed. Will notify ParentFragment or Activity if post is clicked if they implement PostSelectionListener
 * interface.
 * <p/>
 * Date: 4/8/2014
 * Time: 7:13 PM
 *
 * @author MiG35
 */
public class ImageSelectionFromUserFeedFragment extends ActionBarLoaderFragment {

	private static final String USER_FEED_LOADER = "com.redmadrobot.azoft.collage.ui.fragments.ImageSelectionFromUserFeedFragment.USER_FEED_LOADER";

	// we use aggressive cache method. see readme for details
	@SuppressWarnings("StaticNonFinalField")
	private static UserRecentFeedResult sUserRecentFeedResult;

	@InjectView(R.id.gv_image_selection)
	private GridView mImageSelectionGridView;
	@InjectView(R.id.tv_empty)
	private TextView mEmptyTextView;

	@InjectSavedState
	private User mUser;

	public ImageSelectionFromUserFeedFragment() {
		setHasOptionsMenu(true);
		setMenuVisibility(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_image_selection_from_user_feed, container, false);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mImageSelectionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				final Object postObj = parent.getItemAtPosition(position);
				if (postObj instanceof Post) {
					final Post post = (Post) postObj;
					// we should check our parent fragment and then activity if they are listening for post selection
					final Fragment parentFragment = getParentFragment();
					final Activity activity = getActivity();
					if (parentFragment instanceof PostSelectionListener) {
						((PostSelectionListener) parentFragment).onPostSelected(post);
					}
					else if (activity instanceof PostSelectionListener) {
						((PostSelectionListener) activity).onPostSelected(post);
					}
					else {
						Log.e(ImageSelectionFromUserFeedFragment.class.getSimpleName(), "post selected, but no  listener found");
					}
				}
			}
		});
		// we use aggressive user feed cache in static variable
		if (null != sUserRecentFeedResult && mUser.getId().equals(sUserRecentFeedResult.getUserId())) {
			setUserRecentFeedResult(sUserRecentFeedResult);
		}
		else {
			getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(USER_FEED_LOADER), new UserImageFeedLoader(getActivity(), mUser.getId()));
		}
	}

	@Override
	public void onLoaderResult(final int id, final Object result) {
		super.onLoaderResult(id, result);

		if (id == getLoaderHelper().getLoaderId(USER_FEED_LOADER)) {
			getLoaderHelper().removeLoaderFromRunningLoaders(id);

			setUserRecentFeedResult((UserRecentFeedResult) result);

			getActionBarActivity().supportInvalidateOptionsMenu();
		}
	}

	@Override
	public void onLoaderError(final int id, final Exception exception) {
		super.onLoaderError(id, exception);

		getView().post(new Runnable() {
			@Override
			public void run() {
				getActionBarActivity().supportInvalidateOptionsMenu();
			}
		});
	}

	private void setUserRecentFeedResult(final UserRecentFeedResult userRecentFeedResult) {
		// we use aggressive cache method. see readme for details
		//noinspection AssignmentToStaticFieldFromInstanceMethod
		sUserRecentFeedResult = userRecentFeedResult;

		final UserFeedAdapter adapter = new UserFeedAdapter(getActivity(), userRecentFeedResult.getPosts());

		mImageSelectionGridView.setAdapter(adapter);
		mImageSelectionGridView.setVisibility(adapter.getCount() == 0 ? View.GONE : View.VISIBLE);
		mEmptyTextView.setVisibility(adapter.getCount() == 0 ? View.VISIBLE : View.GONE);
		mEmptyTextView.setText(getString(R.string.text_user_no_images, mUser.getNickName()));
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		if (!getLoaderHelper().hasRunningLoaders()) {
			inflater.inflate(R.menu.image_selection_user_feed, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.menu_update) {
			getLoaderHelper()
					.restartAsyncLoader(getLoaderHelper().getLoaderId(USER_FEED_LOADER), new UserImageFeedLoader(getActivity(), mUser.getId()));
			getActionBarActivity().supportInvalidateOptionsMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static Fragment getInstance(final User user) {
		if (null == user) {
			throw new IllegalArgumentException("user can't be null");
		}
		final ImageSelectionFromUserFeedFragment imageSelectionFromUserFeedFragment = new ImageSelectionFromUserFeedFragment();
		imageSelectionFromUserFeedFragment.mUser = user;
		return imageSelectionFromUserFeedFragment;
	}

	public interface PostSelectionListener {

		void onPostSelected(final Post post);
	}
}