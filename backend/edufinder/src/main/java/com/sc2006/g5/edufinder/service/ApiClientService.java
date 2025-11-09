package com.sc2006.g5.edufinder.service;

import java.util.Map;

/**
 * A service for making API client requests.
 * Provides methods to send HTTP requests and receive responses from external APIs.
 */
public interface ApiClientService {

    /**
     * Sends an HTTP GET request to the specified URL with the given headers.
     *
     * @param url the target URL to send the GET request to
     * @param header a map of request headers where the key is the header name and the value is the header value.
     *               Set to null if request header not needed.
     *
     * @return the response body as a {@code String}
     */
    String get(String url, Map<String, String> header);
}
