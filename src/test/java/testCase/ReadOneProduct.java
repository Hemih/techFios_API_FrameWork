package testCase;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*02. ReadOneProduct
		http method=GET 
		EndPointUrl=https://techfios.com/api-prod/api/product/read_one.php
		Authorization:(basic auth)
		username=demo@techfios.com
		password=abc123
		Query Parameters:
		id=6034
		Header/s:
		Content-Type=application/json
		http status code=200
		responseTime= <=1500ms
		
	given()= all input details= (baseUri,header/s,authorization,queryParams,Payload/Body)
	when()submit request= httpMethod(endPoint)
	then()response validation (statusCode , header/s, responseTime, response Payload/Body)
 */

public class ReadOneProduct {

	String baseURI;
	SoftAssert softAssert;

	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();

	}

	@Test
	public void readOneProduct() {

		Response response = 
				
			given()
//				.log().all()	
				.baseUri(baseURI)
				.header("Content-Type", "application/json")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", "6248").
			when()
				.get("/read_one.php").
			then()
//				.log().all()
				.extract().response();
		;

		response.getTimeIn(TimeUnit.MILLISECONDS);

		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time" + responseTime);
		if (responseTime <= 2500) {
			System.out.println("Response time is within Range");
		} else {
			System.out.println("Response time is out of Range");
		}
		int responseStatusCode = response.getStatusCode();
//		Assert.assertEquals(responseStatusCode, 200);
		softAssert.assertEquals(responseStatusCode, 200 , "Response Status codes are not matching! ");
		System.out.println("Response Status Code" + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
//		Assert.assertEquals(responseHeaderContentType, "application/json");
		softAssert.assertEquals(responseHeaderContentType, "application/json" , "Response Header codes are not matching! ");
		System.out.println("Response header ContentType" + responseHeaderContentType);

		String responseBody = response.getBody().asString();
		System.out.println("Response Body" + responseBody);

		/*
		 {
			    "id": "6248",
			    "name": "Amazing Headset 1.0 By Hemi",
			    "description": "The best Headset for amazing programmers.",
			    "price": "199",
			    "category_id": "2",
			    "category_name": "Electronics"
			}
		 */
		
		
		// creating and object to get the JSON PATH
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productName = jp.getString("name");
//		Assert.assertEquals(productName, "Amazing Headset 1.0 By Hemi");
		softAssert.assertEquals(productName, "Amazing Headset 1.0 By Hemi", "Product Name are not matching! ");
		System.out.println("Product Name " + productName);

		String productDescription = jp.getString("description");
//		Assert.assertEquals(productDescription, "The best Headset for amazing programmers.");
		softAssert.assertEquals(productDescription, "The best Headset for amazing programmers.", "Product Description are not matching! ");
		System.out.println("Product Description " + productDescription);
		
		String productPrice = jp.getString("price");
//		Assert.assertEquals(productPrice, "199");
		softAssert.assertEquals(productPrice, "199", "Product Price  are not matching! ");
		System.out.println("Product Price " + productPrice);
		
		softAssert.assertAll();

	}
}
