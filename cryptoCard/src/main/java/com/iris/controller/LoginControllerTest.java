package com.iris.controller;

import static org.assertj.core.api.Assertions.*;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class LoginControllerTest {

    @Test(expected = ResourceAccessException.class)
    public void whenHttpsUrlIsConsumed_thenException() {
        String urlOverHttps =
                "https://localhost:8088";
        ResponseEntity<String> response =
                new RestTemplate().exchange(urlOverHttps, HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    private SSLContext getSSLContext() throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File("/Users/glist/codespace/SmartCards/cryptoCard/src/main/resources/keystore.jks"));
        try {
            trustStore.load(instream, "password".toCharArray());
        } finally {
            instream.close();
        }
        return SSLContexts.custom()
                .loadTrustMaterial(trustStore)
                .build();
    }

    @Test
    public void givenAcceptingAllCertificatesUsing4_4_whenUsingRestTemplate_thenCorrect()
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {


        String urlOverHttps =
                "https://localhost:8088/graine";


        SSLContext sslcontext = getSSLContext();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();


        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        ResponseEntity<String> response
                = new RestTemplate(requestFactory).exchange(
                urlOverHttps, HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}