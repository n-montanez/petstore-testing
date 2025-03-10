package com.globant.tests;

import com.globant.model.ApiResponseDTO;
import com.globant.model.PetDTO;
import com.globant.requests.RequestBuilder;
import com.globant.utils.TestUtils;
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

    /**
     * Test for listing all pets with state as available
     */
    @Test(testName = "Get all available pets")
    public void GetAllAvailablePets() {
        Map<String, String> query = new HashMap<>();
        query.put("status", "available");

        Response response = RequestBuilder.sendGet(url, petPath + "/findByStatus", query);
        List<PetDTO> pets = response.as(new TypeRef<>() {
        });

        Assert.assertFalse(pets.isEmpty(), "List of available pets should not be empty");

        for (PetDTO pet : pets) {
            Assert.assertEquals(pet.getStatus().toLowerCase(), "available");
        }
    }

    /**
     * Test for getting the details of a single pet
     */
    @Test(testName = "Get pet details by its id")
    public void GetSinglePet() {
        PetDTO selectedPet = TestUtils.getRandomAvailablePet(url, petPath);

        // Send GET request to single pet
        Response singlePetResponse = RequestBuilder.sendGet(url, petPath + "/" + selectedPet.getId());
        PetDTO singlePetDTO = singlePetResponse.as(PetDTO.class);

        // Assert pet data matches
        Assert.assertEquals(singlePetDTO, selectedPet);
    }

    /**
     * Test for requesting a non-existing pet
     */
    @Test(testName = "Get pet by non-existing id")
    public void GetNotFoundPet() {
        long notFoundId = 999999999;

        // Send GET request to single pet
        Response singlePetResponse = RequestBuilder.sendGet(url, petPath + "/" + notFoundId);
        ApiResponseDTO apiResponseDTO = singlePetResponse.as(ApiResponseDTO.class);

        // Assert response error
        Assert.assertEquals(singlePetResponse.getStatusCode(), 404);
        Assert.assertEquals(apiResponseDTO.getType(), "error");
        Assert.assertEquals(apiResponseDTO.getMessage().toLowerCase(), "pet not found");
    }
}
