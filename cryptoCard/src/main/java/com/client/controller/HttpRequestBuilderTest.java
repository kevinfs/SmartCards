package com.client.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.iris.service.Tools;

public class HttpRequestBuilderTest {

	public static void main(String[] args) throws Exception {
		Tools tools = new Tools();
		String url;
		MultiValueMap<String, String> requestData;

		// params
		final String login = "caca";
		final String password = "caca";

		// initialisation
		String baseURL = "https://localhost:8088";
		HttpRequestBuilder http = new HttpRequestBuilder(baseURL);

		// récupération graine
		url = "https://localhost:8088/graine";
		String graine = http.get(url);
		System.out.println("graine = " + graine);

		// envoi login
		url = "https://localhost:8088/login";
		requestData = new LinkedMultiValueMap<>();
		requestData.add("login", login);
		String sel = http.post(url, requestData);
		System.out.println("sel = " + sel);

		// envoi mdp
		url = "https://localhost:8088/password";

		requestData = new LinkedMultiValueMap<>();
		String v1 = tools.md5(password + sel);
		String v2 = tools.md5(v1 + graine);
		requestData.add("login", login);
		requestData.add("password", v2);
		requestData.add("graine", graine);
		String response = http.post(url, requestData);

		System.out.println("response = " + response);
	}
}