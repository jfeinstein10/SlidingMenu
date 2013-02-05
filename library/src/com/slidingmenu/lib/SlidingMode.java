package com.slidingmenu.lib;

public class SlidingMode
{

    public static final int DISABLED = 0x0000;
    /**
     * Constant value for use with setMode(). Puts the menu to the left of the content.
     */
    public static final int LEFT = 0x0001;
    /**
     * Constant value for use with setMode(). Puts the menu to the right of the content.
     */
    public static final int RIGHT = 0x0010;
    /**
     * Constant bit mask for left and right menu. Will allow a left and right menu each side of the content.
     */
    public static final int LEFT_RIGHT = LEFT | RIGHT;
    /**
     * Constant value for use with setMode(). Puts menus to Bottom mode
     */
    public static final int TOP = 0x0100;
    /**
     * Constant value for use with setMode(). Puts menus to Bottom mode
     */
    public static final int BOTTOM = 0x1000;

    /**
     * Constat value for use with setMode(). Puts menus to Bottom and Top mode.
     */
    public static final int TOP_BOTTOM = TOP | BOTTOM;
    
    public static final int[] SIDES = new int[] {LEFT, RIGHT, TOP, BOTTOM};
    
    public static boolean isLeft(int mode) {
    	return (mode & LEFT) == LEFT;
    }
    
    public static boolean isRight(int mode) {
    	return (mode & RIGHT) == RIGHT;
    }
    
    public static boolean isTop(int mode) {
    	return (mode & TOP) == TOP;
    }
    
    public static boolean isBottom(int mode) {
    	return (mode & BOTTOM) == BOTTOM;
    }
    
    public static boolean isValidMode(int mode) {
    	return mode > DISABLED && mode <= (TOP|BOTTOM|LEFT|RIGHT);
    }
    
    public static boolean isValidSide(int side) {
    	return side == LEFT || side == RIGHT || side == TOP || side == BOTTOM;
    }

}
