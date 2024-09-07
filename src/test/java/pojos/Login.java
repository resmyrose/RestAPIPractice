package pojos;

import lombok.Data;

@Data
public class Login {
	private String username;
	private String password;
	private String Scenario;
	private String endpoint;
	private String contentType;
	private String method;
	private String expectedStatusCode;
	private String TokenName;
}
