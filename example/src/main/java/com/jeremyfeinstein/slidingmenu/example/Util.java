package com.slidingmenu.example;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Util {

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("http://github.com/jfeinstein10/slidingmenu");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		context.startActivity(launchBrowser);
	}
	
}
