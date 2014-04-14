package com.redmadrobot.azoft.collage.utils;

import java.io.Serializable;

/**
 * Contains CollageRegion position in Collage object. All coordinates are from 0 to 1.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:14 PM
 *
 * @author MiG35
 */
public class CollageRegion implements Serializable {

	private static final long serialVersionUID = -599596516243533187L;

	private final int mId;

	private final double mLeft;
	private final double mTop;
	private final double mRight;
	private final double mBottom;

	/**
	 * @param id     - unique region id in collage
	 * @param left   - should be in [0, 1] region.
	 * @param top    - should be in [0, 1] region.
	 * @param right  - should be in [0, 1] region. Should be greater then left.
	 * @param bottom - should be in [0, 1] region. Should be greater then top.
	 */
	public CollageRegion(final int id, final double left, final double top, final double right, final double bottom) {
		//noinspection OverlyComplexBooleanExpression
		if (0 > left || 0 > top || left > 1 || top > 1) {
			throw new IllegalArgumentException("left and top positions should be from 0 to 1");
		}
		if (left >= right || top >= bottom) {
			throw new IllegalArgumentException("left should be less then right, and top should be less then bottom");
		}
		mId = id;
		mLeft = left;
		mTop = top;
		mRight = right;
		mBottom = bottom;
	}

	public int getId() {
		return mId;
	}

	public double getLeft() {
		return mLeft;
	}

	public double getTop() {
		return mTop;
	}

	public double getRight() {
		return mRight;
	}

	public double getBottom() {
		return mBottom;
	}

	public double getWidth() {
		return mRight - mLeft;
	}

	public double getHeight() {
		return mBottom - mTop;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CollageRegion)) {
			return false;
		}

		final CollageRegion otherCollageRegion = (CollageRegion) o;

		return mId == otherCollageRegion.mId;
	}

	@Override
	public int hashCode() {
		return mId;
	}
}