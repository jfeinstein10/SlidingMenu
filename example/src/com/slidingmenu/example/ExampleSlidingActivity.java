package com.slidingmenu.example;

import android.os.Bundle;
import android.view.View;

import com.slidingmenu.lib.app.SlidingListActivity;

public class ExampleSlidingActivity extends SlidingListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = getWindow().getDecorView();
		this.setSlidingActionBarEnabled(true);
		setContentView(R.layout.list);
		setBehindContentView(R.layout.main2);
		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		v = getWindow().getDecorView();
	}

}