package com.testautomation.apitesting.tests;

import org.testng.annotations.Test;

import com.testautomation.apitesting.utils.RestAPIHelper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class BasicAuth {
	
  @Test
  public void basicAuthAPITest() {
	  
	  RestAssured
	  		.given()
	  			.spec(RestAPIHelper.getBasicAuth())
	  			.contentType(ContentType.JSON)
	  			.baseUri("http://postman-echo.com/basic-auth")
	  		.when()
	  			.get()
	  		.then()
	  			.assertThat()
	  			.statusCode(200);
	  
  }
  
}
