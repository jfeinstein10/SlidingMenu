package com.slidingmenu.lib.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.slidingmenu.lib.SlidingMenu;

public class SlidingFragmentActivity extends FragmentActivity implements SlidingActivityBase {
	
	private SlidingActivityHelper mHelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}
	
	public View findViewById(int id) {
		return mHelper.findViewById(id);
	}
	
	public void setContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	public void setContentView(View v) {
		setBehindContentView(v, null);
	}

	public void setContentView(View v, LayoutParams params) {
		mHelper.setAboveContentView(v, params);
	}

	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	public void setBehindContentView(View v) {
		setBehindContentView(v, null);
	}

	public void setBehindContentView(View v, LayoutParams params) {
		mHelper.setBehindContentView(v, params);
	}

	public SlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	public void toggle() {
		mHelper.toggle();
	}

	public void showAbove() {
		mHelper.showAbove();
	}

	public void showBehind() {
		mHelper.showBehind();
	}

	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyDown(keyCode, event);
		if (b) return b;
		return super.onKeyDown(keyCode, event);
	}
	
}
