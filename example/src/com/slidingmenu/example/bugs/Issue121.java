package com.slidingmenu.example.bugs;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.slidingmenu.example.BaseActivity;
import com.slidingmenu.example.R;

public class Issue121 extends BaseActivity {


    public Issue121() {
        super(R.string.title_issue_121);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the Above View
        setContentView(R.layout.horizontal_slide_issue);

        LinearLayout imageContainer = (LinearLayout) findViewById(R.id.issue_item_container);

        for (int i=0; i < 25; i++) {
            imageContainer.addView(getItemView(imageContainer.getContext()));
        }

        setSlidingActionBarEnabled(true);
    }

    private ImageView getItemView(Context context) {

        ImageView imageView = new ImageView(context);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(50 , 50));
        imageView.setMaxHeight(50);
        imageView.setMaxWidth(50);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        imageView.setImageResource(R.drawable.octocat);

        return imageView;
    }

}