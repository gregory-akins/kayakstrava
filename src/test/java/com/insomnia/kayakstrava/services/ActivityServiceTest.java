package com.insomnia.kayakstrava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.insomnia.kayakstrava.models.ActivityPage;
import com.insomnia.kayakstrava.models.StravaActivity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ActivityServiceTest {

	@Mock
	RestTemplate restTemplate;
	
	@Mock
	StravaActivityRepo activityRepo;

	@InjectMocks
	private ActivityService activityService;
		

	@Test
	void test() {
		assertNotNull(activityService);
	}

	@Test
	void testGetActivities()  {
		
		URIBuilder builder = new URIBuilder().setScheme("https").setHost("www.strava.com")
				.setPath("/api/v3/athlete/activities").addParameter("access_token", "accessToken")
				.addParameter("page", "1");
		String jsonString = "{}";
		try {
			jsonString = FileUtils.readFileToString(new File("./src/test/resources/kayakingActivities.json"), "UTF-8");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		ResponseEntity<String> thing1 = ResponseEntity.ok(jsonString);
		ResponseEntity<String> thing2 = ResponseEntity.ok("[]");
		doReturn(thing1).doReturn(thing2).when(restTemplate).exchange(anyString(), any(HttpMethod.class),
				ArgumentMatchers.<HttpEntity<?>>any(), ArgumentMatchers.<ParameterizedTypeReference<String>>any());
		doReturn(Optional.empty()).when(activityRepo).findById(any(String.class));
		ActivityPage activities = activityService.retrieveActivities(builder, "1", "10", Long.toString(Long.MAX_VALUE));
		assertNotNull(activities);

		List<StravaActivity> activityList = activities.getActivities();
		assertNotNull(activityList);
		assertEquals(4, activityList.size());
		//iterate through list
		assertEquals("Kayaking", activityList.get(0).getType());
		assertEquals("ERGalicious", activityList.get(0).getName());

	}
	
	
	
	
	
	
	@Test
	void getCorrectNumberPerPage() {
		fail();
//		URIBuilder builder = new URIBuilder().setScheme("https").setHost("www.strava.com")
//				.setPath("/api/v3/athlete/activities").addParameter("access_token", "accessToken")
//				.addParameter("page", "1");
//		String jsonString = "{}";
//		try {
//			jsonString = FileUtils.readFileToString(new File("./src/test/resources/kayakingActivities.json"), "UTF-8");
//		} catch (IOException e) {
//			fail(e.getMessage());
//		}
//		ResponseEntity<String> things = ResponseEntity.ok(jsonString);
//		doReturn(things).when(restTemplate).exchange(anyString(), any(HttpMethod.class),
//				ArgumentMatchers.<HttpEntity<?>>any(), ArgumentMatchers.<ParameterizedTypeReference<String>>any());
//
//		ResponseEntity<List<StravaActivity>> activities = activityService.retrieveActivities(restTemplate, builder, "1", "3");
//		assertNotNull(activities);
//
//		List<StravaActivity> activityList = activities.getBody();
//		assertNotNull(activityList);
//		assertEquals(3, activityList.size());
//		//iterate through list
//		assertTrue(activityList.get(0).getType().equals("Kayaking"));
//		assertTrue(activityList.stream().anyMatch(item -> "Kayaking".equals(item.getType())));
	}
	
	@Test
	void getCorrectNumberPerPageWithLargeResultSet() {
		fail();
//		URIBuilder builder = new URIBuilder().setScheme("https").setHost("www.strava.com")
//				.setPath("/api/v3/athlete/activities").addParameter("access_token", "accessToken");
//
//		String jsonString = "{}";
//		try {
//			jsonString = FileUtils.readFileToString(new File("./src/test/resources/kayakingActivitiesLarge3.json"), "UTF-8");
//		} catch (IOException e) {
//			fail(e.getMessage());
//		}
//		ResponseEntity<String> things = ResponseEntity.ok(jsonString);
//		doReturn(things).when(restTemplate).exchange(anyString(), any(HttpMethod.class),
//				ArgumentMatchers.<HttpEntity<?>>any(), ArgumentMatchers.<ParameterizedTypeReference<String>>any());
//
//		ResponseEntity<List<StravaActivity>> activities = activityService.retrieveActivities(restTemplate, builder, "1", "10");
//		assertNotNull(activities);
//
//		List<StravaActivity> activityList = activities.getBody();
//		assertNotNull(activityList);
//		assertEquals(10, activityList.size());
//		//iterate through list
//		assertTrue(activityList.get(0).getType().equals("Kayaking"));
		
	}

}
