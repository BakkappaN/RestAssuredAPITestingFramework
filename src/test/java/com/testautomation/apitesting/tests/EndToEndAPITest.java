package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.listener.RestAssuredListener;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class EndToEndAPITest extends BaseTest {

	private static final Logger logger = LogManager.getLogger(EndToEndAPITest.class);

	@Test
	public void e2eAPIRequest() {

		logger.info("e2eAPIRequest test execution started...");

		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
					"UTF-8");
			String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
					"UTF-8");
			String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
					"UTF-8");
			String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
					"UTF-8");

			// post api call
			Response response = RestAssured
					.given()
						.filter(new RestAssuredListener())
						.contentType(ContentType.JSON).body(postAPIRequestBody)
						.baseUri("https://restful-booker.herokuapp.com/booking")
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();

			JSONArray jsonArray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String firstName = (String) jsonArray.get(0);

			Assert.assertEquals(firstName, "api testing");

			int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
			System.out.println("Booking Id : " + bookingId);

			// get api call
			RestAssured
				.given()
					.filter(new RestAssuredListener())
					.contentType(ContentType.JSON)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("/{bookingId}", bookingId)
				.then()
					.assertThat()
					.statusCode(200);

			// token generation
			Response tokenAPIResponse = RestAssured
					.given()
						.filter(new RestAssuredListener())
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

			String token = JsonPath.read(tokenAPIResponse.body().asString(), "$.token");
			System.out.println("Token Id : " + token);

			// put api call
			RestAssured
			.given()
				.filter(new RestAssuredListener())
				.contentType(ContentType.JSON)
				.body(putAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.put("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200).body("firstname", equalTo("Specflow"))
				.body("lastname", equalTo("Selenium C#"));

			// patch api call
			RestAssured
			.given()
				.filter(new RestAssuredListener())
				.contentType(ContentType.JSON)
				.body(patchAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.patch("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", equalTo("Testers Talk"));

			// delete api call
			RestAssured
			.given()
				.filter(new RestAssuredListener())
				.contentType(ContentType.JSON)
				.header("Cookie", "token=" + token)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.delete("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(201);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("e2eAPIRequest test execution ended...");

	}

}
