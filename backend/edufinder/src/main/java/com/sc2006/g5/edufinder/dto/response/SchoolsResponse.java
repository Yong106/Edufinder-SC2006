package com.sc2006.g5.edufinder.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SchoolsResponse {

    private List<SchoolResponse> schools;
}
