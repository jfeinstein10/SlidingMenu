package com.slidingmenu.lib.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.SlidingMenu;

@SuppressLint("NewApi")
public class WindowHelper extends Window {
	
	private Window mWindow;
	public RelativeLayout mDecor;

	public WindowHelper(Context context, Window window, SlidingMenu slidingMenu) {
		super(context);
		// unregister the content view
		mWindow = window;
		mWindow.getDecorView().findViewById(android.R.id.content).setId(View.NO_ID);
		// register a new content view
		mDecor = new RelativeLayout(context);
		mDecor.setId(android.R.id.content);
		// set up the SlidingMenu
		slidingMenu.setViewAbove(mDecor);
		mWindow.setContentView(slidingMenu);
	}

	@Override
	public void addContentView(View arg0, LayoutParams arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAllPanels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePanel(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getCurrentFocus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getDecorView() {
		return mWindow.getDecorView();
	}

	@Override
	public LayoutInflater getLayoutInflater() {
		return mWindow.getLayoutInflater();
	}

	@Override
	public int getVolumeControlStream() {
		return mWindow.getVolumeControlStream();
	}

	@Override
	public void invalidatePanelMenu(int featureId) {
//		mWindow.invalidatePanelMenu(featureId);
	}

	@Override
	public boolean isFloating() {
		return mWindow.isFloating();
	}

	@Override
	public boolean isShortcutKey(int keyCode, KeyEvent event) {
		return mWindow.isShortcutKey(keyCode, event);
	}

	@Override
	protected void onActive() {
//		mWindow.onActive();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mWindow.onConfigurationChanged(newConfig);
	}

	@Override
	public void openPanel(int featureId, KeyEvent event) {
		mWindow.openPanel(featureId, event);
	}

	@Override
	public View peekDecorView() {
		return getDecorView();
	}

	@Override
	public boolean performContextMenuIdentifierAction(int id, int flags) {
		return mWindow.performContextMenuIdentifierAction(id, flags);
	}

	@Override
	public boolean performPanelIdentifierAction(int featureId, int id, int flags) {
		return mWindow.performPanelIdentifierAction(featureId, id, flags);
	}

	@Override
	public boolean performPanelShortcut(int featureId, int keyCode, KeyEvent event,
			int flags) {
		return mWindow.performPanelShortcut(featureId, keyCode, event, flags);
	}

	@Override
	public void restoreHierarchyState(Bundle savedInstanceState) {
		mWindow.restoreHierarchyState(savedInstanceState);
	}

	@Override
	public Bundle saveHierarchyState() {
		return mWindow.saveHierarchyState();
	}

	@Override
	public void setBackgroundDrawable(Drawable drawable) {
		mWindow.setBackgroundDrawable(drawable);
	}

	@Override
	public void setChildDrawable(int featureId, Drawable drawable) {
		mWindow.setChildDrawable(featureId, drawable);
	}

	@Override
	public void setChildInt(int featureId, int value) {
		mWindow.setChildInt(featureId, value);
	}

	@Override
	public void setContentView(int layoutResID) {
		setContentView(getLayoutInflater().inflate(layoutResID, null));
	}

	@Override
	public void setContentView(View view) {
		mDecor.addView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		mDecor.addView(view, params);
	}

	@Override
	public void setFeatureDrawable(int featureId, Drawable drawable) {
		mWindow.setFeatureDrawable(featureId, drawable);
	}

	@Override
	public void setFeatureDrawableAlpha(int featureId, int alpha) {
		mWindow.setFeatureDrawableAlpha(featureId, alpha);
	}

	@Override
	public void setFeatureDrawableResource(int featureId, int resId) {
		mWindow.setFeatureDrawableResource(featureId, resId);
	}

	@Override
	public void setFeatureDrawableUri(int featureId, Uri uri) {
		mWindow.setFeatureDrawableUri(featureId, uri);
	}

	@Override
	public void setFeatureInt(int featureId, int value) {
		mWindow.setFeatureInt(featureId, value);
	}

	@Override
	public void setTitle(CharSequence title) {
		mWindow.setTitle(title);
	}

	@Override
	public void setTitleColor(int textColor) {
		mWindow.setTitleColor(textColor);
	}

	@Override
	public void setVolumeControlStream(int streamType) {
		mWindow.setVolumeControlStream(streamType);
	}

	@Override
	public boolean superDispatchGenericMotionEvent(MotionEvent event) {
//		return mWindow.superDispatchGenericMotionEvent(event);
		return false;
	}

	@Override
	public boolean superDispatchKeyEvent(KeyEvent event) {
		return mWindow.superDispatchKeyEvent(event);
	}

	@Override
	public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
//		return mWindow.superDispatchKeyShortcutEvent(event);
		return false;
	}

	@Override
	public boolean superDispatchTouchEvent(MotionEvent event) {
		return mWindow.superDispatchTouchEvent(event);
	}

	@Override
	public boolean superDispatchTrackballEvent(MotionEvent event) {
		return mWindow.superDispatchTrackballEvent(event);
	}

	@Override
	public void takeInputQueue(android.view.InputQueue.Callback callback) {
		mWindow.takeInputQueue(callback);
	}

	@Override
	public void takeKeyEvents(boolean get) {
		mWindow.takeKeyEvents(get);
	}

	@Override
	public void takeSurface(Callback2 callback) {
		mWindow.takeSurface(callback);
	}

	@Override
	public void togglePanel(int featureId, KeyEvent event) {
		mWindow.togglePanel(featureId, event);
	}
}
