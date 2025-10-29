package com.sc2006.g5.edufinder.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiSchool {

	private String name;
	private String location;
	private List<String> ccas;
	private List<String> subjects;
	private String level;
	private String natureCode;
	private String type;
	private String sessionCode;
	private String address;
	private String postalCode;
	private String nearbyMrtStation;
	private String nearbyBusStation;
	private String website;
	private String email;
	private String phoneNumber;
	private String faxNumber;

}