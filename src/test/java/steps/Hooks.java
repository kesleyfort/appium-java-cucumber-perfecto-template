package steps;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.result.TestResultFactory;
import io.cucumber.java.*;
import com.perfecto.reportium.test.TestContext;
import utils.DriverManager;

import static utils.DriverManager.getDriver;
import static utils.DriverManager.isLocalExecution;
import static utils.ScreenshotUtils.captureScreenshot;
import static utils.StepInfoPlugin.stepName;

public class Hooks {
    private ReportiumClient reportiumClient;

    @Before
    public void setUp(Scenario scenario) {
        // Initialize driver and reporting client
        DriverManager.initializeDriver();
        if(!isLocalExecution){
            reportiumClient = DriverManager.getReportiumClient();

            // Start a new test in Perfecto Reporting
            reportiumClient.testStart(scenario.getName(), new TestContext("tag1", "tag2"));
        }

    }
    @AfterStep
    public void afterStep(Scenario scenario) {
        byte[] screenshot = captureScreenshot(getDriver());
        scenario.attach(screenshot, "image/png", "Test Step: " + scenario.getName());
        if(!isLocalExecution) {
            reportiumClient.stepEnd("Test Step: " + stepName);
        }
    }
    @BeforeStep
    public void beforeStep(Scenario scenario) {
        if(!isLocalExecution) {
            reportiumClient.stepStart("Test Step: " + stepName);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if(!isLocalExecution) {
            if (scenario.isFailed()) {
                reportiumClient.testStop(TestResultFactory.createFailure("Scenario failed. Please check the logs for more details."));
            } else {
                reportiumClient.testStop(TestResultFactory.createSuccess());
            }
        }
    
        // Quit the driver and stop the Appium server
        DriverManager.tearDown();
    }
}