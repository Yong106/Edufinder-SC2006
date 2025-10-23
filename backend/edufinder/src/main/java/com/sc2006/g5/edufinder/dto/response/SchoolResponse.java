package com.sc2006.g5.edufinder.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolResponse {

    private Long id;
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
    private Integer minCutOffPoint;
    private Integer maxCutOffPoint;

}
