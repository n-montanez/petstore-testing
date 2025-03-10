package com.globant.requests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RequestBuilder {

    /**
     * Sends a GET request to the given URL and path
     *
     * @param url Base url for the request
     * @param path Endpoint path
     * @return Response object with HTTP response
     */
    public static Response sendGet(String url, String path) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
        return requestSpecification.get(path);
    }

    /**
     * Sends a GET request with params to the given URL and path
     *
     * @param url Base url for the request
     * @param path Endpoint path
     * @param queryParams Map of query parameters for the request
     * @return Response object with HTTP response
     */
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

    /**
     * Sends a POST request with the given body to the specified URL and path
     *
     * @param url Base URL for the request
     * @param path Endpoint path
     * @param body Object to be sent as JSON
     * @return Response object with HTTP response
     */
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

    /**
     * Sends a DELETE request to the given URL and path
     *
     * @param url Base url for the request
     * @param path Endpoint path
     */
    public static void sendDelete(String url, String path) {
        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(url)
                .header("Content-Type", "application/json")
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}
