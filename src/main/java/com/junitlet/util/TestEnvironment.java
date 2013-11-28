package com.junitlet.util;

import java.lang.management.ManagementFactory;
import java.util.regex.Pattern;

/**
 * Utility class that should contain various utility methods that deal with testing environment.
 * @author alexr
 */
public class TestEnvironment {
	/**
	 * Distinguishes whether test is running in debug environment.
	 * @return true if code is being debugged, false otherwise
	 */
	public static boolean isDebugging() {
	    Pattern p = Pattern.compile("-Xdebubg|jdwp");
	    for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	        if (p.matcher(arg).find()) {
	            return true;
	        }
	    }
	    return false;
	}
}
