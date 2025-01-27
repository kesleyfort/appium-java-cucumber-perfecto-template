package utils;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static utils.CapabilitiesReader.readCapabilities;

public class DriverManager {

    private static AppiumDriver driver;
    private static ReportiumClient reportiumClient;
    public static boolean isLocalExecution = true; // Set to false for Perfecto execution
    private static String perfectoCloudURL;

    public static void initializeDriver() {
        if (driver == null) {
            if (isLocalExecution) {
                // Start the Appium server for local execution
                AppiumServerManager.startServer();
            }

            DesiredCapabilities capabilities = new DesiredCapabilities();
            if (isLocalExecution) {
                Map<String, Object> emulatorCapabilities = readCapabilities().get("emulator");
                for (Map.Entry<String, Object> entry : emulatorCapabilities.entrySet()) {
                    capabilities.setCapability(entry.getKey(), entry.getValue());
                }
            } else {
                // Perfecto execution capabilities
                perfectoCloudURL  = "https://<your-cloud-name>.perfectomobile.com/nexperience/perfectomobile/wd/hub";
                String securityToken = System.getProperty("PERFECTO_SECURITY_TOKEN");
                Map<String, Object> emulatorCapabilities = readCapabilities().get("perfecto");
                for (Map.Entry<String, Object> entry : emulatorCapabilities.entrySet()) {
                    capabilities.setCapability(entry.getKey(), entry.getValue());
                }
                capabilities.setCapability("securityToken", securityToken);
            }

            try {
                URL url = isLocalExecution ? new URL("http://127.0.0.1:4723/wd/hub") : new URL(perfectoCloudURL);
                driver = new AppiumDriver(url, capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static ReportiumClient getReportiumClient() {
        if (reportiumClient == null) {
            PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withWebDriver(driver)
                .build();
            reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
        }
        return reportiumClient;
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null; // Reset driver
            reportiumClient = null; // Reset reporting client
        }

        if (isLocalExecution) {
            // Stop the Appium server for local execution
            AppiumServerManager.stopServer();
        }
    }
}