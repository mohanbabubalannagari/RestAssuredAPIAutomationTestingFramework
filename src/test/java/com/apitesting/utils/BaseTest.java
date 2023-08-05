package com.apitesting.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.apitesting.tests.DeleteAPIRequest;

import java.io.StringWriter;
import java.io.PrintWriter;
import io.restassured.RestAssured;

public class BaseTest {
	
	private static final Logger logger=LogManager.getLogger(BaseTest.class);

	
	@BeforeMethod
	public void baseMethod()
	{
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result)
	{
        if(result.getStatus()==ITestResult.FAILURE)
        {
        	
        	StringWriter error=new StringWriter();
        	PrintWriter errormsg=new PrintWriter(error);

        	Throwable t=result.getThrowable();
        	t.printStackTrace(errormsg);
        	
        	logger.info(error.toString());
        }
        else
        {
        	
        }
	}

}
