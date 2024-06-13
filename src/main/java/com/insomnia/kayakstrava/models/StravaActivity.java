package com.insomnia.kayakstrava.models;

import java.util.Date;

import lombok.Data;

@Data
public class StravaActivity {
	private String id ; 
	private String type ; 
	private String name;
	private Date startDateLocal;

}
