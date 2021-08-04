package org.visab.util;

/**
 * Helper class for getting the OS VISAB is running on.
 */
public final class OSUtil {

    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS
    }

    private static OS os = null;

    /**
     * Gets the OS that VISAB is currenlty running on
     * 
     * @return The OS enum
     */
    public static final OS getOS() {
        if (os == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
                os = OS.LINUX;
            } else if (operSys.contains("mac")) {
                os = OS.MAC;
            } else if (operSys.contains("sunos")) {
                os = OS.SOLARIS;
            }
        }

        return os;
    }

}
