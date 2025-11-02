package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sc2006.g5.edufinder.config.YesNoBooleanDeserializer;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
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

    @JsonProperty("mothertongue1_code")
    private String motherTongue1;

    @JsonProperty("mothertongue2_code")
    private String motherTongue2;

    @JsonProperty("mothertongue3_code")
    private String motherTongue3;

    @JsonProperty("sap_ind")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private boolean sapInd;

    @JsonProperty("autonomous_ind")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private boolean autonomousInd;

    @JsonProperty("gifted_ind")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private boolean giftedInd;

    @JsonProperty("ip_ind")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private boolean ipInd;
}
