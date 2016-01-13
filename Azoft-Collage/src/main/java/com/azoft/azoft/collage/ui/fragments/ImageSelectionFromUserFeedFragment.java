package com.azoft.azoft.collage.ui.fragments;

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
import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.Post;
import com.azoft.azoft.collage.data.User;
import com.azoft.azoft.collage.data.results.UserRecentFeedResult;
import com.azoft.azoft.collage.loaders.UserImageFeedLoader;
import com.azoft.azoft.collage.ui.adapters.UserFeedAdapter;

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
    @InjectView(R.id.progress_bar)
    private View mProgressBar;

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
                    } else if (activity instanceof PostSelectionListener) {
                        ((PostSelectionListener) activity).onPostSelected(post);
                    } else {
                        Log.e(ImageSelectionFromUserFeedFragment.class.getSimpleName(), "post selected, but no  listener found");
                    }
                }
            }
        });
        // we use aggressive user feed cache in static variable
        if (null != sUserRecentFeedResult && mUser.getId().equals(sUserRecentFeedResult.getUserId())) {
            setUserRecentFeedResult(sUserRecentFeedResult);
        } else {
            State.PROGRESS.apply(this);
            getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(USER_FEED_LOADER), new UserImageFeedLoader(getActivity(), mUser.getId(), getActionBarActivity().getAccessToken()));
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

        getLoaderHelper().destroyAsyncLoader(id);
        getActionBarActivity().supportInvalidateOptionsMenu();
    }

    private void setUserRecentFeedResult(final UserRecentFeedResult userRecentFeedResult) {
        // we use aggressive cache method. see readme for details
        //noinspection AssignmentToStaticFieldFromInstanceMethod
        sUserRecentFeedResult = userRecentFeedResult;

        final UserFeedAdapter adapter = new UserFeedAdapter(getActivity(), userRecentFeedResult.getPosts());

        if (0 == adapter.getCount()) {
            State.EMPTY.apply(this);
        } else {
            mImageSelectionGridView.setAdapter(adapter);
            State.DATA.apply(this);
        }
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
                    .restartAsyncLoader(getLoaderHelper().getLoaderId(USER_FEED_LOADER), new UserImageFeedLoader(getActivity(), mUser.getId(), getActionBarActivity().getAccessToken()));
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

    private enum State {
        DATA {
            @Override
            public void apply(final ImageSelectionFromUserFeedFragment fragment) {
                fragment.mImageSelectionGridView.setVisibility(View.VISIBLE);
                fragment.mEmptyTextView.setVisibility(View.GONE);
                fragment.mProgressBar.setVisibility(View.GONE);
            }
        },
        PROGRESS {
            @Override
            public void apply(final ImageSelectionFromUserFeedFragment fragment) {
                fragment.mImageSelectionGridView.setVisibility(View.GONE);
                fragment.mEmptyTextView.setVisibility(View.GONE);
                fragment.mProgressBar.setVisibility(View.VISIBLE);
            }
        },
        EMPTY {
            @Override
            public void apply(final ImageSelectionFromUserFeedFragment fragment) {
                fragment.mImageSelectionGridView.setVisibility(View.GONE);
                fragment.mEmptyTextView.setVisibility(View.VISIBLE);
                fragment.mProgressBar.setVisibility(View.GONE);
                fragment.mEmptyTextView.setText(fragment.getString(R.string.text_user_no_images, fragment.mUser.getNickName()));
            }
        };

        public abstract void apply(final ImageSelectionFromUserFeedFragment fragment);
    }
}