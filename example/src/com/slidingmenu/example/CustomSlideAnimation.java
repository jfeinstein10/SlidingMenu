package com.slidingmenu.example;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomSlideAnimation extends CustomAnimation {
	
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			// TODO Auto-generated method stub
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};

	public CustomSlideAnimation() {
		super(R.string.anim_slide, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}			
		});
	}

}
