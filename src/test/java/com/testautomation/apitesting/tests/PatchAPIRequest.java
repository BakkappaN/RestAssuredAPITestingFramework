package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PatchAPIRequest {
	
	@Test
	public void patchAPIRequest() {
		
		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");
		
			String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),"UTF-8");
			
			String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),"UTF-8");
			
			String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),"UTF-8");
			
			//post api call
			Response response =
			RestAssured
					.given()
						.contentType(ContentType.JSON)
						.body(postAPIRequestBody)
						.baseUri("https://restful-booker.herokuapp.com/booking")
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();
			
		JSONArray jsonArray = JsonPath.read(response.body().asString(),"$.booking..firstname");
		String firstName = (String) jsonArray.get(0);
		
		Assert.assertEquals(firstName, "api testing");
		
		int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");
		
		//get api call
		RestAssured
				.given()
					.contentType(ContentType.JSON)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("/{bookingId}",bookingId)
				.then()
					.assertThat()
					.statusCode(200);
		
		//token generation
		Response tokenAPIResponse =
		RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(tokenAPIRequestBody)
					.baseUri("https://restful-booker.herokuapp.com/auth")
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
				.extract()
					.response();
		
		String token = JsonPath.read(tokenAPIResponse.body().asString(),"$.token");
		
		//put api call
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(putAPIRequestBody)
				.header("Cookie", "token="+token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.put("/{bookingId}",bookingId)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("Specflow"))
				.body("lastname", Matchers.equalTo("Selenium C#"));
		
		//patch api call
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(patchAPIRequestBody)
				.header("Cookie", "token="+token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.patch("/{bookingId}",bookingId)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("Testers Talk"));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
