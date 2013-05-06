package com.jeremyfeinstein.slidingmenu.example;

import android.os.Bundle;


public class SlidingContent extends BaseActivity {
	
	public SlidingContent() {
		super(R.string.title_bar_content);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new SampleListFragment())
		.commit();
		
		setSlidingActionBarEnabled(false);
	}

}
