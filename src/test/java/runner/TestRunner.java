package runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
 

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/01Login.feature", glue = {"stepdefinition"},

		monochrome = true, plugin = {

				 
				 
				"html:target/HtmlReport/htmlreport.html" })
public class TestRunner {

}
