package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.testautomation.apitesting.utils.FileNameConstants;

public class CreateDynamicTestNGSuiteFile {

	public static void main(String[] args) {
		
		//create testng object
		TestNG testNG = new TestNG();
		
		//create suite object
		XmlSuite xmlSuite = new XmlSuite();
		xmlSuite.setName("Suite1");
		xmlSuite.setParallel(XmlSuite.ParallelMode.METHODS);
		xmlSuite.setThreadCount(5);
		xmlSuite.setVerbose(2);
		
		//create test object
		XmlTest xmlTest = new XmlTest(xmlSuite);
		xmlTest.setName("TestName");
		xmlTest.setPreserveOrder(true);
		
		//create class object
		XmlClass xmlClass = new XmlClass("com.testautomation.apitesting.tests.TestRetry");
		
		// add all test methods
		List<XmlInclude> allMethods = new ArrayList<XmlInclude>();
		
		/*
		 * allMethods.add(new XmlInclude("e2eAPIRequest")); allMethods.add(new
		 * XmlInclude("e2eAPIRequest2")); allMethods.add(new
		 * XmlInclude("e2eAPIRequest3"));
		 */
		
		Fillo fillo = new Fillo();
		Recordset recordset =null;
		Connection connection=null;
		
		try {
			connection = fillo.getConnection(FileNameConstants.TEST_RUNNER);
			String query = "select * from Sheet1";
			recordset = connection.executeQuery(query);
			
			while(recordset.next()) {
				if(recordset.getField("Run").equals("Yes")) {
					allMethods.add(new XmlInclude(recordset.getField("TestMethod")));
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		connection.close();
		
		
		xmlClass.setIncludedMethods(allMethods); //tagging to class
		
		xmlTest.getClasses().add(xmlClass);
		
		//add testng suite files if multiple
		List<XmlSuite> suiteList = new ArrayList<XmlSuite>();
		suiteList.add(xmlSuite);
		
		testNG.setXmlSuites(suiteList);
		
		//run testng suite
		testNG.run();
		
		//generate testng suite file at run time
		
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File("runtimetestngsuitefile.xml"));
			fileWriter.write(xmlSuite.toXml());
			fileWriter.flush();
			fileWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}
