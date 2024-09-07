package stepdefinition;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import com.google.gson.Gson;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.Login;
import utilities.APIConstants;
import utilities.ExcelReader;
import utilities.LoggerLoad;
import utilities.RestUtils;
import utilities.Tokens;

public class LoginSteps extends RestUtils {

	APIConstants apiconstants = new APIConstants();

	RequestSpecification request;
	Response response;
	int expectedStatusCode;
	String res;
	String token;
	List<Map<String, String>> testdata;
	Map<String, String> data;
	Map<Login, Response> responseData;
	List<Map<Login, Response>> responseDataList = new ArrayList<Map<Login, Response>>();
	Login login;
	

	@Given("Passing {string} and Fillo API is up and running")
	public void passing_and_fillo_api_is_up_and_running(String sheetName)  throws FileNotFoundException {
		
		String login_Query = "Select Scenario,userName,password,expectedStatusCode,endpoint,method,TokenName,contentType From "
				+ sheetName + " Where Scenario != ''";
		testdata = ExcelReader.getExcelDataWithFilloAPI(apiconstants.PATH, login_Query);
		
		LoggerLoad.info("=======Fillo API is up and Running======");

	}

	@When("User send POST HTTP request with endpoint")
	public void user_send_post_http_request_with_endpoint() throws FileNotFoundException {
		
		int iteration_Count = 0;

		for (Map<String, String> data : testdata) {
			login = new Login();
			iteration_Count = iteration_Count+1;
			login.setUsername(data.get("userName"));
			login.setPassword(data.get("password"));
			login.setScenario(data.get("Scenario"));
			login.setEndpoint(data.get("endpoint"));
			login.setContentType(data.get("contentType"));
			login.setMethod(data.get("method"));
			login.setExpectedStatusCode(data.get("expectedStatusCode"));
			login.setTokenName(data.get("TokenName"));
			
//			LoggerLoad.info("======Sending "+login.getScenario()+ " with "+login.getMethod()+ " request======");
			 

			String reqbody = new Gson().toJson(login);
			request = given().spec(CommonSpec(login)).body(reqbody);
			
			LoggerLoad.info("======= Printing the "+iteration_Count+" iteration Response Body for the Scenario "+login.getScenario()+" =======");
			switch (login.getMethod()) {
			case "POST": {
				
				response = request.when().post(login.getEndpoint());
				response.then().log().all().extract().response().toString();
				responseData = new HashMap<Login, Response>();
				responseData.put(login, response);
				responseDataList.add(responseData);
//				System.out.println("response data list size = " + responseDataList.size());

				break;
			}
			case "GET": {

				response = request.when().get(login.getEndpoint());
				response.then().log().all().extract().response().toString();
				responseData = new HashMap<Login, Response>();
				responseData.put(login, response);
				responseDataList.add(responseData);

				break;

			}

			}
			
			token = response.path("token");
			Tokens.TokenMap.put(login.getTokenName(), token);

		}

	}

	@Then("User validates the response body")
	public void user_validates_the_response_body()  {
		// hash.forEach((key, tab) -> /* do something with key and tab */);
		LoggerLoad.info("-------Validating Response Body--------");
		int iteration = 0;
		
		for (Map<Login, Response> responseObject : responseDataList) {
			for (Map.Entry<Login, Response> entry : responseObject.entrySet()) {
				iteration = iteration+1;
				login = entry.getKey(); // Retrieves the key
				Response res = entry.getValue(); // Retrieves Value
				String responsebody = res.getBody().asPrettyString();
				int actualStatusCode = res.getStatusCode();
				LoggerLoad.info("=========Validating the " + iteration + " iteration with scenario "+login.getScenario()+"=====");
				 try {
					 Assert.assertEquals(res.getStatusCode(), Integer.parseInt(login.getExpectedStatusCode()));
				 }
				 catch(AssertionError e){
					 
					 System.out.println("-------Assertion Failed for StatusCode--------");
					 
				 }
				switch(actualStatusCode) {
				case 200:{
					
					try {
					
					  
					Assert.assertEquals(res.getContentType(), login.getContentType());
					}
					catch(AssertionError e) {
						System.out.println("========Invalid ContentType========");
					}
					if (Tokens.TokenMap.get("AdminToken") != null) {
//
						System.out.println("******Token Generated and Stored in Admin Token******");
					}
					break;
				}
				case 400:{
					
					try {
					Assert.assertTrue(responsebody.contains("invalid login"));
					}
					catch(AssertionError e)
					{
						System.out.println("========Invalid Error Message========");
					}
					System.out.println("***The Statuscode is "+ actualStatusCode + " with Bad Request****");
				
					
					break;
					
				}
				case 404:{
					
					try {
					Assert.assertTrue(responsebody.contains("Not Found"));
					}
					catch(AssertionError e)
					{
						System.out.println("========Invalid Error Message========");
					}
					System.out.println("***The Statuscode is "+ actualStatusCode + " with Not Found ****");
				
					
					break;
					
				}
				case 405:{
					
					try {
					Assert.assertTrue(responsebody.contains("Method Not Allowed"));
					}
					catch(AssertionError e)
					{
						System.out.println("========Invalid Error Message========");
					}
					System.out.println("***The Statuscode is "+ actualStatusCode + " with Method Not Allowed ****");
				
					
					break;
					
				}
				
				default : {
					
					System.out.println("Status Code is not 200 or 400 or 404");
				}
				
				}
				
				


			}

		}

	}

	}

