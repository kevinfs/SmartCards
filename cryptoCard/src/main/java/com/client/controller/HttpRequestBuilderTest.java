package com.client.controller;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

public class HttpRequestBuilderTest {

    public static void main(String[] args) throws Exception {

        String urlOverHttps =
                "https://localhost:8088/graine";

        ClientHttpRequestFactory requestFactory = HttpRequestBuilder.build();

        ResponseEntity<String> response
                = new RestTemplate(requestFactory).exchange(
                urlOverHttps, HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

}