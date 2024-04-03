package com.izeno.backendapi.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.izeno.backendapi.entity.SubmitMockRequest;
import com.izeno.backendapi.entity.SubmitMockResponse;
import com.izeno.backendapi.model.ForwardRequest;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.service.LHDNService;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
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

    public PayloadRs forwardRequest(ForwardRequest request){

        Gson gson = new Gson();
        PayloadRs payloadRs = new PayloadRs();

        SubmitMockRequest lhdnRequest = new SubmitMockRequest();
        List<SubmitMockRequest.Documents> documentsList = new ArrayList<>();

        try {
            //Insert to batch table
            String uuid = UUID.randomUUID().toString();  //used as batchid
            int result = repository.insertBatchTable(request.getCsvfilename(), uuid);


            for (ForwardRequest.CsvContent csv : request.getCsvContents()){
                SubmitMockRequest.Documents documents = new SubmitMockRequest.Documents();
                documents.setFormat("JSON");
                documents.setDocument(objectMapper.writeValueAsString(csv));
                documents.setDocumentHash("Base64");
                documents.setCodeNumber(csv.getEinvoicenumber());

                documentsList.add(documents);
            }

            lhdnRequest.setDocuments(documentsList);

            //Call LDHN Mock API
            SubmitMockResponse response = lhdnService.submitDocToLHDN(lhdnRequest);

            //Insert into batch table details
            String submissionUID = response.getSubmissionUID();
            String einvoicedocument = CommonUtils.stringToBase64(objectMapper.writeValueAsString(request.getCsvContents()));

            //Accepted Documents
            for (SubmitMockResponse.AcceptedDocuments ad : response.getAcceptedDocuments()){
                int rad = repository.insertBatchDetailsTable(ad.getInvoiceCodeNumber(), einvoicedocument, CommonUtils.getCurrentDate(), "ACCEPTED", uuid, ad.getUuid());
            }

            //Rejected Documents
            for (SubmitMockResponse.RejectedDocuments rd : response.getRejectedDocuments()){
                int rrd = repository.insertBatchDetailsTable(rd.getInvoiceCodeNumber(), einvoicedocument, CommonUtils.getCurrentDate(), "REJECTED", uuid, null);
            }

            //Update Data Table
            int rows = repository.updateBatchTable("COMPLETED", submissionUID, uuid);

            //set success response
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
