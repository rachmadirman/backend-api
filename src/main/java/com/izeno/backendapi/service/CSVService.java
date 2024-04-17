package com.izeno.backendapi.service;

import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.entity.CSVData;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Slf4j
public class CSVService {

    @Autowired
    SnowflakeRepository repository;

    public ByteArrayInputStream load(String id) throws Exception {

        /*Get invoice no by batch id for rejected only*/
        List<String> invoicelist = repository.fetchInvoiceNoByIdRejected(id);

        /*create sql to fetch data based on rejected invoice no*/
        int size = invoicelist.size();
        StringBuilder str = new StringBuilder("SELECT * FROM POC_SAPURA.API_INGESTION.CSV_DATA WHERE \"einvoicenumber\" IN ");
        str.append("( ");

        for (int i=0; i<size; i++){
            if (i == size-1){
                str.append("'").append(invoicelist.get(i)).append("'");
            }else {
                str.append("'").append(invoicelist.get(i)).append("', ");
            }
        }
        str.append(" )");


        List<CSVData> listsData = repository.fetchbyInvoiceNo(str.toString());

        ByteArrayInputStream in = CommonUtils.streamToCSV(listsData);

        log.info("SUCCESS CREATE INPUT STREAM");

        return in;

    }
}
