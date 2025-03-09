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
        Assert.assertEquals(apiResponse.getCode(), 200, "Expected 200 status code");
        Assert.assertTrue(Long.parseLong(apiResponse.getMessage()) > 0, "User ID should be created");
    }

    @Test(testName = "User should not be created when email is not unique")
    public void createUserSameEmail() {
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
        Assert.assertEquals(firstResponse.getStatusCode(), 200);

        // Second user should not be registered with same email
        Response secondResponse = RequestBuilder.sendPost(url, userPath, secondUser);
        Assert.assertEquals(secondResponse.getStatusCode(), 400);
    }

}
