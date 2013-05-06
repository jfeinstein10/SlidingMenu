package com.slidingmenu.example.anim;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

import com.slidingmenu.example.R;
import com.slidingmenu.example.R.string;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomSlideAnimation extends CustomAnimation {
	
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};

	public CustomSlideAnimation() {
		// see the class CustomAnimation for how to attach 
		// the CanvasTransformer to the SlidingMenu
		super(R.string.anim_slide, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}			
		});
	}

}
