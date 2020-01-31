package steps;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class BaseClass {
	

	   @BeforeSuite(alwaysRun=true)
	    public static void setUp(){

	        baseURI = "http://data.fixer.io";
	        basePath = "/api";

	    }
	   
	   public RequestSpecification getRequestSpecification() {
		   return RestAssured.given().contentType(ContentType.JSON);
	   }
	   
	   public Response getResponse(RequestSpecification requestSpecification,String endpoint,int status) {
		   Response response = requestSpecification.get(endpoint);
		   Assert.assertEquals(response.getStatusCode(), status);
		   response.then().log().all();
		   return response;
		 
		   
	   }
	   
	   public static  void writeToCsv(List<String> data,String fileheader) {
		   String user_dir = (System.getProperty("user.dir")+"\\src\\test\\java\\csv_files\\");

	        try {
	            Iterator<String> iter = data.iterator();
	            String file_to_write = System.getProperty("user.home").toString();
	            System.out.println(file_to_write);
	            FileWriter fw = new FileWriter(user_dir+fileheader+".csv");
	         
	            while (iter.hasNext()) {
	                String entry = iter.next();
	                try {
	                    fw.append(entry);
	                    fw.append('\n');
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                } finally {
	                	
	                    iter.remove();
	                }
	            }
	            fw.flush();
	            fw.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	   }
}
