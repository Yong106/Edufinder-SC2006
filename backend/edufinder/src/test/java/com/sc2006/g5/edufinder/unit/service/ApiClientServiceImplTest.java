package com.sc2006.g5.edufinder.unit.service;

import com.sc2006.g5.edufinder.service.ApiClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ApiClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiClientServiceImpl apiClientServiceImpl;

    @Test
    void get_returnsResponseBody(){
        String expectedResponse = "{\"id\":1,\"title\":\"mock-title\"}";
        String url = "https://test.com";

        Map<String, String> headers = new HashMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();

        headers.put("Authorization", "token");
        httpHeaders.set("Authorization", "token");

        headers.put("Key", "key");
        httpHeaders.set("Key", "key");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class))
            .thenReturn(ResponseEntity.ok(expectedResponse));

        String result = apiClientServiceImpl.get(url, headers);
        assertThat(result).isEqualTo(expectedResponse);
    }
}
