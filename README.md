SlidingMenu
===========

A sample Android project to explore creating slide-in menus like in the Spotify and Facebook applications. 
You can use it all you want in your Android apps provided that you cite this project and include the 
license in your app. Also, remember to include the [ActionBarSherlock][2] license if you choose to use the
provided ActionBar.

Here's a recent video of the example application in this repository : http://youtu.be/8vNaANLHw-c

Also, you can follow the project on Twitter : [@SlidingMenu][1]

How to Integrate this Library into Your Projects
------------------------------------------------
In order to integrate SlidingMenu into your own projects you can do one of two things.

__1.__      You can embed the SlidingMenu at the Activity level by making your Activity extend `SlidingMenuActivity`.
* If you choose to do it this way, you have access to a built-in ActionBar via Jake Wharton's [ActionBarSherlock][2].
The ActionBar will slide with the "above" portion of the SlidingMenu.
* In your Activity's onCreate method, you will have to call `setContentView`, as usual, and also 
`setBehindContentView`, which has the same syntax as setContentView. `setBehindContentView` will place 
the view in the "behind" portion of the SlidingMenu. You also have access to methods such as `toggle()`,
`showAbove()` `showBehind()` at the Activity level. 
* More variants of `SlidingMenuActivity` will be coming soon, such as `SlidingFragmentActivity`, etc.

__2.__      You can use the SlidingMenu view directly in your xml layouts or programmatically in you Java code.
* This way, you can treat SlidingMenu as you would any other view type and put it in crazy awesome places like in the
rows of a ListView.
* So. Many. Possibilities.

Usage
-----
If you decide to use SlidingMenu as a view, you can define it in your xml layouts like this:
```xml
<com.slidingmenu.lib.SlidingMenu
    android:id="@+id/slidingmenulayout"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    viewAbove="@layout/YOUR_ABOVE_VIEW"
    viewBehind="@layout/YOUR_BEHIND_BEHIND"
    behindOffset="@dimen/YOUR_OFFSET"
    behindScrollScale="@dimen/YOUR_SCALE" />
```

Customizing SlidingMenu
-----------------------
To come soon!

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
