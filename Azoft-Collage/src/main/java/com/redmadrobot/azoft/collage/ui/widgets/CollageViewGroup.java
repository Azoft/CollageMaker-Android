package com.redmadrobot.azoft.collage.ui.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.data.CollageRegionData;
import com.redmadrobot.azoft.collage.utils.CollageRegion;

/**
 * General view that construct all regions as CollageItemView children. This view calculate there sizes and location.
 * Can handle region clicks.
 * <p/>
 * Date: 4/8/2014
 * Time: 5:23 PM
 *
 * @author MiG35
 */
@SuppressWarnings("UnusedDeclaration")
public class CollageViewGroup extends SquareRelativeLayout {

	private CollageFillData mCollageFillData;
	private final OnClickListener mCollageItemClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			if (null != mRegionClickListener && v instanceof CollageItemView) {
				mRegionClickListener.onRegionClicked(((CollageItemView) v).getCollageRegion());
			}
		}
	};
	private RegionClickListener mRegionClickListener;

	public CollageViewGroup(final Context context) {
		super(context);
	}

	public CollageViewGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public CollageViewGroup(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				setCollage(mCollageFillData);
			}
		});
	}

	public void setCollage(final Collage collage) {
		setCollage(null == collage ? null : new CollageFillData(collage));
	}

	public void setCollage(final CollageFillData collageFillData) {
		if (mCollageFillData != collageFillData || getChildCount() != collageFillData.getRegionsCount()) {
			removeAllViews();
			mCollageFillData = collageFillData;
			initCollageViews();
		}
	}

	public void invalidateRegionData() {
		for (int i = 0; i < getChildCount(); ++i) {
			final View child = getChildAt(i);
			if (child instanceof CollageItemView) {
				final CollageItemView collageItemView = (CollageItemView) child;
				final CollageRegionData regionData = mCollageFillData.getRegionData(collageItemView.getCollageRegion());
				collageItemView.setRegionData(regionData);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	private void updateClickEnable() {
		final boolean clicksEnabled = mRegionClickListener != null;
		for (int i = 0; i < getChildCount(); ++i) {
			final View child = getChildAt(i);
			if (clicksEnabled) {
				child.setOnClickListener(mCollageItemClickListener);
			}
			else {
				child.setOnClickListener(null);
			}
		}
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	private void initCollageViews() {
		setBackgroundColor(getContext().getResources().getColor(R.color.collage_bg_color));

		final int width = getWidth();
		final int height = getHeight();
		if (null == mCollageFillData || width == 0 || height == 0) {
			return;
		}
		for (final CollageRegion collageRegion : mCollageFillData.getCollageRegions()) {
			final int collageWidth = (int) Math.round(collageRegion.getWidth() * width);
			final int collageHeight = (int) Math.round(collageRegion.getHeight() * height);

			final LayoutParams layoutParams = new LayoutParams(collageWidth, collageHeight);
			layoutParams.setMargins((int) Math.round(width * collageRegion.getLeft()), (int) Math.round(height * collageRegion.getTop()), 0, 0);
			final CollageItemView collageItemView = new CollageItemView(getContext());
			collageItemView.setCollageRegion(collageRegion);
			addView(collageItemView, layoutParams);
		}
		updateClickEnable();
		invalidateRegionData();
	}

	public void setRegionClickListener(final RegionClickListener regionClickListener) {
		mRegionClickListener = regionClickListener;
		updateClickEnable();
	}

	public interface RegionClickListener {

		void onRegionClicked(CollageRegion collageRegion);
	}
}