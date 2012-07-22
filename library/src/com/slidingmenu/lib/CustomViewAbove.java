package com.slidingmenu.lib;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class CustomViewAbove extends ViewGroup {
	private static final String TAG = "CustomViewAbove";
	private static final boolean DEBUG = false;

	private static final boolean USE_CACHE = false;

	private static final int MAX_SETTLE_DURATION = 600; // ms
	private static final int MIN_DISTANCE_FOR_FLING = 25; // dips

	private static final int[] LAYOUT_ATTRS = new int[] {
		android.R.attr.layout_gravity
	};

	static class ItemInfo {
		Object object;
		int position;
		boolean scrolling;
	}

	private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>(){

		public int compare(ItemInfo lhs, ItemInfo rhs) {
			return lhs.position - rhs.position;
		}};

		private static final Interpolator sInterpolator = new Interpolator() {
			public float getInterpolation(float t) {
				t -= 1.0f;
				return t * t * t * t * t + 1.0f;
			}
		};

		private ItemInfo mWindow;
		private ItemInfo mContent;

		private int mCurItem;
		private Scroller mScroller;

		private int mShadowWidth;
		private Drawable mShadowDrawable;
		private int mTopPageBounds;
		private int mBottomPageBounds;

		private int mChildWidthMeasureSpec;
		private int mChildHeightMeasureSpec;
		private boolean mInLayout;

		private boolean mScrollingCacheEnabled;

		private boolean mPopulatePending;
		private boolean mScrolling;

		private boolean mIsBeingDragged;
		private boolean mIsUnableToDrag;
		private int mTouchSlop;
		private float mInitialMotionX;
		/**
		 * Position of the last motion event.
		 */
		private float mLastMotionX;
		private float mLastMotionY;
		/**
		 * ID of the active pointer. This is used to retain consistency during
		 * drags/flings if multiple pointers are used.
		 */
		private int mActivePointerId = INVALID_POINTER;
		/**
		 * Sentinel value for no current active pointer.
		 * Used by {@link #mActivePointerId}.
		 */
		private static final int INVALID_POINTER = -1;

		/**
		 * Determines speed during touch scrolling
		 */
		private VelocityTracker mVelocityTracker;
		private int mMinimumVelocity;
		private int mMaximumVelocity;
		private int mFlingDistance;

		private boolean mFirstLayout = true;
		private boolean mCalledSuper;

		private boolean mLastTouchAllowed = false;
		private int mSlidingMenuThreshold = 10;
		private CustomViewBehind mCustomViewBehind2;
		private boolean mEnabled = true;

		private OnPageChangeListener mOnPageChangeListener;
		private OnPageChangeListener mInternalPageChangeListener;

		/**
		 * Indicates that the pager is in an idle, settled state. The current page
		 * is fully in view and no animation is in progress.
		 */
		public static final int SCROLL_STATE_IDLE = 0;

		/**
		 * Indicates that the pager is currently being dragged by the user.
		 */
		public static final int SCROLL_STATE_DRAGGING = 1;

		/**
		 * Indicates that the pager is in the process of settling to a final position.
		 */
		public static final int SCROLL_STATE_SETTLING = 2;

		private int mScrollState = SCROLL_STATE_IDLE;

		/**
		 * Callback interface for responding to changing state of the selected page.
		 */
		public interface OnPageChangeListener {

			/**
			 * This method will be invoked when the current page is scrolled, either as part
			 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
			 *
			 * @param position Position index of the first page currently being displayed.
			 *                 Page position+1 will be visible if positionOffset is nonzero.
			 * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
			 * @param positionOffsetPixels Value in pixels indicating the offset from position.
			 */
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

			/**
			 * This method will be invoked when a new page becomes selected. Animation is not
			 * necessarily complete.
			 *
			 * @param position Position index of the new selected page.
			 */
			public void onPageSelected(int position);

			/**
			 * Called when the scroll state changes. Useful for discovering when the user
			 * begins dragging, when the pager is automatically settling to the current page,
			 * or when it is fully stopped/idle.
			 *
			 * @param state The new scroll state.
			 * @see CustomViewAbove#SCROLL_STATE_IDLE
			 * @see CustomViewAbove#SCROLL_STATE_DRAGGING
			 * @see CustomViewAbove#SCROLL_STATE_SETTLING
			 */
			public void onPageScrollStateChanged(int state);
		}

		/**
		 * Simple implementation of the {@link OnPageChangeListener} interface with stub
		 * implementations of each method. Extend this if you do not intend to override
		 * every method of {@link OnPageChangeListener}.
		 */
		public static class SimpleOnPageChangeListener implements OnPageChangeListener {

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// This space for rent
			}


			public void onPageSelected(int position) {
				// This space for rent
			}


			public void onPageScrollStateChanged(int state) {
				// This space for rent
			}
		}

		/**
		 * Used internally to tag special types of child views that should be added as
		 * pager decorations by default.
		 */
		interface Decor {}

		public CustomViewAbove(Context context) {
			this(context, null);
		}

		public CustomViewAbove(Context context, AttributeSet attrs) {
			this(context, attrs, true);
		}

		public CustomViewAbove(Context context, AttributeSet attrs, boolean isAbove) {
			super(context, attrs);
			initCustomViewAbove(isAbove);
		}

		void initCustomViewAbove() {
			initCustomViewAbove(false);
		}

		void initCustomViewAbove(boolean isAbove) {
			setWillNotDraw(false);
			setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
			setFocusable(true);
			final Context context = getContext();
			mScroller = new Scroller(context, sInterpolator);
			final ViewConfiguration configuration = ViewConfiguration.get(context);
			mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
			mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
			mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

			if (isAbove) {
				View v = new LinearLayout(getContext());
				v.setBackgroundResource(android.R.color.transparent);
				setMenu(v);
			}

			final float density = context.getResources().getDisplayMetrics().density;
			mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
		}

		private void setScrollState(int newState) {
			if (mScrollState == newState) {
				return;
			}

			mScrollState = newState;
			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageScrollStateChanged(newState);
			}
		}

		private void removeNonDecorViews() {
			for (int i = 0; i < getChildCount(); i++) {
				final View child = getChildAt(i);
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (!lp.isDecor) {
					removeViewAt(i);
					i--;
				}
			}
		}

		/**
		 * Set the currently selected page. If the CustomViewPager has already been through its first
		 * layout there will be a smooth animated transition between the current item and the
		 * specified item.
		 *
		 * @param item Item index to select
		 */
		public void setCurrentItem(int item) {
			mPopulatePending = false;
			setCurrentItemInternal(item, !mFirstLayout, false);
		}

		/**
		 * Set the currently selected page.
		 *
		 * @param item Item index to select
		 * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
		 */
		public void setCurrentItem(int item, boolean smoothScroll) {
			mPopulatePending = false;
			setCurrentItemInternal(item, smoothScroll, false);
		}

		public int getCurrentItem() {
			return mCurItem;
		}

		void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
			setCurrentItemInternal(item, smoothScroll, always, 0);
		}

		boolean isNull() {
			return mContent == null;
		}

		int getCount() {
			int count = 0;
			if (mWindow != null) count += 1;
			if (mContent != null) count += 1;
			return count;
		}

		void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
			if (isNull()) {
				setScrollingCacheEnabled(false);
				return;
			}
			if (!always && mCurItem == item && mWindow != null && mContent != null) {
				setScrollingCacheEnabled(false);
				return;
			}
			if (item < 0) {
				item = 0;
			} else if (item >= getCount()) {
				item = getCount() - 1;
			}
			if (item > 0 && item < getCount()) {
				// We are doing a jump by more than one page.  To avoid
				// glitches, we want to keep all current pages in the view
				// until the scroll ends.
				mWindow.scrolling = true;
				mContent.scrolling = true;
			}
			final boolean dispatchSelected = mCurItem != item;
			mCurItem = item;
			populate();
			final int destX = getChildLeft(mCurItem);
			if (smoothScroll) {
				smoothScrollTo(destX, 0, velocity);
				if (dispatchSelected && mOnPageChangeListener != null) {
					mOnPageChangeListener.onPageSelected(item);
				}
				if (dispatchSelected && mInternalPageChangeListener != null) {
					mInternalPageChangeListener.onPageSelected(item);
				}
			} else {
				if (dispatchSelected && mOnPageChangeListener != null) {
					mOnPageChangeListener.onPageSelected(item);
				}
				if (dispatchSelected && mInternalPageChangeListener != null) {
					mInternalPageChangeListener.onPageSelected(item);
				}
				completeScroll();
				scrollTo(destX, 0);
			}
		}

		/**
		 * Set a listener that will be invoked whenever the page changes or is incrementally
		 * scrolled. See {@link OnPageChangeListener}.
		 *
		 * @param listener Listener to set
		 */
		public void setOnPageChangeListener(OnPageChangeListener listener) {
			mOnPageChangeListener = listener;
		}

		/**
		 * Set a separate OnPageChangeListener for internal use by the support library.
		 *
		 * @param listener Listener to set
		 * @return The old listener that was set, if any.
		 */
		OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
			OnPageChangeListener oldListener = mInternalPageChangeListener;
			mInternalPageChangeListener = listener;
			return oldListener;
		}

		/**
		 * Set the margin between pages.
		 *
		 * @param shadowWidth Distance between adjacent pages in pixels
		 * @see #getShadowWidth()
		 * @see #setShadowDrawable(Drawable)
		 * @see #setShadowDrawable(int)
		 */
		public void setShadowWidth(int shadowWidth) {
			final int oldWidth = mShadowWidth;
			mShadowWidth = shadowWidth;
			invalidate();
		}

		/**
		 * Return the margin between pages.
		 *
		 * @return The size of the margin in pixels
		 */
		public int getShadowWidth() {
			return mShadowWidth;
		}

		/**
		 * Set a drawable that will be used to fill the margin between pages.
		 *
		 * @param d Drawable to display between pages
		 */
		public void setShadowDrawable(Drawable d) {
			mShadowDrawable = d;
			if (d != null) refreshDrawableState();
			setWillNotDraw(d == null);
			invalidate();
		}

		/**
		 * Set a drawable that will be used to fill the margin between pages.
		 *
		 * @param resId Resource ID of a drawable to display between pages
		 */
		public void setShadowDrawable(int resId) {
			setShadowDrawable(getContext().getResources().getDrawable(resId));
		}


		protected boolean verifyDrawable(Drawable who) {
			return super.verifyDrawable(who) || who == mShadowDrawable;
		}


		protected void drawableStateChanged() {
			super.drawableStateChanged();
			final Drawable d = mShadowDrawable;
			if (d != null && d.isStateful()) {
				d.setState(getDrawableState());
			}
		}

		// We want the duration of the page snap animation to be influenced by the distance that
		// the screen has to travel, however, we don't want this duration to be effected in a
		// purely linear fashion. Instead, we use this method to moderate the effect that the distance
		// of travel has on the overall snap duration.
		float distanceInfluenceForSnapDuration(float f) {
			f -= 0.5f; // center the values about 0.
			f *= 0.3f * Math.PI / 2.0f;
			return (float) Math.sin(f);
		}

		public int getDestScrollX() {
			if (isMenuOpen()) {
				return getBehindWidth();
			} else {
				return 0;
			}
		}

		public int getChildLeft(int i) {
			if (i <= 0) return 0;
			return getChildWidth(i-1) + getChildLeft(i-1);
		}

		public int getChildRight(int i) {
			return getChildLeft(i) + getChildWidth(i);
		}

		public boolean isMenuOpen() {
			return getCurrentItem() == 0;
		}

		public int getCustomWidth() {
			int i = isMenuOpen()? 0 : 1;
			return getChildWidth(i);
		}

		public int getChildWidth(int i) {
			if (i <= 0) {
				return getBehindWidth();
			} else {
				return getChildAt(i).getMeasuredWidth();
			}
		}

		public int getBehindWidth() {
			if (mCustomViewBehind2 == null) {
				return 0;
			} else {
				return mCustomViewBehind2.getWidth();
			}
		}

		public boolean isSlidingEnabled() {
			return mEnabled;
		}

		public void setSlidingEnabled(boolean b) {
			mEnabled = b;
		}

		/**
		 * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
		 *
		 * @param x the number of pixels to scroll by on the X axis
		 * @param y the number of pixels to scroll by on the Y axis
		 */
		void smoothScrollTo(int x, int y) {
			smoothScrollTo(x, y, 0);
		}

		/**
		 * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
		 *
		 * @param x the number of pixels to scroll by on the X axis
		 * @param y the number of pixels to scroll by on the Y axis
		 * @param velocity the velocity associated with a fling, if applicable. (0 otherwise)
		 */
		void smoothScrollTo(int x, int y, int velocity) {
			if (getChildCount() == 0) {
				// Nothing to do.
				setScrollingCacheEnabled(false);
				return;
			}
			int sx = getScrollX();
			int sy = getScrollY();
			int dx = x - sx;
			int dy = y - sy;
			if (dx == 0 && dy == 0) {
				completeScroll();
				setScrollState(SCROLL_STATE_IDLE);
				return;
			}

			setScrollingCacheEnabled(true);
			mScrolling = true;
			setScrollState(SCROLL_STATE_SETTLING);

			final int width = getCustomWidth();
			final int halfWidth = width / 2;
			final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
			final float distance = halfWidth + halfWidth *
					distanceInfluenceForSnapDuration(distanceRatio);

			int duration = 0;
			velocity = Math.abs(velocity);
			if (velocity > 0) {
				duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
			} else {
				final float pageDelta = (float) Math.abs(dx) / (width + mShadowWidth);
				duration = (int) ((pageDelta + 1) * 100);
				// TODO set custom duration!
				duration = MAX_SETTLE_DURATION;
			}
			duration = Math.min(duration, MAX_SETTLE_DURATION);

			mScroller.startScroll(sx, sy, dx, dy, duration);
			invalidate();
		}

		ArrayList<ItemInfo> getItems() {
			ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
			if (mWindow != null) {
				mItems.add(mWindow);
			}
			if (mContent != null) {
				mItems.add(mContent);
			}
			return mItems;
		}

		void dataSetChanged() {
			// This method only gets called if our observer is attached, so mAdapter is non-null.
			boolean needPopulate = false;
			int newCurrItem = -1;
			ArrayList<ItemInfo> items = getItems();
			for (int i = 0; i < items.size(); i++) {
				final ItemInfo ii = items.get(i);
				final int newPos = ii.position;

				if (ii.position != newPos) {
					if (ii.position == mCurItem) {
						// Our current item changed position. Follow it.
						newCurrItem = newPos;
					}

					ii.position = newPos;
					needPopulate = true;
				}
			}

			if (newCurrItem >= 0) {
				setCurrentItemInternal(newCurrItem, false, true);
				needPopulate = true;
			}
			if (needPopulate) {
				populate();
				requestLayout();
			}
		}

		void populate() {
			// Bail now if we are waiting to populate.  This is to hold off
			// on creating views from the time the user releases their finger to
			// fling to a new position until we have finished the scroll to
			// that position, avoiding glitches from happening at that point.
			if (mPopulatePending) {
				if (DEBUG) Log.i(TAG, "populate is pending, skipping for now...");
				return;
			}

			// Also, don't populate until we are attached to a window.  This is to
			// avoid trying to populate before we have restored our view hierarchy
			// state and conflicting with what is restored.
			if (getWindowToken() == null) {
				return;
			}

			if (DEBUG) {
				Log.i(TAG, "Current page list:");
				for (int i=0; i<getCount(); i++) {
					Log.i(TAG, "#" + i + ": page " + getItems().get(i).position);
				}
			}

			ItemInfo curItem = null;
			if (mWindow != null && mWindow.position == mCurItem) {
				curItem = mWindow;
			} else if (mContent != null && mContent.position == mCurItem) {
				curItem = mContent;
			}

			if (hasFocus()) {
				View currentFocused = findFocus();
				ItemInfo ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
				if (ii == null || ii.position != mCurItem) {
					for (int i=0; i<getChildCount(); i++) {
						View child = getChildAt(i);
						ii = infoForChild(child);
						if (ii != null && ii.position == mCurItem) {
							if (child.requestFocus(FOCUS_FORWARD)) {
								break;
							}
						}
					}
				}
			}
		}

		protected void setMenu(View v) {
			ItemInfo ii = new ItemInfo();
			ii.position = 0;
			ii.object = v;
			if (mWindow != null) {
				removeView((View)mWindow.object);
			}
			addView(v);
			mWindow = ii;
		}

		public void setContent(View v) {
			ItemInfo ii = new ItemInfo();
			ii.position = 1;
			ii.object = v;
			if (mContent != null) {
				removeView((View)mContent.object);
			}
			addView(v);
			mContent = ii;
		}

		public void setCustomViewBehind2(CustomViewBehind cvb) {
			mCustomViewBehind2 = cvb;
		}

		public void addView(View child, int index, ViewGroup.LayoutParams params) {
			if (!checkLayoutParams(params)) {
				params = generateLayoutParams(params);
			}
			final LayoutParams lp = (LayoutParams) params;
			lp.isDecor |= child instanceof Decor;
			if (mInLayout) {
				if (lp != null && lp.isDecor) {
					throw new IllegalStateException("Cannot add pager decor view during layout");
				}
				addViewInLayout(child, index, params);
				child.measure(mChildWidthMeasureSpec, mChildHeightMeasureSpec);
			} else {
				super.addView(child, index, params);
			}

			if (USE_CACHE) {
				if (child.getVisibility() != GONE) {
					child.setDrawingCacheEnabled(mScrollingCacheEnabled);
				} else {
					child.setDrawingCacheEnabled(false);
				}
			}
		}

		ItemInfo infoForChild(View child) {
			if (mWindow != null && child.equals(mWindow.object)) {
				return mWindow;
			} else if (mContent != null && child.equals(mContent.object)) {
				return mContent;
			}
			return null;
		}

		ItemInfo infoForAnyChild(View child) {
			ViewParent parent;
			while ((parent=child.getParent()) != this) {
				if (parent == null || !(parent instanceof View)) {
					return null;
				}
				child = (View)parent;
			}
			return infoForChild(child);
		}


		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			mFirstLayout = true;
		}


		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// For simple implementation, or internal size is always 0.
			// We depend on the container to specify the layout size of
			// our view.  We can't really know what it is since we will be
			// adding and removing different arbitrary views and do not
			// want the layout to change as this happens.
			int width = getDefaultSize(0, widthMeasureSpec);
			int height = getDefaultSize(0, heightMeasureSpec);
			setMeasuredDimension(width, height);

			// Children are just made to fill our space.
			int childWidthSize = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
			int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

			/*
			 * Make sure all children have been properly measured. Decor views first.
			 * Right now we cheat and make this less complicated by assuming decor
			 * views won't intersect. We will pin to edges based on gravity.
			 */
			int size = getChildCount();
			for (int i = 0; i < size; ++i) {
				final View child = getChildAt(i);
				if (child.getVisibility() != GONE) {
					final LayoutParams lp = (LayoutParams) child.getLayoutParams();
					if (lp != null && lp.isDecor) {
						final int hgrav = lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
						final int vgrav = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;
						Log.d(TAG, "gravity: " + lp.gravity + " hgrav: " + hgrav + " vgrav: " + vgrav);
						int widthMode = MeasureSpec.AT_MOST;
						int heightMode = MeasureSpec.AT_MOST;
						boolean consumeVertical = vgrav == Gravity.TOP || vgrav == Gravity.BOTTOM;
						boolean consumeHorizontal = hgrav == Gravity.LEFT || hgrav == Gravity.RIGHT;

						if (consumeVertical) {
							widthMode = MeasureSpec.EXACTLY;
						} else if (consumeHorizontal) {
							heightMode = MeasureSpec.EXACTLY;
						}
						int pos = infoForChild(child).position;
						childWidthSize = getChildWidth(pos);
						final int widthSpec = MeasureSpec.makeMeasureSpec(childWidthSize, widthMode);
						final int heightSpec = MeasureSpec.makeMeasureSpec(childHeightSize, heightMode);
						child.measure(widthSpec, heightSpec);

						if (consumeVertical) {
							childHeightSize -= child.getMeasuredHeight();
						} else if (consumeHorizontal) {
							childWidthSize -= child.getMeasuredWidth();
						}
					}
				}
			}

			mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
			mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

			// Make sure we have created all fragments that we need to have shown.
			mInLayout = true;
			populate();
			mInLayout = false;

			// Page views next.
			size = getChildCount();
			for (int i = 0; i < size; ++i) {
				final View child = getChildAt(i);
				if (child.getVisibility() != GONE) {
					if (DEBUG) Log.v(TAG, "Measuring #" + i + " " + child
							+ ": " + mChildWidthMeasureSpec);

					final LayoutParams lp = (LayoutParams) child.getLayoutParams();
					if (lp == null || !lp.isDecor) {
						child.measure(mChildWidthMeasureSpec, mChildHeightMeasureSpec);
					}
				}
			}
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			// Make sure scroll position is set correctly.
			if (w != oldw) {
				recomputeScrollPosition(w, oldw, mShadowWidth, mShadowWidth);
			}
		}

		private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
			final int widthWithMargin = width + margin;
			if (oldWidth > 0) {
				final int oldScrollPos = getDestScrollX();
				final int oldwwm = oldWidth + oldMargin;
				final int oldScrollItem = oldScrollPos / oldwwm;
				final float scrollOffset = (float) (oldScrollPos % oldwwm) / oldwwm;
				final int scrollPos = (int) ((oldScrollItem + scrollOffset) * widthWithMargin);
				scrollTo(scrollPos, getScrollY());
				if (!mScroller.isFinished()) {
					// We now return to your regularly scheduled scroll, already in progress.
					final int newDuration = mScroller.getDuration() - mScroller.timePassed();
					mScroller.startScroll(scrollPos, 0, getChildLeft(mCurItem), 0, newDuration);
				}
			} else {
				int scrollPos = getChildLeft(mCurItem);
				if (scrollPos != getScrollX()) {
					completeScroll();
					scrollTo(scrollPos, getScrollY());
				}
			}
		}

		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			mInLayout = true;
			populate();
			mInLayout = false;

			final int count = getChildCount();
			int height = b - t;
			int paddingTop = getPaddingTop();
			int paddingBottom = getPaddingBottom();

			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() != GONE) {
					int pos = infoForChild(child).position;
					int childLeft = 0;
					int childTop = 0;
					childLeft = getChildLeft(pos);
					child.layout(childLeft, childTop,
							childLeft + child.getMeasuredWidth(),
							childTop + child.getMeasuredHeight());
					Log.v(TAG, "top: " + childTop + ", left: " + childLeft +
							", height: " + child.getMeasuredHeight() + 
							", width:" + child.getMeasuredWidth());
				}
			}
			mTopPageBounds = paddingTop;
			mBottomPageBounds = height - paddingBottom;
			mFirstLayout = false;
		}


		public void computeScroll() {
			if (DEBUG) Log.i(TAG, "computeScroll: finished=" + mScroller.isFinished());
			if (!mScroller.isFinished()) {
				if (mScroller.computeScrollOffset()) {
					if (DEBUG) Log.i(TAG, "computeScroll: still scrolling");
					int oldX = getScrollX();
					int oldY = getScrollY();
					int x = mScroller.getCurrX();
					int y = mScroller.getCurrY();

					if (oldX != x || oldY != y) {
						scrollTo(x, y);
						pageScrolled(x);
					}

					// Keep on drawing until the animation has finished.
					invalidate();
					return;
				}
			}

			// Done with scroll, clean up state.
			completeScroll();
		}

		private void pageScrolled(int xpos) {
			// TODO
			final int widthWithMargin = getChildWidth(mCurItem) + mShadowWidth;
			final int position = xpos / widthWithMargin;
			final int offsetPixels = xpos % widthWithMargin;
			final float offset = (float) offsetPixels / widthWithMargin;

			mCalledSuper = false;
			onPageScrolled(position, offset, offsetPixels);
			if (!mCalledSuper) {
				throw new IllegalStateException(
						"onPageScrolled did not call superclass implementation");
			}
		}

		/**
		 * This method will be invoked when the current page is scrolled, either as part
		 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
		 * If you override this method you must call through to the superclass implementation
		 * (e.g. super.onPageScrolled(position, offset, offsetPixels)) before onPageScrolled
		 * returns.
		 *
		 * @param position Position index of the first page currently being displayed.
		 *                 Page position+1 will be visible if positionOffset is nonzero.
		 * @param offset Value from [0, 1) indicating the offset from the page at position.
		 * @param offsetPixels Value in pixels indicating the offset from position.
		 */
		protected void onPageScrolled(int position, float offset, int offsetPixels) {
			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
			}
			if (mInternalPageChangeListener != null) {
				mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
			}
			mCalledSuper = true;
		}

		private void completeScroll() {
			boolean needPopulate = mScrolling;
			if (needPopulate) {
				// Done with scroll, no longer want to cache view drawing.
				setScrollingCacheEnabled(false);
				mScroller.abortAnimation();
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				setScrollState(SCROLL_STATE_IDLE);
			}
			mPopulatePending = false;
			mScrolling = false;
			if (mWindow != null && mWindow.scrolling) {
				needPopulate = true;
				mWindow.scrolling = false;
			}
			if (mContent != null && mContent.scrolling) {
				needPopulate = true;
				mContent.scrolling = false;
			}
			if (needPopulate) {
				populate();
			}
		}

		private int mTouchModeAbove = SlidingMenu.TOUCHMODE_MARGIN;
		private int mTouchModeBehind = SlidingMenu.TOUCHMODE_MARGIN;

		public void setTouchModeAbove(int i) {
			mTouchModeAbove = i;
		}

		public int getTouchModeAbove() {
			return mTouchModeAbove;
		}

		public void setTouchModeBehind(int i) {
			mTouchModeBehind = i;
		}

		public int getTouchModeBehind() {
			return mTouchModeBehind;
		}

		private boolean thisTouchAllowed(MotionEvent ev) {
			if (isMenuOpen()) {
				switch (mTouchModeBehind) {
				case SlidingMenu.TOUCHMODE_FULLSCREEN:
					return true;
				case SlidingMenu.TOUCHMODE_MARGIN:
					return ev.getX() >= getBehindWidth() && ev.getX() <= getWidth();
				default:
					return false;
				}
			} else {
				switch (mTouchModeAbove) {
				case SlidingMenu.TOUCHMODE_FULLSCREEN:
					return true;
				case SlidingMenu.TOUCHMODE_MARGIN:
					return ev.getX() >= 0 && ev.getX() <= mSlidingMenuThreshold;
				default:
					return false;
				}
			}
		}

		public boolean onInterceptTouchEvent(MotionEvent ev) {
			/*
			 * This method JUST determines whether we want to intercept the motion.
			 * If we return true, onMotionEvent will be called and we do the actual
			 * scrolling there.
			 */

			if (!mEnabled) {
				return false;
			}

			if (!thisTouchAllowed(ev)) {
				return false;
			}

			final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

			// Always take care of the touch gesture being complete.
			if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
				// Release the drag.
				if (DEBUG) Log.v(TAG, "Intercept done!");
				mIsBeingDragged = false;
				mIsUnableToDrag = false;
				mActivePointerId = INVALID_POINTER;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				return false;
			}

			// Nothing more to do here if we have decided whether or not we
			// are dragging.
			if (action != MotionEvent.ACTION_DOWN) {
				if (mIsBeingDragged) {
					if (DEBUG) Log.v(TAG, "Intercept returning true!");
					return true;
				}
				if (mIsUnableToDrag) {
					if (DEBUG) Log.v(TAG, "Intercept returning false!");
					return false;
				}
			}

			switch (action) {
			case MotionEvent.ACTION_MOVE: {
				/*
				 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
				 * whether the user has moved far enough from his original down touch.
				 */

				/*
				 * Locally do absolute value. mLastMotionY is set to the y value
				 * of the down event.
				 */
				final int activePointerId = mActivePointerId;
				if (activePointerId == INVALID_POINTER) {
					// If we don't have a valid id, the touch down wasn't on content.
					break;
				}

				final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
				final float x = MotionEventCompat.getX(ev, pointerIndex);
				final float dx = x - mLastMotionX;
				final float xDiff = Math.abs(dx);
				final float y = MotionEventCompat.getY(ev, pointerIndex);
				final float yDiff = Math.abs(y - mLastMotionY);
				if (DEBUG) Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);

				if (canScroll(this, false, (int) dx, (int) x, (int) y)) {
					// Nested view has scrollable area under this point. Let it be handled there.
					mInitialMotionX = mLastMotionX = x;
					mLastMotionY = y;
					return false;
				}
				if (xDiff > mTouchSlop && xDiff > yDiff) {
					if (DEBUG) Log.v(TAG, "Starting drag!");
					mIsBeingDragged = true;
					setScrollState(SCROLL_STATE_DRAGGING);
					mLastMotionX = x;
					setScrollingCacheEnabled(true);
				} else {
					if (yDiff > mTouchSlop) {
						// The finger has moved enough in the vertical
						// direction to be counted as a drag...  abort
						// any attempt to drag horizontally, to work correctly
						// with children that have scrolling containers.
						if (DEBUG) Log.v(TAG, "Starting unable to drag!");
						mIsUnableToDrag = true;
					}
				}
				break;
			}

			case MotionEvent.ACTION_DOWN: {
				/*
				 * Remember location of down touch.
				 * ACTION_DOWN always refers to pointer index 0.
				 */
				mLastMotionX = mInitialMotionX = ev.getX();
				mLastMotionY = ev.getY();
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);

				if (mScrollState == SCROLL_STATE_SETTLING) {
					// Let the user 'catch' the pager as it animates.
					mIsBeingDragged = true;
					mIsUnableToDrag = false;
					setScrollState(SCROLL_STATE_DRAGGING);
				} else if (isMenuOpen() ||
						(mTouchModeAbove != SlidingMenu.TOUCHMODE_FULLSCREEN && thisTouchAllowed(ev))) {
					// we want to intercept this touch even though we are not dragging
					// so that we can close the menu on a touch
					mIsBeingDragged = false;
					mIsUnableToDrag = false;
					return true;
				} else {
					completeScroll();
					mIsBeingDragged = false;
					mIsUnableToDrag = false;
				}

				if (DEBUG) Log.v(TAG, "Down at " + mLastMotionX + "," + mLastMotionY
						+ " mIsBeingDragged=" + mIsBeingDragged
						+ "mIsUnableToDrag=" + mIsUnableToDrag);
				break;
			}

			case MotionEventCompat.ACTION_POINTER_UP:
				onSecondaryPointerUp(ev);
				break;
			}

			if (!mIsBeingDragged) {
				// Track the velocity as long as we aren't dragging.
				// Once we start a real drag we will track in onTouchEvent.
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(ev);
			}

			/*
			 * The only time we want to intercept motion events is if we are in the
			 * drag mode.
			 */
			return mIsBeingDragged;
		}


		public boolean onTouchEvent(MotionEvent ev) {
			if (!mEnabled) {
				return false;
			}

			if (!mLastTouchAllowed && !thisTouchAllowed(ev)) {
				return false;
			}

			final int action = ev.getAction();

			if (action == MotionEvent.ACTION_UP || 
					action == MotionEvent.ACTION_POINTER_UP ||
					action == MotionEvent.ACTION_CANCEL ||
					action == MotionEvent.ACTION_OUTSIDE) {
				mLastTouchAllowed = false;
			} else {
				mLastTouchAllowed = true;
			}

			if (getCount() == 0) {
				// Nothing to present or scroll; nothing to touch.
				return false;
			}
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(ev);

			switch (action & MotionEventCompat.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				/*
				 * If being flinged and user touches, stop the fling. isFinished
				 * will be false if being flinged.
				 */
				completeScroll();

				// Remember where the motion event started
				mLastMotionX = mInitialMotionX = ev.getX();
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
				break;
			}
			case MotionEvent.ACTION_MOVE:
				if (!mIsBeingDragged) {
					final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
					final float x = MotionEventCompat.getX(ev, pointerIndex);
					final float xDiff = Math.abs(x - mLastMotionX);
					final float y = MotionEventCompat.getY(ev, pointerIndex);
					final float yDiff = Math.abs(y - mLastMotionY);
					if (DEBUG) Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
					if (xDiff > mTouchSlop && xDiff > yDiff) {
						if (DEBUG) Log.v(TAG, "Starting drag!");
						mIsBeingDragged = true;
						mLastMotionX = x;
						setScrollState(SCROLL_STATE_DRAGGING);
						setScrollingCacheEnabled(true);
					}
				}
				if (mIsBeingDragged) {
					// Scroll to follow the motion event
					final int activePointerIndex = MotionEventCompat.findPointerIndex(
							ev, mActivePointerId);
					final float x = MotionEventCompat.getX(ev, activePointerIndex);
					final float deltaX = mLastMotionX - x;
					mLastMotionX = x;
					float oldScrollX = getScrollX();
					float scrollX = oldScrollX + deltaX;
					// TODO
					final float leftBound = 0;
					final float rightBound = getBehindWidth();
					if (scrollX < leftBound) {
						scrollX = leftBound;
					} else if (scrollX > rightBound) {
						scrollX = rightBound;
					}
					// Don't lose the rounded component
					mLastMotionX += scrollX - (int) scrollX;
					scrollTo((int) scrollX, getScrollY());
					pageScrolled((int) scrollX);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
							velocityTracker, mActivePointerId);
					mPopulatePending = true;
					final int widthWithMargin = getChildWidth(mCurItem) + mShadowWidth;
					final int scrollX = getScrollX();
					final int currentPage = scrollX / widthWithMargin;
					final float pageOffset = (float) (scrollX % widthWithMargin) / widthWithMargin;
					final int activePointerIndex =
							MotionEventCompat.findPointerIndex(ev, mActivePointerId);
					final float x = MotionEventCompat.getX(ev, activePointerIndex);
					final int totalDelta = (int) (x - mInitialMotionX);
					int nextPage = determineTargetPage(currentPage, pageOffset, initialVelocity,
							totalDelta);
					setCurrentItemInternal(nextPage, true, true, initialVelocity);

					mActivePointerId = INVALID_POINTER;
					endDrag();
				} else if (isMenuOpen()) {
					// close the menu
					setCurrentItem(1);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if (mIsBeingDragged) {
					setCurrentItemInternal(mCurItem, true, true);
					mActivePointerId = INVALID_POINTER;
					endDrag();
				}
				break;
			case MotionEventCompat.ACTION_POINTER_DOWN: {
				final int index = MotionEventCompat.getActionIndex(ev);
				final float x = MotionEventCompat.getX(ev, index);
				mLastMotionX = x;
				mActivePointerId = MotionEventCompat.getPointerId(ev, index);
				break;
			}
			case MotionEventCompat.ACTION_POINTER_UP:
				onSecondaryPointerUp(ev);
				mLastMotionX = MotionEventCompat.getX(ev,
						MotionEventCompat.findPointerIndex(ev, mActivePointerId));
				break;
			}
			return true;
		}

		private float mScrollScale;

		public float getScrollScale() {
			return mScrollScale;
		}

		public void setScrollScale(float f) {
			if (f >= 0 && f <= 1) {
				mScrollScale = f;
			}
		}

		@Override
		public void scrollTo(int x, int y) {
			super.scrollTo(x, y);
			if (mCustomViewBehind2 != null && mEnabled) {
				mCustomViewBehind2.scrollTo((int)(x*mScrollScale), y);
			}
			invalidate();
		}

		private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
			int targetPage;
			if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
				targetPage = velocity > 0 ? currentPage : currentPage + 1;
			} else {
				targetPage = (int) (currentPage + pageOffset + 0.5f);
			}
			return targetPage;
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			final int behindWidth = getBehindWidth();
			// Draw the margin drawable if needed.
			if (mShadowWidth > 0 && mShadowDrawable != null) {
				final int left = behindWidth - mShadowWidth;
				mShadowDrawable.setBounds(left, mTopPageBounds, left + mShadowWidth,
						mBottomPageBounds);
				mShadowDrawable.draw(canvas);
			}

//			if (mFadeEnabled) {
//				float openPercent = 0;
//		        if (mScrollState == SCROLL_STATE_DRAGGING) {
//		            openPercent= (behindWidth - Math.min(mLastMotionX, behindWidth)) / (float) behindWidth;
//		            Log.v("STATE_DRAGGING", "openPercent: "+openPercent);
//		        } else {
//		            openPercent= (mScroller.getCurrX()) / (float) behindWidth;
//		            Log.v("STATE_SETTLING", "openPercent: "+openPercent+", scrollerX: "+mScroller.getCurrX());
//		        }
//				onDrawBehindFade(canvas, openPercent, behindWidth);
//			}
		}
		
		private float mFadeDegree;
	    private Paint mBehindFadePaint = new Paint();
	    private boolean mFadeEnabled;

	    private void onDrawBehindFade(Canvas canvas, float openPercent, int width) {
	        final int alpha = (int) (mFadeDegree * 255 * openPercent);

	        if (alpha > 0) {
	            mBehindFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
	            canvas.drawRect(0, 0, width, getHeight(), mBehindFadePaint);
	        }
	    }
	    
	    public void setBehindFadeEnabled(boolean b) {
	    	mFadeEnabled = b;
	    }
	    
	    public void setBehindFadeDegree(float f) {
	    	mFadeDegree = f;
	    }

		private void onSecondaryPointerUp(MotionEvent ev) {
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
				mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
				if (mVelocityTracker != null) {
					mVelocityTracker.clear();
				}
			}
		}

		private void endDrag() {
			mIsBeingDragged = false;
			mIsUnableToDrag = false;

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
		}

		private void setScrollingCacheEnabled(boolean enabled) {
			if (mScrollingCacheEnabled != enabled) {
				mScrollingCacheEnabled = enabled;
				if (USE_CACHE) {
					final int size = getChildCount();
					for (int i = 0; i < size; ++i) {
						final View child = getChildAt(i);
						if (child.getVisibility() != GONE) {
							child.setDrawingCacheEnabled(enabled);
						}
					}
				}
			}
		}

		/**
		 * Tests scrollability within child views of v given a delta of dx.
		 *
		 * @param v View to test for horizontal scrollability
		 * @param checkV Whether the view v passed should itself be checked for scrollability (true),
		 *               or just its children (false).
		 * @param dx Delta scrolled in pixels
		 * @param x X coordinate of the active touch point
		 * @param y Y coordinate of the active touch point
		 * @return true if child views of v can be scrolled by delta of dx.
		 */
		protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
			if (v instanceof ViewGroup) {
				final ViewGroup group = (ViewGroup) v;
				final int scrollX = v.getScrollX();
				final int scrollY = v.getScrollY();
				final int count = group.getChildCount();
				// Count backwards - let topmost views consume scroll distance first.
				for (int i = count - 1; i >= 0; i--) {
					// TODO: Add versioned support here for transformed views.
					// This will not work for transformed views in Honeycomb+
					final View child = group.getChildAt(i);
					if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
							y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
							canScroll(child, true, dx, x + scrollX - child.getLeft(),
									y + scrollY - child.getTop())) {
						return true;
					}
				}
			}

			return checkV && ViewCompat.canScrollHorizontally(v, -dx);
		}


		public boolean dispatchKeyEvent(KeyEvent event) {
			// Let the focused view and/or our descendants get the key first
			return super.dispatchKeyEvent(event) || executeKeyEvent(event);
		}

		/**
		 * You can call this function yourself to have the scroll view perform
		 * scrolling from a key event, just as if the event had been dispatched to
		 * it by the view hierarchy.
		 *
		 * @param event The key event to execute.
		 * @return Return true if the event was handled, else false.
		 */
		public boolean executeKeyEvent(KeyEvent event) {
			boolean handled = false;
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					handled = arrowScroll(FOCUS_LEFT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					handled = arrowScroll(FOCUS_RIGHT);
					break;
				case KeyEvent.KEYCODE_TAB:
					if (Build.VERSION.SDK_INT >= 11) {
						// The focus finder had a bug handling FOCUS_FORWARD and FOCUS_BACKWARD
						// before Android 3.0. Ignore the tab key on those devices.
						if (KeyEventCompat.hasNoModifiers(event)) {
							handled = arrowScroll(FOCUS_FORWARD);
						} else if (KeyEventCompat.hasModifiers(event, KeyEvent.META_SHIFT_ON)) {
							handled = arrowScroll(FOCUS_BACKWARD);
						}
					}
					break;
				}
			}
			return handled;
		}

		public boolean arrowScroll(int direction) {
			View currentFocused = findFocus();
			if (currentFocused == this) currentFocused = null;

			boolean handled = false;

			View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused,
					direction);
			if (nextFocused != null && nextFocused != currentFocused) {
				if (direction == View.FOCUS_LEFT) {
					// If there is nothing to the left, or this is causing us to
					// jump to the right, then what we really want to do is page left.
					if (currentFocused != null && nextFocused.getLeft() >= currentFocused.getLeft()) {
						handled = pageLeft();
					} else {
						handled = nextFocused.requestFocus();
					}
				} else if (direction == View.FOCUS_RIGHT) {
					// If there is nothing to the right, or this is causing us to
					// jump to the left, then what we really want to do is page right.
					if (currentFocused != null && nextFocused.getLeft() <= currentFocused.getLeft()) {
						handled = pageRight();
					} else {
						handled = nextFocused.requestFocus();
					}
				}
			} else if (direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) {
				// Trying to move left and nothing there; try to page.
				handled = pageLeft();
			} else if (direction == FOCUS_RIGHT || direction == FOCUS_FORWARD) {
				// Trying to move right and nothing there; try to page.
				handled = pageRight();
			}
			if (handled) {
				playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
			}
			return handled;
		}

		boolean pageLeft() {
			if (mCurItem > 0) {
				setCurrentItem(mCurItem-1, true);
				return true;
			}
			return false;
		}

		boolean pageRight() {
			if (mCurItem < (getCount()-1)) {
				setCurrentItem(mCurItem+1, true);
				return true;
			}
			return false;
		}

		/**
		 * We only want the current page that is being shown to be focusable.
		 */

		public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
			final int focusableCount = views.size();

			final int descendantFocusability = getDescendantFocusability();

			if (descendantFocusability != FOCUS_BLOCK_DESCENDANTS) {
				for (int i = 0; i < getChildCount(); i++) {
					final View child = getChildAt(i);
					if (child.getVisibility() == VISIBLE) {
						ItemInfo ii = infoForChild(child);
						if (ii != null && ii.position == mCurItem) {
							child.addFocusables(views, direction, focusableMode);
						}
					}
				}
			}

			// we add ourselves (if focusable) in all cases except for when we are
			// FOCUS_AFTER_DESCENDANTS and there are some descendants focusable.  this is
			// to avoid the focus search finding layouts when a more precise search
			// among the focusable children would be more interesting.
			if (
					descendantFocusability != FOCUS_AFTER_DESCENDANTS ||
					// No focusable descendants
					(focusableCount == views.size())) {
				// Note that we can't call the superclass here, because it will
				// add all views in.  So we need to do the same thing View does.
				if (!isFocusable()) {
					return;
				}
				if ((focusableMode & FOCUSABLES_TOUCH_MODE) == FOCUSABLES_TOUCH_MODE &&
						isInTouchMode() && !isFocusableInTouchMode()) {
					return;
				}
				if (views != null) {
					views.add(this);
				}
			}
		}

		/**
		 * We only want the current page that is being shown to be touchable.
		 */

		public void addTouchables(ArrayList<View> views) {
			// Note that we don't call super.addTouchables(), which means that
			// we don't call View.addTouchables().  This is okay because a CustomViewPager
			// is itself not touchable.
			for (int i = 0; i < getChildCount(); i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == VISIBLE) {
					ItemInfo ii = infoForChild(child);
					if (ii != null && ii.position == mCurItem) {
						child.addTouchables(views);
					}
				}
			}
		}

		/**
		 * We only want the current page that is being shown to be focusable.
		 */

		protected boolean onRequestFocusInDescendants(int direction,
				Rect previouslyFocusedRect) {
			int index;
			int increment;
			int end;
			int count = getChildCount();
			if ((direction & FOCUS_FORWARD) != 0) {
				index = 0;
				increment = 1;
				end = count;
			} else {
				index = count - 1;
				increment = -1;
				end = -1;
			}
			for (int i = index; i != end; i += increment) {
				View child = getChildAt(i);
				if (child.getVisibility() == VISIBLE) {
					ItemInfo ii = infoForChild(child);
					if (ii != null && ii.position == mCurItem) {
						if (child.requestFocus(direction, previouslyFocusedRect)) {
							return true;
						}
					}
				}
			}
			return false;
		}


		public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
			// CustomViewPagers should only report accessibility info for the current page,
			// otherwise things get very confusing.

			// TODO: Should this note something about the paging container?

			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == VISIBLE) {
					final ItemInfo ii = infoForChild(child);
					if (ii != null && ii.position == mCurItem &&
							child.dispatchPopulateAccessibilityEvent(event)) {
						return true;
					}
				}
			}

			return false;
		}


		protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
			return new LayoutParams();
		}


		protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
			return generateDefaultLayoutParams();
		}


		protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
			return p instanceof LayoutParams && super.checkLayoutParams(p);
		}


		public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
			return new LayoutParams(getContext(), attrs);
		}

		private class PagerObserver extends DataSetObserver {

			public void onChanged() {
				dataSetChanged();
			}

			public void onInvalidated() {
				dataSetChanged();
			}
		}

		/**
		 * Layout parameters that should be supplied for views added to a
		 * CustomViewPager.
		 */
		public static class LayoutParams extends ViewGroup.LayoutParams {
			/**
			 * true if this view is a decoration on the pager itself and not
			 * a view supplied by the adapter.
			 */
			public boolean isDecor;

			/**
			 * Where to position the view page within the overall CustomViewPager
			 * container; constants are defined in {@link android.view.Gravity}.
			 */
			public int gravity;

			public LayoutParams() {
				this(-1);
			}

			public LayoutParams(int customWidth) {
				super(FILL_PARENT, FILL_PARENT);
				if (customWidth >= 0) {
					width = customWidth;
				}
			}

			public LayoutParams(Context context, AttributeSet attrs) {
				super(context, attrs);

				final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
				gravity = a.getInteger(0, Gravity.NO_GRAVITY);
				a.recycle();
			}
		}
}
