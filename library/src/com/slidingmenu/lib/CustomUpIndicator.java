package com.slidingmenu.lib;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class CustomUpIndicator extends Drawable {

	public static Paint mPaint;
	private RectF mRect;
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		mPaint.setARGB(0, 255, 0, 0);
		
		mRect.left = 0.0f;
		mRect.top = 0.0f;
		mRect.right = 50.0f;
		mRect.bottom = 50.0f;
		
		canvas.drawRect(mRect, mPaint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

}
