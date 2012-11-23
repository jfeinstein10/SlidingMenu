package com.slidingmenu.lib;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.CustomViewAbove.OnPageChangeListener;

public class SlidingMenu extends RelativeLayout {

	/** Constant value for use with setTouchModeAbove(). Allows the SlidingMenu to be opened with a swipe
	 * gesture on the screen's margin
	 */
	public static final int TOUCHMODE_MARGIN = 0;
	
	/** Constant value for use with setTouchModeAbove(). Allows the SlidingMenu to be opened with a swipe
	 * gesture anywhere on the screen
	 */
	public static final int TOUCHMODE_FULLSCREEN = 1;
	
	/** Constant value for use with setTouchModeAbove(). Denies the SlidingMenu to be opened with a swipe
	 * gesture
	 */
	public static final int TOUCHMODE_NONE = 2;
	
	/** Constant value for use with setMode(). Puts the menu to the left of the content.
	 */
	public static final int LEFT = 0;
	
	/** Constant value for use with setMode(). Puts the menu to the right of the content.
	 */
	public static final int RIGHT = 1;

	private CustomViewAbove mViewAbove;
	
	private CustomViewBehind mViewBehind;
	
	private OnOpenListener mOpenListener;
	
	private OnCloseListener mCloseListener;

	/**
     * Attach a given SlidingMenu to a given Activity
     *
     * @param activity the Activity to attach to
     * @param sm the SlidingMenu to be attached
     * @param slidingTitle whether the title is slid with the above view
     */
    public static void attachSlidingMenu(Activity activity, SlidingMenu sm, boolean slidingTitle) {

		if (sm.getParent() != null)
			throw new IllegalStateException("SlidingMenu cannot be attached to another view when" +
					" calling the static method attachSlidingMenu");

		if (slidingTitle) {
			// get the window background
			TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
			int background = a.getResourceId(0, 0);
			a.recycle();
			// move everything into the SlidingMenu
			ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			decor.removeAllViews();
			// save ActionBar themes that have transparent assets
			decorChild.setBackgroundResource(background);
			sm.setContent(decorChild);
			decor.addView(sm);
		} else {
			// take the above view out of
			ViewGroup content = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
			View above = content.getChildAt(0);
			content.removeAllViews();
			sm.setContent(above);
			content.addView(sm, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
	}

	/**
	 * The listener interface for receiving onOpen events.
	 * The class that is interested in processing a onOpen
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnOpenListener<code> method. When
	 * the onOpen event occurs, that object's appropriate
	 * method is invoked
	 */
	public interface OnOpenListener {
		
		/**
		 * On open.
		 */
		public void onOpen();
	}

	/**
	 * The listener interface for receiving onOpened events.
	 * The class that is interested in processing a onOpened
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnOpenedListener<code> method. When
	 * the onOpened event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnOpenedEvent
	 */
	public interface OnOpenedListener {
		
		/**
		 * On opened.
		 */
		public void onOpened();
	}

	/**
	 * The listener interface for receiving onClose events.
	 * The class that is interested in processing a onClose
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnCloseListener<code> method. When
	 * the onClose event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnCloseEvent
	 */
	public interface OnCloseListener {
		
		/**
		 * On close.
		 */
		public void onClose();
	}

	/**
	 * The listener interface for receiving onClosed events.
	 * The class that is interested in processing a onClosed
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnClosedListener<code> method. When
	 * the onClosed event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnClosedEvent
	 */
	public interface OnClosedListener {
		
		/**
		 * On closed.
		 */
		public void onClosed();
	}

	/**
	 * The Interface CanvasTransformer.
	 */
	public interface CanvasTransformer {
		
		/**
		 * Transform canvas.
		 *
		 * @param canvas the canvas
		 * @param percentOpen the percent open
		 */
		public void transformCanvas(Canvas canvas, float percentOpen);
	}

	/**
	 * Instantiates a new SlidingMenu.
	 *
	 * @param context the associated Context
	 */
	public SlidingMenu(Context context) {
		this(context, null);
	}

	/**
	 * Instantiates a new SlidingMenu.
	 *
	 * @param context the associated Context
	 * @param attrs the attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Instantiates a new SlidingMenu.
	 *
	 * @param context the associated Context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewBehind = new CustomViewBehind(context);
		addView(mViewBehind, behindParams);
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		aboveParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mViewAbove = new CustomViewAbove(context);
		addView(mViewAbove, aboveParams);
		// register the CustomViewBehind2 with the CustomViewAbove
		mViewAbove.setCustomViewBehind(mViewBehind);
		mViewBehind.setCustomViewAbove(mViewAbove);
		mViewAbove.setOnPageChangeListener(new OnPageChangeListener() {
			public static final int POSITION_OPEN = 0;
			public static final int POSITION_CLOSE = 1;

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) { }

			public void onPageSelected(int position) {
				if (position == POSITION_OPEN && mOpenListener != null) {
					mOpenListener.onOpen();
				} else if (position == POSITION_CLOSE && mCloseListener != null) {
					mCloseListener.onClose();
				}
			}
		});

		// now style everything!
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
		// set the above and behind views if defined in xml
		int mode = ta.getInt(R.styleable.SlidingMenu_mode, LEFT);
		setMode(mode);
		int viewAbove = ta.getResourceId(R.styleable.SlidingMenu_viewAbove, -1);
		if (viewAbove != -1) {
			setContent(viewAbove);
		} else {
			setContent(new FrameLayout(context));
		}
		int viewBehind = ta.getResourceId(R.styleable.SlidingMenu_viewBehind, -1);
		if (viewBehind != -1) {
			setMenu(viewBehind); 
		} else {
			setMenu(new FrameLayout(context));
		}
		int touchModeAbove = ta.getInt(R.styleable.SlidingMenu_touchModeAbove, TOUCHMODE_MARGIN);
		setTouchModeAbove(touchModeAbove);

		int offsetBehind = (int) ta.getDimension(R.styleable.SlidingMenu_behindOffset, -1);
		int widthBehind = (int) ta.getDimension(R.styleable.SlidingMenu_behindWidth, -1);
		if (offsetBehind != -1 && widthBehind != -1)
			throw new IllegalStateException("Cannot set both behindOffset and behindWidth for a SlidingMenu");
		else if (offsetBehind != -1)
			setBehindOffset(offsetBehind);
		else if (widthBehind != -1)
			setBehindWidth(widthBehind);
		else
			setBehindOffset(0);
		float scrollOffsetBehind = ta.getFloat(R.styleable.SlidingMenu_behindScrollScale, 0.33f);
		setBehindScrollScale(scrollOffsetBehind);
		int shadowRes = ta.getResourceId(R.styleable.SlidingMenu_shadowDrawable, -1);
		if (shadowRes != -1) {
			setShadowDrawable(shadowRes);
		}
		int shadowWidth = (int) ta.getDimension(R.styleable.SlidingMenu_shadowWidth, 0);
		setShadowWidth(shadowWidth);
		boolean fadeEnabled = ta.getBoolean(R.styleable.SlidingMenu_behindFadeEnabled, true);
		setFadeEnabled(fadeEnabled);
		float fadeDeg = ta.getFloat(R.styleable.SlidingMenu_behindFadeDegree, 0.66f);
		setFadeDegree(fadeDeg);
		boolean selectorEnabled = ta.getBoolean(R.styleable.SlidingMenu_selectorEnabled, false);
		setSelectorEnabled(selectorEnabled);
		int selectorRes = ta.getResourceId(R.styleable.SlidingMenu_selectorDrawable, -1);
		if (selectorRes != -1)
			setSelectorDrawable(selectorRes);
		ta.recycle();
	}

	/**
	 * Set the above view content from a layout resource. The resource will be inflated, adding all top-level views
	 * to the above view.
	 *
	 * @param res the new content
	 */
	public void setContent(int res) {
		setContent(LayoutInflater.from(getContext()).inflate(res, null));
	}

	/**
	 * Set the above view content to the given View.
	 *
	 * @param view The desired content to display.
	 */
	public void setContent(View view) {
		mViewAbove.setContent(view);
		mViewAbove.invalidate();
		showAbove();
	}
	
	/**
	 * Retrieves the current content.
	 * @return the current content
	 */
	public View getContent() {
		return mViewAbove.getContent();
	}

	/**
	 * Set the behind view (menu) content from a layout resource. The resource will be inflated, adding all top-level views
	 * to the behind view.
	 *
	 * @param res the new content
	 */
	public void setMenu(int res) {
		setMenu(LayoutInflater.from(getContext()).inflate(res, null));
	}

	/**
	 * Set the behind view (menu) content to the given View.
	 *
	 * @param view The desired content to display.
	 */
	public void setMenu(View v) {
		mViewBehind.setContent(v);
		mViewBehind.invalidate();
	}
	
	/**
	 * Retrieves the current menu.
	 * @return the current menu
	 */
	public View getMenu() {
		return mViewBehind.getContent();
	}

	/**
	 * Sets the sliding enabled.
	 *
	 * @param b true to enable sliding, false to disable it.
	 */
	public void setSlidingEnabled(boolean b) {
		mViewAbove.setSlidingEnabled(b);
	}

	/**
	 * Checks if is sliding enabled.
	 *
	 * @return true, if is sliding enabled
	 */
	public boolean isSlidingEnabled() {
		return mViewAbove.isSlidingEnabled();
	}
	
	/**
	 * Sets which side the SlidingMenu should appear on.
	 * @param mode must be either SlidingMenu.LEFT or SlidingMenu.RIGHT
	 */
	public void setMode(int mode) {
		if (mode != LEFT && mode != RIGHT) {
			throw new IllegalStateException("SlidingMenu mode must be LEFT or RIGHT");
		}
		mViewAbove.setMode(mode);
	}
	
	/**
	 * Returns the current side that the SlidingMenu is on.
	 * @return the current mode, either SlidingMenu.LEFT or SlidingMenu.RIGHT
	 */
	public int getMode() {
		return mViewAbove.getMode();
	}

	/**
	 * Sets whether or not the SlidingMenu is in static mode (i.e. nothing is moving and everything is showing)
	 *
	 * @param b true to set static mode, false to disable static mode.
	 */
	public void setStatic(boolean b) {
		if (b) {
			setSlidingEnabled(false);
			mViewAbove.setCustomViewBehind(null);
			mViewAbove.setCurrentItem(1);
//			mViewBehind.setCurrentItem(0);	
		} else {
			mViewAbove.setCurrentItem(1);
//			mViewBehind.setCurrentItem(1);
			mViewAbove.setCustomViewBehind(mViewBehind);
			setSlidingEnabled(true);
		}
	}

	/**
	 * Opens the menu and shows the behind view.
	 */
	public void showBehind() {
		showBehind(true);
	}
	
	/**
	 * Opens the menu and shows the behind view.
	 *
	 * @param animate true to animate the transition, false to ignore animation
	 */
	public void showBehind(boolean animate) {
		mViewAbove.setCurrentItem(0, animate);
	}

	/**
	 * Closes the menu and shows the above view.
	 */
	public void showAbove() {
		showAbove(true);
	}
	
	/**
	 * Closes the menu and shows the above view.
	 *
	 * @param animate true to animate the transition, false to ignore animation
	 */
	public void showAbove(boolean animate) {
		mViewAbove.setCurrentItem(1, animate);
	}
	
	/**
	 * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
	 */
	public void toggle() {
		toggle(true);
	}
	
	/**
	 * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
	 *
	 * @param animate true to animate the transition, false to ignore animation
	 */
	public void toggle(boolean animate) {
		if (isBehindShowing()) {
			showAbove(animate);
		} else {
			showBehind(animate);
		}
	}

	/**
	 * Checks if is the behind view showing.
	 *
	 * @return Whether or not the behind view is showing
	 */
	public boolean isBehindShowing() {
		return mViewAbove.getCurrentItem() == 0 || mViewAbove.getCurrentItem() == 2;
	}

	/**
	 * Gets the behind offset.
	 *
	 * @return The margin on the right of the screen that the behind view scrolls to
	 */
	public int getBehindOffset() {
		return ((RelativeLayout.LayoutParams)mViewBehind.getLayoutParams()).rightMargin;
	}

	/**
	 * Sets the behind offset.
	 *
	 * @param i The margin, in pixels, on the right of the screen that the behind view scrolls to.
	 */
	public void setBehindOffset(int i) {
//		RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)mViewBehind.getLayoutParams());
//		int bottom = params.bottomMargin;
//		int top = params.topMargin;
//		int left = params.leftMargin;
//		params.setMargins(left, top, i, bottom);
		mViewBehind.setWidthOffset(i);
	}

	/**
	 * Sets the behind offset.
	 *
	 * @param resID The dimension resource id to be set as the behind offset.
	 * The menu, when open, will leave this width margin on the right of the screen.
	 */
	public void setBehindOffsetRes(int resID) {
		int i = (int) getContext().getResources().getDimension(resID);
		setBehindOffset(i);
	}
	
	/**
	 * Sets the above offset.
	 *
	 * @param i the new above offset, in pixels
	 */
	public void setAboveOffset(int i) {
//		RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)mViewAbove.getLayoutParams());
//		int bottom = params.bottomMargin;
//		int top = params.topMargin;
//		int right = params.rightMargin;
//		params.setMargins(i, top, right, bottom);
//		this.requestLayout();
		mViewAbove.setAboveOffset(i);
	}
		
	/**
	 * Sets the above offset.
	 *
	 * @param resID The dimension resource id to be set as the above offset.
	 */
	public void setAboveOffsetRes(int resID) {
		int i = (int) getContext().getResources().getDimension(resID);
		setAboveOffset(i);
	}

	/**
	 * Sets the behind width.
	 *
	 * @param i The width the Sliding Menu will open to, in pixels
	 */
	@SuppressWarnings("deprecation")
	public void setBehindWidth(int i) {
		int width;
		Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		try {
			Class<?> cls = Display.class;
			Class<?>[] parameterTypes = {Point.class};
			Point parameter = new Point();
			Method method = cls.getMethod("getSize", parameterTypes);
			method.invoke(display, parameter);
			width = parameter.x;
		} catch (Exception e) {
			width = display.getWidth();
		}
		setBehindOffset(width-i);
	}
	
	/**
	 * Sets the behind width.
	 *
	 * @param res The dimension resource id to be set as the behind width offset.
	 * The menu, when open, will open this wide.
	 */
	public void setBehindWidthRes(int res) {
		int i = (int) getContext().getResources().getDimension(res);
		setBehindWidth(i);
	}

	/**
	 * Gets the behind scroll scale.
	 *
	 * @return The scale of the parallax scroll
	 */
	public float getBehindScrollScale() {
		return mViewAbove.getScrollScale();
	}

	/**
	 * Sets the behind scroll scale.
	 *
	 * @param f The scale of the parallax scroll (i.e. 1.0f scrolls 1 pixel for every
	 * 1 pixel that the above view scrolls and 0.0f scrolls 0 pixels)
	 */
	public void setBehindScrollScale(float f) {
		mViewAbove.setScrollScale(f);
	}

	/**
	 * Sets the behind canvas transformer.
	 *
	 * @param t the new behind canvas transformer
	 */
	public void setBehindCanvasTransformer(CanvasTransformer t) {
		mViewBehind.setCanvasTransformer(t);
	}

	/**
	 * Gets the touch mode above.
	 *
	 * @return the touch mode above
	 */
	public int getTouchModeAbove() {
		return mViewAbove.getTouchMode();
	}

	/**
	 * Controls whether the SlidingMenu can be opened with a swipe gesture.
	 * Options are {@link #TOUCHMODE_MARGIN TOUCHMODE_MARGIN}, {@link #TOUCHMODE_FULLSCREEN TOUCHMODE_FULLSCREEN},
	 * or {@link #TOUCHMODE_NONE TOUCHMODE_NONE}
	 *
	 * @param i the new touch mode
	 */
	public void setTouchModeAbove(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN
				&& i != TOUCHMODE_NONE) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
		}
		mViewAbove.setTouchMode(i);
	}

	/**
	 * Sets the shadow drawable.
	 *
	 * @param resId the resource ID of the new shadow drawable
	 */
	public void setShadowDrawable(int resId) {
		mViewAbove.setShadowDrawable(resId);
	}
	
	/**
	 * Sets the shadow drawable.
	 *
	 * @param d the new shadow drawable
	 */
	public void setShadowDrawable(Drawable d) {
		mViewAbove.setShadowDrawable(d);
	}

	/**
	 * Sets the shadow width.
	 *
	 * @param resId The dimension resource id to be set as the shadow width.
	 */
	public void setShadowWidthRes(int resId) {
		setShadowWidth((int)getResources().getDimension(resId));
	}

	/**
	 * Sets the shadow width.
	 *
	 * @param pixels the new shadow width, in pixels
	 */
	public void setShadowWidth(int pixels) {
		mViewAbove.setShadowWidth(pixels);
	}

	/**
	 * Enables or disables the SlidingMenu's fade in and out
	 *
	 * @param b true to enable fade, false to disable it
	 */
	public void setFadeEnabled(boolean b) {
		mViewAbove.setBehindFadeEnabled(b);
	}

	/**
	 * Sets how much the SlidingMenu fades in and out. Fade must be enabled, see
	 * {@link #setFadeEnabled(boolean) setFadeEnabled(boolean)}
	 *
	 * @param f the new fade degree, between 0.0f and 1.0f
	 */
	public void setFadeDegree(float f) {
		mViewAbove.setBehindFadeDegree(f);
	}

	/**
	 * Enables or disables whether the selector is drawn
	 *
	 * @param b true to draw the selector, false to not draw the selector
	 */
	public void setSelectorEnabled(boolean b) {
		mViewAbove.setSelectorEnabled(true);
	}

	/**
	 * Sets the selected view. The selector will be drawn here
	 *
	 * @param v the new selected view
	 */
	public void setSelectedView(View v) {
		mViewAbove.setSelectedView(v);
	}

	/**
	 * Sets the selector drawable.
	 *
	 * @param res a resource ID for the selector drawable
	 */
	public void setSelectorDrawable(int res) {
		mViewAbove.setSelectorBitmap(BitmapFactory.decodeResource(getResources(), res));
	}

	/**
	 * Sets the selector drawable.
	 *
	 * @param b the new selector bitmap
	 */
	public void setSelectorBitmap(Bitmap b) {
		mViewAbove.setSelectorBitmap(b);
	}

	/**
	 * Sets the OnOpenListener. {@link OnOpenListener#onOpen() OnOpenListener.onOpen()} will be called when the SlidingMenu is opened
	 *
	 * @param listener the new OnOpenListener
	 */
	public void setOnOpenListener(OnOpenListener listener) {
		//mViewAbove.setOnOpenListener(listener);
		mOpenListener = listener;
	}

	/**
	 * Sets the OnCloseListener. {@link OnCloseListener#onClose() OnCloseListener.onClose()} will be called when the SlidingMenu is closed
	 *
	 * @param listener the new setOnCloseListener
	 */
	public void setOnCloseListener(OnCloseListener listener) {
		//mViewAbove.setOnCloseListener(listener);
		mCloseListener = listener;
	}

	/**
	 * Sets the OnOpenedListener. {@link OnOpenedListener#onOpened() OnOpenedListener.onOpened()} will be called after the SlidingMenu is opened
	 *
	 * @param listener the new OnOpenedListener
	 */
	public void setOnOpenedListener(OnOpenedListener listener) {
		mViewAbove.setOnOpenedListener(listener);
	}

	/**
	 * Sets the OnClosedListener. {@link OnClosedListener#onClosed() OnClosedListener.onClosed()} will be called after the SlidingMenu is closed
	 *
	 * @param listener the new OnClosedListener
	 */
	public void setOnClosedListener(OnClosedListener listener) {
		mViewAbove.setOnClosedListener(listener);
	}

	public static class SavedState extends BaseSavedState {
		
		private final boolean mBehindShowing;

		public SavedState(Parcelable superState, boolean isBehindShowing) {
			super(superState);
			mBehindShowing = isBehindShowing;
		}

		/* (non-Javadoc)
		 * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
		 */
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte(mBehindShowing ? (byte)1 : 0);
		}

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        
		private SavedState(Parcel in) {
			super(in);
			mBehindShowing = in.readByte()!=0;
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState, isBehindShowing());
		return ss;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());

		if (ss.mBehindShowing) {
			showBehind();
		} else {
			showAbove();
		}
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
	 */
	@Override
	protected boolean fitSystemWindows(Rect insets) {
        int leftPadding = insets.left;
        int rightPadding = insets.right;
        int topPadding = insets.top;
        int bottomPadding = insets.bottom;
        setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        return true;
	}

}