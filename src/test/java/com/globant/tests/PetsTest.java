package com.globant.tests;

import com.globant.model.PetDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PetsTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String petPath = "/pet";

    @Test(testName = "Get all available pets")
    public void GetAllAvailablePets() {
        Map<String, String> query = new HashMap<>();
        query.put("status", "available");

        Response response = RequestBuilder.sendGet(url, petPath + "/findByStatus", query);
        List<PetDTO> pets = response.as(new TypeRef<List<PetDTO>>() {
        });

        Assert.assertFalse(pets.isEmpty(), "List of available pets should not be empty");

        for (PetDTO pet : pets) {
            Assert.assertEquals(pet.getStatus().toLowerCase(), "available");
        }
    }
}
