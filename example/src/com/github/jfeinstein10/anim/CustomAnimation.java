package com.github.jfeinstein10.anim;

import android.os.Bundle;

import com.github.jfeinstein10.BaseActivity;
import com.slidingmenu.example.R;
import com.github.jfeinstein10.SampleListFragment;
import com.slidingmenu.example.R.id;
import com.slidingmenu.example.R.layout;
import com.slidingmenu.example.R.menu;
import com.github.jfeinstein10.SlidingMenu;
import com.github.jfeinstein10.SlidingMenu.CanvasTransformer;

public abstract class CustomAnimation extends BaseActivity {
	
	private CanvasTransformer mTransformer;
	
	public CustomAnimation(int titleRes, CanvasTransformer transformer) {
		super(titleRes);
		mTransformer = transformer;
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
		
		SlidingMenu sm = getSlidingMenu();
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
	}

}
