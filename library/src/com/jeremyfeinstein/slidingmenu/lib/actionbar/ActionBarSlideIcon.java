package com.jeremyfeinstein.slidingmenu.lib.actionbar;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 
 * @author Hannes Dorfmann
 * 
 */
public class ActionBarSlideIcon {

	/**
	 * This is a drawable can slide. This will wrap the home icon and allows
	 * that to slide
	 * 
	 * @author Hannes Dorfmann
	 * 
	 */
	private static class SlideDrawable extends Drawable implements
			Drawable.Callback {
		private Drawable mWrapped;
		private float mOffset;
		private float mOffsetBy;

		private final Rect mTmpRect = new Rect();

		public SlideDrawable(Drawable wrapped) {
			mWrapped = wrapped;
		}

		public void setOffset(float offset) {
			mOffset = offset;
			invalidateSelf();
		}

		public float getOffset() {
			return mOffset;
		}

		public void setOffsetBy(float offsetBy) {
			mOffsetBy = offsetBy;
			invalidateSelf();
		}

		@Override
		public void draw(Canvas canvas) {
			mWrapped.copyBounds(mTmpRect);
			canvas.save();
			canvas.translate(mOffsetBy * mTmpRect.width() * -mOffset, 0);
			mWrapped.draw(canvas);
			canvas.restore();

		}

		@Override
		public void setChangingConfigurations(int configs) {
			mWrapped.setChangingConfigurations(configs);
		}

		@Override
		public int getChangingConfigurations() {
			return mWrapped.getChangingConfigurations();
		}

		@Override
		public void setDither(boolean dither) {
			mWrapped.setDither(dither);
		}

		@Override
		public void setFilterBitmap(boolean filter) {
			mWrapped.setFilterBitmap(filter);
		}

		@Override
		public void setAlpha(int alpha) {
			mWrapped.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mWrapped.setColorFilter(cf);
		}

		@Override
		public void setColorFilter(int color, PorterDuff.Mode mode) {
			mWrapped.setColorFilter(color, mode);
		}

		@Override
		public void clearColorFilter() {
			mWrapped.clearColorFilter();
		}

		@Override
		public boolean isStateful() {
			return mWrapped.isStateful();
		}

		@Override
		public boolean setState(int[] stateSet) {
			return mWrapped.setState(stateSet);
		}

		@Override
		public int[] getState() {
			return mWrapped.getState();
		}

		@Override
		public Drawable getCurrent() {
			return mWrapped.getCurrent();
		}

		@Override
		public boolean setVisible(boolean visible, boolean restart) {
			return super.setVisible(visible, restart);
		}

		@Override
		public int getOpacity() {
			return mWrapped.getOpacity();
		}

		@Override
		public Region getTransparentRegion() {
			return mWrapped.getTransparentRegion();
		}

		@Override
		protected boolean onStateChange(int[] state) {
			mWrapped.setState(state);
			return super.onStateChange(state);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mWrapped.setBounds(bounds);
		}

		@Override
		public int getIntrinsicWidth() {
			return mWrapped.getIntrinsicWidth();
		}

		@Override
		public int getIntrinsicHeight() {
			return mWrapped.getIntrinsicHeight();
		}

		@Override
		public int getMinimumWidth() {
			return mWrapped.getMinimumWidth();
		}

		@Override
		public int getMinimumHeight() {
			return mWrapped.getMinimumHeight();
		}

		@Override
		public boolean getPadding(Rect padding) {
			return mWrapped.getPadding(padding);
		}

		@Override
		public ConstantState getConstantState() {
			return super.getConstantState();
		}

		@Override
		public void invalidateDrawable(Drawable who) {
			if (who == mWrapped) {
				invalidateSelf();
			}
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			if (who == mWrapped) {
				scheduleSelf(what, when);
			}
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			if (who == mWrapped) {
				unscheduleSelf(what);
			}
		}
	}

	/**
	 * The {@link SlideDrawable} that will replace the up Indicator
	 */
	private SlideDrawable mSlideDrawble;

	private ActionBarHelper mActionBarHelper = null;

	private int mCloseContentDescription;
	private int mOpenContentDescription;

	/**
	 * Creates a new {@link ActionBarSlideIcon}
	 * 
	 * @param activity
	 *            The activity
	 * @param slideDrawable
	 *            The {@link Drawable} that can slide
	 * @param openContentDescRes
	 *            A String resource to describe the "open drawer" action for
	 *            accessibility
	 * @param closeContentDescRes
	 *            A String resource to describe the "close drawer" action for
	 *            accessibility
	 */
	public ActionBarSlideIcon(Activity activity, Drawable slideDrawable,
			int openContentDescRes, int closeContentDescRes) {

		initActionBar(activity, slideDrawable, openContentDescRes,
				closeContentDescRes);
	}

	/**
	 * Creates a new {@link ActionBarSlideIcon}
	 * 
	 * @param activity
	 *            The activity
	 * @param drawableRes
	 *            The resource id of the drawable that will replace the up
	 *            indicator icon
	 * @param openContentDescRes
	 *            A String resource to describe the "open drawer" action for
	 *            accessibility
	 * @param closeContentDescRes
	 *            A String resource to describe the "close drawer" action for
	 *            accessibility
	 */
	public ActionBarSlideIcon(Activity activity, int drawableRes,
			int openContentDescRes, int closeContentDescRes) {

		this(activity, activity.getResources().getDrawable(drawableRes),
				openContentDescRes, closeContentDescRes);
	}

	/**
	 * Creates a new {@link ActionBarSlideIcon}. Instead of specifying the
	 * drawable that should be replace the up indicator the default up indicator
	 * (specified in the apps theme) will be used to slide
	 * 
	 * @param activity
	 * @param openContentDescRes
	 * @param closeContentDescRes
	 */
	public ActionBarSlideIcon(Activity activity, int openContentDescRes,
			int closeContentDescRes) {

		if (mActionBarHelper == null)
			mActionBarHelper = new ActionBarHelper(activity);

		Drawable themedIcon = mActionBarHelper.getThemeUpIndicator();

		if (themedIcon == null)
			throw new IllegalStateException(
					"The theme of you app has not specified an up indicator icon");

		initActionBar(activity, themedIcon, openContentDescRes,
				closeContentDescRes);

	}

	/**
	 * Initializes the required components. This method is called from the
	 * constructors.
	 * 
	 * @param activity
	 * @param slideDrawable
	 * @param openContentDescRes
	 * @param closeContentDescRes
	 */
	private void initActionBar(Activity activity, Drawable slideDrawable,
			int openContentDescRes, int closeContentDescRes) {

		mCloseContentDescription = closeContentDescRes;
		mOpenContentDescription = openContentDescRes;

		mSlideDrawble = new SlideDrawable(slideDrawable);
		mSlideDrawble.setOffsetBy(1.f / 3);

		if (mActionBarHelper == null)
			mActionBarHelper = new ActionBarHelper(activity);

		mActionBarHelper.setActionBarUpIndicator(mSlideDrawble,
				mOpenContentDescription);
		mActionBarHelper.setDisplayShowHomeAsUpEnabled(true);
	}

	/**
	 * Get the {@link Drawable} that will slide
	 * 
	 * @return
	 */
	public Drawable getDrawable() {
		return mSlideDrawble.mWrapped;
	}

	/**
	 * Set the {@link Drawable} of the ActionBar
	 * 
	 * @param drawable
	 */
	public void setDrawable(Drawable drawable) {
		mSlideDrawble.mWrapped = drawable;
		mSlideDrawble.invalidateSelf();
	}

	/**
	 * Set the offset, how far the menu has been slide out. This method should
	 * only be accessed from {@link SlidingMenu}
	 * 
	 * @param offset
	 */
	public void setSlideOffset(float offset) {

		if (offset == 0)
			mActionBarHelper.setActionBarUpDescription(mOpenContentDescription);

		if (offset == 1)
			mActionBarHelper
					.setActionBarUpDescription(mCloseContentDescription);

		float glyphOffset = mSlideDrawble.getOffset();
		if (offset > 0.5f) {
			glyphOffset = Math.max(glyphOffset,
					Math.max(0.f, offset - 0.5f) * 2);
		} else {
			glyphOffset = Math.min(glyphOffset, offset * 2);
		}
		mSlideDrawble.setOffset(glyphOffset);

	}

}
