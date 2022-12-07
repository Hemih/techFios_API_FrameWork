package testCase;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*05.DeleteOneProduct
		http method=DELETE 
		EndPointUrl=https://techfios.com/api-prod/api/product/delete.php
		Authorization:(basic auth)
		username=demo@techfios.com
		password=abc123
		Header/s:
		Content-Type=application/json; charset=UTF-8
		http status code=200
		responseTime= <=1500ms
		Payload/Body: 
		  
			{
			    "id": "6056"
			}
 */

public class DeleteAProduct {

	SoftAssert softAssert = new SoftAssert();

	ReadOneProduct readAproduct = new ReadOneProduct();

	@Test
	public void delete_A_Product() {

//			https://techfios.com/api-prod/api/product/delete.php

		HashMap<String, String> payload = new HashMap<String, String>();

		payload.put("id", "6056");

		Response response =

				given().baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json; charset=UTF-8").body(payload)

						.when().delete("/delete.php")

						.then().extract().response();

//			String responseBody = response.getBody().asString();
		String responseBody = response.getBody().prettyPrint();
		System.out.println("ResponseBody: " + responseBody);

		String responseHeader = response.header("Content-Type");
		System.out.println("Print ResponseHeader Type : " + responseHeader);

		// Parsing responseBody to Json:
		JsonPath js = new JsonPath(responseBody);

		String message = js.getString("message");
		// Assert.assertEquals(message, "Product was deleted.");
		softAssert.assertEquals(message, "Product was deleted.", "Not Matching the Assertions!!!");

//			readAproduct.read_A_Product("1630");

		int statusCode = response.getStatusCode();
		System.out.println("Print Status Code  " + statusCode);

		// Assert.assertEquals(statusCode, 200); //hardAssert
		softAssert.assertEquals(statusCode, 200); // softAssert

		softAssert.assertAll();

		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time " + responseTime);

		if (responseTime <= 2000) {
			System.out.println("Response Time Is Within The Time");

		} else {
			System.out.println("Not Acceptable!!!");

		}

	}
}
