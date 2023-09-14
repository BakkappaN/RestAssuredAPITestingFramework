package com.testautomation.apitesting.tests;

import org.testng.annotations.Test;

import com.testautomation.apitesting.utils.RestAPIHelper;

import io.restassured.RestAssured;

public class Cookies {
	
  @Test
  public void cookiesAPITest() {
	  
	  RestAssured
	  		.given()
	  			.cookies(RestAPIHelper.getCookiesData())
	  			.baseUri("http://postman-echo.com/cookies/set")
	  			.log().all()
	  		.when()
	  			.get()
	  		.then()
	  			.assertThat()
	  			.statusCode(200);
	  
  }
}
