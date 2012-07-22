package com.slidingmenu.lib.app;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.actionbarsherlock.app.SherlockActivity;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivity extends SherlockActivity implements SlidingActivityBase {

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyDown(keyCode, event);
		if (b) return b;
		return super.onKeyDown(keyCode, event);
	}

}
