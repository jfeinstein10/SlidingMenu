package com.actionbarsherlock.sample.roboguice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.actionbarsherlock.sample.roboguice.R;
import com.actionbarsherlock.sample.roboguice.controller.AstroboyRemoteControl;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * This activity uses an AstroboyRemoteControl to control Astroboy remotely!
 *
 * What you'll learn in this class:
 *   - How to use @InjectView as a typesafe version of findViewById()
 *   - How to inject plain old java objects as well (POJOs)
 *   - When injection happens
 *   - Some basics about injection, including when injection results in a call to
 *     an object's default constructor, versus when it does something "special"
 *     like call getSystemService()
 */
@ContentView(R.layout.main)
public class AstroboyMasterConsole extends RoboSherlockActivity {

    // Various views that we inject into the activity.
    // Equivalent to calling findViewById() in your onCreate(), except more succinct
    @InjectView(R.id.self_destruct) Button selfDestructButton;
    @InjectView(R.id.say_text)      EditText sayText;
    @InjectView(R.id.brush_teeth)   Button brushTeethButton;
    @InjectView(tag="fightevil")    Button fightEvilButton;     // we can also use tags if we want


    // Standard Guice injection of Plain Old Java Objects (POJOs)
    // Guice will find or create the appropriate instance of AstroboyRemoteControl for us
    // Since we haven't specified a special binding for AstroboyRemoteControl, Guice
    // will create a new instance for us using AstroboyRemoteControl's default constructor.
    // Contrast this with Vibrator, which is an Android service that is pre-bound by RoboGuice.
    // Injecting a Vibrator will return a new instance of a Vibrator obtained by calling
    // context.getSystemService(VIBRATOR_SERVICE).  This is configured in DefaultRoboModule, which is
    // used by default to configure every RoboGuice injector.
    @Inject AstroboyRemoteControl remoteControl;
    @Inject Vibrator vibrator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // @Inject, @InjectResource, and @InjectExtra injection happens during super.onCreate()

        sayText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                // Have the remoteControl tell Astroboy to say something
                remoteControl.say(textView.getText().toString());
                textView.setText(null);
                return true;
            }
        });

        brushTeethButton.setOnClickListener( new OnClickListener() {
            public void onClick(View view) {
                remoteControl.brushTeeth();
            }
        });

        selfDestructButton.setOnClickListener( new OnClickListener() {
            public void onClick(View view) {

                // Self destruct the remoteControl
                vibrator.vibrate(2000);
                remoteControl.selfDestruct();
            }
        });

        // Fighting the forces of evil deserves its own activity
        fightEvilButton.setOnClickListener( new OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(AstroboyMasterConsole.this, FightForcesOfEvilActivity.class));
            }
        });

    }

}




