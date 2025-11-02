package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectRecord extends ApiRecord {
    @JsonProperty("School_Name")
    private String schoolName;

    @JsonProperty("Subject_Desc")
    private String subject;
}
