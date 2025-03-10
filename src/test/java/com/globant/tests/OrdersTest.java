package com.globant.tests;

import com.globant.model.ApiResponseDTO;
import com.globant.model.PetDTO;
import com.globant.model.PostOrderDTO;
import com.globant.model.PostOrderResponseDTO;
import com.globant.requests.RequestBuilder;
import com.globant.utils.TestUtils;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
public class OrdersTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String petPath = "/pet";
    private final String storePath = "/store";
    private final String orderPath = "/order";

    /**
     * Test for sending a new valid order request
     */
    @Test(testName = "Order is created when request is valid")
    public void ValidOrder() {
        PetDTO selectedPet = TestUtils.getRandomAvailablePet(url, petPath);

        // Create order request for selected pet
        PostOrderDTO orderDTO = PostOrderDTO.builder()
                .petId(selectedPet.getId())
                .quantity(1)
                .shipDate(TestUtils.getFormattedDateFromDays(2))
                .build();

        // Send new order POST request
        Response orderResponse = RequestBuilder.sendPost(url, storePath + orderPath, orderDTO);
        Assert.assertEquals(orderResponse.getStatusCode(), 200);

        // Assert data match
        PostOrderResponseDTO orderResponseDTO = orderResponse.as(PostOrderResponseDTO.class);
        Assert.assertTrue(orderResponseDTO.getId() > 0);
        Assert.assertEquals(orderResponseDTO.getPetId(), orderDTO.getPetId());
        Assert.assertEquals(orderResponseDTO.getQuantity(), orderDTO.getQuantity());
        Assert.assertFalse(orderResponseDTO.isComplete());
        Assert.assertEquals(orderResponseDTO.getShipDate(), orderDTO.getShipDate());
    }

    /**
     * Test for sending a new order request with a date before current time
     */
    @Test(testName = "Order is not created when ship date is before current date")
    public void InvalidDateOrder() {
        PetDTO selectedPet = TestUtils.getRandomAvailablePet(url, petPath);

        // Create order request for selected pet
        PostOrderDTO orderDTO = PostOrderDTO.builder()
                .petId(selectedPet.getId())
                .quantity(1)
                .shipDate(TestUtils.getFormattedDateFromDays(-5))
                .build();

        // Send new order POST request
        Response orderResponse = RequestBuilder.sendPost(url, storePath + orderPath, orderDTO);
        Assert.assertEquals(orderResponse.getStatusCode(), 400);

        ApiResponseDTO apiResponseDTO = orderResponse.as(ApiResponseDTO.class);
        Assert.assertTrue(apiResponseDTO.getMessage().isEmpty());
        Assert.assertEquals(apiResponseDTO.getMessage().toLowerCase(), "invalid order");
    }

    /**
     * Test for sending a new order with pet quantity as negative
     */
    @Test(testName = "Order is not created when quantity is negative")
    public void InvalidQuantityOrder() {
        PetDTO selectedPet = TestUtils.getRandomAvailablePet(url, petPath);

        // Create order request for selected pet with negative quantity
        PostOrderDTO orderDTO = PostOrderDTO.builder()
                .petId(selectedPet.getId())
                .quantity(-3)
                .shipDate(TestUtils.getFormattedDateFromDays(2))
                .build();

        // Send new order POST request
        Response orderResponse = RequestBuilder.sendPost(url, storePath + orderPath, orderDTO);
        Assert.assertEquals(orderResponse.getStatusCode(), 400);

        ApiResponseDTO apiResponseDTO = orderResponse.as(ApiResponseDTO.class);
        Assert.assertTrue(apiResponseDTO.getMessage().isEmpty());
        Assert.assertEquals(apiResponseDTO.getMessage().toLowerCase(), "invalid order");
    }
}
