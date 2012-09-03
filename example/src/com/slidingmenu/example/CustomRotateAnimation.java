package com.slidingmenu.example;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.animation.Interpolator;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomRotateAnimation extends CustomAnimation {

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	public CustomRotateAnimation() {
		// see the class CustomAnimation for how to attach 
		// the CanvasTransformer to the SlidingMenu
		super(R.string.anim_rot, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.rotate(180.0f*(1-interp.getInterpolation(percentOpen)), 
						canvas.getWidth(), canvas.getHeight()/2);
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setBehindScrollScale(1);
	}

}
