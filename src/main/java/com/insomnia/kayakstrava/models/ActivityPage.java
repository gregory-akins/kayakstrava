package com.insomnia.kayakstrava.models;

import java.util.List;
import lombok.Data;

@Data
public class ActivityPage {
	List<StravaActivity> activities;
	int lastPage;
	String lastId;
	

}
