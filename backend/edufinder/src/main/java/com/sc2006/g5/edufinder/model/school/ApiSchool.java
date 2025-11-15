package com.sc2006.g5.edufinder.model.school;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a school entity fetched from the <a href="https://data.gov.sg/">data.gov.sg</a> API.
 */
@Data
@Builder
public class ApiSchool {

    private String name;

    private List<Cca> ccas;
    private List<String> subjects;
    private List<String> programmes;

    private String level;
    private String natureCode;
    private String type;
    private String sessionCode;

    private String location;
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

    private String motherTongue1;
    private String motherTongue2;
    private String motherTongue3;

    private boolean sapInd;
    private boolean autonomousInd;
    private boolean giftedInd;
    private boolean ipInd;

}