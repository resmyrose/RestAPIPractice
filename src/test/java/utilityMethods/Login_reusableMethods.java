package utilityMethods;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.google.gson.Gson;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.Login;
import utilities.LoggerLoad;
import utilities.RestUtils;
import utilities.Tokens;

import static io.restassured.RestAssured.given;

public class Login_reusableMethods  extends RestUtils{
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
	
	 public Login buildLoginObject(Map<String, String> data) {
	        Login login = new Login();
	        login.setUsername(data.get("userName"));
	        login.setPassword(data.get("password"));
	        login.setScenario(data.get("Scenario"));
	        login.setEndpoint(data.get("endpoint"));
	        login.setContentType(data.get("contentType"));
	        login.setMethod(data.get("method"));
	        login.setExpectedStatusCode(data.get("expectedStatusCode"));
	        login.setTokenName(data.get("TokenName"));
	        return login;
	    }

	    public Response sendRequest(Login login) throws FileNotFoundException {
	        String reqBody = new Gson().toJson(login);
	        request = given().spec(CommonSpec(login)).body(reqBody);
	        LoggerLoad.info("======= Sending " + login.getScenario() + " with " + login.getMethod() + " request =======");

	        Response response = null;
	        if ("POST".equalsIgnoreCase(login.getMethod())) {
	            response = request.post(login.getEndpoint());
	        } else if ("GET".equalsIgnoreCase(login.getMethod())) {
	            response = request.get(login.getEndpoint());
	        }

	        logResponse(login, response);
	        return response;
	    }

	    public void handleResponse(Login login, Response response) {
	        responseDataList.add(Map.of(login, response));
	        String token = response.path("token");
	        if (token != null && login.getTokenName() != null) {
	            Tokens.TokenMap.put(login.getTokenName(), token);
	        }
	    }

	    public void validateResponse(int iteration, Map<Login, Response> responseObject) {
	        responseObject.forEach((login, response) -> {
	            LoggerLoad.info("Validating iteration " + iteration + " with scenario " + login.getScenario());
	            validateStatusCode(login, response);
	            validateResponseBody(login, response);
	        });
	    }

	    public void validateStatusCode(Login login, Response response) {
	        try {
	            Assert.assertEquals(response.getStatusCode(), Integer.parseInt(login.getExpectedStatusCode()));
	        } catch (AssertionError e) {
	            LoggerLoad.error("Assertion Failed for StatusCode in scenario: " + login.getScenario());
	        }
	    }

	    public void validateResponseBody(Login login, Response response) {
	        String responseBody = response.getBody().asPrettyString();
	        int actualStatusCode = response.getStatusCode();
	        
	        switch (actualStatusCode) {
	            case 200:
	                Assert.assertEquals(response.getContentType(), login.getContentType(), "Invalid ContentType");
	                LoggerLoad.info("Token stored in " + login.getTokenName());
	                break;
	            case 400:
	                Assert.assertTrue(responseBody.contains("invalid login"));
	                break;
	            case 404:
	                Assert.assertTrue(responseBody.contains("Not Found"));
	                break;
	            case 405:
	                Assert.assertTrue(responseBody.contains("Method Not Allowed"));
	                break;
	            default:
	                LoggerLoad.warn("Unexpected status code: " + actualStatusCode);
	                break;
	        }
	    }

	    public void logResponse(Login login, Response response) {
	        LoggerLoad.info("Response for scenario " + login.getScenario() + ": " + response.getBody().asString());
	    }

}
