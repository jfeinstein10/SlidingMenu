package com.slidingmenu.lib.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivityHelper {

	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	private ViewGroup mContentView;
	private boolean mBehindContentViewCalled = false;

	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		// unregister the current content view
		mActivity.getWindow().getDecorView().findViewById(android.R.id.content).setId(View.NO_ID);
		// register a new content view
		mContentView = new RelativeLayout(mActivity);
		mContentView.setId(android.R.id.content);

		// customize based on type of Activity
		if (mActivity instanceof SlidingListActivity) {
            ListView lv = new ListView(mActivity);
            lv.setId(android.R.id.list);
            mContentView.addView(lv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }

		// set up the SlidingMenu
		mSlidingMenu = (SlidingMenu) LayoutInflater.from(mActivity).inflate(R.layout.slidingmenumain, null);
		mSlidingMenu.setViewAbove(mContentView);
		mSlidingMenu.setBackgroundDrawable(mActivity.getWindow().getDecorView().getBackground());
		mActivity.getWindow().setContentView(mSlidingMenu);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (!mBehindContentViewCalled) {
			throw new IllegalStateException("Both setBehindContentView must be called " +
					"in onCreate in addition to setContentView.");
		}
	}
	
	public void setBehindContentView(View v, LayoutParams params) {
		if (!mBehindContentViewCalled) {
			mBehindContentViewCalled = true;
		}
		mSlidingMenu.setViewBehind(v);
	}
	
	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}
	
	public void toggle() {
		if (mSlidingMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind();
		}
	}

	public void showAbove() {
		mSlidingMenu.showAbove();
	}

	public void showBehind() {
		mSlidingMenu.showBehind();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isBehindShowing()) {
			showAbove();
			return true;
		}
		return false;
	}

}
