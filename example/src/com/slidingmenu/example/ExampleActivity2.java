package com.slidingmenu.example;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class ExampleActivity2 extends SlidingFragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingmenu);
		final SlidingMenu menu = (SlidingMenu) findViewById(R.id.blahblah);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				menu.setViewBehind(R.layout.main);
			}
		});
	}
	
}
