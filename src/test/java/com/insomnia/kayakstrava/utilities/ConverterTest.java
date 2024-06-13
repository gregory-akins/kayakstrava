package com.insomnia.kayakstrava.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.insomnia.kayakstrava.models.StravaActivity;

class ConverterTest {

	@Test
	void test() {
		Converter converter = new Converter() ;
		try {
		     JSONObject jsonObject = new JSONObject("{\"id\":1,\"type\":\"Kayaking\", "
		     		+ "\"name\":\"ERG\", \"start_date_local\":\"2023-12-25T12:30:40Z\"}");
		     Optional<StravaActivity> activityOpt = converter.convertTo(jsonObject, StravaActivity.class);
		     StravaActivity activity = activityOpt.get();
		     assertNotNull(activity);
		     assertEquals(activity.getId(), "1");
		     assertEquals(activity.getName(), "ERG");
		     assertEquals(activity.getType(), "Kayaking");
		     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
			 Date date;
			try {
				date = dateFormat.parse("2023-12-25T12:30:40Z");
			} catch (JSONException | ParseException e) {
				date = null ; 
			}
		     assertEquals(date, activity.getStartDateLocal());
		}catch (JSONException err){
		     fail(err.getMessage());
		}

		
	}

}
