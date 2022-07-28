package com.qa.utils;

import com.qa.utils.exceptions.AndroidEmulatorNotFoundRuntimeException;
import com.qa.utils.exceptions.CouldNotInstallApkRuntimeException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public final class DriverManager {

    /**
     * Appium Driver permits us to execute cross-platform tests.
     */
    private static ThreadLocal<AppiumDriver<MobileElement>> driverThread = new ThreadLocal<>();

    /**
     * The method is used to retrieve the Appium driver used for Automation.
     *
     * @return The driver
     */
    public AppiumDriver<MobileElement> getDriver() {
        return driverThread.get();
    }

    /**
     * StartAndroidEmulator starts the Emulator based on the Operating system.
     * Thread.sleep(90000) is used to start the Emulator on Windows machine,
     * since on Windows to boot the device takes to long.
     *
     * @throws IOException Throws Exception when none of the operating systems is found.
     */
    public void startAndroidEmulator() throws IOException, InterruptedException {

        String emulatorUDID = GlobalParams.getInstance().getDeviceName();

        String emulatorRunningCmd = TestUtils
                .execCmdForResult(BashUtil.adb("devices"));
        boolean isEmulatorRunning = emulatorRunningCmd
                .contains(emulatorUDID);

        // Check if the Emulator is running
        // If yes skip launching Emulator part
        if (!isEmulatorRunning) {

            String emulatorDeviceName = GlobalParams.getInstance().getDeviceName();

            String emulatorCmdResult = TestUtils
                    .execCmdForResult(BashUtil.emulator("-list-avds"));
            boolean isEmulatorAvailable = emulatorCmdResult
                    .contains(emulatorDeviceName);

            // Check if the emulator with the name form the config file is available.
            // If not then throw an exception and terminate the program.
            if (!isEmulatorAvailable) {

                TestUtils.log().error("Emulators available:\n" + emulatorCmdResult);

                throw new AndroidEmulatorNotFoundRuntimeException(
                        "Emulator with the name \""
                                + emulatorDeviceName
                                + "\" was not found.");
            }

            if (SystemUtils.IS_OS_WINDOWS) {
                Runtime.getRuntime().exec(System.getProperty("user.dir")
                        + File.separator + "src"
                        + File.separator + "main"
                        + File.separator + "resources"
                        + File.separator + "startEmulator.bat");
                Thread.sleep(90000);

            } else if (SystemUtils.IS_OS_MAC_OSX) {
                Runtime.getRuntime().exec(BashUtil.emulator("-avd " + emulatorDeviceName + " -no-snapshot-load"));

                Thread.sleep(60000);

            } else if (SystemUtils.IS_OS_LINUX) {
                TestUtils.log().error("Error: Not yet configured for Linux.");
                throw new UnsupportedOperationException("Error: Not yet configured for Linux.");
                // TODO: Start emulator on Linux
            } else {
                throw new UnsupportedOperationException("Error: OS not detected, no emulator started");
            }
        } else {
            TestUtils.log().info("Emulator already running.");
        }
    }

    public void installAPK() throws IOException, InterruptedException {
        String apkFilePath = "src"
                + File.separator + "test"
                + File.separator + "resources"
                + File.separator + "apps"
                + File.separator + "George Ceska sporitelna.apk";
        File apkFile = new File(apkFilePath);
        if (!apkFile.exists()) {
            throw new FileNotFoundException("The APK file for the tests was not found under: "
                    + apkFile.getAbsolutePath());
        }
        install(BashUtil.adb("install -r " + apkFile.getAbsolutePath()));
    }

    public void installIPA() throws IOException, InterruptedException {
        String apkFilePath = "src"
                + File.separator + "test"
                + File.separator + "resources"
                + File.separator + "apps"
                + File.separator + "app-sportsid.ipa";
        File apkFile = new File(apkFilePath);
        if (!apkFile.exists()) {
            throw new FileNotFoundException("The IPA file for the tests was not found under: "
                    + apkFile.getAbsolutePath());
        }
        install(BashUtil.iDeviceInstaller("-i " + apkFile.getAbsolutePath()));
    }

    private void install(final String command) throws IOException, InterruptedException {
        int result = Runtime.getRuntime().exec(command).waitFor();
        if (result != 0) { // if not equal to 0 then something went wrong
            throw new CouldNotInstallApkRuntimeException(
                    "[ERROR_CODE:" + result + "] "
                            + "Could not install application with following command: "
                            + command
            );
        }
    }

    /**
     * setDriver is used to set the desired driver to be used in automation.
     *
     * @param driver
     */
    public void setDriver(final AppiumDriver<MobileElement> driver) {
        DriverManager.driverThread.set(driver);
    }

    /**
     * It is used to initialize the Appium driver on the corresponding device (Emulator, Simulator or real device).
     * It is important before starting test execution to initialize the Appium driver.
     * Firstly it checks if the deviceName matches to the Emulator/Simulator name and starts them, then it initializes
     * the appium Driver.
     * @param serverUrl Appium url server
     * @param capabilities Required capabilities for new driver
     * @throws Exception Throws Exception when the driver initialization fails
     */
    public void initializeDriver(final URL serverUrl, final DesiredCapabilities capabilities) throws Exception {
        AppiumDriver<MobileElement> driver = null;
        PlatformHelper.Platforms currentPlatform = PlatformHelper.getFromGlobalParams();
        switch (currentPlatform) {
            case ANDROID:
                startAndroidEmulator();
                installAPK();
                TestUtils.log().info("Create Android driver");
                AndroidDriver<MobileElement> androidDriver =
                        new AndroidDriver<>(serverUrl, capabilities);
                androidDriver.allowInvisibleElements(true);
                driver = androidDriver;
                break;
            case iOS:
                //startIOSSimulator();
                installIPA();
                driver = new IOSDriver<>(serverUrl, capabilities);
                break;
            default:
                throw new IllegalStateException("Invalid parameter!!!");
        }
        setDriver(driver);
        TestUtils.log().info("Driver is initialized");
    }
}
