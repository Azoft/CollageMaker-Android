package com.azoft.azoft.collage.ui.activities.phone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.app.CollageApplication;
import com.azoft.azoft.collage.exceptions.DiskWriteException;
import com.azoft.azoft.collage.exceptions.NoHandleInBaseActivityException;
import com.azoft.azoft.collage.exceptions.NotAllowedException;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.Injector;
import com.mig35.loaderlib.exceptions.NoNetworkException;
import com.mig35.loaderlib.utils.ActivityLoaderHelper;
import com.mig35.loaderlib.utils.ActivityLoaderListener;
import com.mig35.loaderlib.utils.FragmentToActivityLoaderTaskListener;
import com.mig35.loaderlib.utils.LoaderHelper;

/**
 * Base Activity class with action bar.
 * <p/>
 * Date: 4/8/2014
 * Time: 11:01 AM
 *
 * @author MiG35
 */
public abstract class CollageActivity extends AppCompatActivity implements ActivityLoaderListener {

    private Injector mInjector;

    @InjectSavedState
    private ActivityLoaderHelper mActivityLoaderHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        mInjector = Injector.init(this);

        mInjector.applyOnActivityCreate(this, savedInstanceState);

        if (null == mActivityLoaderHelper) {
            mActivityLoaderHelper = new ActivityLoaderHelper();
        }

        super.onCreate(savedInstanceState);

        mActivityLoaderHelper.onCreate(this);

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mInjector.applyOnActivityContentChange(this);
    }

    @Override
    protected void onStart() {
        mActivityLoaderHelper.onStart();

        super.onStart();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        mActivityLoaderHelper.onResumeFragments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mActivityLoaderHelper.onDestroy();
        mInjector.applyOnActivityDestroy(this);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        mActivityLoaderHelper.onSaveInstanceState();
        mInjector.applyOnActivitySaveInstanceState(this, outState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mActivityLoaderHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mActivityLoaderHelper.onStop();
    }

    @Override
    public void showProgress(final boolean hasRunningLoaders) {
        // nothing to see here
    }

    @Override
    public void onLoaderResult(final int id, final Object result) {
        // nothing to see here
    }

    @Override
    public void onLoaderError(final int id, final Exception exception) {
        // general base activity exception handle
        if (exception instanceof NoNetworkException) {
            showToast(R.string.error_no_network);
        } else if (exception instanceof NotAllowedException) {
            showToast(R.string.error_not_allowed);
        } else if (exception instanceof DiskWriteException) {
            showToast(R.string.error_disk_error);
        } else if (!(exception instanceof NoHandleInBaseActivityException)) {
            showToast(R.string.error_unknown);
        }
    }

    public void showToast(final int toastMessage, final int gravity) {
        showToast(getString(toastMessage), gravity);
    }

    public void showToast(final int toastMessage) {
        showToast(getString(toastMessage));
    }

    public void showToast(final String message) {
        if (null == message) {
            return;
        }

        CollageApplication.getInstance().showToast(message, null);
    }

    private void showToast(final String message, final int gravity) {
        if (null == message) {
            return;
        }

        CollageApplication.getInstance().showToast(message, gravity);
    }

    @Override
    public LoaderHelper getLoaderHelper() {
        return mActivityLoaderHelper;
    }

    @Override
    public void addLoaderListener(final FragmentToActivityLoaderTaskListener loaderTaskListener) {
        mActivityLoaderHelper.addLoaderListener(loaderTaskListener);
    }

    @Override
    public void removeLoaderListener(final FragmentToActivityLoaderTaskListener loaderFragment) {
        mActivityLoaderHelper.removeLoaderListener(loaderFragment);
    }
}