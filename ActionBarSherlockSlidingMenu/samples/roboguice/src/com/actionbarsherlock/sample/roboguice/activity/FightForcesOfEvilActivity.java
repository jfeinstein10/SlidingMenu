package com.actionbarsherlock.sample.roboguice.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.TextView;
import com.actionbarsherlock.sample.roboguice.R;
import com.actionbarsherlock.sample.roboguice.controller.Astroboy;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.Random;

/**
 * Things you'll learn in this class:
 *     - How to inject Resources
 *     - How to use RoboAsyncTask to do background tasks with injection
 *     - What it means to be a @Singleton
 */
public class FightForcesOfEvilActivity extends RoboSherlockActivity {

    @InjectView(R.id.expletive) TextView expletiveText;

    // You can also inject resources such as Strings, Drawables, and Animations
    @InjectResource(R.anim.expletive_animation) Animation expletiveAnimation;

    // AstroboyRemoteControl is annotated as @ContextSingleton, so the instance
    // we get in FightForcesOfEvilActivity will be a different instance than
    // the one we got in AstroboyMasterConsole
    //@Inject AstroboyRemoteControl remoteControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fight_evil);

        expletiveText.setAnimation(expletiveAnimation);
        expletiveAnimation.start();

        // Throw some punches
        for( int i=0; i<10; ++i )
            new AsyncPunch(this) {
                @Override
                protected void onSuccess(String expletive) throws Exception {
                    expletiveText.setText(expletive);
                }

                // We could also override onException() and onFinally() if we wanted

            }.execute();

    }



    // This class will call Astroboy.punch() in the background
    public static class AsyncPunch extends RoboAsyncTask<String> {

        // Because Astroboy is a @Singleton, this will be the same
        // instance that we inject elsewhere in our app.
        // Random of course will be a new instance of java.util.Random, since
        // we haven't specified any special binding instructions anywhere
        @Inject Astroboy astroboy;
        @Inject Random random;

        public AsyncPunch(Context context) {
            super(context);
        }

        public String call() throws Exception {
            Thread.sleep(random.nextInt(5*1000));
            return astroboy.punch();
        }
    }
}
