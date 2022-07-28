package com.qa.utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.IOException;
import java.util.Properties;

public class CapabilitiesManager {

    private static final String CAPS_KEY_ANDROID_APP_PACKAGE = "appPackage";
    private static final String CAPS_KEY_ANDROID_APP_ACTIVITY = "appActivity";
    public static final String CAPS_KEY_CHROME_DRIVER_PORT = "chromeDriverPort";
    public static final String CAPS_KEY_SYSTEM_PORT = "systemPort";
    private static final String CAPS_NEW_COMMAND_TIMEOUT = "newCommandTimeout";
    private static final String CAPS_KEY_ANDROID_APPS_LOCATION = "appsLocation";
    private static final String CAPS_KEY_IOS_PLATFORM_VERSION = "iOSPlatformVersion";
    private static final String CAPS_KEY_IOS_BUNDLE_ID = "bundleId";
    private static final String CAPS_KEY_WDA_LOCAL_PORT = "wdaLocalPort";
    private static final String CAPS_KEY_WEB_KIT_DEBUG_PROXY_PORT = "webkitDebugProxyPort";
    private static final String CAPS_KEY_XCODE_ORG_ID = "xcodeOrgId";
    private static final String CAPS_KEY_XCODE_SIGNING_ID = "xcodeSigningId";

    // Android
    private static final String CAPS_KEY_ANDROID_WAIT_FOR_DEVICE = "androidDeviceReadyTimeout";

    private static CapabilitiesManager instance;

    /**
     * This instance is created to provide access to all other classes through this single object.
     *
     * @return The object of CapabilitiesManager class.
     */
    public static CapabilitiesManager getInstance() {
        if (instance == null) {
            instance = new CapabilitiesManager();
        }
        return instance;
    }

    /**
     * getCaps method is used to load the capabilities, to start the
     * automation session at the mentioned parameters using Appium.
     * <p>
     * Appium supports both Android and iOS, it has a unique set of Capabilities for both platforms.
     * Firstly it loads the common capabilities like: platform, deviceName, udid and then in switch-case
     * its loads specific Android or iOS capabilities.
     * <p>
     * Please see: https://appium.io/docs/en/writing-running-appium/caps/
     *
     * @return loads the capabilities for the corresponding parameters
     * @throws IOException Throws Exception when no capabilities are loaded
     */
    public DesiredCapabilities getCaps() throws IOException {

        GlobalParams params = GlobalParams.getInstance();

        Properties props = PropertyManager.getInstance().getProps();

        try {
            TestUtils.log().info("getting capabilities");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME,
                    params.getPlatform());
            caps.setCapability(MobileCapabilityType.UDID, params.getUDID());
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, params.getDeviceName());
            caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 20);
            switch (PlatformHelper.get(params.getPlatform())) {
                case ANDROID:
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                            props.getProperty(PropertyManager.PROPERTY_KEY_ANDROID_AUTOMATION_NAME));
                    caps.setCapability(CAPS_KEY_ANDROID_APP_PACKAGE,
                            props.getProperty(PropertyManager.PROPERTY_KEY_ANDROID_APP_PACKAGE));
                    caps.setCapability(CAPS_KEY_ANDROID_APP_ACTIVITY,
                            props.getProperty(PropertyManager.PROPERTY_KEY_ANDROID_APP_ACTIVITY));
                    caps.setCapability(CAPS_KEY_CHROME_DRIVER_PORT, Integer.valueOf(params.getChromeDriverPort()));
                    caps.setCapability(CAPS_KEY_SYSTEM_PORT, Integer.valueOf(params.getSystemPort()));

                    TestUtils.log().info("appUrl is "
                            + props.getProperty(PropertyManager.PROPERTY_KEY_ANDROID_APP_LOCATION));
                    caps.setCapability(CAPS_KEY_ANDROID_APPS_LOCATION,
                            props.getProperty(PropertyManager.PROPERTY_KEY_ANDROID_APP_LOCATION));
                    caps.setCapability(CAPS_KEY_ANDROID_WAIT_FOR_DEVICE,
                           30);
                    caps.setCapability("appium:chromeOptions", ImmutableMap.of("W3C", false));
                    break;

                case iOS:

                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                            props.getProperty(PropertyManager.PROPERTY_KEY_IOS_AUTOMATION_NAME));
                    caps.setCapability(CAPS_KEY_IOS_PLATFORM_VERSION,
                            props.getProperty(PropertyManager.PROPERTY_KEY_IOS_PLATFORM_VERSION));
                    caps.setCapability(CAPS_KEY_IOS_BUNDLE_ID,
                            props.getProperty(PropertyManager.PROPERTY_KEY_IOS_BUNDLE_ID));
                    caps.setCapability(CAPS_KEY_WDA_LOCAL_PORT, params.getWdaLocalPort());
                    caps.setCapability(CAPS_KEY_WEB_KIT_DEBUG_PROXY_PORT, params.getWebkitDebugProxyPort());
                    caps.setCapability(MobileCapabilityType.APP,
                            props.getProperty(PropertyManager.PROPERTY_KEY_IOS_APPS_LOCATION));
                    caps.setCapability(CAPS_KEY_XCODE_ORG_ID,
                            props.getProperty(PropertyManager.PROPERTY_KEY_XCODE_ORG_ID));
                    caps.setCapability(CAPS_KEY_XCODE_SIGNING_ID,
                            props.getProperty(PropertyManager.PROPERTY_KEY_XCODE_SIGNING_ID));
                    caps.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS,
                            props.getProperty(PropertyManager.PROPERTY_KEY_IOS_AUTO_ACCEPT_ALERTS));
                    break;
                default:
                    throw new IllegalStateException("Invalid parameter!!!");
            }
            return caps;
        } catch (Exception e) {
            e.printStackTrace();
            TestUtils.log().fatal("Failed to load capabilities. ABORT!!" + e.toString());
            throw e;
        }
    }
}
