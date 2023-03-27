package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;


public class PostAPIRequestUsingFile extends BaseTest{
	
	@Test
	public void postAPIRequest() {
		
		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");
		
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
		
		JSONArray jsonArrayLastName = JsonPath.read(response.body().asString(),"$.booking..lastname");
		String lastName = (String) jsonArrayLastName.get(0);
		
		Assert.assertEquals(lastName, "tutorial");
		
		JSONArray jsonArrayCheckin = JsonPath.read(response.body().asString(),"$.booking.bookingdates..checkin");
		String checkin = (String) jsonArrayCheckin.get(0);
		
		Assert.assertEquals(checkin, "2018-01-01");
		
		int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");
		
		RestAssured
				.given()
					.contentType(ContentType.JSON)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("/{bookingId}",bookingId)
				.then()
					.assertThat()
					.statusCode(200);
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
