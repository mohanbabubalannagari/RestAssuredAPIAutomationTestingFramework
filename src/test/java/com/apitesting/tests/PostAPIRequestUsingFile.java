package com.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.apitesting.utils.BaseTest;
import com.apitesting.utils.FileNameConstants;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PostAPIRequestUsingFile extends BaseTest{

	@Test
	public void postAPIrequestByFile() throws IOException
	{
		try
		{
			String postAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");

			Response response=
		    RestAssured
			.given()
			.contentType(ContentType.JSON)
			.body(postAPIrequestbody)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.post()
			.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();
			
			JSONArray jsonArray=
					JsonPath.read(response.body().asString(),"$.booking..firstname");
			String fname=(String)jsonArray.get(0);
			
			Assert.assertEquals(fname, "testers");
			
			int b_id=JsonPath.read(response.body().asString(),"$.bookingid");
			
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(201);			

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
