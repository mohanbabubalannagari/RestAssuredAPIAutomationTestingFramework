package com.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.apitesting.listener.RestAssuredListener;
import com.apitesting.utils.BaseTest;
import com.apitesting.utils.FileNameConstants;
import com.jayway.jsonpath.JsonPath;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("Epic-01")
@Feature("Create Update Delete Booking")
public class AllureReportGeneration extends BaseTest 
{
	private static final Logger logger=LogManager.getLogger(DeleteAPIRequest.class);

	@Story("Story-02")
	@Test(description="delete api testing")
	@Description("delete api testing")
	@Severity(SeverityLevel.MINOR)
	public void deleteAPI() throws IOException
	{
		logger.info("execution started");
		
			String postAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");

			String tokenAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),"UTF-8");

			String putAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),"UTF-8");

			String patchAPIrequestbody
			=FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),"UTF-8");

			//post api
			Response postAPIresponse=
					RestAssured
					.given()
					.filter(new RestAssuredListener())
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
	}
	
	@Story("Story-01")
	@Test(description="end to end api testing")
	@Description("end to end api testing")
	@Severity(SeverityLevel.CRITICAL)
	public void allureAPI()
	{
		logger.info("execution started");
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
			Response postAPIresponse=
					RestAssured
					.given()
					.filter(new AllureRestAssured())
					.filter(new RestAssuredListener())
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

			int b_id=JsonPath.read(postAPIresponse.body().asString(),"$.bookingid");
			System.out.println(b_id);

			//get api
			RestAssured
			.given()
			.filter(new AllureRestAssured())
			.filter(new RestAssuredListener())
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(400);

			//token generation
			Response tokenAPIResponse
			=RestAssured
			.given()
			.filter(new AllureRestAssured())
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
			RestAssured
			.given()
			.filter(new AllureRestAssured())
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
			.filter(new AllureRestAssured())
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
			.filter(new AllureRestAssured())
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(200)
			.body("firstname", Matchers.equalTo("Testers Talk"));

			//delete api
			RestAssured
			.given()
			.filter(new AllureRestAssured())
			.contentType(ContentType.JSON)
			.headers("cookie","token="+t_id)
			.headers("cookie","token="+t_id)			
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.delete("/{b_id}",b_id)
			.then()
			.assertThat()
			.statusCode(201);
			
			logger.info("execution ended");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
