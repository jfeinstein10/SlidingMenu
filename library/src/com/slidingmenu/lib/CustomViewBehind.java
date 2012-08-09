package com.slidingmenu.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewBehind extends CustomViewAbove {
	
	private static final String TAG = "CustomViewBehind";
	private boolean mChildrenEnabled;

	public CustomViewBehind(Context context) {
		this(context, null);
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs, false);
	}

	public int getChildLeft(int i) {
		return 0;
	}

	@Override
	public int getCustomWidth() {
		int i = isMenuOpen()? 0 : 1;
		return getChildWidth(i);
	}

	@Override
	public int getChildWidth(int i) {
		if (i <= 0) {
			return getBehindWidth();
		} else {
			return getChildAt(i).getMeasuredWidth();
		}
	}

	public int getBehindWidth() {
		ViewGroup.LayoutParams params = getLayoutParams();
		return params.width;
	}

	@Override
	public void setContent(View v) {
		super.setMenu(v);
	}
	
	public void setChildrenEnabled(boolean enabled) {
		mChildrenEnabled = enabled;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return !mChildrenEnabled;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}

}
