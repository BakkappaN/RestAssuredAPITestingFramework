package com.testautomation.apitesting.tests;

import java.io.File;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class FileUpload {

	@Test
	public void uploadFile() {
		RestAssured
			.given()
				.multiPart(new File(
				"E:\\2023RestAssuredFullCourse\\RestAssuredTutorials\\RestAssuredAPITestingTutorial\\src\\test\\resources\\putapirequestbody.txt"))
				.baseUri("http://postman-echo.com/post")
			.when()
				.post()
			.then()
				.assertThat()
				.statusCode(200);
	}

}
