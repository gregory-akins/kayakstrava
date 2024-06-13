package com.insomnia.kayakstrava.resources;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.insomnia.kayakstrava.models.ActivityPage;
import com.insomnia.kayakstrava.models.StravaActivity;
import com.insomnia.kayakstrava.services.ActivityService;
import com.insomnia.kayakstrava.services.StravaActivityRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

	@Autowired
	private RestTemplate restTemplate;
	
	 
	@Autowired
	private ActivityService activityService ; 

	@GetMapping("/athlete/activities")
	public ResponseEntity<ActivityPage> getActivities(
			@RequestParam(name="page", defaultValue="1") String page, 
			@RequestParam(required = true) String accessToken,
			@RequestParam(name = "per_page", required = false) String perPage, 
			@RequestParam(name = "before", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date beforeDate, 
			@RequestParam(name = "after", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date afterDate,
			@RequestParam(name="last_id", required = false) String lastId
			) {

		

		log.debug("Getting Activities, page {}, last_id = {} ", page, lastId);
		
		URIBuilder builder = new URIBuilder()
			    .setScheme("https")
			    .setHost("www.strava.com")
			    .setPath("/api/v3/athlete/activities")
			    .addParameter("access_token", accessToken);

		if (accessToken != null) {
			builder.addParameter("access_token", accessToken);
		}
		
		
		
		//date to epochTimeStamp	
	    if (beforeDate != null) {
	    	Long beforeMillis = beforeDate.getTime()/1000;			
	    	builder.addParameter("before", beforeMillis.toString());

	    }
	    if (afterDate != null) {
	    	Long afterMillis  = afterDate.getTime()/1000;
	    	builder.addParameter("after", afterMillis.toString() );

	    }


	    ResponseEntity<ActivityPage> response = ResponseEntity.ok(activityService.retrieveActivities(builder, page, perPage, lastId));
	    //let's now convert the string to a Activity Type
	    

	    return response;

	}

	
}
