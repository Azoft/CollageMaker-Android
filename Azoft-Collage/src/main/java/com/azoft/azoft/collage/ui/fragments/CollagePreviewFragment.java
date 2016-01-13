package com.azoft.azoft.collage.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.CollageFillData;
import com.azoft.azoft.collage.exceptions.CollageCreationException;
import com.azoft.azoft.collage.loaders.CollagePreviewCreatorLoader;
import com.azoft.azoft.collage.utils.CommonUtils;
import com.squareup.picasso.Picasso;

/**
 * Date: 4/9/2014
 * Time: 5:57 PM
 *
 * @author MiG35
 */
public class CollagePreviewFragment extends ActionBarLoaderFragment {

    private static final String KOLAJ_PREVIEW_LOADER = "com.redmadrobot.azoft.collage.ui.fragments.CollagePreviewFragment.KOLAJ_PREVIEW_LOADER";
    private static final String CREATION_PROBLEM_FRAGMENT =
            "com.redmadrobot.azoft.collage.ui.fragments.CollagePreviewFragment.CREATION_PROBLEM_FRAGMENT";

    @InjectSavedState
    private CollageFillData mCollageFillData;

    @InjectView(R.id.iv_collage)
    private ImageView mCollageImageView;

    @InjectSavedState
    private String mResultPath;

    public CollagePreviewFragment() {
        setHasOptionsMenu(true);
        setMenuVisibility(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collage_preview, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // we should check if there is no big errors in previous image creation
        if (null == getFragmentManager().findFragmentByTag(CREATION_PROBLEM_FRAGMENT)) {
            // we should check if we already create image or not
            if (null == mResultPath) {
                getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER),
                        new CollagePreviewCreatorLoader(getActivity(), mCollageFillData));
            } else {
                setResultPath(mResultPath);
            }
        }
    }

    @Override
    public void onLoaderResult(final int id, final Object result) {
        super.onLoaderResult(id, result);

        if (id == getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER)) {
            getLoaderHelper().removeLoaderFromRunningLoaders(id);

            setResultPath((String) result);
        }
    }

    @Override
    public void onLoaderError(final int id, final Exception exception) {
        super.onLoaderError(id, exception);

        if (id == getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER) && exception instanceof CollageCreationException) {
            final FragmentManager fm = getFragmentManager();
            fm.beginTransaction().add(new CollageCreationProblemMessageFragment(), CREATION_PROBLEM_FRAGMENT).commitAllowingStateLoss();
        }
    }

    private void setResultPath(final String resultPath) {
        mResultPath = resultPath;

        Picasso.with(getActivity()).load(resultPath).fit().error(R.drawable.ic_action_new).skipMemoryCache().into(mCollageImageView);
        getActionBarActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (null != mResultPath) {
            inflater.inflate(R.menu.collage_preview, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_share_collage) {
            if (null != mResultPath) {
                // because we use internal storage and provider, we should grand other apps permissions and so on...
                CommonUtils.sendEmailWithAttachment(getActivity(), mResultPath, getString(R.string.text_email_subject));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Fragment getInstance(final CollageFillData collageFillData) {
        if (null == collageFillData) {
            throw new IllegalArgumentException("collageFillData can't be null");
        }
        final CollagePreviewFragment collagePreviewFragment = new CollagePreviewFragment();
        collagePreviewFragment.mCollageFillData = collageFillData;
        return collagePreviewFragment;
    }

    public static class CollageCreationProblemMessageFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_collage_creation_problem_title);
            builder.setMessage(R.string.dialog_collage_creation_problem_message);
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    getActivity().finish();
                }
            });

            return builder.create();
        }

        @Override
        public void onCancel(final DialogInterface dialog) {
            super.onCancel(dialog);

            getActivity().finish();
        }
    }
}