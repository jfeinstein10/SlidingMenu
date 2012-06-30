package com.slidingmenu.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slidingmenu.lib.CustomViewAbove.LayoutParams;
import com.slidingmenu.lib.actionbar.ActionBarActivity;
import com.slidingmenu.lib.actionbar.ActionBarHelper;

public class SlidingMenuActivity extends ActionBarActivity {

	private SlidingMenu mSlidingMenu;
	private View mLayout;
	private boolean mContentViewCalled = false;
	private boolean mBehindContentViewCalled = false;
	private SlidingMenuList mMenuList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.slidingmenumain);
		mSlidingMenu = (SlidingMenu) super.findViewById(R.id.slidingmenulayout);
		mLayout = super.findViewById(R.id.slidingmenulayout);
		mActionBarHelper.setRefreshActionItemState(true);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (!mContentViewCalled || !mBehindContentViewCalled) {
			throw new IllegalStateException("Both setContentView and " +
					"setBehindContentView must be called in onCreate.");
		}
		mSlidingMenu.setStatic(isStatic());
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	public void setContentView(View v) {
		setContentView(v, null);
	}

	public void setContentView(View v, LayoutParams params) {
		if (!mContentViewCalled) {
			mContentViewCalled = true;
		}
		RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				(int) getResources().getDimension(R.dimen.actionbar_compat_height));
		p1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layout.addView(mActionBarHelper.getActionBar(), p1);

		RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		p2.addRule(RelativeLayout.BELOW, mActionBarHelper.getActionBar().getId());
		p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layout.addView(v, p2);
		mSlidingMenu.setAboveContent(layout, params);
	}

	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	public void setBehindContentView(View v) {
		setBehindContentView(v, null);
	}

	public void setBehindContentView(View v, LayoutParams params) {
		if (!mBehindContentViewCalled) {
			mBehindContentViewCalled = true;
		}
		mSlidingMenu.setBehindContent(v);
	}

	private boolean isStatic() {
		return mLayout instanceof LinearLayout;
	}

	public int getBehindOffset() {
		// TODO
		return 0;
	}

	public void setBehindOffset(int i) {
		mSlidingMenu.setBehindOffset(i);
	}

	public float getBehindScrollScale() {
		// TODO
		return 0;
	}

	public void setBehindScrollScale(float f) {
		mSlidingMenu.setBehindScrollScale(f);
	}

	@Override
	public View findViewById(int id) {
		return mSlidingMenu.findViewById(id);
	}

	public void toggle() {
		//		if (isStatic()) return;
		if (mSlidingMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind();
		}
	}

	public void showAbove() {
		//		if (isStatic()) return;
		mSlidingMenu.showAbove();
	}

	public void showBehind() {
		//		if (isStatic()) return;
		mSlidingMenu.showBehind();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isBehindShowing()) {
			showAbove();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void addMenuListItem(MenuListItem mli) {
		mMenuList.add(mli);
	}

	public static class SlidingMenuList extends ListView {
		public SlidingMenuList(final Context context) {
			super(context);
			setAdapter(new SlidingMenuListAdapter(context));
			setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					OnClickListener listener = ((SlidingMenuListAdapter)getAdapter()).getItem(position).mListener;
					if (listener != null) listener.onClick(view);
				}				
			});
		}
		public void add(MenuListItem mli) {
			((SlidingMenuListAdapter)getAdapter()).add(mli);
		}
	}

	public static class SlidingMenuListAdapter extends ArrayAdapter<MenuListItem> {

		public SlidingMenuListAdapter(Context context) {
			super(context, 0);
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView != null) {
				v = convertView;
			} else {
				LayoutInflater inflater = 
						(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.slidingmenurow, null);
			}
			MenuListItem item = getItem(position);
			ImageView icon = (ImageView) v.findViewById(R.id.slidingmenurowicon);
			icon.setImageDrawable(item.mIcon);
			TextView title = (TextView) v.findViewById(R.id.slidingmenurowtitle);
			title.setText(item.mTitle);
			return v;
		}
	}

	public class MenuListItem {
		private Drawable mIcon;
		private String mTitle;
		private OnClickListener mListener;
		public MenuListItem(String title) {
			mTitle = title;
		}
		public void setTitle(String title) {
			mTitle = title;
		}
		public void setOnClickListener(OnClickListener listener) {
			mListener = listener;
		}
		public View toListViewRow() {
			View v = SlidingMenuActivity.this.getLayoutInflater().inflate(R.layout.slidingmenurow, null);
			((TextView)v.findViewById(R.id.slidingmenurowtitle)).setText(mTitle);
			((ImageView)v.findViewById(R.id.slidingmenurowicon)).setImageDrawable(mIcon);
			return v;
		}
	}



}
