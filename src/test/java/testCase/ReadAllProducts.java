package testCase;

//import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*01. ReadAllProducts
		http method=GET 
		EndPointUrl=https://techfios.com/api-prod/api/product/read.php
		Authorization:(basic auth)
		username=demo@techfios.com
		password=abc123
		Header/s:
		Content-Type=application/json; charset=UTF-8
		http status code=200
		responseTime= <=1500ms
		
	given()= all input details= (baseUri,header/s,authorization,queryParams,Payload/Body)
	when()submit request= httpMethod(endPoint)
	then()response validation (statusCode , header/s, responseTime, response Payload/Body)
 */

public class ReadAllProducts {

	@Test
	public void readAllProducts() {

		Response response =

				given().baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
						.basic("demo@techfios.com", "abc123")
						.log().all(). // this will show the result in console.
						when().get("/read.php").then().extract().response();

		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time " + responseTime);

		if (responseTime <= 2500) {
			System.out.println("Response time is within the limit");
		} else
			System.out.println("Response time is out of the limit");

		int responseStatusCode = response.getStatusCode();
		System.out.println("Response Code " + responseStatusCode);
		Assert.assertEquals(responseStatusCode, 200, "Status code does not match");

		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response header " + responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8", "Header does not match");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body " + responseBody);

		// creating and object to get the JSON PATH
		JsonPath jp = new JsonPath(responseBody);
		String firstProductId = jp.getString("records[0].id");
		System.out.println("First Product ID " + firstProductId);

		if (firstProductId != null) {
			System.out.println("Products list is not empty");
		} else {
			System.out.println("***Products list is empty ***");
		}

	}

}
