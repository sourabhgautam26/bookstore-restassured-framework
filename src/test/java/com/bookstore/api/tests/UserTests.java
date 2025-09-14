package com.bookstore.api.tests;

import com.bookstore.api.factory.DataFactory;
import com.bookstore.api.payloads.UserPayload;
import com.bookstore.api.utils.Endpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class UserTests extends BaseTest {

    // ---------------- Helper Methods ----------------
    private Response signup(UserPayload user) {
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);
        resp.then().statusCode(200); // ✅ Ensure signup succeeded
        return resp;
    }

    private Response login(UserPayload user) {
        Response resp = executeRequest("POST", Endpoints.LOGIN, user, null);// ✅ Ensure login succeeded
        return resp;
    }

    // ---------------- Positive Tests ----------------
    @Test(priority = 1, description = "Validate new user can successfully sign up",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupPositive() {
        UserPayload user = DataFactory.getValidUser();
        Response resp = signup(user);
        Assert.assertEquals(resp.path("message"), "User created successfully");
    }

    @Test(priority = 2, description = "Validate user can login with valid credentials",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void loginPositive() {
        UserPayload user = DataFactory.getValidUser();
        signup(user);
        Response resp = login(user);
        String token = resp.path("access_token");
        resp.then().statusCode(200);
        Assert.assertNotNull(token, "Token should not be null after successful login");
    }
    // ---------------- Negative Signup Tests ----------------
    @Test(priority = 3, description = "Signup with empty email should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupEmptyEmail() {
        UserPayload user = DataFactory.getUserWithEmptyEmail();
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);
        resp.then().statusCode(400);
    }
    @Test(priority = 4, description = "Signup with empty Id should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupEmptyId() {
        UserPayload user = DataFactory.getUserWithEmptyId();
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);
        resp.then().statusCode(400);
    }

    @Test(priority = 5, description = "Signup with empty password should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupEmptyPassword() {
        UserPayload user = DataFactory.getUserWithEmptyPassword();
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);
        resp.then().statusCode(400);
    }

    @Test(priority = 6, description = "Signup with invalid email format should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupInvalidEmail() {
        UserPayload user = DataFactory.getInvalidUser();
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);
        resp.then().statusCode(400);
    }

    @Test(priority = 7, description = "Signup with existing email should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupDuplicateEmail() {
        UserPayload user = DataFactory.getValidUser();

        signup(user); // first time should work
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, user, null);

        resp.then().statusCode(400)
                .body("detail", containsString("Email already registered"));
    }
    @Test(priority = 8, description = "Signup with missing email field should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupMissingEmail() {
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, DataFactory.getUserWithMissingEmail(), null);
        resp.then().statusCode(400);
    }
    @Test(priority = 9, description = "Signup with missing Password field should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupMissingPassword() {
        Response resp = executeRequest("POST", Endpoints.SIGN_UP,  DataFactory.getUserWithMissingPassword(), null);
        resp.then().statusCode(400);
    }
    @Test(priority = 10, description = "Signup with missing Id field should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void signupMissingId() {
        Response resp = executeRequest("POST", Endpoints.SIGN_UP, DataFactory.getUserWithMissingId(), null);
        resp.then().statusCode(400);
    }

    // ---------------- Negative Login Tests ----------------
    @Test(priority = 11, description = "Login with wrong password should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void loginWrongPassword() {
        UserPayload user = DataFactory.getValidUser();
        signup(user);
        // Wrong password
        UserPayload invalidLogin = new UserPayload(user.getId(), user.getEmail(), "WrongPassword");
        Response resp = login(invalidLogin);
        resp.then().statusCode(400).body("detail", containsString("Incorrect email or password"));;
    }

    @Test(priority = 12, description = "Login with unregistered email should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void loginUnregisteredEmail() {
        UserPayload invalidUser = new UserPayload("2001", "nouser@example.com", "Password123");
        Response resp = login(invalidUser);
        resp.then().statusCode(400).body("detail", containsString("Incorrect email or password"));
    }

    @Test(priority = 13, description = "Login with empty email should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void loginEmptyEmail() {
        UserPayload user = DataFactory.getUserWithEmptyEmail();
        Response resp = login(user);
        resp.then().statusCode(400);
    }

    @Test(priority = 14, description = "Login with empty password should return 400",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void loginEmptyPassword() {
        UserPayload user = DataFactory.getUserWithEmptyPassword();
        Response resp = login(user);
        resp.then().statusCode(400);
    }
}
