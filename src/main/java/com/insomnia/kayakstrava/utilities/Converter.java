package com.insomnia.kayakstrava.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import com.insomnia.kayakstrava.models.StravaActivity;

public class Converter {
	public <T> Optional<T> convertTo(JSONObject obj, Class<T> clazz) {		
		StravaActivity activity = new StravaActivity() ;
		activity.setId(String.valueOf(obj.getLong("id")));		
		activity.setType(obj.getString("type"));
		activity.setName(obj.getString("name"));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		 Date date;
		try {
			date = dateFormat.parse(obj.getString("start_date_local"));
		} catch (JSONException | ParseException e) {
			date = null ; 
		}
		
		activity.setStartDateLocal(date);
		return clazz.isInstance(activity) ? Optional.of(clazz.cast(activity)) : null;	    
	    		
	}
}
