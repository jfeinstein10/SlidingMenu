package com.actionbarsherlock.sample.demos;

import android.os.Bundle;

public class TabNavigationCollapsed extends TabNavigation {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //The following two options trigger the collapsing of the main action bar view.
        //See the parent activity for the rest of the implementation
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
