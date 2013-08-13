package com.jeremyfeinstein.slidingmenu.lib.actionbar;

import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

public final class ActionBarHelper {

	public static final String TAG = "ActionBarHelper";

	public static final boolean DEBUG = false;

	private final Activity mActivity;

	private final ActionBarUpIndicator mUpIndicator;

	private boolean mUsesCompat;

	public ActionBarHelper(Activity activity) {
		mActivity = activity;

		try {
			Class<?> clazz = activity.getClass();
			Method m = clazz.getMethod("getSupportActionBar");
			mUsesCompat = true;
		} catch (NoSuchMethodException e) {
			if (DEBUG) {
				Log.e(TAG, "Activity " + activity.getClass().getSimpleName()
						+ " does not use a compatibility action bar", e);
			}
		}

		mUpIndicator = getUpIndicator();
	}

	/**
	 * Get the concrete and correct {@link ActionBarUpIndicator} depending on
	 * the Android version
	 * 
	 * @return
	 */
	private ActionBarUpIndicator getUpIndicator() {
		if (mUsesCompat
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new CompatUpIndicator(mActivity);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new NativeUpIndicator(mActivity);
		}

		return null;
	}

	/**
	 * Set the {@link ActionBarUpIndicator}s drawable and content description
	 * 
	 * @param drawable
	 * @param contentDesc
	 */
	public void setActionBarUpIndicator(Drawable drawable, int contentDesc) {

		if (mUpIndicator != null) {
			mUpIndicator.setDrawable(mActivity, drawable);
			mUpIndicator.setContentDescription(mActivity, contentDesc);
		}
	}

	/**
	 * Set the content description of the up indicator
	 * 
	 * @param contentDesc
	 */
	public void setActionBarUpDescription(int contentDesc) {

		if (mUpIndicator != null) {
			mUpIndicator.setContentDescription(mActivity, contentDesc);
		}

	}

	/**
	 * Get the up indicator that has been set by the app theme
	 * 
	 * @return The themed up indicator or null
	 */
	public Drawable getThemeUpIndicator() {

		if (mUpIndicator != null) {
			return mUpIndicator.getThemeUpIndicator(mActivity);
		}

		return null;
	}

	/**
	 * 
	 * @param enabled
	 */
	public void setDisplayShowHomeAsUpEnabled(boolean enabled) {
		if (mUpIndicator != null)
			mUpIndicator.setDisplayHomeAsUpEnabled(mActivity, enabled);
	}
}
