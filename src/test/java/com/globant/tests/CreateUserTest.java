package com.globant.tests;

import com.globant.model.ApiResponseDTO;
import com.globant.model.CreateUserDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateUserTest {

    private final String url = "https://petstore.swagger.io/v2";
    private final String userPath = "/user";
    private final String username = "DoeDoe01";

    @AfterTest
    public void deleteUser() {
        RequestBuilder.sendDelete(url, userPath + "/" + username);
    }

    @Test(testName = "Create a new valid user")
    public void createUser() {
        SoftAssert softAssert = new SoftAssert();

        CreateUserDTO newUser = CreateUserDTO.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("securepass39")
                .phone("55512389")
                .build();

        Response response = RequestBuilder.sendPost(url, userPath, newUser);
        ApiResponseDTO apiResponse = response.as(ApiResponseDTO.class);
        softAssert.assertEquals(apiResponse.getCode(), 201, "POST Response code should be 201 when resource is created");
        Assert.assertTrue(Long.parseLong(apiResponse.getMessage()) > 0, "User ID should be created and greater than 0");
        softAssert.assertAll();
    }

    @Test(testName = "User should not be created when email is not unique")
    public void createUserSameEmail() {
        SoftAssert softAssert = new SoftAssert();

        CreateUserDTO firstUser = CreateUserDTO.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("securepass39")
                .phone("55512389")
                .build();

        CreateUserDTO secondUser = CreateUserDTO.builder()
                .username("DoeDoe02")
                .firstName("Jane")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("anotherpass123")
                .phone("55576890")
                .build();

        // Assert that first user is created correctly
        Response firstResponse = RequestBuilder.sendPost(url, userPath, firstUser);
        softAssert.assertEquals(firstResponse.getStatusCode(), 201);

        // Second user should not be registered with same email
        Response secondResponse = RequestBuilder.sendPost(url, userPath, secondUser);
        softAssert.assertEquals(secondResponse.getStatusCode(), 400);

        softAssert.assertAll();
    }

}
