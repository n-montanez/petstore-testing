package com.globant.utils;

import com.globant.model.PetDTO;
import com.globant.requests.RequestBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {
    private static final String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

    public static PetDTO getRandomAvailablePet(String url, String path) {
        // Get all available pets
        Response response = RequestBuilder.sendGet(url, path + "/findByStatus", Map.of("status", "available"));
        List<PetDTO> pets = response.as(new TypeRef<>() {
        });

        // Select a random pet from the response
        int randomSelect = ThreadLocalRandom.current().nextInt(0, pets.size());
        return pets.get(randomSelect);
    }

    public static String getFormattedDateFromDays(int daysFrom) {
        return ZonedDateTime.now(ZoneOffset.UTC)
                .plusDays(daysFrom)
                .withHour(0).withMinute(0).withSecond(0).withNano(0)
                .format(formatter);
    }
}
