package com.slidingmenu.example;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

@SuppressLint("NewApi")
public class ExampleActivity extends SlidingFragmentActivity {

	private ListFragment mFrag;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.pager);
		ViewPager vp = (ViewPager) findViewById(R.id.pager);
		PagerAdapter adapter = new PagerAdapter(getFragmentManager(), 
				vp, getActionBar());
		for (int i = 0; i < 3; i++) {
			adapter.addTab(new SampleListFragment());
		}
		
		// set the Behind View
		setBehindContentView(R.layout.frame);
		FragmentTransaction t = this.getFragmentManager().beginTransaction();
		mFrag = new SampleListFragment();
		t.replace(R.id.frame, mFrag);
		t.commit();

		// customize the SlidingMenu
		this.setSlidingActionBarEnabled(true);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		getSlidingMenu().setBehindScrollScale(0.0f);
		getSlidingMenu().setSelectorEnabled(true);
		getSlidingMenu().setSelectorDrawable(R.drawable.ic_launcher);
		getSlidingMenu().setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, percentOpen, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		});

		// customize the ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mFrag.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				getSlidingMenu().setSelectedView(arg1);
			}
		});
	}
	
	public class PagerAdapter extends FragmentPagerAdapter implements 
	ViewPager.OnPageChangeListener, TabListener{

		private List<Fragment> mFragments = new ArrayList<Fragment>();
		private ViewPager mPager;
		private ActionBar mActionBar;

		public PagerAdapter(FragmentManager fm, ViewPager vp, ActionBar ab) {
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
			mPager.setOnPageChangeListener(this);
			mActionBar = ab;
		}
		
		public void addTab(Fragment frag) {
			mFragments.add(frag);
			mActionBar.addTab(mActionBar.newTab().setTabListener(this).
					setText("Tab "+mFragments.size()));
		}
		
		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			mPager.setCurrentItem(tab.getPosition());
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) { }
		public void onTabReselected(Tab tab, FragmentTransaction ft) { }
		public void onPageScrollStateChanged(int arg0) { }
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
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
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
