package com.redmadrobot.azoft.collage.data;

import com.redmadrobot.azoft.collage.utils.CollageRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains collage fill data for all its regions.
 * <p/>
 * Date: 4/9/2014
 * Time: 11:40 AM
 *
 * @author MiG35
 */
public class CollageFillData extends Collage {

	private static final long serialVersionUID = 9161249919940778595L;

	private final Map<CollageRegion, CollageRegionData> mRectangleDataMap;

	public CollageFillData(final Collage collage) {
		super(collage.getCollageRegions());

		mRectangleDataMap = new HashMap<CollageRegion, CollageRegionData>();
	}

	public void setRegionData(final CollageRegion collageRegion, final CollageRegionData collageRegionData) {
		if (mCollageRegions.contains(collageRegion)) {
			mRectangleDataMap.put(collageRegion, collageRegionData);
		}
		else {
			throw new IllegalArgumentException("wrong collageRegion entered");
		}
	}

	public CollageRegionData getRegionData(final CollageRegion collageRegion) {
		return mRectangleDataMap.get(collageRegion);
	}

	public boolean hasAllRegions() {
		if (mRectangleDataMap.size() == mCollageRegions.size()) {
			for (final Map.Entry<CollageRegion, CollageRegionData> entryItem : mRectangleDataMap.entrySet()) {
				if (null == entryItem.getValue()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}