package com.bingebox.booking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestGateway {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T get(String servicePath, String path, Class<T> responseType, Object... uriVars) {
        String url = servicePath + path;
        return restTemplate.getForObject(url, responseType, uriVars);
    }

    public <T> ResponseEntity<T> getEntity(String servicePath, String path, Class<T> responseType, Object... uriVars) {
        String url = servicePath + path;
        return restTemplate.getForEntity(url, responseType, uriVars);
    }

    public <T, R> R post(String servicePath, String path, T body, Class<R> responseType, Object... uriVars) {
        String url = servicePath + path;
        return restTemplate.postForObject(url, body, responseType, uriVars);
    }
}
