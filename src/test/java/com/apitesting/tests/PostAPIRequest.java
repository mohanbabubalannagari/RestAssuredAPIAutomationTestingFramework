package com.apitesting.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.apitesting.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class PostAPIRequest extends BaseTest
{
	@Test
	public void createBooking()
	{
		//prepare request body using json object - 1 way

		JSONObject bookingDetails=new JSONObject();
		JSONObject bookingDates=new JSONObject();

		bookingDetails.put("firstname", "testers");
		bookingDetails.put("lastname", "talk");
		bookingDetails.put("totalprice", 1000);
		bookingDetails.put("depositpaid", true);
		bookingDetails.put("additionalneeds", "breakfast");

		bookingDetails.put("bookingdates", bookingDates);

		bookingDates.put("checkin", "2018-01-01");
		bookingDates.put("checkout", "2019-01-01");

		Response response=
				RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body(bookingDetails.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().all()
				.when()
				.post()
				.then()
				.assertThat()
				//.log().ifValidationFails()
				.statusCode(200)
				.body("booking.firstname", Matchers.equalTo("testers"))
				.body("booking.lastname", Matchers.equalTo("talk"))
				.body("booking.totalprice", Matchers.equalTo(1000))
				.body("booking.bookingdates.checkin", Matchers.equalTo("2018-01-01"))
				.extract()
				.response();

		int bookingId=response.path("bookingid");


		RestAssured
		.given()
		.contentType(ContentType.JSON)
		.pathParam("b_id", bookingId)
		.baseUri("https://restful-booker.herokuapp.com/booking")
		.when()
		.get("{b_id}")
		.then()
		.assertThat()
		.statusCode(200)
		.body("firstname", Matchers.equalTo("testers"))
		.body("lastname", Matchers.equalTo("talk"))
		.log().all();
		
	}
}
