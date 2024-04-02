package com.izeno.backendapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.entity.BatchTable;
import com.izeno.backendapi.entity.SubmitMockResponse;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.usecase.SubmitToMock;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("demo/v1/sapura")
public class MainController {

    @Autowired
    SnowflakeRepository snowflakeRepository;

    @Autowired
    SubmitToMock submitToMock;

    @GetMapping(value = "/fetch/batch", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllData(HttpServletRequest httpServletRequest) throws Exception {

        List<BatchTable> result = snowflakeRepository.fetchBatchTable();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/fetch/details", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getDetailsData(HttpServletRequest httpServletRequest) throws Exception {

        List<BatchDetailTable> result = snowflakeRepository.fetchBatchDetailTable();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    // only to test mock call
    @GetMapping(value = "/test/mock", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getMock(HttpServletRequest httpServletRequest) throws Exception, JsonProcessingException {

        SubmitMockResponse result = submitToMock.testMock();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

}
