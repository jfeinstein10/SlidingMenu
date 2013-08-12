package com.jeremyfeinstein.slidingmenu.lib.actionbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;

/**
 * This interface provides the basic methods to access the ActionBar up
 * indicator
 * 
 * @author Hannes Dorfmann
 * 
 */
public interface ActionBarUpIndicator {

	/**
	 * Sets the content description of the up indicator
	 * 
	 * @param contentDescResId
	 */
	public void setContentDescription(Activity activity, int contentDescResId);

	/**
	 * Sets the drawable that will replace the default up indicator
	 * 
	 * @param activity
	 * @param drawable
	 *            The new drawable that will replace the default up indicator
	 *            drawable
	 */
	public void setDrawable(Activity activity, Drawable drawable);

	/**
	 * Enables the up method button to be clickable
	 */
	public void setDisplayHomeAsUpEnabled(Activity activity, boolean enabled);

	/**
	 * Get the Drawable that has been specified as up indicator in the theme
	 * 
	 * @param activity
	 * @return
	 */
	public Drawable getThemeUpIndicator(Activity activity);
}
