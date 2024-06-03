package com.izeno.backendapi.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izeno.backendapi.config.SnowflakeConn;
import com.izeno.backendapi.entity.*;
import com.izeno.backendapi.model.ForwardRequest;
import com.izeno.backendapi.model.ForwardRequestV2;
import com.izeno.backendapi.model.LoginReq;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.service.LHDNService;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
            int result = repository.insertBatchTable(StringUtils.substringBefore(request.getCsvfilename(), ".csv"), uuid, request.getCsvfilename());


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

    public PayloadRs login(LoginReq request)  {
        //init
        String sql = "SELECT * FROM  POC_SAPURA.API_INGESTION.USER_ROLE WHERE \"username\" = '%USERNAME%'".replace("%USERNAME%", request.getUsername());
        PayloadRs payloadRs = new PayloadRs();
        try {

            //validate user and password
            Connection connection = SnowflakeConn.snowflakeConnection(request.getUsername(), request.getPassword(), CommonUtils.account(request.getUsername()));

            //Validate connecttion
            if (connection.isValid(30)){
                payloadRs.setStatus("Success");
                payloadRs.setStatusCode(200);
                payloadRs.setStatusDescription("Success Login in");
            }else {
                payloadRs.setStatus("Error");
                payloadRs.setStatusCode(400);
                payloadRs.setStatusDescription("Failed Login in");
            }

//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery(sql);
//            if (rs.next()){
//                String password = String.valueOf(rs.getObject("password"));
//                if (password.equalsIgnoreCase(request.getPassword())){
//                    // set success response
//                    payloadRs.setStatus("Success");
//                    payloadRs.setStatusCode(200);
//                    payloadRs.setStatusDescription("Success Login in");
//
//                    log.info("SUCCESS LOGIN WITH USER : {}", request.getUsername());
//                }else {
//                    // set error response
//                    payloadRs.setStatus("Error");
//                    payloadRs.setStatusCode(400);
//                    payloadRs.setStatusDescription("Failed Login in");
//                }
//            }else {
//                // set error response
//                payloadRs.setStatus("Error");
//                payloadRs.setStatusCode(400);
//                payloadRs.setStatusDescription("Failed Login in");
//            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return payloadRs;
    }

    public List<BatchTable> fetchBatchData(String user)  {
        //init
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.BATCH_TABLE ORDER BY \"start_date\" DESC ";
        List<BatchTable> result = new ArrayList<>();
        try {

            //validate user and password
            Connection connection = SnowflakeConn.snowflakeConnection(user, CommonUtils.encPass(user), CommonUtils.account(user));
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                BatchTable b = new BatchTable();
                b.setBatchid(String.valueOf(rs.getObject("batchid")));
                b.setCsvfilename(String.valueOf(rs.getObject("csvfilename")));
                b.setStart_date(CommonUtils.formatDate(String.valueOf(rs.getObject("start_date"))));
                b.setEnd_date(CommonUtils.formatDate(String.valueOf(rs.getObject("end_date"))));
                b.setStatus(String.valueOf(rs.getObject("status")));
                b.setSubmissionuuid(String.valueOf(rs.getObject("submissionuuid")));
                b.setFileLink(String.valueOf(rs.getObject("file_link")));

                result.add(b);
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<BatchDetailTable> fetchBatchDetail(String user)  {
        //init
        String sql = "SELECT \"einvoicenumber\", \"requestdate\" FROM EINVOICE.API_INGESTION.BATCH_DETATIL_TABLE order by \"requestdate\" desc;";
        String sql2 = "select \"einvoicenumber\", \"requestdate\" from LHDN_1.API_INGESTION.BATCH_DETATIL_TABLE ORDER BY \"requestdate\" DESC";
        List<BatchDetailTable> result = new ArrayList<>();
        try {

            //validate user and password
            Connection connection = SnowflakeConn.snowflakeConnection(user, CommonUtils.encPass(user), CommonUtils.account(user));
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                BatchDetailTable b = new BatchDetailTable();
                b.setEinvoicenumber(String.valueOf(rs.getObject("einvoicenumber")));
                b.setRequestdate(CommonUtils.formatDate(String.valueOf(rs.getObject("requestdate"))));
                result.add(b);
            }

            //parent user
            if (user.equalsIgnoreCase("Kennard")){
                Statement statement2 = connection.createStatement();
                ResultSet rs2 = statement2.executeQuery(sql2);
                while (rs2.next()){
                    BatchDetailTable b = new BatchDetailTable();
                    b.setEinvoicenumber(String.valueOf(rs2.getObject("einvoicenumber")));
                    b.setRequestdate(CommonUtils.formatDate(String.valueOf(rs2.getObject("requestdate"))));

                    result.add(b);
                }
            }
            connection.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<BatchDetailTableAll> fetchBatchDetailById(String id, String user)  {
        //init
        String sql = ("SELECT \"einvoicedocument\" ,\"einvoicenumber\", \"requestdate\", \"validationdate\", \"validationstatus\", \"uuid\", \"reason\" FROM EINVOICE.API_INGESTION.BATCH_DETATIL_TABLE WHERE \"validationstatus\" = 'REJECTED' " +
                "and  \"batchid\" = '%id%' ORDER BY \"requestdate\" DESC ").replace("%id%",id);
        List<BatchDetailTableAll> result = new ArrayList<>();
        try {

            //validate user and password
            Connection connection = SnowflakeConn.snowflakeConnection(user, CommonUtils.encPass(user), CommonUtils.account(user));
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                BatchDetailTableAll b = new BatchDetailTableAll();
                b.setEinvoicenumber(String.valueOf(rs.getObject("einvoicenumber")));
                b.setEinvoicedocument(String.valueOf(rs.getObject("einvoicedocument")));
                b.setRequestdate(CommonUtils.formatDate(String.valueOf(rs.getObject("requestdate"))));
                b.setValidationdate(CommonUtils.formatDate(String.valueOf(rs.getObject("validationdate"))));
                b.setValidationstatus(String.valueOf(rs.getObject("validationstatus")));
                b.setReason(String.valueOf(rs.getObject("reason")));

                result.add(b);
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<BatchDetailTableByInvoice> fetchBatchDetailByinvNo(String invNo, String user)  {
        //init
        String sql = "SELECT \"requestdate\", \"validationdate\", \"validationstatus\", \"batchid\", \"uuid\", \"reason\"\n" +
                "FROM EINVOICE.API_INGESTION.BATCH_DETATIL_TABLE where \"einvoicenumber\"='%invNO%' order by \"requestdate\" desc".replace("%invNO%",invNo);
        List<BatchDetailTableByInvoice> result = new ArrayList<>();
        try {
            //validate user and password
            Connection connection = SnowflakeConn.snowflakeConnection(user, CommonUtils.encPass(user), CommonUtils.account(user));
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                BatchDetailTableByInvoice b = new BatchDetailTableByInvoice();
                b.setRequestdate(CommonUtils.formatDate(String.valueOf(rs.getObject("requestdate"))));
                b.setValidationstatus(String.valueOf(rs.getObject("validationstatus")));
                b.setBatchid(String.valueOf(rs.getObject("batchid")));
                b.setUuid(String.valueOf(rs.getObject("uuid")));
                b.setReason(String.valueOf(rs.getObject("reason")));
                b.setValidationdate(CommonUtils.formatDate(String.valueOf(rs.getObject("validationdate"))));

                result.add(b);
            }

            connection.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
