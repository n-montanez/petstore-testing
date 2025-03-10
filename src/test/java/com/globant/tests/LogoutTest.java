package com.globant.tests;

import com.globant.model.ApiResponseDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
public class LogoutTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String userPath = "/user";
    private final String logoutPath = "/logout";

    @Test(testName = "Log out", priority = 3)
    public void LogoutUser() {
        Response response = RequestBuilder.sendGet(url, userPath + logoutPath);
        Assert.assertEquals(response.getStatusCode(), 200);

        ApiResponseDTO apiResponseDTO = response.as(ApiResponseDTO.class);
        Assert.assertNotNull(apiResponseDTO.getMessage());
        Assert.assertFalse(apiResponseDTO.getMessage().isEmpty());
        Assert.assertEquals(apiResponseDTO.getMessage().toLowerCase(), "ok");
    }
}
