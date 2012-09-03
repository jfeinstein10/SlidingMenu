package com.slidingmenu.example;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomAnimation extends BaseActivity {

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
		
		SlidingMenu sm = getSlidingMenu();
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
