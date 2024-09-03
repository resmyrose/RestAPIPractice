package runner;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/RestAPIPractice.html", "pretty",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "pretty",
		"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" }, monochrome = false, dryRun = false, features = {
				"src/test/resources/features" }, glue = { "stepdefinition"})

public class TestRunner {

}
