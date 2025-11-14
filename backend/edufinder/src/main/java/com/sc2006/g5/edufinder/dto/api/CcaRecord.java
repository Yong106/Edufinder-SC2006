package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the CCA data record contained within a response from the
 * <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_9aba12b5527843afb0b2e8e4ed6ac6bd">data.gov.sg</a> API.
 *
 * @see ApiRecord
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcaRecord extends ApiRecord {

    @JsonProperty("School_name")
    private String schoolName;

    @JsonProperty("cca_grouping_desc")
    private String cca;

    @JsonProperty("cca_generic_name")
    private String type;

}
