package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;

public class ScreenshotUtils {

    public static byte[] captureScreenshot(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BYTES);
    }
}