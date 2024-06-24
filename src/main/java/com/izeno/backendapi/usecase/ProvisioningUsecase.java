package com.izeno.backendapi.usecase;

import com.izeno.backendapi.config.SnowflakeConn;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.model.ProvUserReq;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

@Component
@Slf4j
public class ProvisioningUsecase {

    @Autowired
    SnowflakeRepository repository;

    public PayloadRs provUser(ProvUserReq request, String user) throws Exception {

        //init
        PayloadRs response = new PayloadRs();
        String sqlCreateDB = "create database %dbname%".replace("%dbname%", request.getDbname());
        String sqlCreateSchema = "create schema %schemaname%".replace("%schemaname%", request.getSchemaname());
        String sqlCreateBatch = String.format("CREATE TABLE %s.%s.BATCH_TABLE (\n" +
                "    \"batchid\" STRING,\n" +
                "    \"csvfilename\" STRING,\n" +
                "    \"start_date\" STRING,\n" +
                "    \"end_date\" STRING,\n" +
                "    \"status\" STRING(20),\n" +
                "    \"submissionuuid\" STRING,\n" +
                "    \"file_link\" STRING,\n" +
                "    \"submittedby\" STRING(50),\n" +
                "    \"tenantid\" STRING(50)\n" +
                ")", request.getDbname(), request.getSchemaname());

        String sqlCreateBatchDetail = String.format("CREATE TABLE %s.%s.BATCH_DETATIL_TABLE (\n" +
                "    \"einvoicenumber\" STRING(50),\n" +
                "    \"einvoicedocument\" STRING,\n" +
                "    \"requestdate\" STRING(50),\n" +
                "    \"validationdate\" STRING(20),\n" +
                "    \"validationstatus\" STRING(20),\n" +
                "    \"batchid\" STRING,\n" +
                "    \"uuid\" STRING,\n" +
                "    \"reason\" STRING,\n" +
                "    \"submittedby\" STRING(50),\n" +
                "    \"tenantid\" STRING(50)\n" +
                ")", request.getDbname(), request.getSchemaname());

        try {

            //connect to snowflake
            Connection connection = SnowflakeConn.initSnowflake(user, CommonUtils.encPass(user), request.getAccountname());

            Statement statement = connection.createStatement();
            statement.executeQuery(sqlCreateDB);
            statement.executeQuery(sqlCreateSchema);
            statement.executeQuery(sqlCreateBatch);
            statement.executeQuery(sqlCreateBatchDetail);

            //store config
            repository.insertConfig(request.getAccountname(), request.getDbname(), request.getSchemaname(), user);

            connection.close();

            response.setStatus("Success");
            response.setStatusCode(200);
            response.setStatusDescription("Success Provisiong New user");

        }catch (Exception e){
            e.printStackTrace();
            log.error("[ERROR IN PROVISIONING NEW USER : {}]", e.getMessage());
            throw new Exception(e);
        }
        return response;
    }
}
