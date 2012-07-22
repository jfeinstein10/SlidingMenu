package com.slidingmenu.lib.app;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.slidingmenu.lib.SlidingMenu;

public interface SlidingActivityBase {
	
	public void setBehindContentView(View v, LayoutParams p);

	public SlidingMenu getSlidingMenu();
		
	public void toggle();
	
	public void showAbove();
	
	public void showBehind();
	
}
