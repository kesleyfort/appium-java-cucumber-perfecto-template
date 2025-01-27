package steps;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DriverManager;

public class ExampleSteps {

    private AppiumDriver driver;

    public ExampleSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Given("I launch the app on a Perfecto device")
    public void i_launch_the_app_on_a_perfecto_device() {
        System.out.println("App launched successfully!");
    }

    @When("I perform an action")
    public void i_perform_an_action() {
        System.out.println("Action performed successfully!");
    }

    @Then("the action should be successful")
    public void the_action_should_be_successful() {
        System.out.println("Action verified successfully!");
    }
}