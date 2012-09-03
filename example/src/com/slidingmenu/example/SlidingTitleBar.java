package com.slidingmenu.example;

import android.os.Bundle;
import android.support.v4.view.ViewPager;


public class SlidingTitleBar extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.pager);
		ViewPager vp = (ViewPager) findViewById(R.id.pager);
		PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), vp);
		for (int i = 0; i < 3; i++) {
			adapter.addTab(new SampleListFragment());
		}
		
		setSlidingActionBarEnabled(true);
	}
	
}
