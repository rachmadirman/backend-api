package com.izeno.backendapi.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izeno.backendapi.entity.SubmitMockRequest;
import com.izeno.backendapi.entity.SubmitMockResponse;
import com.izeno.backendapi.model.ForwardRequest;
import com.izeno.backendapi.model.ForwardRequestV2;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.service.LHDNService;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ForwardDataUsecase {

    @Autowired
    SnowflakeRepository repository;

    @Autowired
    LHDNService lhdnService;

    @Autowired
    ObjectMapper objectMapper;

    public PayloadRs forwardRequest(ForwardRequestV2 request) {

        PayloadRs payloadRs = new PayloadRs();

        SubmitMockRequest lhdnRequest = new SubmitMockRequest();
        List<SubmitMockRequest.Documents> documentsList = new ArrayList<>();

        String filename = StringUtils.substringBefore(request.getCsvfilename(), ".csv");

        try {
            // Insert to batch table
            String uuid = UUID.randomUUID().toString(); // used as batchid
            int result = repository.insertBatchTable(StringUtils.substringBefore(request.getCsvfilename(), ".csv"), uuid);


            // Call LDHN Mock API
            SubmitMockResponse response = lhdnService.submitDocToLHDN(request.getDocuments());

            // Insert into batch table details
            String submissionUID = response.getSubmissionUID();


            // Accepted Documents
            for (SubmitMockResponse.AcceptedDocuments ad : response.getAcceptedDocuments()) {
                int rad = repository.insertBatchDetailsTable(ad.getInvoiceCodeNumber(), filename,
                        CommonUtils.getCurrentDate(), "ACCEPTED", uuid, ad.getUuid(), "");
            }

            // Rejected Documents
            for (SubmitMockResponse.RejectedDocuments rd : response.getRejectedDocuments()) {
                SubmitMockResponse.Error error = rd.getError();

                String erros = error.getCode().concat("-").concat(error.getMessage());

                int rrd = repository.insertBatchDetailsTable(rd.getInvoiceCodeNumber(), filename,
                        CommonUtils.getCurrentDate(), "REJECTED", uuid, null, erros);
            }

            // Update Data Table
            int rows = repository.updateBatchTable("COMPLETED", submissionUID, uuid);

            // set success response
            payloadRs.setStatus("Success");
            payloadRs.setStatusCode(200);
            payloadRs.setStatusDescription("Success Forward Data");

            return payloadRs;

        } catch (Exception e) {
            log.error("[ERROR IN FORWARD DATA : {}]", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
