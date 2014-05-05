package com.jeremyfeinstein.slidingmenu.lib;
import android.view.animation.Interpolator;
 
/**
* Created with IntelliJ IDEA, best IDE in the world. User: castorflex Date: 06/06/13 Time: 22:18
*/
public class CustomBounceInterpolator implements Interpolator {
 
@Override
public float getInterpolation(float t) {
return -(float) Math.abs(Math.sin((float) Math.PI * (t + 1) * (t + 1)) * (1 - t));
}
}