package com.azoft.azoft.collage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.User;
import com.azoft.azoft.collage.data.results.UserDataResult;
import com.azoft.azoft.collage.data.results.UsersSearchResult;
import com.azoft.azoft.collage.loaders.UserDataLoader;
import com.azoft.azoft.collage.loaders.UsersSearchLoader;
import com.azoft.azoft.collage.ui.adapters.UsersAdapter;

import java.util.regex.Pattern;

/**
 * Will load and show users for selected nickname. Will notify ParentFragment or Activity if user is clicked if they implement OnUserSelectedListener
 * interface.
 * Will use user's name pattern as Instagram has.
 * Date: 4/8/2014
 * Time: 4:49 PM
 *
 * @author MiG35
 */
public class UserSelectionFragment extends ActionBarLoaderFragment {

    private static final String LOADER_USER_HAS_DATA = "com.redmadrobot.azoft.collage.ui.activities.phone.UserSelectionActivity.LOADER_USER_HAS_DATA";
    private static final String LOADER_USERS = "com.redmadrobot.azoft.collage.ui.activities.phone.UserSelectionActivity.LOADER_USERS";

    private static final Pattern USER_NAME_PATTERN = Pattern.compile("[\\w]+");

    @InjectView(R.id.lv_user_accounts)
    private ListView mUserAccountsListView;
    @InjectView(R.id.tv_empty)
    private TextView mEmptyTextView;

    @InjectSavedState
    private boolean mExpandMenu = true;
    @InjectSavedState
    private String mSearchString;

    @InjectSavedState
    private UsersSearchResult mUsersSearchResult;

    private UsersAdapter mUsersAdapter;

    public UserSelectionFragment() {
        setHasOptionsMenu(true);
        setMenuVisibility(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_selection, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserAccountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private User mPrevSelectedUser;

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final User user = mUsersAdapter.getItem(position);
                if (mPrevSelectedUser == user) {
                    getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(LOADER_USER_HAS_DATA), new UserDataLoader(getActivity(), user, getActionBarActivity().getAccessToken()));
                } else {
                    mPrevSelectedUser = user;
                    getLoaderHelper()
                            .restartAsyncLoader(getLoaderHelper().getLoaderId(LOADER_USER_HAS_DATA), new UserDataLoader(getActivity(), user, getActionBarActivity().getAccessToken()));
                }
            }
        });

        if (getLoaderHelper().hasLoader(getLoaderHelper().getLoaderId(LOADER_USERS))) {
            getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(LOADER_USERS), null);
        } else if (null != mSearchString) {
            onUserNickSelected(mSearchString);
        } else {
            getActionBarActivity().getSupportActionBar().setTitle(R.string.title_user_nick_selection);
        }
    }

    private void onUserNickSelected(final String nickName) {
        if (nickName.equals(mSearchString) && null != mUsersSearchResult) {
            // if we have all information for this input, simply show it
            setUserSearchResult(mUsersSearchResult);
        } else if (nickName.equals(mSearchString)) {
            // is we already enter it, but don't have data, we should connect to last loader
            getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(LOADER_USERS), new UsersSearchLoader(getActivity(), nickName, getActionBarActivity().getAccessToken()));
        } else {
            // or we should load new data
            mUsersSearchResult = null;
            mSearchString = nickName;
            getLoaderHelper().restartAsyncLoader(getLoaderHelper().getLoaderId(LOADER_USERS), new UsersSearchLoader(getActivity(), nickName, getActionBarActivity().getAccessToken()));
        }
    }

    @Override
    public void onLoaderResult(final int id, final Object result) {
        super.onLoaderResult(id, result);

        if (id == getLoaderHelper().getLoaderId(LOADER_USERS)) {
            getLoaderHelper().removeLoaderFromRunningLoaders(id);

            setUserSearchResult((UsersSearchResult) result);
        } else if (id == getLoaderHelper().getLoaderId(LOADER_USER_HAS_DATA)) {
            getLoaderHelper().destroyAsyncLoader(id);

            final UserDataResult userDataResult = (UserDataResult) result;

            if (userDataResult.isHasImages()) {
                final User user = userDataResult.getUser();
                final Fragment parentFragment = getParentFragment();
                final Activity activity = getActivity();
                if (parentFragment instanceof OnUserSelectedListener) {
                    ((OnUserSelectedListener) parentFragment).onUserSelected(user);
                } else if (activity instanceof OnUserSelectedListener) {
                    ((OnUserSelectedListener) activity).onUserSelected(user);
                } else {
                    Log.e(UserSelectionFragment.class.getSimpleName(), "user selected, but no listener found");
                }
            } else if (userDataResult.isDataViewAllowed()) {
                getActionBarActivity().showToast(R.string.error_user_no_images, Gravity.CENTER);
            } else {
                getActionBarActivity().showToast(R.string.error_not_allowed, Gravity.CENTER);
            }
        }
    }

    private void setUserSearchResult(final UsersSearchResult userSearchResult) {
        mUsersSearchResult = userSearchResult;
        getActionBarActivity().getSupportActionBar().setTitle(getString(R.string.title_user_nick_selection_result, userSearchResult.getNickName()));

        if (null == mUsersAdapter) {
            mUsersAdapter = new UsersAdapter(getActivity(), userSearchResult.getUsers());
        } else {
            mUsersAdapter.setUsers(userSearchResult.getUsers());
        }
        if (null == mUserAccountsListView.getAdapter()) {
            mUserAccountsListView.setAdapter(mUsersAdapter);
        }
        mUserAccountsListView.setVisibility(mUsersAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
        mEmptyTextView.setVisibility(mUsersAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
        mEmptyTextView.setText(getString(R.string.text_no_users_found_for, userSearchResult.getNickName()));
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.user_account_selection, menu);

        initSearchView(menu.findItem(R.id.menu_search));
    }

    @SuppressWarnings("ConstantConditions")
    private void initSearchView(final MenuItem searchMenuItem) {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint(getString(R.string.hint_enter_user_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String s) {
                final String nickName = s.trim();
                if (USER_NAME_PATTERN.matcher(nickName).matches()) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    onUserNickSelected(nickName);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                final String nickName = s.trim();
                if (USER_NAME_PATTERN.matcher(nickName).matches()) {
                    onUserNickSelected(nickName);
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                mExpandMenu = true;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                mExpandMenu = false;
                return true;
            }
        });
        if (mExpandMenu) {
            MenuItemCompat.expandActionView(searchMenuItem);
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            MenuItemCompat.collapseActionView(searchMenuItem);
        }
    }

    public interface OnUserSelectedListener {

        void onUserSelected(final User user);
    }
}