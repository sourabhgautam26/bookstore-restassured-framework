package com.bookstore.api.tests;

import com.bookstore.api.utils.Endpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseTest {

    @Test(priority = 0, description = "Validate that the API health check endpoint returns 'up'")
    public void validateHealthCheckEndpoint() {
        Reporter.log("Executing Health Check API: " + Endpoints.HEALTH_CHECK, true);

        Response response = executeRequest("GET", Endpoints.HEALTH_CHECK, null, null);
        response.then().statusCode(200); // Ensure status code


        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "up", "Expected API health status to be 'up'");
    }
}
