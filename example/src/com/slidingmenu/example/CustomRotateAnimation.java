package com.slidingmenu.example;

import android.graphics.Canvas;
import android.os.Bundle;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomRotateAnimation extends CustomAnimation {

	public CustomRotateAnimation() {
		super(R.string.anim_rot, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.rotate(180.0f*(1-percentOpen), canvas.getWidth(), canvas.getHeight()/2);
			}
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setBehindScrollScale(1);
	}

}
