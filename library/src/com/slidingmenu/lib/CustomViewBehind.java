package com.slidingmenu.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomViewBehind extends CustomViewAbove {

	private static final String TAG = "CustomViewBehind";
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private CustomViewAbove mViewAbove;
	private int mMode;
	private CanvasTransformer mTransformer;
	private boolean mChildrenEnabled;

	public CustomViewBehind(Context context, int mode) {
		super(context);
		if (mode != LEFT && mode != RIGHT)
			throw new IllegalStateException("mode must be LEFT or RIGHT");
		mMode = mode;
	}

	public void setCustomViewAbove(CustomViewAbove customViewAbove) {
		mViewAbove = customViewAbove;
		mViewAbove.setTouchModeBehind(mTouchMode);
	}

	public void setTouchMode(int i) {
		mTouchMode = i;
		if (mViewAbove != null)
			mViewAbove.setTouchModeBehind(i);
	}

	public void setCanvasTransformer(CanvasTransformer t) {
		mTransformer = t;
	}

	public int getChildLeft(int i) {
		return 0;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContent.layout(0, 0, width, height);
	}

	@Override
	public int getCustomWidth() {
		int i = isLeftOpen()? 0 : 1;
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

	public void setChildrenEnabled(boolean enabled) {
		mChildrenEnabled = enabled;
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		if (mTransformer != null)
			invalidate();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return !mChildrenEnabled;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mTransformer != null) {
			canvas.save();
			float percent = 0.0f;
			switch (mMode) {
			case LEFT:
				percent = mViewAbove.getPercentOpenLeft();
				break;
			case RIGHT:
				percent = mViewAbove.getPercentOpenRight();
				break;
			}
			mTransformer.transformCanvas(canvas, percent);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else
			super.dispatchDraw(canvas);
	}

}
