package com.globant.tests;

import com.globant.model.ApiResponseDTO;
import com.globant.model.CreateUserDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateUserTest {

    private final String url = "https://petstore.swagger.io/v2";
    private final String newUserPath = "/user";

    @Test(testName = "Create a new valid user")
    public void createUser() {
        SoftAssert softAssert = new SoftAssert();

        CreateUserDTO newUser = CreateUserDTO.builder()
                .username("DoeDoe01")
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("securepass39")
                .phone("55512389")
                .build();

        Response response = RequestBuilder.sendPost(url, newUserPath, newUser);
        ApiResponseDTO apiResponse = response.as(ApiResponseDTO.class);
        softAssert.assertEquals(apiResponse.getCode(), 201, "POST Response code should be 201 when resource is created");
        Assert.assertTrue(Long.parseLong(apiResponse.getMessage()) > 0, "User ID should be created and greater than 0");
        softAssert.assertAll();
    }

}
