package testCase;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class UpdateOneProductAdvanced {

	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> updatePayload;
	String updateProductId;

	public UpdateOneProductAdvanced() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadPath = "src\\main\\java\\data\\CreatePayload.json";
		createPayload = new HashMap<String, String>();
		updatePayload = new HashMap<String, String>();
	}

	public Map<String, String> createPayloadMap() {
		createPayload.put("name", "Amazing Headset 1.0 By MD");
		createPayload.put("description", "The Fine Headset for amazing programmers.");
		createPayload.put("price", "199");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		return createPayload;
	}

	public Map<String, String> updatePayloadMap() {
		updatePayload.put("id", updateProductId);
		updatePayload.put("name", "Updated Headset 7.0 By MD");
		updatePayload.put("description", "The Super Headset for amazing programmers.");
		updatePayload.put("price", "899");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		return updatePayload;
	}

	@Test(priority = 1)
	public void createOneProduct() {
		Response response = given().baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth()
				.preemptive().basic("demo@techfios.com", "abc123")
				// .body(new File(createPayloadPath)).
				.body(createPayloadMap()).when().post("/create.php").then().extract().response();

		int responseStatusCode = response.getStatusCode();
		softAssert.assertEquals(responseStatusCode, 201, "Response Status Codes are not matching!");
		System.out.println("Response Status Code:" + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8",
				"Response Header Content Types are not matching!");
		System.out.println("Response Header ContentType:" + responseHeaderContentType);

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product Message is not matching!");
		System.out.println("Product Message:" + productMessage);
		softAssert.assertAll();

	}

	@Test(priority = 2)
	public void readAllProducts() {
		Response response = given().baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth()
				.preemptive().basic("demo@techfios.com", "abc123").when().get("/read.php").then().extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		firstProductId = jp.getString("records[0].id");
		System.out.println("First Product Id:" + firstProductId);
		updateProductId = firstProductId;
	}

	@Test(priority = 3)
	public void uapdateOneProduct() {
		Response response = given().baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth()
				.preemptive().basic("demo@techfios.com", "abc123")
				// .body(new File(createPayloadPath)).
				.body(updatePayloadMap()).when().put("/update.php").then().extract().response();

		int responseStatusCode = response.getStatusCode();
		softAssert.assertEquals(responseStatusCode, 200, "Response Status Codes are not matching!");
		System.out.println("Response Status Code:" + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8",
				"Response Header Content Types are not matching!");
		System.out.println("Response Header ContentType:" + responseHeaderContentType);

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage, "Product was updated.", "Product Message is not matching!");
		System.out.println("Product Message:" + productMessage);
		softAssert.assertAll();

	}

	@Test(priority = 4)
	public void readOneUpdatedProduct() {
		Response response = given().baseUri(baseURI).header("Content-Type", "application/json").auth().preemptive()
				.basic("demo@techfios.com", "abc123").queryParam("id", updatePayloadMap().get("id")).when()
				.get("/read_one.php").then().extract().response();

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualProductName = jp.getString("name");
		String expectedProductName = updatePayloadMap().get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
		System.out.println("Actual Product Name:" + actualProductName);

		String actualProductDescription = jp.getString("description");
		String expectedProductDescription = updatePayloadMap().get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription,
				"Product Description is not matching!");
		System.out.println("Actual Product Description:" + actualProductDescription);

		String actualProductPrice = jp.getString("price");
		String expectedProductPrice = updatePayloadMap().get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product Price is not matching!");
		System.out.println("Actual Product Price:" + actualProductPrice);
		softAssert.assertAll();

	}
}