package com.slidingmenu.lib.menu;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slidingmenu.lib.R;

public class MenuAdapter extends PinnedHeaderListAdapter {
	
	private ArrayList<String> mTitles;

	public MenuAdapter(Context context) {
		super(context);
		mTitles = new ArrayList<String>();
		setPinnedPartitionHeadersEnabled(true);
	}
	
	public void addSection (String title, String[] labels, int[] icons) {
		mTitles.add(title);
		addPartition(false, title == null);
		changeCursor(mTitles.size()-1, makeCursor(labels, icons));
	}
	
	private Cursor makeCursor(String[] labels, int[] icons) {
		MatrixCursor cursor = new MatrixCursor(new String[]{"label", "icon"});
		for (int i = 0; i < labels.length; i++) {
			cursor.addRow(new Object[]{labels[i], icons[i]});
		}
		return cursor;
	}
	
	@Override
	public int getPinnedHeaderCount() {
		return mTitles.size();
	}

	@Override
	protected View newHeaderView(Context context, int partition, Cursor cursor,
			ViewGroup parent) {
		View header = LayoutInflater.from(context).inflate(R.layout.menu_header, null);
		header.setFocusable(false);
		header.setEnabled(false);
		return header;
	}
	
	@Override
	protected void bindHeaderView(View v, int partition, Cursor cursor) {
		TextView label = (TextView) v.findViewById(R.id.label);
		label.setText(mTitles.get(partition));
	}

	@Override
	protected View newView(Context context, int partition, Cursor cursor,
			int position, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.menu_row, null);
	}

	@Override
	protected void bindView(View v, int partition, Cursor cursor, int position) {
		TextView label = (TextView) v.findViewById(R.id.label);
		label.setText(cursor.getString(0));
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		icon.setImageResource(cursor.getInt(1));
	}

}
