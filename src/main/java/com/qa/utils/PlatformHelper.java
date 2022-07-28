package com.qa.utils;

import com.qa.utils.exceptions.UnsupportedPlatformException;
import io.appium.java_client.AppiumDriver;

public final class PlatformHelper {
    public enum Platforms {
        ANDROID, iOS;
    }

    private PlatformHelper() {

    }

    public static Platforms get(final AppiumDriver<?> driver) {
        return get(driver.getPlatformName());
    }

    public static Platforms get(final String platformName) throws UnsupportedPlatformException {
        if (platformName == null) {
            throw new IllegalArgumentException("Platform name must be defined");
        }
        switch (platformName.toLowerCase()) {
            case "android": return Platforms.ANDROID;
            case "ios": return Platforms.iOS;
            default:
                throw new UnsupportedPlatformException(platformName);
        }
    }

    public static Platforms get(final GlobalParams params) {
        return get(params.getPlatform());
    }

    public static Platforms getFromGlobalParams() {
        String platformName = GlobalParams.getInstance().getPlatform();
        if (platformName == null || platformName.isEmpty()) {
            throw new NullPointerException("GlobalParams.getPlatform must be defined!");
        }
        return get(platformName);
    }
}
