package com.apitesting.tests;

import com.apitesting.pojos.BookingDates;
import com.apitesting.pojos.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import com.apitesting.utils.BaseTest;
import com.apitesting.utils.FileNameConstants;


public class PostAPIRequestUsingPojos extends BaseTest{

	@Test
	public void postAPIrequestByPojos() throws IOException
	{
		try {
			String jsonSchema
			=FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA),"UTF-8");
		    
			BookingDates bookingDates=new BookingDates("2023-03-25","2023-12-25");
			Booking booking=new Booking("testers","talk","super bowls",1000,true,bookingDates);

			ObjectMapper objectMapper=new ObjectMapper();
			String requestBody=
					objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
			System.out.println(requestBody);

			//de-serialization
			Booking bookingDetails=objectMapper.readValue(requestBody, Booking.class);
			System.out.println(bookingDetails.getFirstname());		
			System.out.println(bookingDetails.getTotalprice());
			System.out.println(bookingDetails.getBookingdates().getCheckin());		
			System.out.println(bookingDetails.getBookingdates().getCheckout());	

			Response response=RestAssured
					.given()
					.contentType(ContentType.JSON)
					.body(requestBody)
					.baseUri("https://restful-booker.herokuapp.com/booking")
					.when()
					.post()
					.then()
					.assertThat()
					.statusCode(200)
					.extract()
					.response();

			int b_id=response.path("bookingid");

			System.out.println(jsonSchema);
			
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
		} 
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
