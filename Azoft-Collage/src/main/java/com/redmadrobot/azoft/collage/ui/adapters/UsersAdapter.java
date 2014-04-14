package com.redmadrobot.azoft.collage.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Date: 4/8/2014
 * Time: 1:09 PM
 *
 * @author MiG35
 */
public class UsersAdapter extends BaseAdapter {

	private final LayoutInflater mLayoutInflater;
	private List<User> mUsers;

	public UsersAdapter(final Context context, final List<User> users) {
		mLayoutInflater = LayoutInflater.from(context);
		mUsers = users;
	}

	@Override
	public int getCount() {
		return null == mUsers ? 0 : mUsers.size();
	}

	@Override
	public User getItem(final int position) {
		return mUsers.get(position);
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
			resultView = mLayoutInflater.inflate(R.layout.item_user, parent, false);
			holder = new Holder(resultView);
			//noinspection ConstantConditions
			resultView.setTag(R.id.holder, holder);
		}
		else {
			resultView = convertView;
			holder = (Holder) resultView.getTag(R.id.holder);
		}
		final User user = getItem(position);

		final String userName = getUserName(user);
		holder.mNickNameTextView.setText(user.getNickName());
		holder.mNameTextView.setText(userName);
		holder.mNameTextView.setVisibility(null == userName ? View.GONE : View.VISIBLE);

		Picasso.with(mLayoutInflater.getContext()).load(user.getProfilePicture())
				.resizeDimen(R.dimen.item_user_picture_size, R.dimen.item_user_picture_size).centerInside().error(R.drawable.ic_action_user)
				.into(holder.mPictureImageView);

		return resultView;
	}

	private String getUserName(final User user) {
		final StringBuilder result = new StringBuilder(10);
		if (!TextUtils.isEmpty(user.getFirstName())) {
			result.append(user.getFirstName());

		}
		if (!TextUtils.isEmpty(user.getLastName())) {
			if (result.length() != 0) {
				result.append(' ');
			}
			result.append(user.getLastName());
		}
		if (result.length() == 0) {
			return null;
		}
		return result.toString();
	}

	public void setUsers(final List<User> users) {
		mUsers = users;
		notifyDataSetChanged();
	}

	private static class Holder {


		final ImageView mPictureImageView;
		final TextView mNickNameTextView;
		final TextView mNameTextView;

		Holder(final View resultView) {
			mPictureImageView = (ImageView) resultView.findViewById(R.id.iv_picture);
			mNickNameTextView = (TextView) resultView.findViewById(R.id.tv_nickname);
			mNameTextView = (TextView) resultView.findViewById(R.id.tv_name);
		}
	}
}