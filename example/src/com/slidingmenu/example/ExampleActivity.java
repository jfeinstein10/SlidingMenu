package com.slidingmenu.example;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class ExampleActivity extends SlidingFragmentActivity implements TabListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager);
		ViewPager vp = (ViewPager) findViewById(R.id.pager);
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(new SampleListFragment());
		fragments.add(new SampleListFragment());
		fragments.add(new SampleListFragment());
		vp.setAdapter(new PagerAdapter(super.getSupportFragmentManager(), fragments));
		setBehindContentView(R.layout.frame);

		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.add(R.id.frame, new SampleListFragment());
		t.commit();

		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		getSlidingMenu().setBehindScrollScale(0.5f);
		//		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		for (int i = 0; i < 3; i++) {
			Tab tab = actionBar.newTab();
			tab.setText("Tab " + i);
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
		View v = this.getWindow().getDecorView();
		View a = findViewById(android.R.id.home);
	}
	public class PagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments;
		/**
		 * @param fm
		 * @param fragments
		 */
		public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}
		/* (non-Javadoc)
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//		switch (tab.getPosition()) {
		//		case 0:
		//			findViewById(R.id.main).setBackgroundResource(android.R.color.white);
		//			break;
		//		case 1:
		//			findViewById(R.id.main).setBackgroundResource(android.R.color.black);
		//			break;
		//		case 2:
		//			findViewById(R.id.main).setBackgroundResource(android.R.color.darker_gray);
		//			break;
		//		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}



}
