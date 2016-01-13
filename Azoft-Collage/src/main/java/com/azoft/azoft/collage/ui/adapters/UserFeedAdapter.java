package com.azoft.azoft.collage.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Date: 4/9/2014
 * Time: 10:13 AM
 *
 * @author MiG35
 */
public class UserFeedAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final List<Post> mPosts;

    public UserFeedAdapter(final Context context, final List<Post> posts) {
        mLayoutInflater = LayoutInflater.from(context);
        mPosts = posts;
    }

    @Override
    public int getCount() {
        return null == mPosts ? 0 : mPosts.size();
    }

    @Override
    public Post getItem(final int position) {
        return mPosts.get(position);
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
            resultView = mLayoutInflater.inflate(R.layout.item_user_feed, parent, false);
            holder = new Holder(resultView);
            //noinspection ConstantConditions
            resultView.setTag(R.id.holder, holder);
        } else {
            resultView = convertView;
            holder = (Holder) resultView.getTag(R.id.holder);
        }
        final Post post = getItem(position);

        holder.mProgressView.setVisibility(View.GONE);
        holder.mImageView.setVisibility(View.VISIBLE);

        Picasso.with(mLayoutInflater.getContext()).load(post.getLowResolutionImage().getUrl())
                .resizeDimen(R.dimen.item_image_selection_grid_size, R.dimen.item_image_selection_grid_size).centerInside()
                .error(R.drawable.ic_action_picture).into(holder.mImageView, new ProgressCallback(holder.mProgressView));

        return resultView;
    }

    private static class Holder {

        private final ImageView mImageView;
        private final View mProgressView;

        Holder(final View resultView) {
            mImageView = (ImageView) resultView.findViewById(R.id.iv_user_feed);
            mProgressView = resultView.findViewById(R.id.pb_user_feed);
        }
    }

    private static class ProgressCallback implements Callback {

        private final WeakReference<View> mProgressViewRef;

        public ProgressCallback(final View progressView) {
            mProgressViewRef = new WeakReference<>(progressView);
        }

        @Override
        public void onSuccess() {
            doHide();
        }

        @Override
        public void onError() {
            doHide();
        }

        private void doHide() {
            final View view = mProgressViewRef.get();
            if (null != view) {
                view.setVisibility(View.GONE);
            }
            mProgressViewRef.clear();
        }
    }
}