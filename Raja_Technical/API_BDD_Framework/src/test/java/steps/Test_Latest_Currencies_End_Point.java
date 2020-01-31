package steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.github.fge.jsonschema.core.report.LogLevel;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class Test_Latest_Currencies_End_Point extends BaseClass {
	
	public static Response _response;
	public static RequestSpecification _request;
    //public static Map<String,String> headers ;
	public static  String api_key_parameter_name="access_key";
	public static  String api_key_parameter_value="f465fa90d65f392fd6c2c15b1ab49f91";
	public static  String symbols_parameter_name="symbols";
	public static String symbols_parameter_value;
	public static  String latest_currencies_end_point="/latest";
	 public ExtentHtmlReporter htmlReporter;
	 public ExtentReports extent;
	 public ExtentTest test;


	@BeforeTest
	public void startTest() {
		// specify location of the report
		  htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/extent/myReport.html");
		  htmlReporter.start();
		  htmlReporter.config().setDocumentTitle("Automation Report"); // Tile of report
		  htmlReporter.config().setReportName("API testing"); // Name of the report
		  htmlReporter.config().setTheme(Theme.DARK);
		  
		  extent = new ExtentReports();
		  extent.attachReporter(htmlReporter);
		  
		  

		  
		  // Passing General information
		  extent.setSystemInfo("Host name", "localhost");
		  extent.setSystemInfo("Environemnt", "QA");
		  extent.setSystemInfo("user", "Raja");
		  
		  
		  test = extent.createTest(this.getClass().getSimpleName());

	}
	
	 @AfterTest
	 public void endReport() {
	  extent.flush();
	 }

	@Test
	public void test_Write_files_to_CSV() throws JSONException {
		
		
		test.log(Status.INFO , "The Test  has Started");
		_request = new BaseClass().getRequestSpecification();
		_request.queryParam(api_key_parameter_name, api_key_parameter_value);

		_response = new BaseClass().getResponse(_request, latest_currencies_end_point, HttpStatus.SC_OK);
		
		
		// Asserting if the response code is 200
		Assert.assertEquals(_response.getStatusCode(), 200);
		test.log(Status.PASS , "Response was successfully received ");

		JSONArray json = new JSONArray("["+_response.asString()+"]");
		
		
		
		String rates = new String();
		String timestamp = new String();
		String base_currency = new String();
		String date = new String();
		String fileheader = new String();
		
		List<String> data= new ArrayList<String>();

		
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = json.getJSONObject(i);
			timestamp = "Time_Stamp:" + obj.getString("timestamp");
			fileheader="_"+obj.getString("date")+"_"+obj.getString("timestamp");
			base_currency = "base currency " + obj.getString("base");
			date =  "Date:" + obj.getString("date");
			rates = obj.getString("rates").replace("{", "").replace("}", "");
		}
		
		fileheader="10_currency_"+fileheader;
		
		List<String> list = Arrays.asList(rates.split(","));

		
		test.log(Status.INFO , "Currency rates  was successfully extracted ");
		
		data.add(date);
		data.add(timestamp);
		data.add(base_currency);
		
		for(int i=0;i<10;i++) {

	        Random rand = new Random(); 
	        String temp = list.get(rand.nextInt(list.size()));
	        data.add(temp);

		}
		
		test.log(Status.INFO , "From the List of Currencies we have chose 10 random currencies successfully ");
		
		writeToCsv(data,fileheader);
		 
		test.log(Status.PASS , "CSV files was created successfullly"+fileheader);
	
		
	}
	
	
}
