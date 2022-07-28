package com.qa.utils.exceptions;

public class UnsupportedPlatformException extends IllegalStateException {
    public UnsupportedPlatformException() {
        super("Unknown platform name. Platform name must be android or iOS");
    }

    public UnsupportedPlatformException(final String platformName) {
        super("Unknown [" + platformName + "] platform name. Platform name must be android or iOS");
    }
}
