package com.slidingmenu.lib.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivityHelper {

	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	
	private boolean mViewBehindSet = false;
	
	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenu = new SlidingMenu(mActivity);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (!mViewBehindSet) {
			throw new IllegalStateException("Both setBehindContentView must be called " +
					"in onCreate in addition to setContentView.");
		}
		ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
		LinearLayout newDecor = new LinearLayout(mActivity);
		while (decor.getChildCount() > 0) {
			View child = decor.getChildAt(0);
			decor.removeView(child);
			newDecor.addView(child);
		}
		decor.addView(mSlidingMenu, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mSlidingMenu.setViewAbove(newDecor);
	}
	
	public View findViewById(int id) {
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null) 
				return v;
		}
		return mActivity.findViewById(id);
	}

	public void setBehindContentView(View v, LayoutParams params) {
		mSlidingMenu.setViewBehind(v);
		mViewBehindSet = true;
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
