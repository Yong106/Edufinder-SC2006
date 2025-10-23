package com.sc2006.g5.edufinder.service;

import java.util.Map;

public interface ApiClientService {
    public String get(String url, Map<String, String> header);
}
