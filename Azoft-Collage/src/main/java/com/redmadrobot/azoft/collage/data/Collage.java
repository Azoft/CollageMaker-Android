package com.redmadrobot.azoft.collage.data;

import com.redmadrobot.azoft.collage.utils.CollageRegion;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Contains collage structure as CollageRegion's list.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:12 PM
 *
 * @author MiG35
 */
public class Collage implements Serializable {

	private static final long serialVersionUID = -3545166311422144328L;

	protected final List<CollageRegion> mCollageRegions;
	private final int mRegionsCount;

	/**
	 * @param collageRegions - can't be null or empty
	 */
	public Collage(final List<CollageRegion> collageRegions) {
		if (null == collageRegions || collageRegions.isEmpty()) {
			throw new IllegalArgumentException("collageRegions shouldn't be empty");
		}
		mCollageRegions = Collections.unmodifiableList(collageRegions);
		mRegionsCount = mCollageRegions.size();
	}

	public List<CollageRegion> getCollageRegions() {
		return mCollageRegions;
	}

	public int getRegionsCount() {
		return mRegionsCount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Collage)) {
			return false;
		}

		final Collage collage = (Collage) o;

		return mCollageRegions.equals(collage.mCollageRegions);
	}

	@Override
	public int hashCode() {
		return mCollageRegions.hashCode();
	}
}