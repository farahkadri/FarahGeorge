package com.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to handle property files in the resources folder to load dynamically configurations to run
 * different kind of tests.
 */
public class PropertyManager {

    public static final String PROPERTY_KEY_PLATFORM = "platform";
    public static final String PROPERTY_KEY_DEVICE_NAME = "deviceName";
    public static final String PROPERTY_KEY_UDID = "udid";
    public static final String PROPERTY_KEY_ANDROID_AUTOMATION_NAME = "androidAutomationName";
    public static final String PROPERTY_KEY_ANDROID_APP_PACKAGE = "androidAppPackage";
    public static final String PROPERTY_KEY_ANDROID_APP_ACTIVITY = "androidAppActivity";
    public static final String PROPERTY_KEY_ANDROID_APP_LOCATION = "androidAppLocation";
    public static final String PROPERTY_KEY_SYSTEM_PORT = "systemPort";
    public static final String PROPERTY_KEY_CHROME_DRIVER_PORT = "chromeDriverPort";

    public static final String PROPERTY_KEY_IOS_AUTOMATION_NAME = "iOSAutomationName";
    public static final String PROPERTY_KEY_IOS_PLATFORM_VERSION = "iOSPlatformVersion";
    public static final String PROPERTY_KEY_IOS_BUNDLE_ID = "bundleId";
    public static final String PROPERTY_KEY_WDA_LOCAL_PORT = "wdaLocalPort";
    public static final String PROPERTY_KEY_WEBKIT_DEBUG_PROXY_PORT = "webkitDebugProxyPort";
    public static final String PROPERTY_KEY_IOS_APPS_LOCATION = "app";
    public static final String PROPERTY_KEY_XCODE_ORG_ID = "xcodeOrgId";
    public static final String PROPERTY_KEY_XCODE_SIGNING_ID = "xcodeSigningId";
    public static final String PROPERTY_KEY_IOS_AUTO_ACCEPT_ALERTS = "autoAcceptAlerts";

    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_FILE_NAME = "config_default.properties";
    private static final String SYSTEM_PROPERTY_KEY = "configFileName";
    private static PropertyManager instance;

    /**
     * This instance is created to provide access to all other classes through this single object.
     *
     * @return The object of PropertyManager class.
     */

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    /**
     * Reads a property file from the resources directory and loads its content.
     * <p>
     * First the method will try to read a System Property called "configFileName" and if
     * it is set, then the file name is used. If no System Property is set, then the default
     * config file with the name "config_default.properties" is loaded.
     *
     * @return The properties to read the values.
     * @throws IOException Throws Exception when the file is not found.
     */
    public Properties getProps() throws IOException {

        String propsFileName = System.getProperty(SYSTEM_PROPERTY_KEY, DEFAULT_FILE_NAME);

        TestUtils.log().info("##### PROPERTIES FILE USED #####");
        TestUtils.log().info(propsFileName);
        TestUtils.log().info("################################");

        InputStream is = null;

        if (PROPERTIES.isEmpty()) {
            try {
                TestUtils.log().info("loading config properties");
                is = getClass().getClassLoader().getResourceAsStream(propsFileName);
                PROPERTIES.load(is);
            } catch (IOException e) {
                e.printStackTrace();
                TestUtils.log().fatal("Failed to load config properties. ABORT!!" + e.toString());
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return PROPERTIES;
    }
}
