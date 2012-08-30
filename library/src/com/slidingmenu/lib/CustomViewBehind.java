package com.slidingmenu.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.slidingmenu.lib.SlidingMenu.SlidingMenuCanvasTransformer;

public class CustomViewBehind extends CustomViewAbove {

	private static final String TAG = "CustomViewBehind";

	private CustomViewAbove mViewAbove;
	private SlidingMenuCanvasTransformer mTransformer;
	private boolean mChildrenEnabled;

	public CustomViewBehind(Context context) {
		this(context, null);
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs, false);
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
		if (!mChildrenEnabled)
			return !mChildrenEnabled;
		return !mChildrenEnabled;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		mScrollX = x;
	}

	private float mScrollX = 0.0f;
	private boolean mFadeEnabled = true;
	private float mFadeDegree = 0.5f;
	private final Paint mBehindFadePaint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mTransformer != null)
			mTransformer.transformCanvas(canvas, (int) (mScrollX / mViewAbove.getScrollScale()));
		
		if (mFadeEnabled) {
			float openPercent = mScrollX / (getWidth() * mViewAbove.getScrollScale());
			onDrawBehindFade(canvas, openPercent);
		}
	}

	private void onDrawBehindFade(Canvas canvas, float openPercent) {
		final int alpha = (int) (mFadeDegree * 255 * openPercent);
		if (alpha > 0) {
			mBehindFadePaint.setColor(Color.argb(alpha, 255, 0, 0));
			canvas.drawRect(0, 0, getWidth(), getHeight(), mBehindFadePaint);
		}
	}

	public void setBehindFadeEnabled(boolean b) {
		mFadeEnabled = b;
	}

	public void setBehindFadeDegree(float f) {
		mFadeDegree = f;
	}

}
