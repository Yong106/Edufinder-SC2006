package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the MOE Programme data record contained within a response from the
 * <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_b0697d22a7837a4eddf72efb66a36fc2">data.gov.sg</a> API.
 *
 * @see ApiRecord
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgrammeRecord extends ApiRecord {

    @JsonProperty("school_name")
    private String schoolName;

    @JsonProperty("moe_programme_desc")
    private String programme;

}
