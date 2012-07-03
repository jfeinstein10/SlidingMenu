SlidingMenu
===========

A sample Android project to explore creating slide-in menus like in the Spotify and Facebook applications. 
You can use it all you want in your Android apps provided that you cite this project and include the license in your app.

Here's a very early (~3 weeks ago) demo video of it in action :  http://www.youtube.com/watch?v=dfR9kR55E8I

A better video showing the new [ActionBarSherlock][2] integration will come soon!

Also, you can follow the project on Twitter : [@SlidingMenu][1]

How to Integrate this Library into Your Projects
================================================
In order to integrate SlidingMenu into your own projects you can do one of two things.

1 - You can embed the SlidingMenu at the Activity level by making your Activity extend SlidingMenuActivity.
* If you choose to do it this way, you have access to a built-in ActionBar via Jake Wharton's [ActionBarSherlock][2].
The ActionBar will slide with the "above" portion of the SlidingMenu.
* In your Activity's onCreate method, you will have to call setContentView, as usual, and also setBehindContentView,
which has the same syntax as setContentView. setBehindContentView will place the view in the "behind" portion of
the SlidingMenu. You also have access to methods such as toggle() and showMenu() at the Activity level. 
* More variants of SlidingMenuActivity will be coming soon, such as SlidingFragmentActivity, etc.

2 - You can use the SlidingMenu view directly in your xml layouts or programmatically in you Java code.
* This way, you can treat SlidingMenu as you would any other view type and put it in crazy awesome places like in the
rows of a ListView: 
* So. Many. Possibilities.

Customizing SlidingMenu
=======================

Developed By
============
* Jeremy Feinstein

License
=======

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
