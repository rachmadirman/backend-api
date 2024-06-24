package com.izeno.backendapi.controller;

import com.izeno.backendapi.entity.UserConfig;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.model.ProvUserReq;
import com.izeno.backendapi.usecase.ProvisioningUsecase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("prov/v1/einvoice")
public class ProvController {

    @Autowired
    ProvisioningUsecase provisioningUsecase;


    @PostMapping(value = "/create/{user}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> provNewUser(@Valid @PathVariable String user,
                                         @RequestBody ProvUserReq request,
                                         HttpServletRequest httpServletRequest) throws Exception {

        PayloadRs payloadRs = provisioningUsecase.provUser(request, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(payloadRs);
    }
}
