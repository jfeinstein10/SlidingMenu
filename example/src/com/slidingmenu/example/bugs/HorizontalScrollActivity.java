package com.slidingmenu.example.bugs;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.slidingmenu.example.BaseActivity;
import com.slidingmenu.example.R;

public class HorizontalScrollActivity extends BaseActivity {

    public HorizontalScrollActivity() {
        super(R.string.horizontal_scroll_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.horizontal_scroll_bug);

        setSlidingActionBarEnabled(true);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.issue_image_container);

        for (int i = 0; i < 50; i++){
            addImageViewToContainer(linearLayout);
        }
    }


    protected void addImageViewToContainer(LinearLayout container) {
        ImageView imageView = new ImageView(container.getContext());

        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageResource(R.drawable.octocat);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        imageView.setMaxHeight(100);

        container.addView(imageView);
    }
}
