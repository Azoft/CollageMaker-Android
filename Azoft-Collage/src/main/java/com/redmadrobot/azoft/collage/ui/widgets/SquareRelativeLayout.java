package com.redmadrobot.azoft.collage.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Help view that will be squared view with size as smallest size in measure.
 * Can't be used as WRAP_CONTENT on both sizes.
 * <p/>
 * Date: 4/9/2014
 * Time: 10:34 AM
 *
 * @author MiG35
 */
@SuppressWarnings("UnusedDeclaration")
public class SquareRelativeLayout extends RelativeLayout {

	public SquareRelativeLayout(final Context context) {
		super(context);
	}

	public SquareRelativeLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareRelativeLayout(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
			throw new IllegalStateException("this view should have at least one specific or max size");
		}
		final int neededSize;
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			neededSize = heightSize;
		}
		else if (heightMode == MeasureSpec.UNSPECIFIED) {
			neededSize = widthSize;
		}
		else {
			neededSize = Math.min(widthSize, heightSize);
		}

		super.onMeasure(MeasureSpec.makeMeasureSpec(neededSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(neededSize, MeasureSpec.EXACTLY));
	}
}
