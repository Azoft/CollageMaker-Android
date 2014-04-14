package com.redmadrobot.azoft.collage.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
		}
		else {
			resultView = convertView;
			holder = (Holder) resultView.getTag(R.id.holder);
		}
		final Post post = getItem(position);

		holder.mProgressView.setVisibility(View.VISIBLE);

		Picasso.with(mLayoutInflater.getContext()).load(post.getThumbnailImage().getUrl())
				.resizeDimen(R.dimen.item_image_selection_grid_size, R.dimen.item_image_selection_grid_size).centerInside()
				.error(R.drawable.ic_action_picture).into(holder.mImageView, new Callback() {
			@Override
			public void onSuccess() {
				holder.mProgressView.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				holder.mProgressView.setVisibility(View.GONE);
			}
		});

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
}