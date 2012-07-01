/*
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.actionbarsherlock.sample.demos;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

public class StaticAttachment extends Activity implements OnCreateOptionsMenuListener {
    ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        /*
         * Most interactions with what would otherwise be the system UI should
         * now be done through this instance. Content, title, action bar, and
         * menu inflation can all be done.
         *
         * All of the base activities use this class to provide the normal
         * action bar functionality so everything that they can do is possible
         * using this static attachment method.
         *
         * Calling something like setContentView or getActionBar on this
         * instance is required in order to properly set up the wrapped layout
         * and dispatch menu events (if they are needed).
         */
        mSherlock.setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
        mSherlock.setContentView(R.layout.text);

        ((TextView)findViewById(R.id.text)).setText(R.string.static_attach_content);
    }

    /*
     * In order to use action items properly with static attachment you
     * need to dispatch create, prepare, and selected events for the
     * native type to the ActionBarSherlock instance. If for some reason
     * you need to use static attachment you should probably create a
     * common base activity that does this for all three methods.
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return mSherlock.dispatchCreateOptionsMenu(menu);
    }

    /*
     * In order to receive these events you need to implement an interface
     * from ActionBarSherlock so it knows to dispatch to this callback.
     * There are three possible interface you can implement, one for each
     * menu event.
     *
     * Remember, there are no superclass implementations of these methods so
     * you must return a value with meaning.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

        menu.add("Save")
            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Search")
            .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }
}
