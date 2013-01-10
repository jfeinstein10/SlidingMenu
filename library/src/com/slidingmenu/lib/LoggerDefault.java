package com.slidingmenu.lib;

import android.util.Log;

/**
 * Helper class to redirect {@link LogManager#logger} to {@link android.util.Log}
 */
public class LoggerDefault implements Logger {

	@Override
	public int v(String tag, String msg) {
		return Log.v(tag, msg);
	}

	@Override
	public int v(String tag, String msg, Throwable tr) {
		return Log.v(tag, msg, tr);
	}

	@Override
	public int d(String tag, String msg) {
		return Log.d(tag, msg);
	}

	@Override
	public int d(String tag, String msg, Throwable tr) {
		return Log.d(tag, msg, tr);
	}

	@Override
	public int i(String tag, String msg) {
		return Log.i(tag, msg);
	}

	@Override
	public int i(String tag, String msg, Throwable tr) {
		return Log.i(tag, msg, tr);
	}

	@Override
	public int w(String tag, String msg) {
		return Log.w(tag, msg);
	}

	@Override
	public int w(String tag, String msg, Throwable tr) {
		return Log.w(tag, msg, tr);
	}

	@Override
	public int e(String tag, String msg) {
		return Log.e(tag, msg);
	}

	@Override
	public int e(String tag, String msg, Throwable tr) {
		return Log.e(tag, msg, tr);
	}

}
