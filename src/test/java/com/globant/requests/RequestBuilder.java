package com.globant.requests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RequestBuilder {

    public static Response sendGet(String url, String path) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
        return requestSpecification.get(path);
    }

    public static Response sendGet(String url, String path, Map<String, String> queryParams) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .queryParams(queryParams)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
        return requestSpecification.get(path);
    }

    public static Response sendPost(String url, String path, Object body) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .body(body)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
        return requestSpecification.post(path);
    }

    public static Response sendDelete(String url, String path) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
        return requestSpecification.delete(path);
    }
}
