package com.insomnia.kayakstrava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.insomnia.kayakstrava.models.ActivityPage;
import com.insomnia.kayakstrava.models.StravaActivity;
import com.insomnia.kayakstrava.utilities.Converter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService {
	
	private final StravaActivityRepo activityRepo ; 
	private final RestTemplate restTemplate;

	public ActivityPage retrieveActivities(URIBuilder builder,
			String page, String perPage, String previousLastId) {
		
		// get more results each time, unless it's specified
		if (StringUtils.isBlank(perPage)) {
			perPage = "10";
		}
		builder.addParameter("per_page", perPage);

		builder.addParameter("page", page);
		int pageNum = Integer.valueOf(page).intValue();
		List<StravaActivity> activityList = new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> request = new HttpEntity<>(headers);
		//	while we still need enough results for the page
		int count = 0;
		boolean moreResults = true ;
		int lastPage = 1;
		String lastId = previousLastId;
		while (needResults(perPage, count) > 0 && moreResults) {
			
			ResponseEntity<String> response = restTemplate.exchange(builder.toString(), HttpMethod.GET, request,
					new ParameterizedTypeReference<String>() {
					});
	
			String jsonString = response.getBody();
			
			JSONArray jsonArray = new JSONArray(jsonString);
			moreResults = jsonArray.length() >  0; 
			if (moreResults) {
				log.debug("############## Got {} results", jsonArray.length());
			
				activityList = filterKayaking(activityList, jsonArray, needResults(perPage,count), lastId);
				
				count = activityList.size();
				lastPage = pageNum;
				pageNum++;
				
				// if we're not done, get the next page
				builder.setParameter("page", ""+pageNum);
				
			}
		}
		
		ActivityPage result = new ActivityPage();
		result.setActivities(activityList);
		result.setLastId(activityList.get(activityList.size()-1).getId());
		result.setLastPage(lastPage);
		return result;
	}
	/**
	 * Return the number of results needed to satisfy our pageCount
	 * @param perPage
	 * @param count
	 * @return
	 */
	private int needResults(String perPage, int count) {
		return Integer.valueOf(perPage) - count ;
	}

	private List<StravaActivity> filterKayaking(List<StravaActivity> activityList, JSONArray jsonArray, int numNeeded, String lastId) {		
		Converter converter = new Converter();
		int retrieved = 0;
		for (Object obj : jsonArray) {
			JSONObject jsonObject = (JSONObject) obj;
			Optional<StravaActivity> result = converter.convertTo(jsonObject, StravaActivity.class);
			if (result.get().getType().equals("Kayaking") && Long.valueOf(result.get().getId()) < Long.valueOf(lastId) ) {
				if (activityRepo.findById(result.get().getId()).isEmpty()) {
				  activityList.add(result.get());
				  retrieved++ ; 
				}				
			}
			if (retrieved >= numNeeded) break ;
		}
		return activityList;
	}

	

}
