package com.slidingmenu.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SlidingMenu extends RelativeLayout {

	public static final int TOUCHMODE_MARGIN = 0;
	public static final int TOUCHMODE_FULLSCREEN = 1;
	
	private CustomViewAbove mViewAbove;
	private CustomViewBehind mViewBehind;
	
	public SlidingMenu(Context context) {
		this(context, null);
	}
	
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewBehind = new CustomViewBehind(context);
		addView(mViewBehind, behindParams);
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewAbove = new CustomViewAbove(context);
		addView(mViewAbove, aboveParams);
		// register the CustomViewBehind2 with the CustomViewAbove
		mViewAbove.setCustomViewBehind2(mViewBehind);
//		Drawable bg = ((Activity)context).getWindow().getDecorView().getBackground();
//		mViewBehind.setBackgroundDrawable(bg);
//		mViewAbove.setBackgroundDrawable(bg);
		
		// now style everything!
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
		// set the above and behind views if defined in xml
		int viewAbove = ta.getResourceId(R.styleable.SlidingMenu_viewAbove, -1);
		if (viewAbove != -1) {
			View v = LayoutInflater.from(context).inflate(viewAbove, null);
			setViewAbove(v);
		}
		int viewBehind = ta.getResourceId(R.styleable.SlidingMenu_viewBehind, -1);
		if (viewBehind != -1) {
			View v = LayoutInflater.from(context).inflate(viewBehind, null);
			setViewBehind(v);
		}
		int touchModeAbove = ta.getInt(R.styleable.SlidingMenu_aboveTouchMode, TOUCHMODE_MARGIN);
		setTouchModeAbove(touchModeAbove);
		int touchModeBehind = ta.getInt(R.styleable.SlidingMenu_behindTouchMode, TOUCHMODE_MARGIN);
		setTouchModeBehind(touchModeBehind);
		// set the offset and scroll scale if defined in xml
		int offsetBehind = (int) ta.getDimension(R.styleable.SlidingMenu_behindOffset, 0);
		setBehindOffset(offsetBehind);
		float scrollOffsetBehind = ta.getFloat(R.styleable.SlidingMenu_behindScrollScale, 0.0f);
		setBehindScrollScale(scrollOffsetBehind);
		
		showAbove();
	}
	
	public void setViewAbove(int res) {
		setViewAbove(LayoutInflater.from(getContext()).inflate(res, null));
	}
	
	public void setViewAbove(View v) {
		mViewAbove.setContent(v);
		mViewAbove.invalidate();
		mViewAbove.dataSetChanged();
	}
	
	public void setViewBehind(int res) {
		setViewBehind(LayoutInflater.from(getContext()).inflate(res, null));
	}
	
	public void setViewBehind(View v) {
		mViewBehind.setContent(v);
		mViewBehind.invalidate();
		mViewBehind.dataSetChanged();
	}
	
	public void setSlidingEnabled(boolean b) {
		mViewAbove.setSlidingEnabled(b);
	}
	
	public boolean isSlidingEnabled() {
		return mViewAbove.isSlidingEnabled();
	}
	
	/**
	 * 
	 * @param b Whether or not the SlidingMenu is in a static mode 
	 * (i.e. nothing is moving and everything is showing)
	 */
	public void setStatic(boolean b) {
		if (b) {
			setSlidingEnabled(false);
			mViewAbove.setCustomViewBehind2(null);
			mViewAbove.setCurrentItem(1);
			mViewBehind.setCurrentItem(0);	
		} else {
			mViewAbove.setCurrentItem(1);
			mViewBehind.setCurrentItem(1);
			mViewAbove.setCustomViewBehind2(mViewBehind);
			setSlidingEnabled(true);
		}
	}

	/**
	 * Shows the behind view
	 */
	public void showBehind() {
		mViewAbove.setCurrentItem(0);
	}

	/**
	 * Shows the above view
	 */
	public void showAbove() {
		mViewAbove.setCurrentItem(1);
	}

	/**
	 * 
	 * @return Whether or not the behind view is showing
	 */
	public boolean isBehindShowing() {
		return mViewAbove.getCurrentItem() == 0;
	}
	
	/**
	 * 
	 * @return The margin on the right of the screen that the behind view scrolls to
	 */
	public int getBehindOffset() {
		return ((RelativeLayout.LayoutParams)mViewBehind.getLayoutParams()).rightMargin;
	}
	
	/**
	 * 
	 * @param i The margin on the right of the screen that the behind view scrolls to
	 */
	public void setBehindOffset(int i) {
		((RelativeLayout.LayoutParams)mViewBehind.getLayoutParams()).setMargins(0, 0, i, 0);
	}
	
	/**
	 * 
	 * @param res The dimension resource to be set as the behind offset
	 */
	public void setBehindOffsetRes(int res) {
		int i = (int) getContext().getResources().getDimension(res);
		setBehindOffset(i);
	}
	
	/**
	 * 
	 * @return The scale of the parallax scroll
	 */
	public float getBehindScrollScale() {
		return mViewAbove.getScrollScale();
	}
	
	/**
	 * 
	 * @param f The scale of the parallax scroll (i.e. 1.0f scrolls 1 pixel for every
	 * 1 pixel that the above view scrolls and 0.0f scrolls 0 pixels)
	 */
	public void setBehindScrollScale(float f) {
		mViewAbove.setScrollScale(f);
	}

	public int getTouchModeAbove() {
		return mViewAbove.getTouchModeAbove();
	}
	
	public void setTouchModeAbove(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
		}
		mViewAbove.setTouchModeAbove(i);
	}

	public int getTouchModeBehind() {
		return mViewAbove.getTouchModeBehind();
	}
	
	public void setTouchModeBehind(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
		}
		mViewAbove.setTouchModeBehind(i);
	}

}