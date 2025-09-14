package com.bookstore.api.tests;

import com.bookstore.api.factory.DataFactory;
import com.bookstore.api.payloads.UserPayload;
import com.bookstore.api.utils.ConfigReader;
import com.bookstore.api.utils.Endpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected RequestSpecification requestSpec;
    protected String token;
    protected UserPayload testUser;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        // 🔹 Build reusable request spec
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("url"))
                .setContentType("application/json")
                .build();

        // 🔹 Generate test user dynamically from DataFactory
        testUser = DataFactory.getValidUser();

        // 🔹 Try Signup (skip if user already exists)
        Response signupResp = given()
                .spec(requestSpec)
                .body(testUser)
                .when()
                .post(Endpoints.SIGN_UP);

        int status = signupResp.getStatusCode();
        switch (status) {
            case 200 -> System.out.println("✅ User signed up: " + testUser.getEmail());
            case 409 -> System.out.println("ℹ️ User already exists, skipping signup");
            default -> throw new RuntimeException("❌ Unexpected signup status: " + status + "\nResponse: " + signupResp.asString());
        }

        // 🔹 Login to fetch token
        Response loginResp = given()
                .spec(requestSpec)
                .body(testUser)
                .when()
                .post(Endpoints.LOGIN);

        loginResp.then().statusCode(200);
        token = loginResp.jsonPath().getString("access_token");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("❌ Failed to generate auth token for user: " + testUser.getEmail());
        }

        System.out.println("🔑 Auth token generated successfully");
    }
}
