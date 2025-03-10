package com.globant.tests;

import com.globant.model.PetDTO;
import com.globant.model.PostOrderDTO;
import com.globant.model.PostOrderResponseDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class OrdersTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String petPath = "/pet";
    private final String storePath = "/store";
    private final String orderPath = "/order";

    @Test(testName = "Order is created whe request is valid")
    public void ValidOrder() {
        Map<String, String> query = new HashMap<>();
        query.put("status", "available");

        // Get all available pets
        Response response = RequestBuilder.sendGet(url, petPath + "/findByStatus", query);
        List<PetDTO> pets = response.as(new TypeRef<List<PetDTO>>() {
        });

        // Select a random pet from the response
        int randomSelect = ThreadLocalRandom.current().nextInt(1, pets.size());
        PetDTO selectedPet = pets.get(randomSelect);

        // Order date 2 days from current time
        ZonedDateTime date = ZonedDateTime.now(ZoneOffset.UTC).plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String shipDate = date.format(formatter);

        // Create order request for selected pet
        PostOrderDTO orderDTO = PostOrderDTO.builder()
                .petId(selectedPet.getId())
                .quantity(1)
                .shipDate(shipDate)
                .build();

        // Send new order POST request
        Response orderResponse = RequestBuilder.sendPost(url, storePath + orderPath, orderDTO);
        PostOrderResponseDTO orderResponseDTO = orderResponse.as(PostOrderResponseDTO.class);

        // Assert data match
        Assert.assertEquals(orderResponse.getStatusCode(), 200);
        Assert.assertTrue(orderResponseDTO.getId() > 0);
        Assert.assertEquals(orderResponseDTO.getPetId(), orderDTO.getPetId());
        Assert.assertEquals(orderResponseDTO.getQuantity(), orderDTO.getQuantity());
        Assert.assertFalse(orderResponseDTO.isComplete());
        Assert.assertEquals(orderResponseDTO.getShipDate(), orderDTO.getShipDate());
    }
}
