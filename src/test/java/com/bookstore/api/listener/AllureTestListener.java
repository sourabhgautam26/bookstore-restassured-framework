package com.bookstore.api.listener;

import com.bookstore.api.tests.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.charset.StandardCharsets;

public class AllureTestListener implements ITestListener {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onTestFailure(ITestResult result) {
        attachRequestAndResponse("FAILED");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        attachRequestAndResponse("PASSED");
    }

    private void attachRequestAndResponse(String status) {
        Response response = BaseTest.getLastResponse();
        Object request = BaseTest.getLastRequest();

        if (response != null) {
            String requestBody;

            if (request != null) {
                try {
                    // Convert Java object to JSON string
                    requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                } catch (Exception e) {
                    requestBody = request.toString();
                }
            } else {
                requestBody = "Request body not available";
            }
            String attachment = "Request Body:\n" + requestBody +
                    "\n\nStatus Code: " + response.getStatusCode() +
                    "\n\nResponse Body:\n" + response.getBody().asPrettyString();

            // Attach combined info
            Allure.addAttachment("Request & Response (" + status + ")", attachment);

        }
    }

    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
