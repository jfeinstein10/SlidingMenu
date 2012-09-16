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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.CustomViewAbove.OnPageChangeListener;

public class SlidingMenu extends RelativeLayout {

	public static final String TAG = "SlidingMenu";
	
	public static final int TOUCHMODE_MARGIN = 0;
	public static final int TOUCHMODE_FULLSCREEN = 1;

	public static final int SIDES_LEFT = 0;
	public static final int SIDES_RIGHT = 1;
	public static final int SIDES_LEFT_RIGHT = 2;

	private CustomViewAbove mViewAbove;
	private CustomViewBehind mViewBehindLeft;
	private CustomViewBehind mViewBehindRight;
	private OnOpenListener mOpenListener;
	private OnCloseListener mCloseListener;

	private boolean mSlidingEnabled;

	public static void attachSlidingMenu(Activity activity, SlidingMenu sm, boolean slidingTitle) {

		if (sm.getParent() != null)
			throw new IllegalStateException("SlidingMenu cannot be attached to another view when" +
					" calling the static method attachSlidingMenu");

		if (slidingTitle) {
			// get the window background
			TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
			int background = a.getResourceId(0, 0);
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
			content.addView(sm, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}
	}

	public interface OnOpenListener {
		public void onOpen();
	}

	public interface OnOpenedListener {
		public void onOpened();
	}

	public interface OnCloseListener {
		public void onClose();
	}

	public interface OnClosedListener {
		public void onClosed();
	}

	public interface CanvasTransformer {
		public void transformCanvas(Canvas canvas, float percentOpen);
	}

	public SlidingMenu(Context context) {
		this(context, null);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// above view
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewAbove = new CustomViewAbove(context);
		addView(mViewAbove, aboveParams);
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
		int slidingSides = ta.getInt(R.styleable.SlidingMenu_slidingSides, SIDES_LEFT);
		switch (slidingSides) {
		case SIDES_LEFT:
			initializeLeft();
			break;
		case SIDES_RIGHT:
			initializeRight();
			break;
		case SIDES_LEFT_RIGHT:
			initializeLeft();
			initializeRight();
			break;
		}
		// set the above and behind views if defined in xml
		int viewAbove = ta.getResourceId(R.styleable.SlidingMenu_viewAbove, -1);
		if (viewAbove != -1) {
			setContent(viewAbove);
		}
		int viewBehindLeft = ta.getResourceId(R.styleable.SlidingMenu_viewBehindLeft, -1);
		if (viewBehindLeft != -1) {
			setViewBehindLeft(viewBehindLeft);
		}
		int viewBehindRight = ta.getResourceId(R.styleable.SlidingMenu_viewBehindRight, -1);
		if (viewBehindRight != -1) {
			setViewBehindRight(viewBehindRight);
		}
		int touchModeAbove = ta.getInt(R.styleable.SlidingMenu_aboveTouchMode, TOUCHMODE_MARGIN);
		setTouchModeAbove(touchModeAbove);
		int touchModeBehind = ta.getInt(R.styleable.SlidingMenu_behindTouchMode, TOUCHMODE_MARGIN);
		setTouchModeBehind(touchModeBehind);

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
	}

	private void initializeLeft() {
		if (mViewBehindLeft == null) {
			LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mViewBehindLeft = new CustomViewBehind(getContext(), CustomViewBehind.LEFT);
			addView(mViewBehindLeft, 0, behindParams);
			mViewAbove.setViewBehindLeft(mViewBehindLeft);
			mViewBehindLeft.setCustomViewAbove(mViewAbove);
		}
	}

	private void initializeRight() {
		if (mViewBehindRight == null) {
			LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mViewBehindRight = new CustomViewBehind(getContext(), CustomViewBehind.RIGHT);
			addView(mViewBehindRight, 0, behindParams);
			mViewAbove.setViewBehindRight(mViewBehindRight);
			mViewBehindRight.setCustomViewAbove(mViewAbove);
		}
	}

	public void setContent(int res) {
		setContent(LayoutInflater.from(getContext()).inflate(res, null));
	}

	public void setContent(View v) {
		mViewAbove.setContent(v);
		mViewAbove.invalidate();
		showAbove();
	}

	public void setViewBehindRight(int res) {
		setViewBehindRight(LayoutInflater.from(getContext()).inflate(res, null));
	}

	public void setViewBehindRight(View v) {
		initializeRight();
		mViewBehindRight.setContent(v);
		mViewBehindRight.invalidate();
	}

	public void setViewBehindLeft(int res) {
		setViewBehindLeft(LayoutInflater.from(getContext()).inflate(res, null));
	}

	public void setViewBehindLeft(View v) {
		initializeLeft();
		mViewBehindLeft.setContent(v);
		mViewBehindLeft.invalidate();
	}

	public void setSlidingEnabled(boolean b) {
		mViewAbove.setSlidingEnabled(b);
	}

	public boolean isSlidingEnabled() {
		return mViewAbove.isSlidingEnabled();
	}

	/**
	 * 
	 * @param b Whether or not the SlidingMenu is in a static mode 
	 * (i.e. nothing is moving and everything is showing)
	 */
	public void setStatic(boolean b) {
		if (b) {
			setSlidingEnabled(false);
			mViewAbove.setViewBehindLeft(null);
			mViewAbove.setCurrentItem(1);
			mViewBehindLeft.setCurrentItem(0);	
		} else {
			mViewAbove.setCurrentItem(1);
			mViewBehindLeft.setCurrentItem(1);
			mViewAbove.setViewBehindLeft(mViewBehindLeft);
			setSlidingEnabled(true);
		}
	}

	/**
	 * Shows the behind view
	 */
	public void showBehind() {
		mViewAbove.setCurrentItem(0);
	}

	/**
	 * Shows the above view
	 */
	public void showAbove() {
		mViewAbove.setCurrentItem(1);
	}

	/**
	 * 
	 * @return Whether or not the behind view is showing
	 */
	public boolean isBehindShowing() {
		return mViewAbove.getCurrentItem() == 0 || mViewAbove.getCurrentItem() == 2;
	}

	/**
	 * 
	 * @return The margin on the right of the screen that the behind view scrolls to
	 */
	public int getBehindOffset() {
		return ((RelativeLayout.LayoutParams)mViewBehindLeft.getLayoutParams()).rightMargin;
	}

	/**
	 * 
	 * @param i The margin on the right of the screen that the behind view scrolls to
	 */
	public void setBehindOffset(int i) {
		// behind left
		if (mViewBehindLeft != null) {
			RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)mViewBehindLeft.getLayoutParams());
			int bottom = params.bottomMargin;
			int top = params.topMargin;
			int left = params.leftMargin;
			params.setMargins(left, top, i, bottom);
		}
		// behind right
		if (mViewBehindRight != null) {
			RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)mViewBehindRight.getLayoutParams());
			int bottom = params.bottomMargin;
			int top = params.topMargin;
			int right = params.rightMargin;
			Log.v(TAG, "left margin" + i);
			params.setMargins(i, top, right, bottom);
		}
		requestLayout();
		showAbove();
	}

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
	 * 
	 * @param res The dimension resource to be set as the behind offset
	 */
	public void setBehindOffsetRes(int res) {
		int i = (int) getContext().getResources().getDimension(res);
		setBehindOffset(i);
	}

	/**
	 * 
	 * @return The scale of the parallax scroll
	 */
	public float getBehindScrollScale() {
		return mViewAbove.getScrollScale();
	}

	/**
	 * 
	 * @param f The scale of the parallax scroll (i.e. 1.0f scrolls 1 pixel for every
	 * 1 pixel that the above view scrolls and 0.0f scrolls 0 pixels)
	 */
	public void setBehindScrollScale(float f) {
		mViewAbove.setScrollScale(f);
	}

	public void setBehindCanvasTransformer(CanvasTransformer t) {
		mViewBehindLeft.setCanvasTransformer(t);
	}

	public int getTouchModeAbove() {
		return mViewAbove.getTouchMode();
	}

	public void setTouchModeAbove(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
		}
		mViewAbove.setTouchMode(i);
	}

	public int getTouchModeBehind() {
		return mViewAbove.getTouchModeBehind();
		//		return mViewBehindLeft.getTouchMode();
	}

	public void setTouchModeBehind(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN.");
		}
		mViewAbove.setTouchModeBehind(i);
		//		mViewBehindLeft.setTouchMode(i);
	}

	public void setShadowDrawable(int resId) {
		mViewAbove.setShadowDrawable(resId);
	}

	public void setShadowWidthRes(int resId) {
		setShadowWidth((int)getResources().getDimension(resId));
	}

	public void setShadowWidth(int pixels) {
		mViewAbove.setShadowWidth(pixels);
	}

	public void setFadeEnabled(boolean b) {
		mViewAbove.setBehindFadeEnabled(b);
	}

	public void setFadeDegree(float f) {
		mViewAbove.setBehindFadeDegree(f);
	}

	public void setSelectorEnabled(boolean b) {
		mViewAbove.setSelectorEnabled(true);
	}

	public void setSelectedView(View v) {
		mViewAbove.setSelectedView(v);
	}

	public void setSelectorDrawable(int res) {
		mViewAbove.setSelectorDrawable(BitmapFactory.decodeResource(getResources(), res));
	}

	public void setSelectorDrawable(Bitmap b) {
		mViewAbove.setSelectorDrawable(b);
	}

	public void setOnOpenListener(OnOpenListener listener) {
		mViewAbove.setOnOpenListener(listener);
		mOpenListener = listener;
	}

	public void setOnCloseListener(OnCloseListener listener) {
		mViewAbove.setOnCloseListener(listener);
		mCloseListener = listener;
	}

	public void setOnOpenedListener(OnOpenedListener listener) {
		mViewAbove.setOnOpenedListener(listener);
	}

	public void setOnClosedListener(OnClosedListener listener) {
		mViewAbove.setOnClosedListener(listener);
	}

	private static class SavedState extends BaseSavedState {
		boolean mBehindShowing;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[]{mBehindShowing});
		}

		public static final Parcelable.Creator<SavedState> CREATOR
		= ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {

			public SavedState createFromParcel(Parcel in, ClassLoader loader) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		});

		SavedState(Parcel in) {
			super(in);
			boolean[] showing = new boolean[1];
			in.readBooleanArray(showing);
			mBehindShowing = showing[0];
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.mBehindShowing = isBehindShowing();
		return ss;
	}

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

	@Override
	protected boolean fitSystemWindows(Rect insets) {

		int leftPadding = getPaddingLeft() + insets.left;
		int rightPadding = getPaddingRight() + insets.right;
		int topPadding = getPaddingTop() + insets.top;
		int bottomPadding = getPaddingBottom() + insets.bottom;
		this.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

		return super.fitSystemWindows(insets);
	}

}