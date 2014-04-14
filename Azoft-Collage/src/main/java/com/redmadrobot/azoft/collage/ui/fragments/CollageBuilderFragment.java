package com.redmadrobot.azoft.collage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.data.CollageRegionData;
import com.redmadrobot.azoft.collage.data.Post;
import com.redmadrobot.azoft.collage.data.User;
import com.redmadrobot.azoft.collage.loaders.CollageBigImageLoader;
import com.redmadrobot.azoft.collage.ui.activities.phone.CollagePreviewActivity;
import com.redmadrobot.azoft.collage.ui.activities.phone.ImageSelectionFromUserFeedActivity;
import com.redmadrobot.azoft.collage.ui.widgets.CollageViewGroup;
import com.redmadrobot.azoft.collage.utils.CollageRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Will show collage and performs all actions for its filling.
 * <p/>
 * Date: 4/8/2014
 * Time: 6:41 PM
 *
 * @author MiG35
 */
public class CollageBuilderFragment extends ActionBarLoaderFragment {

	private static final String BIG_IMAGE_LOADER = "com.redmadrobot.azoft.collage.ui.fragments.CollageBuilderFragment.BIG_IMAGE_LOADER_%d";
	private static final int REQUEST_CODE_IMAGE_SELECTION = 156;

	@InjectSavedState
	private User mUser;
	@InjectSavedState
	private CollageFillData mCollageFillData;
	@InjectSavedState
	private CollageRegion mSelectedCollageRegion;

	@InjectView(R.id.collage_view_group)
	private CollageViewGroup mCollageViewGroup;

	// this is waiting images. user ask as to load them to collage regions, but we may not load them because of full recreation (as if don't keep
	// activities set).
	@SuppressWarnings("FieldMayBeFinal")
	@InjectSavedState
	private Map<CollageRegion, Post> mWaitingImages = new HashMap<CollageRegion, Post>();

	public CollageBuilderFragment() {
		setHasOptionsMenu(true);
		setMenuVisibility(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_collage_builder, container, false);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mCollageViewGroup.setCollage(mCollageFillData);
		mCollageViewGroup.setRegionClickListener(new CollageViewGroup.RegionClickListener() {
			@Override
			public void onRegionClicked(final CollageRegion collageRegion) {
				mSelectedCollageRegion = collageRegion;
				startActivityForResult(new Intent(getActivity(), ImageSelectionFromUserFeedActivity.class)
						.putExtra(ImageSelectionFromUserFeedActivity.EXTRA_USER, mUser), REQUEST_CODE_IMAGE_SELECTION);
			}
		});

		for (final Map.Entry<CollageRegion, Post> setItem : mWaitingImages.entrySet()) {
			// check if we have waiting images and they are not loaded yet
			if (!getLoaderHelper().hasLoader(getLoaderHelper().getLoaderId(createCollageRegionLoaderId(setItem.getKey())))) {
				// we should start this loader again
				//noinspection ObjectAllocationInLoop
				getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(createCollageRegionLoaderId(setItem.getKey())),
						new CollageBigImageLoader(getActivity(), setItem.getKey(), setItem.getValue()));
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.collage_builder, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (R.id.menu_preview_collage == item.getItemId()) {
			if (mCollageFillData.hasAllRegions()) {
				startActivity(new Intent(getActivity(), CollagePreviewActivity.class)
						.putExtra(CollagePreviewActivity.EXTRA_KOLAJ_FILL_DATA, mCollageFillData));
			}
			else {
				getActionBarActivity().showToast(R.string.error_fill_all_items_first);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLoaderResult(final int id, final Object result) {
		super.onLoaderResult(id, result);

		for (final CollageRegion collageRegion : mCollageFillData.getCollageRegions()) {
			if (id == getLoaderHelper().getLoaderId(createCollageRegionLoaderId(collageRegion))) {
				getLoaderHelper().removeLoaderFromRunningLoaders(id);

				final CollageRegionData collageRegionData = (CollageRegionData) result;
				mCollageFillData.setRegionData(collageRegion, collageRegionData);
				mCollageViewGroup.invalidateRegionData();

				// now we finished our loading, so it will be saved in save state, so it is save to remove it from this list.
				mWaitingImages.remove(collageRegion);

				break;
			}
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (REQUEST_CODE_IMAGE_SELECTION == requestCode) {
			if (resultCode == Activity.RESULT_OK && null != data) {
				if (null == mSelectedCollageRegion) {
					Log.e(CollageBuilderFragment.class.getSimpleName(), "Wrong state! How does it happen?! =/");
					return;
				}
				final Post post = (Post) data.getSerializableExtra(ImageSelectionFromUserFeedActivity.RESULT_POST);
				// we should remember that this image is not loading
				mWaitingImages.put(mSelectedCollageRegion, post);
				getLoaderHelper().restartAsyncLoader(getLoaderHelper().getLoaderId(createCollageRegionLoaderId(mSelectedCollageRegion)),
						new CollageBigImageLoader(getActivity(), mSelectedCollageRegion, post));
			}
			mSelectedCollageRegion = null;
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private String createCollageRegionLoaderId(final CollageRegion collageRegion) {
		return String.format(BIG_IMAGE_LOADER, collageRegion.getId());
	}

	public static Fragment getInstance(final User user, final Collage collage) {
		if (null == user || null == collage) {
			throw new IllegalArgumentException("non user neither collage can be null");
		}
		final CollageBuilderFragment collageBuilderFragment = new CollageBuilderFragment();
		collageBuilderFragment.mUser = user;
		collageBuilderFragment.mCollageFillData = new CollageFillData(collage);
		return collageBuilderFragment;
	}
}