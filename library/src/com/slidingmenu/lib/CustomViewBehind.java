package com.slidingmenu.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewBehind extends CustomViewAbove {
	
	private static final String TAG = "CustomViewBehind";

	public CustomViewBehind(Context context) {
		this(context, null);
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs, false);
	}

	public int getDestScrollX() {
		if (isMenuOpen()) {
			return getBehindWidth();
		} else {
			return 0;
		}
	}

	public int getChildLeft(int i) {
		return 0;
	}

	public int getChildRight(int i) {
		return getChildLeft(i) + getChildWidth(i);
	}

	public boolean isMenuOpen() {
		return getScrollX() == 0;
	}

	public int getCustomWidth() {
		int i = isMenuOpen()? 0 : 1;
		return getChildWidth(i);
	}

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
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}

}
