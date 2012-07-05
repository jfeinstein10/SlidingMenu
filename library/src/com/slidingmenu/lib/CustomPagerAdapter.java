package com.slidingmenu.lib;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CustomPagerAdapter {
	private DataSetObservable mObservable = new DataSetObservable();

	public static final int POSITION_UNCHANGED = -1;
	public static final int POSITION_NONE = -2;

	private View mBehind;
	private View mContent;

	public void setBehind(View v) {
		mBehind = v;
	}

	public void setContent(View v) {
		mContent = v;
	}

	/**
	 * Return the number of views available.
	 */
	public int getCount() {
		int count = 0;
		count += (mBehind != null)? 1 : 0;
		count += (mContent != null)? 1 : 0;
		return count;
	}

	/**
	 * Called when a change in the shown pages is going to start being made.
	 * @param container The containing View which is displaying this adapter's
	 * page views.
	 */
	public void startUpdate(ViewGroup container) {
		startUpdate((View) container);
	}

	/**
	 * Create the page for the given position.  The adapter is responsible
	 * for adding the view to the container given here, although it only
	 * must ensure this is done by the time it returns from
	 * {@link #finishUpdate(ViewGroup)}.
	 *
	 * @param container The containing View in which the page will be shown.
	 * @param position The page position to be instantiated.
	 * @return Returns an Object representing the new page.  This does not
	 * need to be a View, but can be some other container of the page.
	 */
	public Object instantiateItem(ViewGroup container, int position) {
		View view = null;
		switch (position) {
		case 0:
			view = mBehind;
			break;
		case 1:
			view = mContent;
			break;
		}
		if (container instanceof CustomViewAbove) {
			CustomViewAbove pager = (CustomViewAbove) container;
			pager.addView(view, position);
		} else if (container instanceof CustomViewBehind) {
			CustomViewBehind pager = (CustomViewBehind) container;
			pager.addView(view, position);
		}
		return view;    
	}

	/**
	 * Remove a page for the given position.  The adapter is responsible
	 * for removing the view from its container, although it only must ensure
	 * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
	 *
	 * @param container The containing View from which the page will be removed.
	 * @param position The page position to be removed.
	 * @param object The same object that was returned by
	 * {@link #instantiateItem(View, int)}.
	 */
	public void destroyItem(ViewGroup container, int position, Object object) {
		((CustomViewAbove) container).removeView((View)object);
	}

	/**
	 * Called to inform the adapter of which item is currently considered to
	 * be the "primary", that is the one show to the user as the current page.
	 *
	 * @param containviewer The containing View from which the page will be removed.
	 * @param position The page position that is now the primary.
	 * @param object The same object that was returned by
	 * {@link #instantiateItem(View, int)}.
	 */
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		setPrimaryItem((View) container, position, object);
	}

	/**
	 * Called when the a change in the shown pages has been completed.  At this
	 * point you must ensure that all of the pages have actually been added or
	 * removed from the container as appropriate.
	 * @param container The containing View which is displaying this adapter's
	 * page views.
	 */
	public void finishUpdate(ViewGroup container) {
		finishUpdate((View) container);
	}

	/**
	 * Called when a change in the shown pages is going to start being made.
	 * @param container The containing View which is displaying this adapter's
	 * page views.
	 *
	 * @deprecated Use {@link #startUpdate(ViewGroup)}
	 */
	public void startUpdate(View container) {
	}

	/**
	 * Create the page for the given position.  The adapter is responsible
	 * for adding the view to the container given here, although it only
	 * must ensure this is done by the time it returns from
	 * {@link #finishUpdate(ViewGroup)}.
	 *
	 * @param container The containing View in which the page will be shown.
	 * @param position The page position to be instantiated.
	 * @return Returns an Object representing the new page.  This does not
	 * need to be a View, but can be some other container of the page.
	 *
	 * @deprecated Use {@link #instantiateItem(ViewGroup, int)}
	 */
	public Object instantiateItem(View container, int position) {
		throw new UnsupportedOperationException(
				"Required method instantiateItem was not overridden");
	}

	/**
	 * Remove a page for the given position.  The adapter is responsible
	 * for removing the view from its container, although it only must ensure
	 * this is done by the time it returns from {@link #finishUpdate(View)}.
	 *
	 * @param container The containing View from which the page will be removed.
	 * @param position The page position to be removed.
	 * @param object The same object that was returned by
	 * {@link #instantiateItem(View, int)}.
	 *
	 * @deprecated Use {@link #destroyItem(ViewGroup, int, Object)}
	 */
	public void destroyItem(View container, int position, Object object) {
		throw new UnsupportedOperationException("Required method destroyItem was not overridden");
	}

	/**
	 * Called to inform the adapter of which item is currently considered to
	 * be the "primary", that is the one show to the user as the current page.
	 *
	 * @param container The containing View from which the page will be removed.
	 * @param position The page position that is now the primary.
	 * @param object The same object that was returned by
	 * {@link #instantiateItem(View, int)}.
	 *
	 * @deprecated Use {@link #setPrimaryItem(ViewGroup, int, Object)}
	 */
	public void setPrimaryItem(View container, int position, Object object) {
	}

	/**
	 * Called when the a change in the shown pages has been completed.  At this
	 * point you must ensure that all of the pages have actually been added or
	 * removed from the container as appropriate.
	 * @param container The containing View which is displaying this adapter's
	 * page views.
	 *
	 * @deprecated Use {@link #setPrimaryItem(ViewGroup, int, Object)}
	 */
	public void finishUpdate(View container) {
	}

	/**
	 * Determines whether a page View is associated with a specific key object
	 * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
	 * required for a PagerAdapter to function properly.
	 *
	 * @param view Page View to check for association with <code>object</code>
	 * @param object Object to check for association with <code>view</code>
	 * @return true if <code>view</code> is associated with the key object <code>object</code>
	 */
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/**
	 * Save any instance state associated with this adapter and its pages that should be
	 * restored if the current UI state needs to be reconstructed.
	 *
	 * @return Saved state for this adapter
	 */
	public Parcelable saveState() {
		return null;
	}

	/**
	 * Restore any instance state associated with this adapter and its pages
	 * that was previously saved by {@link #saveState()}.
	 *
	 * @param state State previously saved by a call to {@link #saveState()}
	 * @param loader A ClassLoader that should be used to instantiate any restored objects
	 */
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	/**
	 * Called when the host view is attempting to determine if an item's position
	 * has changed. Returns {@link #POSITION_UNCHANGED} if the position of the given
	 * item has not changed or {@link #POSITION_NONE} if the item is no longer present
	 * in the adapter.
	 *
	 * <p>The default implementation assumes that items will never
	 * change position and always returns {@link #POSITION_UNCHANGED}.
	 *
	 * @param object Object representing an item, previously returned by a call to
	 *               {@link #instantiateItem(View, int)}.
	 * @return object's new position index from [0, {@link #getCount()}),
	 *         {@link #POSITION_UNCHANGED} if the object's position has not changed,
	 *         or {@link #POSITION_NONE} if the item is no longer present.
	 */
	public int getItemPosition(Object object) {
		return POSITION_UNCHANGED;
	}

	/**
	 * This method should be called by the application if the data backing this adapter has changed
	 * and associated views should update.
	 */
	public void notifyDataSetChanged() {
		mObservable.notifyChanged();
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mObservable.registerObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mObservable.unregisterObserver(observer);
	}

	/**
	 * This method may be called by the ViewPager to obtain a title string
	 * to describe the specified page. This method may return null
	 * indicating no title for this page. The default implementation returns
	 * null.
	 *
	 * @param position The position of the title requested
	 * @return A title for the requested page
	 */
	public CharSequence getPageTitle(int position) {
		return null;
	}
}
