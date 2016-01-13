package com.azoft.azoft.collage.utils.collagegenerators;

import android.util.SparseArray;

import com.azoft.azoft.collage.data.Collage;
import com.azoft.azoft.collage.utils.CollageRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator with hardcoded numbers of collagees.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:20 PM
 *
 * @author MiG35
 */
public final class SimpleCollageGenerator implements CollageFactory {

    private static final int KOLAJ_ITEMS_COUNT = 4;
    private final SparseArray<Collage> mCollages = new SparseArray<>();

    @Override
    public Collage getCollage(final int number) {
        if (number < 0 || number >= 4) {
            throw new IllegalArgumentException("SimpleCollageGenerator can create collagees from 0 to 3 items only");
        }
        Collage collage = mCollages.get(number);
        if (null == collage) {
            collage = generateCollage(number);
            mCollages.put(number, collage);
        }
        return collage;
    }

    @Override
    public int getCollageCount() {
        return KOLAJ_ITEMS_COUNT;
    }

    @SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "MagicNumber"})
    private Collage generateCollage(final int number) {
        final List<CollageRegion> collageRegions = new ArrayList<>();
        int regionId = 0;
        if (0 == number) {
            collageRegions.add(new CollageRegion(regionId, 0.01, 0.01, 0.99, 0.99));
        } else if (1 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.99));
            collageRegions.add(new CollageRegion(regionId, 0.51d, 0.01, 0.99, 0.99));
        } else if (2 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.01, 0.99, 0.49d));
            collageRegions.add(new CollageRegion(regionId, 0.51d, 0.51d, 0.99, 0.99));
        } else if (3 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.49d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.51d, 0.49d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.01, 0.99, 0.49d));
            collageRegions.add(new CollageRegion(regionId, 0.51d, 0.51d, 0.99, 0.99));
        }
        return new Collage(collageRegions);
    }
}