package com.izeno.backendapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.izeno.backendapi.entity.*;
import com.izeno.backendapi.model.ForwardRequestV2;
import com.izeno.backendapi.model.LoginReq;
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

    @Autowired
    FileController fileController;



    @GetMapping(value = "/fetch/batch/{user}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllData(@PathVariable String user,
                                        HttpServletRequest httpServletRequest) throws Exception {

        //List<BatchTable> result = snowflakeRepository.fetchBatchTable();
        log.info("**START FETCH BATCH DATA**");
        List<BatchTable> result = forwardDataUsecase.fetchBatchData(user);
        log.info("**FINISH FETCH BATCH DATA**");

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/fetch/details/{user}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getDetailsData(@PathVariable String user,
            HttpServletRequest httpServletRequest) throws Exception {

        //List<BatchDetailTable> result = snowflakeRepository.fetchBatchDetailTable();
        List<BatchDetailTable> result = forwardDataUsecase.fetchBatchDetail(user);
        log.info("**FINISH FETCH BATCH DETAILS**");

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/fetch/detailsbyid/{id}/{user}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getDetailsDataById(@PathVariable String id,
                                                       @PathVariable String user,
                                                       HttpServletRequest httpServletRequest) throws Exception {

        //List<BatchDetailTable> result = snowflakeRepository.fetchBatchDetailTableById(id);
        List<BatchDetailTableAll> result = forwardDataUsecase.fetchBatchDetailById(id, user);
        log.info("**FINISH FETCH BATCH DETAILS BY ID**");

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/fetch/detailsbyinvno/{invno}/{user}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getDetailsDataByInvNo(@PathVariable String invno,
                                                @PathVariable String user,
                                                HttpServletRequest httpServletRequest) throws Exception {

        List<BatchDetailTableByInvoice> result = forwardDataUsecase.fetchBatchDetailByinvNo(invno, user);
        log.info("**FINISH FETCH BATCH DETAILS BY INVNO**");

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

    @PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> validateLogin(@RequestBody LoginReq request,
                                         HttpServletRequest httpServletRequest)  {

        PayloadRs payloadRs = forwardDataUsecase.login(request);

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

    // only to test sceduler call
    @GetMapping(value = "/test/scheduler", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getScheduler(HttpServletRequest httpServletRequest) throws Exception, JsonProcessingException {

        fileController.uploadFiles();

        return ResponseEntity.status(HttpStatus.OK)
                .body("Done call scheduler api");
    }

}
