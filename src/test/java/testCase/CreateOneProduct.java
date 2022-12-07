package testCase;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*03.CreateOneProduct
		http method=POST 
		EndPointUrl=https://techfios.com/api-prod/api/product/create.php
		Authorization:(basic auth)
		username=demo@techfios.com
		password=abc123
		Header/s:
		Content-Type=application/json; charset=UTF-8
		http status code=201
		responseTime= <=1500ms
		Payload/Body: 
		  
			{
			    "name": "Amazing Headset 1.0 By MD",
			    "description": "The best Headset for amazing programmers.",
			    "price": "199",
			    "category_id": "2",
			    "category_name": "Electronics"
			}
 */

	public class CreateOneProduct {

	String baseURI;
	SoftAssert softAssert;
	String createPayLoadPath;
	HashMap<String,String> createPayload;
	
	
	public CreateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayLoadPath = "src/main/java/data/CreatePayload.json";
		createPayload = new HashMap<String,String>();
	}
	
	public Map<String,String> createPayloadMap(){
		createPayload.put("name", "Amazing Headset 3.0 By Hemi");
		createPayload.put("description", "The best Headset for amazing programmers.");
		createPayload.put("price", "199");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
		
	}

	@Test(priority=1)
	public void createOneProduct() {

		System.out.println("Create Payload Map: "+ createPayloadMap());
		Response response = 
				
			given()
//				.log().all()	
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.body(new File (createPayLoadPath)). //data is from attached json file
//				.body(createPayloadMap()). //data is from createPayloadMap method see line 52-62
			when()
				.post("/create.php").
			then()
//				.log().all()
				.extract().response();
		
		int responseStatusCode = response.getStatusCode();
//		Assert.assertEquals(responseStatusCode, 201);
		softAssert.assertEquals(responseStatusCode, 201 , "Response Status codes are not matching! ");
		System.out.println("Response Status Code" + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
//		Assert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8" , "Response Header codes are not matching! ");
		System.out.println("Response header ContentType" + responseHeaderContentType);

		String responseBody = response.getBody().asString();
		System.out.println("Response Body" + responseBody);

		/*
		 * {
			    "message": "Product was created."
			}
		 */
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
//		Assert.assertEquals(productMessage, " "message": "Product was created.");
		softAssert.assertEquals(productMessage,  "Product was created.", "Product Message are not matching! ");
		System.out.println("Product Message " + productMessage);

		softAssert.assertAll();

	}
}


