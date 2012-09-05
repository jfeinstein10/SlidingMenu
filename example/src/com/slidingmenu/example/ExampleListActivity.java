package com.slidingmenu.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExampleListActivity extends PreferenceActivity {

	private ActivityAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		this.addPreferencesFromResource(R.xml.main);
		//		mAdapter = new ActivityAdapter(this);
		//		mAdapter.addInfo("Sliding Title Bar", new Intent(this, SlidingTitleBar.class));
		//		mAdapter.addInfo("Sliding Content", new Intent(this, SlidingContent.class));
		//		mAdapter.addInfo("Custom Opening Animation (Zoom)", new Intent(this, CustomZoomAnimation.class));
		//		mAdapter.addInfo("Custom Opening Animation (Rotate)", new Intent(this, CustomRotateAnimation.class));
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
		Class cls = null;
		String title = pref.getTitle().toString();
		if (title.equals(getString(R.string.properties))) {
			cls = PropertiesActivity.class;	
		} else if (title.equals(getString(R.string.title_bar_slide))) {
			cls = SlidingTitleBar.class;
		} else if (title.equals(getString(R.string.title_bar_content))) {
			cls = SlidingContent.class;
		} else if (title.equals(getString(R.string.anim_zoom))) {
			cls = CustomZoomAnimation.class;
		} else if (title.equals(getString(R.string.anim_scale))) {
			cls = CustomScaleAnimation.class;
		} else if (title.equals(getString(R.string.anim_fold))) {
			cls = CustomFoldAnimation.class;
		} else if (title.equals(getString(R.string.anim_slide))) {
			cls = CustomSlideAnimation.class;
		} else if (title.equals(getString(R.string.anim_rot))) {
			cls = CustomRotateAnimation.class;
		}
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		return true;
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
