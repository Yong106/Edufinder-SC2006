package com.sc2006.g5.edufinder.model.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a CCA entity fetched from the <a href="https://data.gov.sg/">data.gov.sg</a> API.
 */
@Data
@Builder
@AllArgsConstructor
public class Cca {

    private String name;
    private String type;
}
