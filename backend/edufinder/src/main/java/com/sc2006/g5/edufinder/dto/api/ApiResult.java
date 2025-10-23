package com.sc2006.g5.edufinder.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResult<R extends ApiRecord> {
    private List<R> records;

    @JsonProperty("_links")
    private ApiLink link;

    private int total;
}
