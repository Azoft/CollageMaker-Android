package com.azoft.azoft.collage.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.Collage;
import com.azoft.azoft.collage.ui.widgets.CollageViewGroup;
import com.azoft.azoft.collage.utils.collagegenerators.CollageFactory;

/**
 * <p/>
 * Date: 4/8/2014
 * Time: 5:22 PM
 *
 * @author MiG35
 */
public class CollageAdapter extends BaseAdapter {

    private final CollageFactory mCollageFactory;
    private final LayoutInflater mLayoutInflater;

    private Integer mSelectedNumber;

    /**
     * Will show collage template items. Will use factory as collage generator.
     *
     * @param context        application or activity context
     * @param collageFactory factory for collage creation
     * @param selectedNumber previously selected collage. may be null if nothing selected.
     */
    public CollageAdapter(final Context context, final CollageFactory collageFactory, final Integer selectedNumber) {
        mLayoutInflater = LayoutInflater.from(context);
        mCollageFactory = collageFactory;
        mSelectedNumber = selectedNumber;
    }

    @Override
    public int getCount() {
        return mCollageFactory.getCollageCount();
    }

    @Override
    public Collage getItem(final int position) {
        return mCollageFactory.getCollage(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View resultView;
        final Holder holder;
        if (null == convertView) {
            resultView = mLayoutInflater.inflate(R.layout.item_collage, parent, false);
            holder = new Holder(resultView);
            //noinspection ConstantConditions
            resultView.setTag(R.id.holder, holder);
        } else {
            resultView = convertView;
            holder = (Holder) convertView.getTag(R.id.holder);
        }

        holder.mCollageViewGroup.setCollage(getItem(position));
        if (null == mSelectedNumber || mSelectedNumber != position) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }

        return resultView;
    }

    /**
     * Will set new selected collage
     */
    public void setSelected(final int position) {
        mSelectedNumber = position;
        notifyDataSetChanged();
    }

    private static class Holder {

        final ImageView mCheckBox;
        final CollageViewGroup mCollageViewGroup;

        Holder(final View resultView) {
            mCheckBox = (ImageView) resultView.findViewById(R.id.iv_selection_box);
            mCollageViewGroup = (CollageViewGroup) resultView.findViewById(R.id.collage_view_group);
        }
    }
}