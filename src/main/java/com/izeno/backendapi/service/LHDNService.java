package com.izeno.backendapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.izeno.backendapi.config.RestTemplateConfig;
import com.izeno.backendapi.entity.SubmitMockRequest;
import com.izeno.backendapi.entity.SubmitMockResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@Slf4j
public class LHDNService {

    @Autowired
    RestTemplateConfig restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public SubmitMockResponse submitDocToLHDN(SubmitMockRequest requests) throws Exception {

        SubmitMockResponse response = new SubmitMockResponse();
        String responseBody = "";
        ResponseEntity<String> responseEntity;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Gson gson = new Gson();

        HttpEntity<?> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(requests), httpHeaders);
        String url = "https://api.mockfly.dev/mocks/f40d6b62-65a1-4f7f-8e57-44ce8265127f/v1/api/mock/submit";

        try {

            responseEntity = restTemplate.restTemplate().exchange(url, HttpMethod.POST, httpEntity, String.class);
            responseBody = responseEntity.getBody();

            log.info("[SUCCESS SUBMIT TO LHDN : {}]", responseBody);

            response = gson.fromJson(responseBody, SubmitMockResponse.class);

            return response;


        } catch (Exception e) {
            log.error("[ERROR SUBMIT TO LHDN : {}]", e.getMessage());
            throw new Exception(e);
        }

    }
}
