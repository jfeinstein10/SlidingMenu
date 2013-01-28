package com.slidingmenu.lib;

/**
 * class that holds the {@link Logger} for this library
 */
public final class LogManager {

	static Logger logger = new LoggerDefault();
	
	public static void setLogger(Logger newLogger) {
		logger = newLogger;
	}
	
	public static Logger getLogger() {
		return logger;
	}
}
