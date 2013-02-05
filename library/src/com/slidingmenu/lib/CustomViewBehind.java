package com.slidingmenu.lib;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import static com.slidingmenu.lib.SlidingMode.LEFT;
import static com.slidingmenu.lib.SlidingMode.RIGHT;
import static com.slidingmenu.lib.SlidingMode.TOP;
import static com.slidingmenu.lib.SlidingMode.BOTTOM;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CustomViewBehind extends ViewGroup {

	private static final String TAG = "CustomViewBehind";

	private static final int MARGIN_THRESHOLD = 48; // dips
	private int mTouchMode = SlidingMenu.TOUCHMODE_MARGIN;

	private CustomViewAbove mViewAbove;

	private static class Side {
		public int side;
		public View view = null;
		public Drawable shadow = null;
		public int shadowWidth = 0;
		public int offset = 0;
		public float scrollScale = 0.0f;
		public Side(int side) {
			this.side = side;
		}
	}

	private ArrayList<Side> mSides = new ArrayList<Side>();

	private int mMarginThreshold;

	private CanvasTransformer mTransformer;

	private int mMode;
	private boolean mFadeEnabled;
	private final Paint mFadePaint = new Paint();
	private float mFadeDegree;

	public CustomViewBehind(Context context) {
		this(context, null);
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs);
		mMarginThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				MARGIN_THRESHOLD, getResources().getDisplayMetrics());
		for (int side : SlidingMode.SIDES)
			mSides.add(new Side(side));
	}

	public void setCustomViewAbove(CustomViewAbove customViewAbove) {
		mViewAbove = customViewAbove;
	}

	public void setCanvasTransformer(CanvasTransformer t) {
		mTransformer = t;
	}

	public void setWidthOffset(int i, int side) {
		//		mWidthOffset = i;
		Side s = getSide(side);
		s.offset = i;
		requestLayout();
	}

	public int getBehindWidth(int page) {
		switch (page) {
		case 0:
			if (SlidingMode.isLeft(mMode) && getContent(LEFT) != null)
				return getContent(LEFT).getWidth();
			break;
		case 2:
			if (SlidingMode.isRight(mMode) && getContent(RIGHT) != null)
				return getContent(RIGHT).getWidth();
			break;
		}
		return getWidth();
	}

	public int getBehindHeight(int page) {
		switch (page) {
		case 3:
			if (SlidingMode.isTop(mMode) && getContent(TOP) != null)
				return getContent(TOP).getHeight();
		case 4:
			if (SlidingMode.isBottom(mMode) && getContent(BOTTOM) != null)
				return getContent(BOTTOM).getHeight();
		}
		return getHeight();
	}

	private Side getSide(int side) {
		for (Side s : mSides)
			if ((s.side & side) == side)
				return s;
		return null;
	}

	public View getContent(int side) {
		return getSide(side).view;
	}

	public void setContent(View v, int side) {
		Side s = getSide(side);
		if (s.view != null) {
			removeView(s.view);
		}
		if (v != null) {
			s.view = v;
			addView(v);
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		if (mTransformer != null)
			invalidate();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return getVisibility() == View.INVISIBLE;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return getVisibility() == View.INVISIBLE;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mTransformer != null) {
			canvas.save();
			mTransformer.transformCanvas(canvas, mViewAbove.getPercentOpen());
			super.dispatchDraw(canvas);
			canvas.restore();
		} else
			super.dispatchDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		for (Side s : mSides) {
			if (s.view == null)
				continue;
			if (SlidingMode.isTop(s.side) || SlidingMode.isBottom(s.side))
				s.view.layout(0, 0, width, height-s.offset);
			else if (SlidingMode.isLeft(s.side) || SlidingMode.isRight(s.side))
				s.view.layout(0, 0, width-s.offset, height);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);
		for (Side s : mSides) {
			if (s.view == null)
				continue;
			int contentW = getChildMeasureSpec(widthMeasureSpec, 0, 
					width-(isVertical(s.side) ? 0 : s.offset));
			int contentH = getChildMeasureSpec(heightMeasureSpec, 0, 
					height-(isVertical(s.side) ? s.offset : 0));
			s.view.measure(contentW, contentH);
		}
	}

	public int correctMenuPage(int page) {
		if ((page == 0 && !SlidingMode.isLeft(mMode)) ||
				(page == 2 && !SlidingMode.isRight(mMode)) ||
				(page == 3 && !SlidingMode.isTop(mMode)) ||
				(page == 4 && !SlidingMode.isBottom(mMode)))
			return 1;
		return page;
	}

	private boolean isVertical(int side) {
		return SlidingMode.isTop(side) || SlidingMode.isBottom(side);
	}

	public void setMode(int mode) {
		for (Side s : mSides)
			if (s.view != null)
				s.view.setVisibility((s.side & mode) == s.side ? View.VISIBLE : View.GONE);
		mMode = mode;
	}

	public int getMode() {
		return mMode;
	}

	public void setScrollScale(float scrollScale, int side) {
		getSide(side).scrollScale = scrollScale;
	}

	public float getScrollScale(int side) {
		return getSide(side).scrollScale;
	}

	public void setShadowDrawable(Drawable shadow, int side) {
		Side s = getSide(side);
		s.shadow = shadow;
		invalidate();
	}

	public void setShadowWidth(int width, int side) {
		getSide(side).shadowWidth = width;
		invalidate();
	}

	public void setFadeEnabled(boolean b) {
		mFadeEnabled = b;
	}

	public void setFadeDegree(float degree) {
		if (degree > 1.0f || degree < 0.0f)
			throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
		mFadeDegree = degree;
	}

	public void scrollBehindTo(View content, int x, int y) {

		setVisibility(x == 0 && y == 0 ? View.INVISIBLE : View.VISIBLE);

		if (SlidingMode.isLeft(mMode)) {
			Side s = getSide(LEFT);
			if (s.view != null) {
				s.view.setVisibility(x >= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
				if (x <= content.getLeft())
					scrollTo((int)((x + s.view.getWidth())*s.scrollScale), y);
			}
		} 

		if (SlidingMode.isRight(mMode)) {
			Side s = getSide(RIGHT);
			if (s.view != null) {
				s.view.setVisibility(x <= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
				if (x > content.getLeft())
					scrollTo((int)(s.view.getWidth() - getWidth() + 
							(x-s.view.getWidth())*s.scrollScale), y);
			}
		}

		if (SlidingMode.isTop(mMode)) {
			Side s = getSide(TOP);
			if (s.view != null) {
				s.view.setVisibility(y >= content.getTop() ? View.INVISIBLE : View.VISIBLE);
				if (y <= content.getTop()) {
					scrollTo(x, (int) ((y + s.view.getHeight()) * s.scrollScale));
				}
			}
		}

		if (SlidingMode.isBottom(mMode)) {
			Side s = getSide(BOTTOM);
			if (s.view != null) {
				s.view.setVisibility(y < content.getTop() ? View.INVISIBLE : View.VISIBLE);
				if (y > content.getTop()) {
					scrollTo(x, (int) (s.view.getHeight() - getHeight() +
							(y - s.view.getHeight()) * s.scrollScale));
				}
			}
		}

	}

	public int getMenuLeft(View content, int page) {
		if (SlidingMode.isLeft(mMode) && SlidingMode.isRight(mMode)) {
			switch (page) {
			case 0:
				return content.getLeft() - getContent(LEFT).getWidth();				
			case 2:
				return content.getLeft() + getContent(RIGHT).getWidth();				
			}
		} else if (SlidingMode.isLeft(mMode)) {
			return content.getLeft() - getContent(LEFT).getWidth();
		} else if (SlidingMode.isRight(mMode)) {
			return content.getLeft() + getContent(RIGHT).getWidth();
		}
		return content.getLeft();
	}

	public int getMenuTop(View content, int page) {
		if (SlidingMode.isTop(mMode) && SlidingMode.isBottom(mMode)) {
			switch (page) {
			case 3:
				return content.getTop() - getContent(TOP).getHeight();
			case 4:
				return content.getTop() + getContent(BOTTOM).getHeight();
			}
		} else if (SlidingMode.isTop(mMode)) {
			return content.getTop() - getContent(TOP).getHeight();
		} else if (SlidingMode.isBottom(mMode)) {
			return content.getTop() + getContent(BOTTOM).getHeight();
		}
		return content.getTop();
	}

	public int getAbsLeftBound(View content) {
		if (SlidingMode.isLeft(mMode)) {
			return content.getLeft() - getContent(LEFT).getWidth();
		}
		return content.getLeft();
	}

	public int getAbsRightBound(View content) {
		if (SlidingMode.isRight(mMode)) {
			return content.getLeft() + getContent(RIGHT).getWidth();
		}
		return content.getLeft();
	}

	public int getAbsTopBound(View content) {
		if (SlidingMode.isTop(mMode)) {
			return content.getTop() - getContent(TOP).getHeight();
		} 
		return content.getTop();
	}

	public int getAbsBottomBound(View content) {
		if (SlidingMode.isBottom(mMode)) {
			return content.getTop() + getContent(BOTTOM).getHeight();
		}
		return 0;
	}

	public boolean menuClosedTouchAllowed(View content, int x, int y) {
		int left = content.getLeft();
		int right = content.getRight();
		int top = content.getTop();
		int bottom = content.getBottom();
		boolean touch = false;
		if (SlidingMode.isLeft(mMode))
			touch |= (x >= left && x <= mMarginThreshold + left);
		if (SlidingMode.isRight(mMode))
			touch |= (x <= right && x >= right - mMarginThreshold);
		if (SlidingMode.isTop(mMode))
			touch |= (y >= top && y <= mMarginThreshold + top);
		if (SlidingMode.isBottom(mMode))
			touch |= (y <= bottom && y >= bottom - mMarginThreshold);
		return touch;
	}
	
	public boolean menuClosedTouchHoz(View content, int x, int y) {
		int left = content.getLeft();
		int right = content.getRight();
		if (SlidingMode.isLeft(mMode))
			if (x >= left && x <= mMarginThreshold + left)
				return true;
		if (SlidingMode.isRight(mMode))
			if (x <= right && x >= right - mMarginThreshold)
				return true;
		return false;
	}

	public void setTouchMode(int i) {
		mTouchMode = i;
	}

	public boolean menuOpenTouchAllowed(View content, int currPage, float x, float y) {
		switch (mTouchMode) {
		case SlidingMenu.TOUCHMODE_FULLSCREEN:
			return true;
		case SlidingMenu.TOUCHMODE_MARGIN:
			return menuTouchInQuickReturn(content, currPage, x, y);
		}
		return false;
	}

	public boolean menuTouchInQuickReturn(View content, int currPage, float x, float y) {
		boolean allowed = false;
		if (SlidingMode.isLeft(mMode) && currPage == 0) {
			allowed |= x >= content.getLeft();
		}
		if (SlidingMode.isRight(mMode) && currPage == 2) {
			allowed |= x <= content.getRight();
		}
		if (SlidingMode.isTop(mMode) && currPage == 3) {
			allowed |= y >= content.getTop();
		}
		if (SlidingMode.isBottom(mMode) && currPage == 4) {
			allowed |= y <= content.getBottom();
		}
		return allowed;
	}

	public boolean menuOpenSlideAllowed(float dx, float dy, int page) {
		boolean allowed = false;
		if (SlidingMode.isLeft(mMode) && page == 0) {
			allowed |= dx < 0;
		}
		if (SlidingMode.isRight(mMode) && page == 2) {
			allowed |= dx > 0;
		}
		if (SlidingMode.isTop(mMode) && page == 3) {
			allowed |= dy < 0;
		}	
		if (SlidingMode.isBottom(mMode) && page == 4) {
			allowed |= dy > 0;
		}
		return allowed;
	}

	public boolean menuClosedSlideAllowed(float dx, float dy) {
		boolean allowed = false;
		if (SlidingMode.isLeft(mMode)) {
			allowed |= dx > 0;
		}
		if (SlidingMode.isRight(mMode)) {
			allowed |= dx < 0;
		}
		if (SlidingMode.isTop(mMode)) {
			allowed |= dy > 0;
		}	
		if (SlidingMode.isBottom(mMode)) {
			allowed |= dy < 0;
		}
		return allowed;
	}

	public void drawShadow(View content, Canvas canvas) {
		if (SlidingMode.isLeft(mMode)) {
			Side s = getSide(LEFT);
			View v = getContent(LEFT);
			if (s.shadow != null && v != null && v.getVisibility() != View.INVISIBLE)
				drawShadowLeft(canvas, s.shadow, content.getLeft()-s.shadowWidth, s.shadowWidth);
		}
		if (SlidingMode.isRight(mMode)) {
			Side s = getSide(RIGHT);
			View v = getContent(RIGHT);
			if (s.shadow != null && v != null && v.getVisibility() != View.INVISIBLE)
				drawShadowLeft(canvas, s.shadow, content.getRight(), s.shadowWidth);
		}
		if (SlidingMode.isTop(mMode)) {
			Side s = getSide(TOP);
			View v = getContent(TOP);
			if (s.shadow != null && v != null && v.getVisibility() != View.INVISIBLE)
				drawShadowTop(canvas, s.shadow, content.getTop()-s.shadowWidth, s.shadowWidth);
		}
		if (SlidingMode.isBottom(mMode)) {
			Side s = getSide(BOTTOM);
			View v = getContent(BOTTOM);
			if (s.shadow != null && v != null && v.getVisibility() != View.INVISIBLE)
				drawShadowTop(canvas, s.shadow, content.getBottom(), s.shadowWidth);
		}
	}

	private void drawShadowLeft(Canvas canvas, Drawable d, int left, int width) {
		d.setBounds(left, 0, left + width, getHeight());
		d.draw(canvas);
	}

	private void drawShadowTop(Canvas canvas, Drawable d, int top, int width) {
		d.setBounds(0, top, getWidth(), top + width);
		d.draw(canvas);		
	}

	public void drawFade(View content, Canvas canvas, float openPercent) {
		if (!mFadeEnabled) return;
		if (openPercent == 1.0f) return;
		final int alpha = (int) (mFadeDegree * 255 * Math.abs(1-openPercent));
		mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;
		if (SlidingMode.isLeft(mMode)) {
			right = content.getLeft();
			left = right - getContent(LEFT).getWidth();
			canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
		} 
		if (SlidingMode.isRight(mMode)) {
			left = content.getRight();
			right = left + getContent(RIGHT).getWidth();	
			canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
		}
		if (SlidingMode.isTop(mMode)) {
			bottom = content.getTop();	
			top = bottom - getContent(TOP).getHeight();
			canvas.drawRect(0, top, getWidth(), bottom, mFadePaint);
		}
		if (SlidingMode.isBottom(mMode)) {
			top = content.getBottom();
			bottom = top + getContent(BOTTOM).getHeight();	
			canvas.drawRect(0, top, getWidth(), bottom, mFadePaint);
		}
		canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
	}

	private boolean mSelectorEnabled = true;
	private Bitmap mSelectorDrawable;
	private View mSelectedView;

	public void drawSelector(View content, Canvas canvas, float openPercent) {
		if (!mSelectorEnabled) return;
		if (mSelectorDrawable != null && mSelectedView != null) {
			String tag = (String) mSelectedView.getTag(R.id.selected_view);
			if (tag.equals(TAG+"SelectedView")) {
				canvas.save();
				int left, right, offset;
				offset = (int) (mSelectorDrawable.getWidth() * openPercent);
				if (SlidingMode.isLeft(mMode)) {
					right = content.getLeft();
					left = right - offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, left, getSelectorTop(), null);		
				} else if (SlidingMode.isRight(mMode)) {
					left = content.getRight();
					right = left + offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, right - mSelectorDrawable.getWidth(), getSelectorTop(), null);
				}
				canvas.restore();
			}
		}
	}

	public void setSelectorEnabled(boolean b) {
		mSelectorEnabled = b;
	}

	public void setSelectedView(View v) {
		if (mSelectedView != null) {
			mSelectedView.setTag(R.id.selected_view, null);
			mSelectedView = null;
		}
		if (v != null && v.getParent() != null) {
			mSelectedView = v;
			mSelectedView.setTag(R.id.selected_view, TAG+"SelectedView");
			invalidate();
		}
	}

	private int getSelectorTop() {
		int y = mSelectedView.getTop();
		y += (mSelectedView.getHeight() - mSelectorDrawable.getHeight()) / 2;
		return y;
	}

	public void setSelectorBitmap(Bitmap b) {
		mSelectorDrawable = b;
		refreshDrawableState();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setChildLayerType(int layerType) {
		if (Build.VERSION.SDK_INT < 11) return;
		for (Side s : mSides)
			if (s.view != null)
				s.view.setLayerType(layerType, null);
	}

}
