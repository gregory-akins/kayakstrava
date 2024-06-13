package com.insomnia.kayakstrava.resources;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

@RestController
@Slf4j

public class AuthController {
	
	@Value("${clientId}")
	private String clientId;

	@Value("${clientSecret}")
	private String clientSecret;
	

	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/getauth")
	public ResponseEntity<Object> getAuth(@RequestParam(required = true) String code) {
		log.debug("Got a token to auth against Strava {}", code);
		
		
		
		String urlString = "https://www.strava.com/api/v3/oauth/token";

		log.debug("uri = {}", urlString);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("client_id", clientId);
		map.add("client_secret", clientSecret);
		map.add("code", code);
		map.add("grant_type", "authorization_code");



		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		Object refreshToken = restTemplate.postForEntity(urlString, request, Object.class);

		return ResponseEntity.ok(refreshToken);
	}
	
	@PostMapping("/refreshauth")
	public ResponseEntity<Object> refreshAuth(
			@RequestParam(required = true) String refreshToken, 
			@RequestParam(required = true) String code) {
		 
		log.debug("Got a token to auth against Strava {}", refreshToken);
		RestTemplate restTemplate = new RestTemplate();
		
		String urlString = "https://www.strava.com/api/v3/oauth/token";

		log.debug("uri = {}", urlString);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject json = new JSONObject();
		json.put("client_id", clientId);
		json.put("client_secret", clientSecret);
		json.put("refresh_token", refreshToken);
		json.put("grant_type", "refresh_token");

		HttpEntity<String> request = 
			      new HttpEntity<String>(json.toString(), headers);
		
		
		ResponseEntity<Object> newRefreshToken = restTemplate.postForEntity(urlString, request, Object.class);
		
		return newRefreshToken;
	}
}
