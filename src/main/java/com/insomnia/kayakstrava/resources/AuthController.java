package com.insomnia.kayakstrava.resources;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j

public class AuthController {
	
	@Value("${clientId}")
	private String clientId;

	@Value("${clientSecret}")
	private String clientSecret;
	
	@PostMapping("/hello")
	public ResponseEntity<Object> helloWorld(@RequestParam(required = true) String authToken) {
		log.debug("Got a token to auth against Strava {}", authToken);
		RestTemplate restTemplate = new RestTemplate();

		
		String urlString = "https://www.strava.com/api/v3/oauth/token";

		log.debug("uri = {}", urlString);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject json = new JSONObject();
		json.put("client_id", clientId);
		json.put("client_secret", clientSecret);
		json.put("code", authToken);
		json.put("grant_type", "authorization_code");

		HttpEntity<String> request = 
			      new HttpEntity<String>(json.toString(), headers);
		
		
		Object refreshToken = restTemplate.postForEntity(urlString, request, Object.class);

		return ResponseEntity.ok(refreshToken);
	}
}
