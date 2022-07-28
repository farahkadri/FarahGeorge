package com.qa.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.util.HashMap;

public final class ServerManager {
    private static final ThreadLocal<AppiumDriverLocalService> SERVER = new ThreadLocal<>();

    public AppiumDriverLocalService getServer() {
        return SERVER.get();
    }

    public void startServer() {
        TestUtils.log().info("starting appium server");
        AppiumDriverLocalService server = windowsGetAppiumService();
        server.start();
        if (!server.isRunning()) {
            TestUtils.log().fatal("Appium server not started. ABORT!!!");
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
        server.clearOutPutStreams();
        ServerManager.SERVER.set(server);
        TestUtils.log().info("Appium server started");
    }

    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService windowsGetAppiumService() {
        GlobalParams params = GlobalParams.getInstance();
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                        .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withLogFile(new File(params.getPlatform() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }

    public AppiumDriverLocalService macGetAppiumService() {
        GlobalParams params = new GlobalParams();
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "/Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/"
                + "Home/bin:/Users/Om/Library/Android/sdk/tools:/Users/Om/Library/"
                + "Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
                + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "/Users/Om/Library/Android/sdk");
        environment.put("JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File(params.getPlatform() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }
}
