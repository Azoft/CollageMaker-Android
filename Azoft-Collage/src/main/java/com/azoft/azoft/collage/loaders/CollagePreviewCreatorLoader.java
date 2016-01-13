package com.azoft.azoft.collage.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.CollageFillData;
import com.azoft.azoft.collage.data.CollageRegionData;
import com.azoft.azoft.collage.exceptions.CollageCreationException;
import com.azoft.azoft.collage.utils.CollageRegion;
import com.azoft.azoft.collage.utils.MediaUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Construct result image. And store it to internal directory and return its path in FileProvider.
 * <p/>
 * Date: 4/9/2014
 * Time: 6:19 PM
 *
 * @author MiG35
 */
public class CollagePreviewCreatorLoader extends DataAsyncTaskLibLoader<String> {

    private final CollageFillData mCollageFillData;

    public CollagePreviewCreatorLoader(final Context context, final CollageFillData collageFillData) {
        super(context);
        if (null == collageFillData) {
            throw new IllegalArgumentException("collageFillData can't be null");
        }

        mCollageFillData = collageFillData;
    }

    @Override
    protected String performLoad() throws Exception {
        final Map<CollageRegion, CollageRegionData> collageDataMap = new HashMap<>();

        int maxSize = 0; // we need to know our result image size (as biggest image size).
        for (final CollageRegion collageRegion : mCollageFillData.getCollageRegions()) {
            final CollageRegionData regionData = mCollageFillData.getRegionData(collageRegion);
            maxSize = Math.max(maxSize, getImageSize(regionData.getImageFile()));
            collageDataMap.put(collageRegion, regionData);
        }
        Bitmap outBitmap = null;
        try {
            outBitmap = Bitmap.createBitmap(maxSize, maxSize, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(outBitmap);
            canvas.drawColor(getContext().getResources().getColor(R.color.collage_bg_color));

            for (final Map.Entry<CollageRegion, CollageRegionData> entryItem : collageDataMap.entrySet()) {
                drawCollageRegionOnCanvas(canvas, entryItem.getKey(), entryItem.getValue());
            }
            return MediaUtils.insertImage(outBitmap, getContext().getString(R.string.text_image_collage_preview));
        } catch (final Throwable throwable) {
            throw new CollageCreationException(throwable);
        } finally {
            if (null != outBitmap) {
                outBitmap.recycle();
            }
        }
    }

    private void drawCollageRegionOnCanvas(final Canvas canvas, final CollageRegion collageRegion, final CollageRegionData collageRegionData)
            throws CollageCreationException {
        // region visible width and height
        final int regionWidth = (int) (canvas.getWidth() * collageRegion.getWidth());
        final int regionHeight = (int) (canvas.getWidth() * collageRegion.getHeight());

        // sample size for memory optimization
        final int sampleSize = getCollageItemSampleSize(collageRegionData.getImageFile(), regionWidth, regionHeight);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        final Bitmap regionDecodedBitmap = BitmapFactory.decodeFile(collageRegionData.getImageFile().getAbsolutePath(), options);
        if (null == regionDecodedBitmap) {
            throw new CollageCreationException();
        }
        final int maxRegionSize = Math.max(regionWidth, regionHeight);
        final Bitmap memoryOptimizedDecodedBitmap;
        if (regionDecodedBitmap.getWidth() == regionWidth && regionDecodedBitmap.getHeight() == regionHeight) {
            // decoded bitmap is the same as our region. nothing to do more
            memoryOptimizedDecodedBitmap = regionDecodedBitmap;
        } else {
            // we should make our decoded bitmap scaled for region. because region may be not square we use it's max size
            final Bitmap tmp = Bitmap.createScaledBitmap(regionDecodedBitmap, maxRegionSize, maxRegionSize, true);
            if (tmp != regionDecodedBitmap) {
                regionDecodedBitmap.recycle();
            }
            if (null == tmp) {
                memoryOptimizedDecodedBitmap = null;
            } else {
                memoryOptimizedDecodedBitmap = tmp;
            }
        }
        if (null == memoryOptimizedDecodedBitmap) {
            throw new CollageCreationException();
        }
        final float scale = collageRegionData.getImageScale();
        final float left = collageRegionData.getImageLeft() == null ? 0 : collageRegionData.getImageLeft();
        final float top = collageRegionData.getImageTop() == null ? 0 : collageRegionData.getImageTop();

        // we should crop image to be exactly as our scaled region (then we should scale it upper)
        final Bitmap scaledResultBitmap =
                Bitmap.createBitmap(memoryOptimizedDecodedBitmap, (int) (left * (memoryOptimizedDecodedBitmap.getWidth() - regionWidth / scale)),
                        (int) (top * (memoryOptimizedDecodedBitmap.getHeight() - regionHeight / scale)), (int) (regionWidth / scale),
                        (int) (regionHeight / scale));
        if (scaledResultBitmap != memoryOptimizedDecodedBitmap) {
            memoryOptimizedDecodedBitmap.recycle();
        }
        final Bitmap resultBitmap = Bitmap.createScaledBitmap(scaledResultBitmap, regionWidth, regionHeight, true);
        if (resultBitmap != scaledResultBitmap) {
            scaledResultBitmap.recycle();
        }

        canvas.drawBitmap(resultBitmap, (int) (collageRegion.getLeft() * canvas.getWidth()), (int) (collageRegion.getTop() * canvas.getHeight()),
                null);
        resultBitmap.recycle();
    }

    private int getCollageItemSampleSize(final File imageFile, final int regionWidth, final int regionHeight) {
        final int imageSize = getImageSize(imageFile);
        return Math.max(1, imageSize / Math.max(regionWidth, regionHeight));
    }

    private int getImageSize(final File imageFile) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        return Math.max(options.outWidth, options.outHeight);
    }
}
