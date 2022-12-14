package appl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import appl.api.APIDetails;
import framework.util.RespBodyValidators;
import framework.util.RespValidators;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

/**
 * This is the test class having different test cases
 * @author Biswajit Sundara
 * @date 21/07/2021
 *
 */

public class SpaceXTests extends BaseTest{


	@Test
	public void TC001_validateResponseAndContent() {

		RequestSpecification spec = RestAssured.given().spec(reqspec);
		Response resp = spec.get(APIDetails.latestEndPoint);
		System.out.println("Response: "+resp.asString());
		ValidatableResponse res = resp.then().spec(resspec);

		RespValidators respVal = new RespValidators(res);
		//Validate the api response code is 200 & content type is json
		respVal.successStatus();
		respVal.contentTypeJson();

	}

	@Test
	public void TC002_validateRocketDetails() {

		RequestSpecification spec = RestAssured.given().spec(reqspec);
		Response resp = spec.get(APIDetails.latestEndPoint);
		System.out.print("Response body: "+resp.asString());

		RespBodyValidators spcVal = new RespBodyValidators(resp.asString());
		System.out.println("\nField value: "+spcVal.getStringNodeFieldValue("rocket")+" "+spcVal.toString());
		
		Assert.assertNotNull(spcVal.getStringNodeFieldValue("rocket"));
		Assert.assertTrue(spcVal.getBooleanFieldValue("success"));
//		Assert.assertNotNull(spcVal.getStringNodeFieldValue("details"));
		Assert.assertNull(spcVal.getStringNodeFieldValue("details"));

	}


	@Test 
	public void TC003_validateLaunchLandings() {

		RequestSpecification spec = RestAssured.given().spec(reqspec);
		Response resp = spec.get(APIDetails.latestEndPoint);

		RespBodyValidators spcVal = new RespBodyValidators(resp.asString());
		Assert.assertNotNull(spcVal.getStringNodeFieldValue("launchpad"));
		Assert.assertEquals(spcVal.getNumberNodeFieldValue("flight_number"), 187);
		Assert.assertEquals(spcVal.getStringNodeFieldValue("name"), "Crew-5");
		Assert.assertEquals(spcVal.getNumberFieldFromArray("cores",1,"flight"), 1);
		Assert.assertEquals(spcVal.getStringFieldFromArray("cores",1,"landing_type"), "ASDS");
		Assert.assertTrue(spcVal.getBooleanFieldFromArray("cores",1,"landing_success"));
	}
}