package com.jeremyfeinstein.slidingmenu.lib.actionbar;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * This is the implementation of the {@link ActionBarUpIndicator} for the native
 * ActionBar
 * 
 * @author Hannes Dorfmann
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NativeUpIndicator implements ActionBarUpIndicator {

	private static final int[] THEME_ATTRS = new int[] { android.R.attr.homeAsUpIndicator };

	public Method setHomeAsUpIndicator;
	public Method setHomeActionContentDescription;
	public ImageView upIndicatorView;

	public NativeUpIndicator(Activity activity) {
		try {
			setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod(
					"setHomeAsUpIndicator", Drawable.class);
			setHomeActionContentDescription = ActionBar.class
					.getDeclaredMethod("setHomeActionContentDescription",
							Integer.TYPE);

			// If we got the method we won't need the stuff below.
			return;
		} catch (Throwable t) {
			// Oh well. We'll use the other mechanism below instead.
		}

		final View home = activity.findViewById(android.R.id.home);
		if (home == null) {
			// Action bar doesn't have a known configuration, an OEM messed
			// with things.
			return;
		}

		final ViewGroup parent = (ViewGroup) home.getParent();
		final int childCount = parent.getChildCount();
		if (childCount != 2) {
			// No idea which one will be the right one, an OEM messed with
			// things.
			return;
		}

		final View first = parent.getChildAt(0);
		final View second = parent.getChildAt(1);
		final View up = first.getId() == android.R.id.home ? second : first;

		if (up instanceof ImageView) {
			// Jackpot! (Probably...)
			upIndicatorView = (ImageView) up;
		}
	}

	@Override
	public void setContentDescription(Activity activity, int contentDescResId) {

		try {
			final ActionBar actionBar = activity.getActionBar();
			setHomeActionContentDescription.invoke(actionBar, contentDescResId);
		} catch (Throwable t) {
			if (ActionBarHelper.DEBUG)
				Log.e(ActionBarHelper.TAG,
						"Couldn't set content description via JB-MR2 API", t);
		}
	}

	@Override
	public void setDrawable(Activity activity, Drawable drawable) {
		if (setHomeAsUpIndicator != null) {
			try {
				final ActionBar actionBar = activity.getActionBar();
				setHomeAsUpIndicator.invoke(actionBar, drawable);

			} catch (Throwable t) {
				if (ActionBarHelper.DEBUG)
					Log.e(ActionBarHelper.TAG,
							"Couldn't set home-as-up indicator via JB-MR2 API",
							t);
			}
		} else if (upIndicatorView != null) {
			upIndicatorView.setImageDrawable(drawable);
		} else {
			if (ActionBarHelper.DEBUG)
				Log.e(ActionBarHelper.TAG, "Couldn't set home-as-up indicator");
		}

	}

	@Override
	public void setDisplayHomeAsUpEnabled(Activity activity, boolean enabled) {

		ActionBar actionBar = activity.getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(enabled);
		}

	}

	@Override
	public Drawable getThemeUpIndicator(Activity activity) {

		final TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);
		final Drawable result = a.getDrawable(0);
		a.recycle();
		return result;

	}
}