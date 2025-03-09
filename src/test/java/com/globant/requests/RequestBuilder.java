package com.globant.requests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestBuilder {

    public static Response sendGet(String url, String path) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json");
        return requestSpecification.get(path);
    }

    public static Response sendPost(String url, String path, Object body) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .body(body);
        return requestSpecification.post(path);
    }
}
