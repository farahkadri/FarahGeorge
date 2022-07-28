package com.qa.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

public final class TestManager {
    private ServerManager serverManager;
    private DriverManager driverManager;
    private static volatile TestManager instance;

    public static TestManager getInstance() {
        synchronized (TestManager.class) {
            if (instance == null) {
                instance = new TestManager();
            }
            return instance;
        }
    }

    private TestManager() {
        serverManager = new ServerManager();
        driverManager = new DriverManager();
    }

    public boolean init() {
        GlobalParams params;
        // Initialise global params. If it fails, test will not get executed as this is required.
        try {
            params = GlobalParams.getInstance();
            params.initializeGlobalParams();
        } catch (IOException exception) {
            TestUtils.log().fatal("Params initialization failed.", exception);
            return false;
        }
        // Try to start appium server if one is not launched already (through tests, not externally)
        try {
            AppiumDriverLocalService appiumDriverLocalService = getServerManager().getServer();
            if (!(appiumDriverLocalService != null && appiumDriverLocalService.isRunning())) {
                getServerManager().startServer();
            } else  {
                TestUtils.log().info("Appium server is running, canceled starting of new server.");
            }
        } catch (Exception exception) {
            TestUtils.log().fatal("Appium server did not start!", exception);
            return false;
        }
        // Initialise connection/driver to test device
        try {
            AppiumDriver<MobileElement> driver = getDriverManager().getDriver();
            if (!(driver != null && !driver.getStatus().isEmpty())) {
                getDriverManager()
                        .initializeDriver(
                                getServerManager().getServer().getUrl(),
                                createDeviceSpecificCaps()
                        );
            }
        } catch (Exception exception) {
            TestUtils.log().fatal("Driver not initialised, exiting tests.", exception);
            return false;
        }
        return true;
    }


    public void closeDriver() {
        if (getDriverManager().getDriver() != null) {
            getDriverManager().getDriver().quit();
        }
    }

    public void closeServer() {
        if (getServerManager().getServer() != null) {
            getServerManager().getServer().stop();
        }
    }

    public synchronized ServerManager getServerManager() {
        return serverManager;
    }

    public synchronized DriverManager getDriverManager() {
        return driverManager;
    }

    private DesiredCapabilities createDeviceSpecificCaps(final boolean freshInstall) throws IOException {
        DesiredCapabilities caps = CapabilitiesManager.getInstance().getCaps();
        // FULL_RESET and NO_RESET are used to skip the onboarding process
        caps.setCapability(MobileCapabilityType.FULL_RESET, freshInstall);
        caps.setCapability(MobileCapabilityType.NO_RESET, !freshInstall);
        switch (PlatformHelper.get(caps.getPlatform().name())) {
            case ANDROID:
                // caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 30);
                // caps.setCapability(MobileCapabilityType.LOCALE, "DE"); TODO: language support
                // caps.setCapability(MobileCapabilityType.LANGUAGE, "de"); TODO: different language support
                break;
            case iOS:
                //TODO: Check if timeout delays are required
                break;
            default:
                throw new IllegalStateException("Unknown platform");
        }
        return caps;
    }

    private DesiredCapabilities createDeviceSpecificCaps() throws IOException {
        return createDeviceSpecificCaps(false);
    }
}
