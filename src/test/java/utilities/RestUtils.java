package utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

 
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pojos.Login;
public class RestUtils {

	public static RequestSpecification req;
	 
	 
	public static  RequestSpecification CommonSpec(Login login ) throws FileNotFoundException {
		
		
		if(req==null)
		{
		PrintStream log=new PrintStream (new FileOutputStream("logging.txt"));
		 req=new RequestSpecBuilder().setBaseUri(APIConstants.BASE_URL)
				 .addFilter(RequestLoggingFilter.logRequestTo(log))
				 .addFilter(ResponseLoggingFilter.logResponseTo(log))
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON).build();
		 return req;
		}
		return req;
	}
		
		 
//		PrintStream log=new PrintStream (new FileOutputStream("logging.txt"));
//		 RequestSpecBuilder reqBuilder1 = new RequestSpecBuilder();
//		 reqBuilder1.setBaseUri(BaseURL)
//		 			.addFilter(RequestLoggingFilter.logRequestTo(log))
//		 			.addFilter(ResponseLoggingFilter.logResponseTo(log));
//		 reqBuilder1.setContentType(login.getContentType());
//		 reqBuilder1.setAccept(ContentType.JSON);
//		 requestSpec = reqBuilder1.build();
//		   return requestSpec;
//	 }
	
}
