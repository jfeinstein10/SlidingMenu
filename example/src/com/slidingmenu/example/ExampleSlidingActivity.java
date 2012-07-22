package com.slidingmenu.example;

import android.os.Bundle;
import android.view.View;

import com.slidingmenu.lib.app.SlidingActivity;

public class ExampleSlidingActivity extends SlidingActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = getWindow().getDecorView();
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setSlidingActionBarEnabled(true);
		setContentView(R.layout.main);
		setBehindContentView(R.layout.main2);
		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		v = getWindow().getDecorView();
	}

//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			toggle();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		this.getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}