package com.slidingmenu.example;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.slidingmenu.lib.SlidingMenu;

public class PropertiesActivity extends BaseActivity {

	public PropertiesActivity() {
		super(R.string.properties);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSlidingActionBarEnabled(true);
		setContentView(R.layout.properties);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// touch mode stuff
		RadioGroup touchAbove = (RadioGroup) findViewById(R.id.touch_above);
		touchAbove.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.touch_above_full:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				case R.id.touch_above_margin:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}
		});
		touchAbove.check(R.id.touch_above_full);
		
		RadioGroup touchBehind = (RadioGroup) findViewById(R.id.touch_behind);
		touchBehind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.touch_behind_full:
					getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				case R.id.touch_behind_margin:
					getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}			
		});
		touchBehind.check(R.id.touch_behind_full);

		
		// scroll scale stuff
		SeekBar scrollScale = (SeekBar) findViewById(R.id.scroll_scale);
		scrollScale.setMax(1000);
		scrollScale.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				getSlidingMenu().setBehindScrollScale((float) seekBar.getProgress()/seekBar.getMax());
			}
		});
		
		
		// behind width stuff
		SeekBar behindWidth = (SeekBar) findViewById(R.id.behind_width);
		behindWidth.setMax(1000);
		behindWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				float percent = (float) seekBar.getProgress()/seekBar.getMax();
				getSlidingMenu().setBehindWidth((int) (percent * getSlidingMenu().getWidth()));
			}
		});
		
		
		// fading stuff
		CheckBox fadeEnabled = (CheckBox) findViewById(R.id.fade_enabled);
		fadeEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getSlidingMenu().setFadeEnabled(isChecked);
			}			
		});
		fadeEnabled.setChecked(false);
		SeekBar fadeDeg = (SeekBar) findViewById(R.id.fade_degree);
		fadeDeg.setMax(1000);
		fadeDeg.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				getSlidingMenu().setFadeDegree((float) seekBar.getProgress()/seekBar.getMax());
			}			
		});
		
	}

}
