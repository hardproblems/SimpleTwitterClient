package com.codepath.simpletwitterclient.app.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
	private static class ViewHolder {
		ImageView ivProfile;
		TextView tvName;
		TextView tvHandle;
		TextView tvTime;
		TextView tvBody;
	}

	public TweetsAdapter(Context context, List<Tweet> objects) {
		super(context, R.layout.item_tweet, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, null);
			viewHolder = new ViewHolder();
			viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);
			viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(tweet.user.profileImageUrl, viewHolder.ivProfile);
		viewHolder.tvName.setText(tweet.user.name);
		viewHolder.tvHandle.setText("@" + tweet.user.screenName);
		viewHolder.tvTime.setText(Utils.getRelativeDate(tweet));
		viewHolder.tvBody.setText(tweet.text);
		return convertView;
	}
}
