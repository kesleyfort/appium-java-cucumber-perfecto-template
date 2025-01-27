package utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    public static void startServer() {
        // Set the path to the Appium main.js file
        String appiumJsPath = System.getenv("APPIUM_HOME");
        if (appiumJsPath == null) {
            throw new RuntimeException("APPIUM_HOME environment variable is not set!");
        } else {
            appiumJsPath += "/build/lib/main.js";
        }

        // Build the Appium service
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumJsPath))
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info");

        // Start the Appium server
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        System.out.println("Appium server started on: " + service.getUrl());
    }

    public static void stopServer() {
        if (service != null) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }

    public static boolean isServerRunning() {
        return service != null && service.isRunning();
    }
}