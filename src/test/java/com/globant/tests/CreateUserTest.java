package com.globant.tests;

import com.globant.data.DataProviders;
import com.globant.model.ApiResponseDTO;
import com.globant.model.CreateUserDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class CreateUserTest {

    private final String url = "https://petstore.swagger.io/v2";
    private final String userPath = "/user";
    private final String username = "DoeDoe01";

    @AfterMethod
    public void deleteUsers(Object[] testData) {
        RequestBuilder.sendDelete(url, userPath + "/" + testData[0]);
        RequestBuilder.sendDelete(url, userPath + "/" + testData[0] + "_copy");
    }

    @Test(testName = "Create a new valid user", dataProviderClass = DataProviders.class, dataProvider = "userData")
    public void createUser(String username, String firstName, String lastName, String email, String password, String phone) {
        CreateUserDTO newUser = new CreateUserDTO(
                username,
                firstName,
                lastName,
                email,
                password,
                phone
        );

        // User should be created and given an id
        Response response = RequestBuilder.sendPost(url, userPath, newUser);
        ApiResponseDTO apiResponse = response.as(ApiResponseDTO.class);
        Assert.assertEquals(apiResponse.getCode(), 200, "Expected 200 status code");
        Assert.assertTrue(Long.parseLong(apiResponse.getMessage()) > 0, "User ID should be created");
    }

    @Test(testName = "User should not be created when email is not unique", dataProviderClass = DataProviders.class, dataProvider = "userData")
    public void createUserSameEmail(String username, String firstName, String lastName, String email, String password, String phone) {
        // First base user
        CreateUserDTO firstUser = new CreateUserDTO(
                username,
                firstName,
                lastName,
                email,
                password,
                phone
        );

        // Second user using same email
        CreateUserDTO secondUser = new CreateUserDTO(
                username + "_copy",
                firstName,
                lastName,
                email,
                "anotherpass1234",
                "55567112"
        );

        // Assert that first user is created correctly
        Response firstResponse = RequestBuilder.sendPost(url, userPath, firstUser);
        Assert.assertEquals(firstResponse.getStatusCode(), 200, "First user should be created successfully");

        // Second user should not be registered with same email
        Response secondResponse = RequestBuilder.sendPost(url, userPath, secondUser);
        Assert.assertEquals(secondResponse.getStatusCode(), 400, "Second user with duplicate email should not be created");
    }

}
