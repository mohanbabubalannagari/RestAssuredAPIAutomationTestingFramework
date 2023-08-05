package com.apitesting.tests;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import com.apitesting.listener.RestAssuredListener;
import com.apitesting.pojos.Booking;
import com.apitesting.pojos.BookingDates;
import com.apitesting.utils.FileNameConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataDrivenTestingUsingCSVFile 
{
	@Test(dataProvider="CSVTestData")
	public void DataDrivenTestingUsingCSV(Map<String,String> testData) throws JsonProcessingException
	{
		int totalprice=Integer.parseInt(testData.get("totalprice"));
		
		BookingDates bookingDates = 
				new BookingDates("2023-03-25", "2023-03-30");
		Booking booking = 
				new Booking(testData.get("firstname"), testData.get("lastname"), "breakfast", totalprice, true, bookingDates);

		//serialization
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody 
		= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

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

	@DataProvider(name="CSVTestData")
	public Object[][] getTestData() throws Exception, IOException
	{
		Object[][] objArray=null;

		Map<String,String> map=null;

		List<Map<String,String>> testDataList=null;

		try 
		{
			CSVReader csvreader=new CSVReader(new FileReader(FileNameConstants.CSV_TEST_DATA));

			testDataList=new ArrayList<Map<String,String>>();

			String[] line=null;

			int count=0;
			
			
			
			while((line=csvreader.readNext())!=null)
			{
				if(count==0)
				{
					count++;
					continue;
				}
				
				map=new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
				map.put("firstname",line[0]);
				map.put("lastname",line[1]);
				map.put("totalprice",line[2]);

				testDataList.add(map);
			}

			objArray=new Object[testDataList.size()][1];

			for (int i = 0; i < testDataList.size(); i++) 
			{
				objArray[i][0]=testDataList.get(i);
			}

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return objArray;

	}
}
