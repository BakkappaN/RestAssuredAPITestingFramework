package com.testautomation.apitesting.utils;

import java.util.HashMap;
import java.util.Map;

import com.testautomation.apitesting.listener.RestAssuredListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAPIHelper {

	public Response postAPIRequest(String uri, String requestBody, Header header) {
		return RestAssured.given().contentType(ContentType.JSON).body(requestBody).baseUri(uri).when().post().then()
				.assertThat().statusCode(200).extract().response();
	}

	public Response getAPIRequest(String uri, String requestBody, Header header) {
		return RestAssured.given().contentType(ContentType.JSON).baseUri(uri).when().get().then().assertThat()
				.statusCode(200).extract().response();
	}

	public Response putAPIRequest(String uri, String requestBody, Header header) {
		return RestAssured.given().contentType(ContentType.JSON).body(requestBody).header(header).baseUri(uri).when()
				.put().then().assertThat().statusCode(200).extract().response();
	}

	public Response patchAPIRequest(String uri, String requestBody, Header header) {
		return RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON).body(requestBody)
				.header(header).baseUri(uri).when().patch().then().assertThat().statusCode(200).extract().response();
	}

	public Response deleteAPIRequest(String uri, String requestBody, Header header) {
		return RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON).header(header)
				.baseUri(uri).when().delete().then().extract().response();
	}
	
	public static String prepareAPIRequestDynamically(String request,String ... args) {
		for (int i = 0; i < args.length; i++) {
			request = request.replace("#"+i+"#", args[i]);
		}
		return request;
	}
	
	public static Map getCookiesData() {
		Map<String,String> cookie = new HashMap<String,String>();
		cookie.put("skill1", "rest assured by testers talk");
		cookie.put("skill2", "postman by testers talk");
		cookie.put("skill3", "specflow by testers talk");
		return cookie;
	}
	
	public static RequestSpecification getBasicAuth() {
		return RestAssured.given().auth().basic("postman", "password");
	}
	
}





























