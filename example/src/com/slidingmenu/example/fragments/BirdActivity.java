package com.slidingmenu.example.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.slidingmenu.example.R;

public class BirdActivity extends Activity {

	public static Intent newInstance(Activity activity, int pos) {
		Intent intent = new Intent(activity, BirdActivity.class);
		intent.putExtra("pos", pos);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int pos = 0;
		if (getIntent().getExtras() != null) {
			pos = getIntent().getExtras().getInt("pos");
		}
		
		String[] birds = getResources().getStringArray(R.array.birds);
		TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
		int resId = imgs.getResourceId(pos, -1);
		
		setTitle(birds[pos]);		
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setImageResource(resId);
		setContentView(imageView);
		this.getWindow().setBackgroundDrawableResource(android.R.color.black);
	}
}
