package utilities;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import utilities.APIConstants;
import utilities.Tokens;

public class APIRequestBuilder {

	RequestSpecBuilder builder = new RequestSpecBuilder();
	public String method;
	public String url;
	public Response response;

	public String contentType;

	public APIRequestBuilder() {

	}

	/**
	 * APIFunction is constructor used for invalid type if pass through file
	 * 
	 * @param body
	 * @param endpoint
	 * @param method
	 * @param singleDataRow
	 */
	public APIRequestBuilder(String body, String endpoint, String method, Map<String, String> singleDataRow) {

		commonInit(body, endpoint, method, singleDataRow);

	}

	public APIRequestBuilder(String body, String endpoint, String method, Map<String, String> singleDataRow, String token) {

		commonInit(body, endpoint, method, singleDataRow);

		if (token != null) {

			//readTknFromExcel(singleDataRow, token);
		}
	}

	private void commonInit(String body, String endpoint, String method, Map<String, String> singleDataRow) {

		if (singleDataRow.get("endpoint") != null && !singleDataRow.get("endpoint").isEmpty())
			endpoint = singleDataRow.get("endpoint");

		if (singleDataRow.get("method") != null && !singleDataRow.get("method").isEmpty())
			this.method = singleDataRow.get("method");

		if (singleDataRow.get("contentType") != null && !singleDataRow.get("contentType").isEmpty())
			this.contentType = singleDataRow.get("contentType");
		else
			this.contentType = "application/json";

		//this.url = APIConstants.BASE_URL + endpoint;

		if (body != null)
			builder.setBody(body);

	}

	/**
	 * APIFunction is constructor works for get / delete / logout
	 * 
	 * @param token
	 * @param uri
	 * @param method
	 */
	public APIRequestBuilder(String token, String uri, String method) {

		reuse(uri, method);

		if (token != null) {
			builder.addHeader("Authorization", "Bearer " + token);
		}

	}

	/**
	 * APIFunction is constructor works for post / put / login
	 * 
	 * @param token
	 * @param uri
	 * @param method
	 * @param body
	 */

	public APIRequestBuilder(String token, String uri, String method, String body) {

		reuse(uri, method);
		if (body != null) {
			builder.setBody(body);
		}
		if (token != null) {
			builder.addHeader("Authorization", "Bearer " + token);

		}
	}

	/**
	 * APIFunction for patient with following parameter
	 * 
	 * @param uri
	 * @param method
	 * @param token
	 * @param patientDataFieldsVo
	 */

	/*public APIRequestBuilder(String endpoint, String method, String token,
			dieticianPojo.PatientDataFieldsVo patientDataFieldsVo, Map<String, String> singleDataRow) {

		commonInit(endpoint, method, singleDataRow);

		builder.addMultiPart("patientInfo", patientDataFieldsVo.getPatientInfo());

		if (patientDataFieldsVo.getVitals() != null)
			builder.addMultiPart("vitals", patientDataFieldsVo.getVitals());

		if (patientDataFieldsVo.getReportFile() != null)
			builder.addMultiPart("file", patientDataFieldsVo.getReportFile(), "application/pdf");

		if (token != null) {

			readTknFromExcel(singleDataRow, token);
		}
	}*/

	// Read token from excel
	/*private void readTknFromExcel(Map<String, String> singleDataRow, String token) {
		token = singleDataRow.get("TokenName");
		if (token.equalsIgnoreCase("AdminToken")) {
			builder.addHeader("Authorization", "Bearer " + Tokens.TokenMap.get("AdminToken"));
		} else if (token.equalsIgnoreCase("PatientToken")) {
			builder.addHeader("Authorization", "Bearer " + Tokens.TokenMap.get("patBearerToken"));
		} else if (token.equalsIgnoreCase("DieticianToken")) {
			builder.addHeader("Authorization", "Bearer " + Tokens.TokenMap.get("dietBearerToken"));
		} else if (token.equalsIgnoreCase("No Auth")) {
			builder.addHeader("Authorization", " ");
		}
	} */

	private void commonInit(String endpoint, String method, Map<String, String> singleDataRow) {
		if (singleDataRow.get("endpoint") != null && !singleDataRow.get("endpoint").isEmpty())
			endpoint = singleDataRow.get("endpoint");

		if (singleDataRow.get("method") != null && !singleDataRow.get("method").isEmpty())
			this.method = singleDataRow.get("method");

		if (singleDataRow.get("contentType") != null && !singleDataRow.get("contentType").isEmpty())
			this.contentType = singleDataRow.get("contentType");
		else
			this.contentType = "multipart/form-data";

		//this.url = APIConstant.BASE_URL + endpoint;

	}

	private void reuse(String uri, String method) {

	//	this.url = APIConstant.BASE_URL + uri;
		this.method = method;

	}

	/**
	 * ExecuteAPI to execute the API for POST,GET,DELETE,PUT
	 * 
	 * @return ResponseOptions<Response>
	 */
	/*public Response ExecuteAPI() {

		RequestSpecification requestSpecification = builder.build();
		RequestSpecification request = RestAssured.given();

		if (contentType == null || contentType.isBlank())
			contentType = "application/json";

		request.contentType(contentType);

		request.spec(requestSpecification);

		if (this.method.equalsIgnoreCase(APIConstant.POST))
			response = request.post(this.url);
		else if (this.method.equalsIgnoreCase(APIConstant.DELETE))
			response = request.delete(this.url);
		else if (this.method.equalsIgnoreCase(APIConstant.GET))
			response = request.get(this.url);
		else if (this.method.equalsIgnoreCase(APIConstant.PUT))
			response = request.put(this.url);

		return response;
	} */

	/**
	 * Executing API with query params being passed as the input of it
	 * 
	 * @param queryPath
	 * @return ResponseOptions<Response>
	 */
	/*public ResponseOptions<Response> ExecuteWithQueryParam(Map<String, String> queryPath) {
		builder.addQueryParams(queryPath);
		return ExecuteAPI();
	}*/
}
