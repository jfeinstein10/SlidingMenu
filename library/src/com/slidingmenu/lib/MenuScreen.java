package com.slidingmenu.lib;

import java.util.HashMap;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.LinearLayout;

public class MenuScreen extends LinearLayout {

	private PreferenceManager mManager;
	private PreferenceScreen mPrefs;
	private HashMap<String, PreferenceCategory> mCats;

	public MenuScreen(Context context, PreferenceManager manager) {
		super(context);
		mManager = manager;
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mPrefs = mManager.createPreferenceScreen(getContext());
	}
	
	public void addCategory(String title) {
		PreferenceCategory cat = new PreferenceCategory(getContext());
		cat.setTitle(title);
		mCats.put(title, cat);
		mPrefs.addPreference(cat);
	}
	
	public void addItem(String title, String catKey) {
		PreferenceCategory cat = mCats.get(catKey);
		CheckBoxPreference cbp = new CheckBoxPreference(getContext());
		cbp.setTitle(title);
		cat.addPreference(cbp);
	}

}
