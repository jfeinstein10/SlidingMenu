package com.slidingmenu.example;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected ListFragment mFrag;

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);
		
		addLeft();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);

		// customize the ActionBar
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private void addLeft() {
		FrameLayout left = new FrameLayout(this);
		left.setId("LEFT".hashCode());
		setBehindLeftContentView(left);
		getSupportFragmentManager()
		.beginTransaction()
		.replace("LEFT".hashCode(), new SampleListFragment())
		.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowDrawable(R.drawable.shadow, SlidingMenu.LEFT);
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width, SlidingMenu.LEFT);
	}
	
	private void addRight() {
		FrameLayout right = new FrameLayout(this);
		right.setId("RIGHT".hashCode());
		this.setBehindRightContentView(right);
		getSupportFragmentManager()
		.beginTransaction()
		.replace("RIGHT".hashCode(), new SampleListFragment())
		.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowDrawable(R.drawable.shadow_right, SlidingMenu.RIGHT);
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width, SlidingMenu.RIGHT);
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		getSlidingMenu().showAbove();
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle(SlidingMenu.LEFT);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class PagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> mFragments = new ArrayList<Fragment>();
		private ViewPager mPager;

		public PagerAdapter(FragmentManager fm, ViewPager vp) {
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
			for (int i = 0; i < 3; i++)
				addTab(new SampleListFragment());
		}

		public void addTab(Fragment frag) {
			mFragments.add(frag);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
	}

}
