package com.slidingmenu.lib.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

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
			ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
			LinearLayout newDecor = new LinearLayout(mActivity);
			while (decor.getChildCount() > 0) {
				View child = decor.getChildAt(0);
				decor.removeView(child);
				newDecor.addView(child);
			}
			decor.addView(mSlidingMenu);
			mSlidingMenu.setViewAbove(newDecor);
		} else {
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
