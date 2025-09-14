package com.bookstore.api.tests;

import com.bookstore.api.factory.DataFactory;
import com.bookstore.api.payloads.UserPayload;
import com.bookstore.api.utils.ConfigReader;
import com.bookstore.api.utils.Endpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected RequestSpecification requestSpec;
    protected String token;
    protected UserPayload testUser;

    // ThreadLocal to store last request & last response per test
    private static final ThreadLocal<Object> lastRequest = new ThreadLocal<>();
    private static final ThreadLocal<Response> lastResponse = new ThreadLocal<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        // Build reusable RequestSpec
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("url"))
                .setContentType(ContentType.JSON)
                .build();

        // Generate dynamic test user
        testUser = DataFactory.getValidUser();

        // Signup
        Response signupResp = executeRequest("POST", Endpoints.SIGN_UP, testUser, null);
        int signupStatus = signupResp.getStatusCode();
        if (signupStatus == 200) System.out.println("✅ User signed up: " + testUser.getEmail());
        else if (signupStatus == 409) System.out.println("ℹ️ User already exists, skipping signup");
        else throw new RuntimeException("❌ Signup failed: " + signupResp.asString());

        // Login
        Response loginResp = executeRequest("POST", Endpoints.LOGIN, testUser, null);
        loginResp.then().statusCode(200);
        token = loginResp.jsonPath().getString("access_token");
        if (token == null || token.isEmpty())
            throw new RuntimeException("❌ Failed to generate auth token");

        System.out.println("🔑 Auth token generated successfully");
    }

    /**
     * Generic method to execute HTTP requests and store last request/response
     *
     * @param method    HTTP method (GET, POST, PUT, DELETE)
     * @param endpoint  API endpoint
     * @param body      Request body (optional)
     * @param authToken Bearer token (optional)
     * @return Response object
     */
    protected Response executeRequest(String method, String endpoint, Object body, String authToken) {
        RequestSpecification spec = given().spec(requestSpec);

        // Add auth header only if token is provided
        if (authToken != null) {
            spec.header("Authorization", "Bearer " + authToken);
        }

        // Add body only if present
        if (body != null) {
            spec.body(body);
            lastRequest.set(body); // store last request for listener
        } else {
            lastRequest.set(null);
        }

        Response response;
        switch (method.toUpperCase()) {
            case "POST" -> response = spec.post(endpoint);
            case "PUT" -> response = spec.put(endpoint);
            case "GET" -> response = spec.get(endpoint);
            case "DELETE" -> response = spec.delete(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        lastResponse.set(response); // store last response for listener
        return response;
    }

    // Getter for listener to access last request & response
    public static Object getLastRequest() {
        return lastRequest.get();
    }

    public static Response getLastResponse() {
        return lastResponse.get();
    }
}
