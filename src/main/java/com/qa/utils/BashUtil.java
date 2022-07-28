package com.qa.utils;

public final class BashUtil {

    private BashUtil() {

    }

    public static String getAndroidHome() {
        String androidHome = System.getenv().getOrDefault("ANDROID_HOME", null);

        if (androidHome == null || androidHome.isEmpty()) {
            throw new IllegalStateException(
                    "System env variable ANDROID_HOME must be defined in order to manage emulators"
            );
        }

        return androidHome;
    }

    public static String adb(final String args) {
        String androidHome = getAndroidHome();
        if (args == null || args.isEmpty()) {
            return androidHome + "/platform-tools/adb";
        } else  {
            return androidHome + "/platform-tools/adb " + args;
        }
    }

    public static String emulator(final String args) {
        String androidHome = getAndroidHome();
        if (args == null || args.isEmpty()) {
            return androidHome + "/emulator/emulator";
        } else  {
            return androidHome + "/emulator/emulator " + args;
        }
    }

    public static String iDeviceInstaller(final String args) {
        if (args == null || args.isEmpty()) {
            return "ideviceinstaller";
        } else  {
            return "ideviceinstaller " + args;
        }
    }
}
