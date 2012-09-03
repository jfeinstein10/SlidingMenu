package com.slidingmenu.example;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExampleListActivity extends ListActivity {
	
	private ActivityAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ActivityAdapter(this);
		mAdapter.addInfo("Sliding Title Bar", new Intent(this, SlidingTitleBar.class));
		mAdapter.addInfo("Sliding Content", new Intent(this, SlidingContent.class));
		mAdapter.addInfo("Custom Opening Animation", new Intent(this, CustomAnimation.class));
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ActivityInfo info = mAdapter.getItem(position);
		startActivity(info.intent);
	}
	
	public class ActivityInfo {
		public String name;
		public Intent intent;
		public ActivityInfo(String name, Intent intent) {
			this.name = name;
			this.intent = intent;
		}
	}
	
	public class ActivityAdapter extends ArrayAdapter<ActivityInfo> {

		public ActivityAdapter(Context context) {
			super(context, R.layout.row, R.id.row_title);
		}
		
		public void addInfo(String name, Intent intent) {
			this.add(new ActivityInfo(name, intent));
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setVisibility(View.GONE);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).name);
			return convertView;
		}
		
	}
}
