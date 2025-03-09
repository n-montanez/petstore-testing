package com.globant.tests;

import com.globant.model.PetDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class PetsTest {
    private final String url = "https://petstore.swagger.io/v2";
    private final String petPath = "/pet";

    @Test(testName = "Get all available pets", priority = 1)
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

    @Test(testName = "Get pet details by its id", priority = 2)
    public void GetSinglePet() {
        Map<String, String> query = new HashMap<>();
        query.put("status", "available");

        Response petResponse = RequestBuilder.sendGet(url, petPath + "/findByStatus", query);
        List<PetDTO> pets = petResponse.as(new TypeRef<List<PetDTO>>() {
        });

        // Select a random pet from the response
        int randomSelect = ThreadLocalRandom.current().nextInt(1, pets.size() + 1);
        PetDTO selectedPet = pets.get(randomSelect);

        // Send GET request to single pet
        Response singlePetResponse = RequestBuilder.sendGet(url, petPath + "/" + selectedPet.getId());
        PetDTO singlePetDTO = singlePetResponse.as(PetDTO.class);

        // Assert pet data matches
        Assert.assertEquals(singlePetDTO, selectedPet);
    }
}
