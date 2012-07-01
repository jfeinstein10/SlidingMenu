package com.actionbarsherlock.sample.demos;

import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class FeatureToggles extends SherlockActivity implements ActionBar.TabListener {
    private static final Random RANDOM = new Random();

    private int items = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < items; i++) {
            menu.add("Text")
                .setIcon(R.drawable.ic_title_share_default)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(SampleList.THEME); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feature_toggles);
        setSupportProgressBarIndeterminateVisibility(false);
        setSupportProgressBarVisibility(false);

        getSupportActionBar().setCustomView(R.layout.custom_view);
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
        listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setListNavigationCallbacks(listAdapter, null);

        findViewById(R.id.display_progress_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSupportProgressBarVisibility(true);
                setSupportProgressBarIndeterminateVisibility(false);
                setSupportProgress(RANDOM.nextInt(8000) + 10);
            }
        });
        findViewById(R.id.display_progress_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSupportProgressBarVisibility(false);
            }
        });
        findViewById(R.id.display_iprogress_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hack to hide the regular progress bar
                setSupportProgress(Window.PROGRESS_END);
                setSupportProgressBarIndeterminateVisibility(true);
            }
        });
        findViewById(R.id.display_iprogress_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSupportProgressBarIndeterminateVisibility(false);
            }
        });

        findViewById(R.id.display_items_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items = 0;
                invalidateOptionsMenu();
            }
        });
        findViewById(R.id.display_items_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items += 1;
                invalidateOptionsMenu();
            }
        });

        findViewById(R.id.display_subtitle_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setSubtitle("The quick brown fox jumps over the lazy dog.");
            }
        });
        findViewById(R.id.display_subtitle_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setSubtitle(null);
            }
        });

        findViewById(R.id.display_title_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        });
        findViewById(R.id.display_title_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        });

        findViewById(R.id.display_custom_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowCustomEnabled(true);
            }
        });
        findViewById(R.id.display_custom_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowCustomEnabled(false);
            }
        });

        findViewById(R.id.navigation_standard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            }
        });
        findViewById(R.id.navigation_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            }
        });
        findViewById(R.id.navigation_tabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            }
        });

        findViewById(R.id.display_home_as_up_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
        findViewById(R.id.display_home_as_up_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });

        findViewById(R.id.display_logo_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayUseLogoEnabled(true);
            }
        });
        findViewById(R.id.display_logo_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayUseLogoEnabled(false);
            }
        });

        findViewById(R.id.display_home_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        });
        findViewById(R.id.display_home_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            }
        });

        findViewById(R.id.display_actionbar_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().show();
            }
        });
        findViewById(R.id.display_actionbar_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().hide();
            }
        });

        Button tabAdd = (Button)findViewById(R.id.display_tab_add);
        tabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionBar.Tab newTab = getSupportActionBar().newTab();

                if (RANDOM.nextBoolean()) {
                    newTab.setCustomView(R.layout.tab_custom_view);
                } else {
                    boolean icon = RANDOM.nextBoolean();
                    if (icon) {
                        newTab.setIcon(R.drawable.ic_title_share_default);
                    }
                    if (!icon || RANDOM.nextBoolean()) {
                        newTab.setText("Text!");
                    }
                }
                newTab.setTabListener(FeatureToggles.this);
                getSupportActionBar().addTab(newTab);
            }
        });
        //Add some tabs
        tabAdd.performClick();
        tabAdd.performClick();
        tabAdd.performClick();

        findViewById(R.id.display_tab_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportActionBar().getTabCount() > 0) {
                    getSupportActionBar().selectTab(
                            getSupportActionBar().getTabAt(
                                    RANDOM.nextInt(getSupportActionBar().getTabCount())
                            )
                    );
                }
            }
        });
        findViewById(R.id.display_tab_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportActionBar().getTabCount() > 0) {
                    getSupportActionBar().removeTabAt(getSupportActionBar().getTabCount() - 1);
                }
            }
        });
        findViewById(R.id.display_tab_remove_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().removeAllTabs();
            }
        });
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {}

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {}
}
