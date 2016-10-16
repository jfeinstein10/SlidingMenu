package com.jeremyfeinstein.slidingmenu.lib;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface MenuInterface {

	void scrollBehindTo(int x, int y,
	                    CustomViewBehind cvb, float scrollScale);

	int getMenuLeft(CustomViewBehind cvb, View content);

	int getAbsLeftBound(CustomViewBehind cvb, View content);

	int getAbsRightBound(CustomViewBehind cvb, View content);

	boolean marginTouchAllowed(View content, int x, int threshold);

	boolean menuOpenTouchAllowed(View content, int currPage, int x);

	boolean menuTouchInQuickReturn(View content, int currPage, int x);

	boolean menuClosedSlideAllowed(int x);

	boolean menuOpenSlideAllowed(int x);

	void drawShadow(Canvas canvas, Drawable shadow, int width);

	void drawFade(Canvas canvas, int alpha,
	              CustomViewBehind cvb, View content);

	void drawSelector(View content, Canvas canvas, float percentOpen);
	
}
