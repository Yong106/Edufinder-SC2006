package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the subject data record contained within a response from the
 * <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_f1d144e423570c9d84dbc5102c2e664d">data.gov.sg</a> API.
 * <p>
 *
 * @see ApiRecord
 */
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
