package stepdefinition;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.Login;
import utilities.APIConstants;
import utilities.ExcelReader;
import utilities.LoggerLoad;
import utilityMethods.Login_reusableMethods;


public class Optimized_Login_Steps extends Login_reusableMethods{
	
	APIConstants apiconstants = new APIConstants();
	List<Map<String, String>> testdata;
	Map<String, String> data;
	Map<Login, Response> responseData;
	List<Map<Login, Response>> responseDataList = new ArrayList<Map<Login, Response>>();
	 
	
	@Given("Passing {string} and Fillo API is up and running")
    public void passing_and_fillo_api_is_up_and_running(String sheetName) throws FileNotFoundException {
        String query = String.format("SELECT Scenario,userName,password,expectedStatusCode,endpoint,method,TokenName,contentType FROM %s WHERE Scenario != ''", sheetName);
        testdata = ExcelReader.getExcelDataWithFilloAPI(apiconstants.PATH, query);
        LoggerLoad.info("======= Fillo API is up and running =======");
    }

    @When("User send POST HTTP request with endpoint")
    public void user_sends_post_http_request_with_endpoint() throws FileNotFoundException {
        for (Map<String, String> data : testdata) {
            Login login = buildLoginObject(data);
            Response response = sendRequest(login);
            handleResponse(login, response);
        }
    }

    @Then("User validates the response body")
    public void user_validates_the_response_body() {
        LoggerLoad.info("------- Validating Response Body --------");
        int iteration = 0;
        for (Map<Login, Response> responseObject : responseDataList) {
            iteration++;
            validateResponse(iteration, responseObject);
        }
    }

	
	

}
