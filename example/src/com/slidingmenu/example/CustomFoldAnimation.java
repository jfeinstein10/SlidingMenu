package com.slidingmenu.example;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomFoldAnimation extends CustomAnimation {

	public CustomFoldAnimation() {
		super(R.string.anim_fold, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// the left side
//				canvas.save();
//				canvas.clipRect(0, 0, canvas.getWidth()/2, canvas.getHeight());
				Matrix m = new Matrix();
				float[] src = new float[] {0, 0, canvas.getWidth(), 0,
						canvas.getWidth()/2, 0, canvas.getWidth()/2, canvas.getHeight()};
				float[] dest = new float[] {0, 0, canvas.getWidth(), 0,
						canvas.getWidth()/2, 100, canvas.getWidth()/2, canvas.getHeight()-100};
				m.setPolyToPoly(src, 0, dest, 0, 4);
				canvas.setMatrix(m);
//				canvas.restore();
				
//				// the right side
//				canvas.save();
//				canvas.clipRect(canvas.getWidth()/2, 0, canvas.getWidth(), canvas.getHeight());
				float[] src2 = new float[] {0, 0, 0, canvas.getHeight(),
						canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight()};
				float[] dest2 = new float[] {0, 50, 0, canvas.getHeight()-50,
						canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight()};
//				m.setPolyToPoly(src, 0, dest, 0, 4);
//				canvas.setMatrix(m);
//				canvas.restore();
			}			
		});
	}

}
