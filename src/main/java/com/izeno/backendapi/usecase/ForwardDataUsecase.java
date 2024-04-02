package com.izeno.backendapi.usecase;

import com.izeno.backendapi.entity.BatchTable;
import com.izeno.backendapi.model.ForwardRequest;
import com.izeno.backendapi.repository.SnowflakeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ForwardDataUsecase {

    @Autowired
    SnowflakeRepository repository;

    public String fetchBatchTableData(ForwardRequest request){

        try {
            //Insert to batch table
            String uuid = UUID.randomUUID().toString();
            int result = repository.insertBatchTable(request.getCsvfilename(), uuid);

            //Call LDHN Mock API


            //Update Data Table


            for (ForwardRequest.CsvContent csv : request.getCsvContents()){
                // Insert Data to Snowflake
                repository.
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";

    }
}
