package com.qa.utils;

import java.io.IOException;
import java.util.Properties;

public final class GlobalParams {

    /**
     * The list of the device parameters
     * Please see the description for each at /main/resources/config_default.properties.
     */
    private static ThreadLocal<String> platformName = new ThreadLocal<String>();
    private static ThreadLocal<String> udid = new ThreadLocal<String>();
    private static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static ThreadLocal<String> systemPort = new ThreadLocal<String>();
    private static ThreadLocal<String> chromeDriverPort = new ThreadLocal<String>();
    private static ThreadLocal<String> wdaLocalPort = new ThreadLocal<String>();
    private static ThreadLocal<String> webkitDebugProxyPort = new ThreadLocal<String>();

    private static GlobalParams instance;

    public static GlobalParams getInstance() {
        if (instance == null) {
            instance = new GlobalParams();
        }
        return instance;
    }

    public void setPlatform(final String platform) {
        GlobalParams.platformName.set(platform);
    }

    public String getPlatform() {
        return platformName.get();
    }

    public String getUDID() {
        return udid.get();
    }

    public void setUDID(final String udid2) {
        udid.set(udid2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(final String deviceName2) {
        deviceName.set(deviceName2);
    }

    public String getSystemPort() {
        return systemPort.get();
    }

    public void setSystemPort(final String systemPort2) {
        systemPort.set(systemPort2);
    }

    public String getChromeDriverPort() {
        return chromeDriverPort.get();
    }

    public void setChromeDriverPort(final String chromeDriverPort2) {
        chromeDriverPort.set(chromeDriverPort2);
    }

    public String getWdaLocalPort() {
        return wdaLocalPort.get();
    }

    public void setWdaLocalPort(final String wdaLocalPort2) {
        wdaLocalPort.set(wdaLocalPort2);
    }

    public String getWebkitDebugProxyPort() {
        return webkitDebugProxyPort.get();
    }

    public void setWebkitDebugProxyPort(final String webkitDebugProxyPort2) {
        webkitDebugProxyPort.set(webkitDebugProxyPort2);
    }

    /**
     * This loads all parameters to run the tests either on Android or iOS.
     *
     * @throws IOException Throws Exception if the device parameters are wrong.
     */

    public void initializeGlobalParams() throws IOException {

        GlobalParams params = new GlobalParams();
        Properties props = PropertyManager.getInstance().getProps();

        params.setPlatform(props.getProperty(PropertyManager.PROPERTY_KEY_PLATFORM));
        params.setUDID(props.getProperty(PropertyManager.PROPERTY_KEY_UDID));
        params.setDeviceName(props.getProperty(PropertyManager.PROPERTY_KEY_DEVICE_NAME));

        switch (params.getPlatform()) {
            case "Android":
                params.setSystemPort(props.getProperty(PropertyManager.PROPERTY_KEY_SYSTEM_PORT));
                params.setChromeDriverPort(props.getProperty(PropertyManager.PROPERTY_KEY_CHROME_DRIVER_PORT));
                break;

            case "iOS":
                params.setWdaLocalPort(props.getProperty(PropertyManager.PROPERTY_KEY_WDA_LOCAL_PORT));
                params.setWebkitDebugProxyPort(props.getProperty(PropertyManager.PROPERTY_KEY_WEBKIT_DEBUG_PROXY_PORT));
                break;
            default:
                throw new IllegalStateException("Invalid Platform Name!");
        }

        TestUtils.log().info("####### GlobalParams #######");
        TestUtils.log().info(params.toString());
        TestUtils.log().info("############################");
    }

    @Override
    public String toString() {
        return "GlobalParams{"
                + "platformName=" + platformName.get()
                + ", udid=" + udid.get()
                + ", deviceName=" + deviceName.get()
                + ", systemPort=" + systemPort.get()
                + ", chromeDriverPort=" + chromeDriverPort.get()
                + ", wdaLocalPort=" + wdaLocalPort.get()
                + ", webkitDebugProxyPort="
                + webkitDebugProxyPort.get()
                + '}';
    }
}
