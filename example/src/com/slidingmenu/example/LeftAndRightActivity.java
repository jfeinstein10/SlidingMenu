package com.slidingmenu.example;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.slidingmenu.example.fragments.ColorFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;


public class LeftAndRightActivity extends BaseActivity {

//	private SlidingMenu mRight;

	public LeftAndRightActivity() {
		super(R.string.left_and_right);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

//		mRight = new SlidingMenu(this);		
//		mRight.setMode(SlidingMenu.RIGHT);
//		mRight.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		mRight.setShadowWidthRes(R.dimen.shadow_width);
//		mRight.setShadowDrawable(R.drawable.shadowright);
//		mRight.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		mRight.setFadeDegree(0.35f);
//		mRight.setMenu(R.layout.menu_frame_two);
//		mRight.setOnOpenedListener(new OnOpenedListener() {
//			@Override
//			public void onOpened() {
//				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//			}
//		});
//		mRight.setOnClosedListener(new OnClosedListener() {
//			@Override
//			public void onClosed() {
//				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//			}			
//		});
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new SampleListFragment())
		.commit();
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new SampleListFragment())
		.commit();
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

//		View v = getSlidingMenu().getContent();
//		((ViewGroup) v.getParent()).removeView(v);
//		mRight.setContent(v);
//		getSlidingMenu().setContent(mRight);

	}

}
