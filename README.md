SlidingMenu
===========

A sample Android project to explore creating slide-in menus like in the Spotify and Facebook applications. 
You can use it all you want in your Android apps provided that you cite this project and include the 
license in your app. Also, remember to include the [ActionBarSherlock][2] license if you choose to use the
provided ActionBar.

Here's a recent video of the example application in this repository : http://youtu.be/8vNaANLHw-c

Also, you can follow the project on Twitter : [@SlidingMenu][1]

Setup
-----
* In Eclipse, just import the library as an Android library project. Project > Clean to generate the binaries 
you need, like R.java, etc.
* Then, just add SlidingMenu as a dependency to your existing project and you're good to go!

How to Integrate this Library into Your Projects
------------------------------------------------
In order to integrate SlidingMenu into your own projects you can do one of two things.

__1.__      You can embed the SlidingMenu at the Activity level by making your Activity extend `SlidingActivity`.
* In your Activity's onCreate method, you will have to call `setContentView`, as usual, and also 
`setBehindContentView`, which has the same syntax as setContentView. `setBehindContentView` will place 
the view in the "behind" portion of the SlidingMenu. You will have access to the `getSlidingMenu` method so you can
customize the SlidingMenu to your liking.
* If you want to use another library such as ActionBarSherlock, you can just change the SlidingActivities to extend
the SherlockActivities instead of the regular Activities.

__2.__      You can use the SlidingMenu view directly in your xml layouts or programmatically in you Java code.
* This way, you can treat SlidingMenu as you would any other view type and put it in crazy awesome places like in the
rows of a ListView.
* So. Many. Possibilities.

Usage
-----
If you decide to use SlidingMenu as a view, you can define it in your xml layouts like this:
```xml
<com.slidingmenu.lib.SlidingMenu
    xmlns:sliding="http://schemas.android.com/apk/res-auto"
    android:id="@+id/slidingmenulayout"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    sliding:viewAbove="@layout/YOUR_ABOVE_VIEW"
    sliding:viewBehind="@layout/YOUR_BEHIND_BEHIND"
    sliding:touchModeAbove="margin|fullscreen"
    sliding:touchModeBehind="margin|fullscreen"
    sliding:behindOffset="@dimen/YOUR_OFFSET"
    sliding:behindScrollScale="@dimen/YOUR_SCALE"
    sliding:shadowDrawable="@drawable/YOUR_SHADOW"
    sliding:shadowWidth="@dimen/YOUR_SHADOW_WIDTH" />
```
* `viewAbove` - a reference to the layout that you want to use as the above view of the SlidingMenu
* `viewBehind` - a reference to the layout that you want to use as the behind view of the SlidingMenu
* `touchModeAbove` - an enum that designates what part of the screen is touchable when the above view is 
showing. Margin means only the left margin. Fullscreen means the entire screen. Default is margin.
* `touchModeBehind` - an enum that designates what part of the screen is touchable when the behind view
is showing. Margin means only what is showing of the above view. Fullscreen means the entire screen.
Default is margin.
* `behindOffset` - a dimension representing the number of pixels that you want the above view to show when the
behind view is showing. Default is 0.
* `behindScrollScale` - a float representing the relationship between the above view scrolling and the behind
behind view scrolling. If set to 0.5f, the behind view will scroll 1px for every 2px that the above view scrolls.
If set to 1.0f, the behind view will scroll 1px for every 1px that the above view scrolls. And if set to 0.0f, the
behind view will never scroll; it will be static. This one is fun to play around with. Default is 0.25f.
* `shadowDrawable` - a reference to a drawable to be used as a drop shadow from the above view onto the below view.
Default is no shadow for now.
* `shadowWidth` - a dimension representing the width of the shadow drawable. Default is 0.

Caveats
-------
* Your layouts have to be based on a viewgroup, unfortunatly this negates the `<merge>` optimisations.
            

Developed By
------------
* Jeremy Feinstein

License
-------

    Copyright 2012 Jeremy Feinstein
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: http://twitter.com/slidingmenu
[2]: http://actionbarsherlock.com/
