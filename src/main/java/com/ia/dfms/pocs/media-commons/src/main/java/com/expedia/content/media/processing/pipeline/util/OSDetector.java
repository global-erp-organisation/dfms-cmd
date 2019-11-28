package com.expedia.content.media.processing.pipeline.util;

/**
 * Shamefully stolen from the internet:
 * @see <a href="http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/">Detecting OS</a>
 */
public final class OSDetector {
    private static final String OS_STRING = System.getProperty("os.name").toLowerCase();

    private OSDetector() {
        /* static class */
    }

    public static OS detectOS(String osString) {
        if (isWindows(osString)) {
            return OS.WINDOWS;
        } else if (isMac(osString)) {
            return OS.MAC;
        } else if (isUnix(osString)) {
            return OS.UNIX;
        } else if (isSolaris(osString)) {
            return OS.SOLARIS;
        } else {
            return OS.UNKNOWN;
        }
    }

    public static OS detectOS() {
        return detectOS(OS_STRING);
    }

    private static boolean isWindows(String osString) {
        return osString.contains("win");
    }

    private static boolean isMac(String osString) {
        return osString.contains("mac");
    }

    private static boolean isUnix(String osString) {
        return osString.contains("nix") || osString.contains("nux") || osString.contains("aix");
    }

    private static boolean isSolaris(String osString) {
        return osString.contains("sunos");
    }

    public static enum OS {
        UNKNOWN,
        SOLARIS,
        WINDOWS,
        UNIX,
        MAC
    }
}
