package com.slidingmenu.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.slidingmenu.example.fragments.ColorFragment;
import com.slidingmenu.lib.SlidingMenu;

public class ViewPagerActivity extends BaseActivity {

	public ViewPagerActivity() {
		super(R.string.viewpager);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewPager vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setAdapter(new ColorPagerAdapter(getSupportFragmentManager()));
		setContentView(vp);

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		
		vp.setCurrentItem(0);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	public class ColorPagerAdapter extends FragmentPagerAdapter {

		private final int[] COLORS = new int[] {
			R.color.red,
			R.color.green,
			R.color.blue,
			android.R.color.white,
			android.R.color.black
		};
		
		public ColorPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return COLORS.length;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return new ColorFragment(COLORS[position % getCount()]);
		}

	}

}
