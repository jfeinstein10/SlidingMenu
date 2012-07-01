package com.actionbarsherlock.sample.roboguice.controller;

import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;

import android.app.Activity;
import android.widget.Toast;

import com.google.inject.Inject;

/**
 * A class to control Astroboy remotely.
 *
 * This class uses the current context, so we must make it @ContextSingleton.
 * This means that there will be one AstroboyRemoteControl for every activity or
 * service that requires one.
 * Note that we actually ask for the Activity, rather than the Context (which is
 * the same thing), because we need access to some activity-related methods and this
 * saves us from having to downcast to an Activity manually.
 *
 * It also asks RoboGuice to inject the Astroboy instance so we can control him.
 *
 * What you'll learn in this class
 *   - What @ContextScope means and when to use it
 *   - How to inject an Activity instead of a Context (which is really the same thing)
 *   - How to use RoboGuice's convenient and flexible logging facility, Ln.
 */
@ContextSingleton
public class AstroboyRemoteControl {


    // The Astroboy class has been decorated with @Singleton, so this instance of
    // Astroboy will be the same instance used elsewhere in our app.
    // Injecting an Activity is basically equivalent to "@Inject Context context",
    // and thus also requires @ContextScope. If you wanted, you could also
    // @Inject Application, Service, etc. wherever appropriate.
    @Inject Astroboy astroboy;
    @Inject Activity activity;



    public void brushTeeth() {
        // More info about logging available here: http://code.google.com/p/roboguice/wiki/Logging
        Ln.d("Sent brushTeeth command to Astroboy");
        astroboy.brushTeeth();
    }

    public void say( String something ) {
        Ln.d("Sent say(%s) command to Astroboy",something);
        astroboy.say(something);
    }

    public void selfDestruct() {
        Toast.makeText(activity, "Your evil remote control has exploded! Now Astroboy is FREEEEEEEEEE!", Toast.LENGTH_LONG).show();
        activity.finish();
    }
}
