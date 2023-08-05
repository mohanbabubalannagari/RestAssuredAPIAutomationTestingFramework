package com.apitesting.tests;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.apitesting.utils.BaseTest;
import com.apitesting.utils.FileNameConstants;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class PatchAPIRequest extends BaseTest
{
	@Test
	public void patchAPI()
	{
		try
		{
			String postAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");

			String tokenAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),"UTF-8");

			String putAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),"UTF-8");

			String patchAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),"UTF-8");

			//post api
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


			//get api
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200);

			//token generation
			Response tokenAPIResponse
			=RestAssured
			.given()
			.contentType(ContentType.JSON)
			.body(tokenAPIrequestbody)
			.baseUri("https://restful-booker.herokuapp.com/auth")
			.when()
			.post()
			.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();

			String t_id=JsonPath.read(tokenAPIResponse.body().asString(),"$.token");

			System.out.println(t_id);

			//put api

			//esponse putAPIResponse

			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.body(putAPIrequestbody)
			.headers("cookie","token="+t_id)
			.baseUri("https://restful-booker.herokuapp.com/booking/")
			.when()
			.put("{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200)
			.body("firstname",Matchers.equalTo("Specflow"))
			.body("lastname",Matchers.equalTo("Selenium C#"));

			//patch api
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.body(patchAPIrequestbody)
			.headers("cookie","token="+t_id)
			.baseUri("https://restful-booker.herokuapp.com/booking/")
			.when()
			.patch("{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200);

			//get api
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200)
			.body("firstname", Matchers.equalTo("Testers Talk"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
