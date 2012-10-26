package com.slidingmenu.example;

import android.os.Bundle;
import android.support.v4.view.ViewPager;


public class SlidingTitleBar extends BaseActivity {

	public SlidingTitleBar() {
		super(R.string.title_bar_slide);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
//		setContentView(R.layout.content_frame);
//		getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.content_frame, new SampleListFragment())
//		.commit();
		
		ViewPager vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setAdapter(new PagerAdapter(getSupportFragmentManager(), vp));
		setContentView(vp);
		
		getSlidingMenu().setAboveOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setBehindScrollScale(0.0f);
		
		setSlidingActionBarEnabled(false);
	}
	
}
