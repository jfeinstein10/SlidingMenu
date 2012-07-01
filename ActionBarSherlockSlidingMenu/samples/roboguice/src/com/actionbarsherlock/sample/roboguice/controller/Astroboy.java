package com.actionbarsherlock.sample.roboguice.controller;

import android.app.Application;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Random;


/**
 * What you'll learn in this class:
 *   - What it means to be a @Singleton
 *   - That Singletons must use Provider<Context> instead of Context to get
 *     the current context
 *   - Some basics about injection, including when injection results in a call to
 *     an object's default constructor, versus when it does something "special" like
 *     call getSystemService()
 */

// There's only one Astroboy, so make it a @Singleton.
// This means that there will be only one instance of Astroboy in the entire app.
// Any class that requires an instance of Astroboy will get the same instance.
// This also means this class needs to be thread safe, of course
@Singleton
public class Astroboy {

    // Because Astroboy is a Singleton, we can't directly inject the current Context
    // since the current context may change depending on what activity is using Astroboy
    // at the time.  Instead we use the application context.
    // Vibrator is bound to context.getSystemService(VIBRATOR_SERVICE) in DefaultRoboModule.
    // Random has no special bindings, so Guice will create a new instance for us.
    @Inject Application application;
    @Inject Vibrator vibrator;
    @Inject Random random;

    public void say(String something) {
        // Make a Toast, using the current context as returned by the Context Provider
        Toast.makeText(application, "Astroboy says, \"" + something + "\"", Toast.LENGTH_LONG).show();
    }

    public void brushTeeth() {
        vibrator.vibrate(new long[]{0, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50,  }, -1);
    }

    public String punch() {
        final String expletives[] = new String[]{"POW!", "BANG!", "KERPOW!", "OOF!"};
        return expletives[random.nextInt(expletives.length)];
    }
}
