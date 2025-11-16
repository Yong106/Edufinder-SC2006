package com.sc2006.g5.edufinder.dto.api;

/**
 * Represents an individual data record contained within a response from the
 * <a href="https://data.gov.sg/">data.gov.sg</a> API.
 * <p>
 * This is an abstract base class that defines the structure of a single record entry returned
 * by the API. Each dataset from data.gov.sg may have its own specific record schema,
 * so subclasses should extend this class to model the fields relevant to a particular dataset.
 * </p>
 *
 * <p>
 * For example, different API endpoints may define records for schools, weather readings,
 * or carpark availability, each represented by their own subclass of {@code ApiRecord}.
 * </p>
 *
 * @see ApiResponse
 * @see ApiResult
 */
public abstract class ApiRecord {}