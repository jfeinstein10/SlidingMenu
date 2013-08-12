package com.jeremyfeinstein.slidingmenu.lib.actionbar;

import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * This is the implementation of the {@link ActionBarUpIndicator} for the compat
 * libraries like ActionBarSherlock ActionBar. I assume that the compat Activity
 * has a mehtod <code>getSupportActionBar</code>.
 * 
 * @author Hannes Dorfmann
 * 
 */
public class CompatUpIndicator implements ActionBarUpIndicator {

	private ImageView mUpIndicatorView;
	private Object mActionBar;
	private Method mHomeAsUpEnabled;

	public CompatUpIndicator(Activity activity) {
		try {
			String appPackage = activity.getPackageName();

			try {
				// Attempt to find ActionBarSherlock up indicator
				final int homeId = activity.getResources().getIdentifier(
						"abs__home", "id", appPackage);
				View v = activity.findViewById(homeId);
				ViewGroup parent = (ViewGroup) v.getParent();
				final int upId = activity.getResources().getIdentifier(
						"abs__up", "id", appPackage);
				mUpIndicatorView = (ImageView) parent.findViewById(upId);
			} catch (Throwable t) {
				if (ActionBarHelper.DEBUG) {
					Log.e(ActionBarHelper.TAG, "ABS action bar not found", t);
				}
			}

			if (mUpIndicatorView == null) {
				// Attempt to find AppCompat up indicator
				final int homeId = activity.getResources().getIdentifier(
						"home", "id", appPackage);
				View v = activity.findViewById(homeId);
				ViewGroup parent = (ViewGroup) v.getParent();
				final int upId = activity.getResources().getIdentifier("up",
						"id", appPackage);
				mUpIndicatorView = (ImageView) parent.findViewById(upId);
			}

			Class<?> supportActivity = activity.getClass();
			Method getActionBar = supportActivity
					.getMethod("getSupportActionBar");

			mActionBar = getActionBar.invoke(activity, null);
			Class<?> supportActionBar = mActionBar.getClass();
			mHomeAsUpEnabled = supportActionBar.getMethod(
					"setDisplayHomeAsUpEnabled", Boolean.TYPE);

		} catch (Throwable t) {
			if (ActionBarHelper.DEBUG) {
				Log.e(ActionBarHelper.TAG,
						"Unable to init IndicatorWrapper for ABS", t);
			}
		}
	}

	@Override
	public void setContentDescription(Activity activity, int contentDescResId) {

		if (mUpIndicatorView != null) {
			final String contentDescription = contentDescResId == 0 ? null
					: activity.getString(contentDescResId);
			mUpIndicatorView.setContentDescription(contentDescription);
		}

	}

	@Override
	public void setDrawable(Activity activity, Drawable drawable) {

		if (mUpIndicatorView != null) {
			mUpIndicatorView.setImageDrawable(drawable);
		}

	}

	@Override
	public void setDisplayHomeAsUpEnabled(Activity activity, boolean enabled) {

		if (mHomeAsUpEnabled != null) {
			try {
				mHomeAsUpEnabled.invoke(mActionBar, enabled);
			} catch (Throwable t) {
				if (ActionBarHelper.DEBUG) {
					Log.e(ActionBarHelper.TAG,
							"Unable to call setHomeAsUpEnabled", t);
				}
			}
		}

	}

	@Override
	public Drawable getThemeUpIndicator(Activity activity) {

		if (mUpIndicatorView != null) {
			return mUpIndicatorView.getDrawable();
		}

		return null;
	}

}
