package com.apitesting.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apitesting.listener.RestAssuredListener;
import com.apitesting.pojos.Booking;
import com.apitesting.pojos.BookingDates;
import com.apitesting.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DataDrivenTestingUsingExcel {

	@Test(dataProvider="ExcelTestData")
	public void DataDrivenTestingExcel(Map<String,String> testData)
	{
		int totalprice = Integer.parseInt(testData.get("TotalPrice"));
		BookingDates bookingDates = new BookingDates("2023-03-25", "2023-03-30");
		Booking booking = new Booking(testData.get("FirstName"), testData.get("LastName"), "breakfast", totalprice, true, bookingDates);

		//serialization
		ObjectMapper objectMapper = new ObjectMapper();
		try 
		{
			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
			Response response =
					RestAssured
					.given().filter(new RestAssuredListener())
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
		} 
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	@DataProvider(name="ExcelTestData")
	public Object[][] getTestData()
	{
		String query="select * from Sheet1 where Run='yes'";

		Object[][] objArray=null;
		Map<String,String> testData=null;
		List<Map<String,String>> testDataList=null;

		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;		

		try 
		{
			connection=fillo.getConnection(FileNameConstants.EXCEL_TEST_DATA);
			recordset=connection.executeQuery(query);

			testDataList=new ArrayList<Map<String,String>>();

			while(recordset.next())
			{
				testData=new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);

				for(String field:recordset.getFieldNames())
				{
					testData.put(field,recordset.getField(field));	
				}

				testDataList.add(testData);
			}

			objArray=new Object[testDataList.size()][1];

			for (int i = 0; i < objArray.length; i++) 
			{
				objArray[i][0]=testDataList.get(i);
			}
		} 
		catch (FilloException e) {
			e.printStackTrace();
		}

		return objArray;

	}
}
