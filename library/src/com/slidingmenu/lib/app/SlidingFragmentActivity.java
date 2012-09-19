package com.slidingmenu.lib.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.slidingmenu.lib.SlidingMenu;

public class SlidingFragmentActivity extends FragmentActivity implements SlidingActivityBase {

	private SlidingActivityHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}

	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	// behind left view
	public void setBehindLeftContentView(int id) {
		setBehindLeftContentView(getLayoutInflater().inflate(id, null));
	}

	public void setBehindLeftContentView(View v) {
		setBehindLeftContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setBehindLeftContentView(View v, LayoutParams params) {
		mHelper.setBehindLeftContentView(v);
	}

	// behind right view
	public void setBehindRightContentView(int id) {
		setBehindRightContentView(getLayoutInflater().inflate(id, null));
	}

	public void setBehindRightContentView(View v) {
		setBehindRightContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setBehindRightContentView(View v, LayoutParams params) {
		mHelper.setBehindRightContentView(v);
	}
	
	public SlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	public void toggle(int side) {
		mHelper.toggle(side);
	}

	public void showAbove() {
		mHelper.showAbove();
	}

	public void showBehind(int side) {
		mHelper.showBehind(side);
	}

	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

}
