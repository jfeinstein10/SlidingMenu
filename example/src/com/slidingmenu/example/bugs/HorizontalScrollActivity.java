package com.slidingmenu.example.bugs;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.slidingmenu.example.BaseActivity;
import com.slidingmenu.example.R;
import com.slidingmenu.lib.SlidingMenu;

public class HorizontalScrollActivity extends BaseActivity implements SlidingMenu.OnTouchedEventListener {

    public HorizontalScrollActivity() {
        super(R.string.horizontal_scroll_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.horizontal_scroll_bug);

        setSlidingActionBarEnabled(true);

        getSlidingMenu().setOnTouchedEventListener(this);

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

    @Override
    public boolean onTouchEventIntercepted(MotionEvent event) {
        Rect r = new Rect();
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView)findViewById(R.id.issue_scroll_view);

        horizontalScrollView.getHitRect(r);

        return r.contains((int)event.getX(), (int)event.getY());
    }
}
