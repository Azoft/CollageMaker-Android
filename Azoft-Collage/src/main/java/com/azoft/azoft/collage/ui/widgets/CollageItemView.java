package com.azoft.azoft.collage.ui.widgets;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.CollageRegionData;
import com.azoft.azoft.collage.utils.CollageRegion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Will show empty view (plus view) or image data for this region it correct place. If this view is square image is scaled and no gesture can be
 * made, if not, image can be moved.
 * <p/>
 * Date: 4/8/2014
 * Time: 5:24 PM
 *
 * @author MiG35
 */
@SuppressWarnings("UnusedDeclaration")
public class CollageItemView extends ImageView {

    private static final float MIN_SCALE_FACTOR = 1f;
    private static final float MAX_SCALE_FACTOR = 5f;

    private CollageRegion mCollageRegion;
    private CollageRegionData mRegionData;

    private OnClickListener mOnClickListener;
    private GestureDetectorCompat mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private boolean mNeedInit = true;

    private float mMinScale;
    private float mMaxScale;
    private ScrollHolder mScrollHolder;

    public CollageItemView(final Context context) {
        super(context);
        init(context);
    }

    public CollageItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CollageItemView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            private static final float HALF = 0.5f;

            @Override
            public boolean onScale(final ScaleGestureDetector detector) {
                final float oldScaleFactor = mRegionData.getImageScale();
                // we use "+" but "*" because here it is more suitable I think
                float newScaleFactor = mRegionData.getImageScale() + detector.getScaleFactor() - 1;
                // Don't let the object get too small or too large.
                newScaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(newScaleFactor, MAX_SCALE_FACTOR));

                if (0 != Float.compare(oldScaleFactor, newScaleFactor)) {
                    updateImagePosition(mMinScale * oldScaleFactor, mMinScale * newScaleFactor);
                    mRegionData.setImageScale(newScaleFactor);
                    updateScale();
                }

                return super.onScale(detector);
            }

            private void updateImagePosition(final float oldImageScaleFactor, final float newImageScaleFactor) {
                final float imageCenterX;
                final float imageCenterY;
                if (null == mRegionData.getImageLeft() || null == mRegionData.getImageTop()) {
                    imageCenterX = (mScrollHolder.mScrollXMax + getWidth()) * HALF;
                    imageCenterY = (mScrollHolder.mScrollYMax + getHeight()) * HALF;
                } else {
                    imageCenterX = mScrollHolder.mScrollXMax * mRegionData.getImageLeft() + getWidth() * HALF;
                    imageCenterY = mScrollHolder.mScrollYMax * mRegionData.getImageTop() + getHeight() * HALF;
                }
                final float newImageCenterX = imageCenterX * newImageScaleFactor / oldImageScaleFactor;
                final float newImageCenterY = imageCenterY * newImageScaleFactor / oldImageScaleFactor;

                final ScrollHolder scrollHolder = generateScrollHolder(newImageScaleFactor);

                mRegionData.setImageLeft(scrollHolder.mScrollXMax == 0 ? 0 :
                        Math.max(0, Math.min(scrollHolder.mScrollXMax, (newImageCenterX - getWidth() * HALF) / scrollHolder.mScrollXMax)));
                mRegionData.setImageTop(scrollHolder.mScrollYMax == 0 ? 0 :
                        Math.max(0, Math.min(scrollHolder.mScrollYMax, (newImageCenterY - getHeight() * HALF) / scrollHolder.mScrollYMax)));
            }

            @Override
            public boolean onScaleBegin(final ScaleGestureDetector detector) {
                return null != mScrollHolder;
            }
        });
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(final MotionEvent e) {
                return null != mOnClickListener || null != mScrollHolder;
            }

            @Override
            public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
                if (null != mScrollHolder) {
                    final float curScrollX = getScrollX();
                    final float curScrollY = getScrollY();

                    scrollTo((int) (curScrollX + distanceX), (int) (curScrollY + distanceY));
                }
                return true;
            }

            @Override
            public boolean onSingleTapUp(final MotionEvent e) {
                if (null != mOnClickListener) {
                    mOnClickListener.onClick(CollageItemView.this);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handle = false;
        if (null != mScrollHolder) {
            handle = mScaleGestureDetector.onTouchEvent(event);
        }
        handle |= mGestureDetector.onTouchEvent(event);
        return handle;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (oldw != 0 && oldh != 0) {
            mNeedInit = true;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mNeedInit) {
                    setRegionData(mRegionData);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(final OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setCollageRegion(final CollageRegion collageRegion) {
        mCollageRegion = collageRegion;
    }

    public CollageRegion getCollageRegion() {
        return mCollageRegion;
    }

    public void setRegionData(final CollageRegionData regionData) {
        if (mRegionData != regionData) {
            mNeedInit = true;
            mRegionData = regionData;
            updateRegionData();
        } else if (mNeedInit) {
            updateRegionData();
        }
    }

    private void updateRegionData() {
        mScrollHolder = null;
        setImageMatrix(null);
        setScaleType(ScaleType.FIT_XY);

        if (null == mRegionData) {
            mNeedInit = false;
            setImageResource(R.drawable.ic_action_new);
        } else {
            final int width = getWidth();
            final int height = getHeight();
            if (0 == width || 0 == height) {
                return;
            }
            mNeedInit = false;

            final int maxSize = Math.max(width, height);
            Picasso.with(getContext()).load(mRegionData.getImageFile()).resize(maxSize, maxSize).skipMemoryCache().noFade()
                    .error(R.drawable.ic_action_new).into(this, new Callback() {
                        @Override
                        public void onSuccess() {
                            final int width = getWidth();
                            final int height = getHeight();
                            if (0 == width || 0 == height) {
                                return;
                            }
                            final Drawable drawable = getDrawable();
                            if (null == drawable) {
                                return;
                            }
                            final int bitmapWidth = drawable.getIntrinsicWidth();
                            final int bitmapHeight = drawable.getIntrinsicHeight();
                            if (bitmapWidth < 0 || bitmapHeight < 0) {
                                return;
                            }
                            final int maxSize = Math.max(width, height);

                            mMinScale = 1f * maxSize / bitmapWidth;
                            mMaxScale = mMinScale * MAX_SCALE_FACTOR;

                            updateScale();
                        }

                        @Override
                        public void onError() {
                            // pass
                        }
                    }
            );
        }
    }

    private void updateScale() {
        final float scaleFactor = mMinScale * mRegionData.getImageScale();
        mScrollHolder = generateScrollHolder(scaleFactor);
        final Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);

        if (mRegionData.getImageLeft() == null || mRegionData.getImageTop() == null) {
            scrollTo(mScrollHolder.mScrollXMax / 2, mScrollHolder.mScrollYMax / 2);
        } else {
            CollageItemView.super.scrollTo((int) (mRegionData.getImageLeft() * mScrollHolder.mScrollXMax),
                    (int) (mRegionData.getImageTop() * mScrollHolder.mScrollYMax));
        }
    }

    private ScrollHolder generateScrollHolder(final float scaleFactor) {
        final int width = getWidth();
        final int height = getHeight();
        if (0 == width || 0 == height) {
            return ScrollHolder.EMPTY;
        }
        final Drawable drawable = getDrawable();
        if (null == drawable) {
            return ScrollHolder.EMPTY;
        }
        final int bitmapWidth = drawable.getIntrinsicWidth();
        final int bitmapHeight = drawable.getIntrinsicHeight();
        if (bitmapWidth < 0 || bitmapHeight < 0) {
            return ScrollHolder.EMPTY;
        }

        final int scrollXMax = Math.abs(Math.round(width - bitmapHeight * scaleFactor));
        final int scrollYMax = Math.abs(Math.round(height - bitmapWidth * scaleFactor));
        return new ScrollHolder(scrollXMax, scrollYMax);
    }

    @Override
    public void scrollTo(final int x, final int y) {
        int newScrollX = x;
        int newScrollY = y;
        if (null != mScrollHolder) {
            if (newScrollX > mScrollHolder.mScrollXMax) {
                newScrollX = mScrollHolder.mScrollXMax;
            }
            if (newScrollX < 0) {
                newScrollX = 0;
            }
            if (newScrollY > mScrollHolder.mScrollYMax) {
                newScrollY = mScrollHolder.mScrollYMax;
            }
            if (newScrollY < 0) {
                newScrollY = 0;
            }

            mRegionData.setImageLeft(mScrollHolder.mScrollXMax == 0 ? 0 : 1f * newScrollX / mScrollHolder.mScrollXMax);
            mRegionData.setImageTop(mScrollHolder.mScrollYMax == 0 ? 0 : 1f * newScrollY / mScrollHolder.mScrollYMax);
        }

        super.scrollTo(newScrollX, newScrollY);
    }

    private static class ScrollHolder {

        static final ScrollHolder EMPTY = new ScrollHolder();

        int mScrollXMax;
        int mScrollYMax;

        ScrollHolder(final int scrollXMax, final int scrollYMax) {
            mScrollXMax = scrollXMax;
            mScrollYMax = scrollYMax;
        }

        ScrollHolder() {
        }
    }
}