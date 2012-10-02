package com.slidingmenu.example;

import java.util.Random;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class StartWithMenuOpen extends BaseActivity {

	static int[] lastScrolledPositions = new int[2];

	public StartWithMenuOpen() {
		super(R.string.title_bar_slide);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.start_with_menu_open);
		((TextView) findViewById(R.id.title)).setText("Pick an item from the menu");

		setSlidingActionBarEnabled(true);
		if (getIntent().getBooleanExtra("startWithMenuOpen", false)) {
			getSlidingMenu().setStartWithMenuOpen(true, 1000);
		}	
		getSlidingMenu().setCloseMenuOnExit(true);

		int[] colors = { Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.RED, Color.CYAN };
		Random randomGenerator = new Random();
		int i = randomGenerator.nextInt(colors.length);
		findViewById(R.id.container).setBackgroundColor(colors[i]);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent i = new Intent(StartWithMenuOpen.this, StartWithMenuOpen.class);
				i.putExtra("startWithMenuOpen", true);
				StartWithMenuOpen.this.startActivity(i);
				overridePendingTransition(0, 0);

				StartWithMenuOpen.lastScrolledPositions[0] = arg0.getFirstVisiblePosition();
				StartWithMenuOpen.lastScrolledPositions[1] = arg0.getChildAt(0) != null ? arg0.getChildAt(0).getTop() : 0;
			}
		});
		list.setSelectionFromTop(lastScrolledPositions[0], lastScrolledPositions[1]);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
