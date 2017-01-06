package com.client.controller;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpRequestBuilder {

    RestTemplate restTemplate;
    HttpHeaders headers;
    public static final String KEYSTORE_PATH = "cryptoCard/src/main/resources/keystore.jks";

    HttpRequestBuilder() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        init();
    }

    private SSLContext getSSLContext() throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File(KEYSTORE_PATH));
        try {
            trustStore.load(instream, "password".toCharArray());
        } finally {
            instream.close();
        }
        return SSLContexts.custom()
                .loadTrustMaterial(trustStore)
                .build();
    }

    public void init()
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContext sslcontext = getSSLContext();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();


        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        final RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        this.restTemplate = restTemplate;

        // Set the Content-Type header
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    }

    public String get(String url) {
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
    }

    public String post(String url, MultiValueMap<String, String> map) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(url, request, String.class).getBody();
    }
}
