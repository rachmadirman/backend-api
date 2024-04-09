package com.izeno.backendapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.entity.BatchTable;
import com.izeno.backendapi.entity.SubmitMockResponse;
import com.izeno.backendapi.entity.UserConfig;
import com.izeno.backendapi.model.ForwardRequestV2;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.service.CSVService;
import com.izeno.backendapi.usecase.DownloadUsecase;
import com.izeno.backendapi.usecase.ForwardDataUsecase;
import com.izeno.backendapi.usecase.SubmitToMock;

import com.izeno.backendapi.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("demo/v1/sapura")
public class MainController {

    @Autowired
    SnowflakeRepository snowflakeRepository;

    @Autowired
    SubmitToMock submitToMock;

    @Autowired
    CSVService csvService;

    @Autowired
    ForwardDataUsecase forwardDataUsecase;

    @Autowired
    DownloadUsecase downloadUsecase;



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

    @GetMapping(value = "/fetch/detailsbyid/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getDetailsDataById(@PathVariable String id,
                                                       HttpServletRequest httpServletRequest) throws Exception {

        List<BatchDetailTable> result = snowflakeRepository.fetchBatchDetailTableById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/download/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Resource> downloadRejectedDoc(@PathVariable String id,
                                                HttpServletRequest httpServletRequest) throws Exception {

        String fileName = "REJECTEDINVOICE-"+ CommonUtils.getCurrentDate()+".csv";
        InputStreamResource file = new InputStreamResource(csvService.load(id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @GetMapping(value = "/csv/download/{filename}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Resource> downloadCSV(@PathVariable String filename,
                                                HttpServletRequest httpServletRequest) throws Exception {

        String fName = "";

        if (filename.contains(".csv")){
            fName = filename;
        }else {
            fName=filename.concat(".csv");
        }

        InputStreamResource file = downloadUsecase.downloadCSVFromSnowflake(fName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }


    @GetMapping(value = "/config/details", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getConfigDetails(HttpServletRequest httpServletRequest) throws Exception {

        List<UserConfig> result = snowflakeRepository.fetchUserConfig();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping(value = "/config/details", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateConfigDetails(@Valid @RequestBody UserConfig request,
                                                 HttpServletRequest httpServletRequest) throws Exception {

        PayloadRs payloadRs = snowflakeRepository.updateUserConfig(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(payloadRs);
    }

    @PostMapping(value = "/forward", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> forwardData(@Valid @RequestBody ForwardRequestV2 request,
            HttpServletRequest httpServletRequest) {

        PayloadRs payloadRs = forwardDataUsecase.forwardRequest(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(payloadRs);
    }



    // only to test mock call
    @GetMapping(value = "/test/mock", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getMock(HttpServletRequest httpServletRequest) throws Exception, JsonProcessingException {

        SubmitMockResponse result = submitToMock.testMock();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

}
