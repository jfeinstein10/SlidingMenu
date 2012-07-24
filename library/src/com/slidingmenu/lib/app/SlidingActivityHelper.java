package com.slidingmenu.lib.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivityHelper {

	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	private View mViewAbove;
	private boolean mBroadcasting = false;

	private boolean mViewBehindSet = false;
	private boolean mOnPostCreateCalled = false;
	private boolean mEnableSlide = true;

	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenu = (SlidingMenu) mActivity.getLayoutInflater().inflate(R.layout.slidingmenumain, null);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (!mViewBehindSet || mViewAbove == null) {
			throw new IllegalStateException("Both setBehindContentView must be called " +
					"in onCreate in addition to setContentView.");
		}

		mOnPostCreateCalled = true;

		if (mEnableSlide) {
			if (Build.VERSION.SDK_INT >= 14) {
				// make sure it clears the status bar
				mSlidingMenu.setFitsSystemWindows(true);
			}
			// move everything into the SlidingMenu
			ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			decor.removeView(decorChild);
			mSlidingMenu.setViewAbove(decorChild);
			decor.addView(mSlidingMenu);
		} else {
			// take the above view out of 
			ViewGroup parent = (ViewGroup) mViewAbove.getParent();
			if (parent != null) {
				parent.removeView(mViewAbove);
			}
			mSlidingMenu.setViewAbove(mViewAbove);
			mActivity.getWindow().setContentView(mSlidingMenu);
		}
	}

	public void setSlidingActionBarEnabled(boolean b) {
		if (mOnPostCreateCalled)
			throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
		mEnableSlide = b;
	}

	public View findViewById(int id) {
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null) 
				return v;
		}
		return null;
	}

	public void registerAboveContentView(View v, LayoutParams params) {
		if (!mBroadcasting)
			mViewAbove = v;
	}

	public void setContentView(View v) {
		mBroadcasting = true;
		mActivity.setContentView(v);
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
