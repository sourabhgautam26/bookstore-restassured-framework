package com.bookstore.api.base;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    @BeforeClass
    public void setup() {
        // Base URI from Endpoints
        RestAssured.baseURI = "http://127.0.0.1:8000";

        // Enable logging for requests and responses
        RestAssured.filters(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL));

        // Default content type
        RestAssured.useRelaxedHTTPSValidation();  // handles SSL if needed
    }
}
