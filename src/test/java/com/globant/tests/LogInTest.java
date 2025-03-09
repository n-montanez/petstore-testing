package com.globant.tests;

import com.globant.data.DataProviders;
import com.globant.model.ApiResponseDTO;
import com.globant.model.CreateUserDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class LogInTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String userPath = "/user";
    private final String loginPath = "/login";

    @BeforeMethod
    public void registerUser(Object[] user) {
        CreateUserDTO newUser = new CreateUserDTO(
                user[0].toString(),
                user[1].toString(),
                user[2].toString(),
                user[3].toString(),
                user[4].toString(),
                user[5].toString()
        );

        RequestBuilder.sendPost(url, userPath, newUser);
    }

    @AfterMethod
    public void deleteUser(Object[] testData) {
        RequestBuilder.sendDelete(url, userPath + "/" + testData[0]);
    }

    @Test(testName = "Log in with valid credentials", priority = 1, dataProviderClass = DataProviders.class, dataProvider = "userData")
    public void ValidLogin(String username, String firstName, String lastName, String email, String password, String phone) {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("username", username);
        loginParams.put("password", password);

        // Send valid login request
        Response response = RequestBuilder.sendGet(url, userPath + loginPath, loginParams);
        ApiResponseDTO apiResponse = response.as(ApiResponseDTO.class);

        // Verify success status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 status code.");

        // Verify that message is not empty
        Assert.assertFalse(apiResponse.getMessage().isEmpty());

        // Verify that the given Unix timestamp is from the last minute of the request
        String[] messageParts = apiResponse.getMessage().split(":");
        Assert.assertEquals(messageParts.length, 2);

        long epochTime = Long.parseLong(messageParts[1].trim()) / 1000;
        long currentEpoch = Instant.now().getEpochSecond();
        Assert.assertTrue(epochTime >= (currentEpoch - 60) && epochTime <= currentEpoch);
    }

    @Test(testName = "Log in with invalid credentials", priority = 2, dataProviderClass = DataProviders.class, dataProvider = "userData")
    public void InvalidLogin(String username, String firstName, String lastName, String email, String password, String phone) {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("username", username);
        loginParams.put("password", password + "wrong");

        // Send invalid login request
        Response response = RequestBuilder.sendGet(url, userPath + loginPath, loginParams);

        // Verify bad request status code
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 status code.");
    }
}
