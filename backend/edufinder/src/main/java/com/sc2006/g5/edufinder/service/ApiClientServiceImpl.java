package com.sc2006.g5.edufinder.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClientServiceImpl implements ApiClientService {
	private final RestTemplate restTemplate;

	public ApiClientServiceImpl(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	@Override
	public String get(String url, Map<String, String> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();

		if(headers != null){
			for(Map.Entry<String, String> header:headers.entrySet()){
				httpHeaders.add(header.getKey(), header.getValue());
			}
		}

		HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
			url, 
			HttpMethod.GET,
			httpEntity,
			String.class
		);

		return responseEntity.getBody();
	}

}