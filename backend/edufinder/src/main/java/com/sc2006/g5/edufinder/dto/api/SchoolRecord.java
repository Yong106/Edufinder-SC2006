package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolRecord extends ApiRecord {

    @JsonProperty("school_name")
    private String name;

    @JsonProperty("url_address")
    private String website;

    private String address;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("telephone_no")
    private String phoneNumber;

    @JsonProperty("fax_no")
    private String faxNumber;

    @JsonProperty("email_address")
    private String email;

    @JsonProperty("mrt_desc")
    private String nearbyMrtStation;

    @JsonProperty("bus_desc")
    private String nearbyBusStation;

    @JsonProperty("dgp_code")
    private String location;

    @JsonProperty("type_code")
    private String type;

    @JsonProperty("nature_code")
    private String natureCode;

    @JsonProperty("session_code")
    private String sessionCode;

    @JsonProperty("mainlevel_code")
    private String level;
}
