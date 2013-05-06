package com.jeremyfeinstein.slidingmenu.example;

import java.net.URLEncoder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.crittercism.app.Crittercism;
import com.jeremyfeinstein.slidingmenu.example.anim.CustomScaleAnimation;
import com.jeremyfeinstein.slidingmenu.example.anim.CustomSlideAnimation;
import com.jeremyfeinstein.slidingmenu.example.anim.CustomZoomAnimation;
import com.jeremyfeinstein.slidingmenu.example.fragments.FragmentChangeActivity;
import com.jeremyfeinstein.slidingmenu.example.fragments.ResponsiveUIActivity;

public class ExampleListActivity extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		
//		Crittercism.init(getApplicationContext(), "508ab27601ed857a20000003");
		this.addPreferencesFromResource(R.xml.main);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
		Class<?> cls = null;
		String title = pref.getTitle().toString();
		if (title.equals(getString(R.string.properties))) {
			cls = PropertiesActivity.class;	
		} else if (title.equals(getString(R.string.attach))) {
			cls = AttachExample.class;
		} else if (title.equals(getString(R.string.changing_fragments))) {
			cls = FragmentChangeActivity.class;
		} else if (title.equals(getString(R.string.left_and_right))) {
			cls = LeftAndRightActivity.class;
		} else if (title.equals(getString(R.string.responsive_ui))) {
			cls = ResponsiveUIActivity.class;
		} else if (title.equals(getString(R.string.viewpager))) {
			cls = ViewPagerActivity.class;
		} else if (title.equals(getString(R.string.title_bar_slide))) {
			cls = SlidingTitleBar.class;
		} else if (title.equals(getString(R.string.title_bar_content))) {
			cls = SlidingContent.class;
		} else if (title.equals(getString(R.string.anim_zoom))) {
			cls = CustomZoomAnimation.class;
		} else if (title.equals(getString(R.string.anim_scale))) {
			cls = CustomScaleAnimation.class;
		} else if (title.equals(getString(R.string.anim_slide))) {
			cls = CustomSlideAnimation.class;
		}
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.github:
			Util.goToGitHub(this);
			return true;
		case R.id.about:
			new AlertDialog.Builder(this)
			.setTitle(R.string.about)
			.setMessage(Html.fromHtml(getString(R.string.about_msg)))
			.show();
			break;
		case R.id.licenses:
			new AlertDialog.Builder(this)
			.setTitle(R.string.licenses)
			.setMessage(Html.fromHtml(getString(R.string.apache_license)))
			.show();
			break;
		case R.id.contact:
			final Intent email = new Intent(android.content.Intent.ACTION_SENDTO);
			String uriText = "mailto:jfeinstein10@gmail.com" +
					"?subject=" + URLEncoder.encode("SlidingMenu Demos Feedback"); 
			email.setData(Uri.parse(uriText));
			try {
				startActivity(email);
			} catch (Exception e) {
				Toast.makeText(this, R.string.no_email, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.example_list, menu);
		return true;
	}
	
}
