package com.izeno.backendapi.controller;

import com.izeno.backendapi.config.SnowflakeConn;
import com.izeno.backendapi.entity.CSVData;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeConnection;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;


@Slf4j
@Component
public class FileController {

    @Autowired
    SnowflakeRepository repository;


    //@Scheduled(cron = "1 * * * * *")
    public void uploadFiles()  {


        try {

            log.info("CRONJOB UPLOAD DATA START TIME : {}", CommonUtils.getCurrentDate());

            //create snowflake connection
            Connection connection = SnowflakeConn.snowflakeConnection();

            //String path = "D:\\doc\\done";
            String path = "/opt/doc/done";
            String movePath = "D:\\doc\\done\\uploaded\\";
            //String movePath = "/opt/doc/reserve/";
            File[] filesInDirectory = new File(path).listFiles();
            String [] HEADERS  = {"SupplierName","SupplierTIN","SupplierEmail","BuyerName","BuyerTIN","BuyerEmail",
                    "eInvoiceVersion","eInvoiceTypeCode","eInvoiceNumber","eInvoiceDate","eInvoiceCurrencyCode"};

            if (filesInDirectory != null && filesInDirectory.length > 0){
                for (File f : filesInDirectory){
                    //File file = new File(f.getAbsolutePath());
                    File file = new File(f.getPath());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String filename = f.getName();
                    log.info("FILE NAME : {}",filename);

                    connection.unwrap(SnowflakeConnection.class).uploadStream("MM2_STAGE","", fileInputStream, filename, false);
                    log.info("SUCCESS UPLOAD TO SNOWFLAKE, FILE : {}", filename);

                    /*Read csv files and put to db*/
                    CSVFormat csvFormat = CSVFormat.newFormat(';').builder()
                            .setHeader(HEADERS)
                                    .setSkipHeaderRecord(true)
                                            .build();

                    Reader in = new FileReader(f.getPath());
                    Iterable<CSVRecord> records = csvFormat.parse(in);

                    for (CSVRecord record : records){
                        CSVData csvData = new CSVData();
                        csvData.setSuppliername(record.get("SupplierName"));
                        csvData.setSuppliertin(record.get("SupplierTIN"));
                        csvData.setSupplieremail(record.get("SupplierEmail"));
                        csvData.setBuyername(record.get("BuyerName"));
                        csvData.setBuyertin(record.get("BuyerTIN"));
                        csvData.setBuyeremail(record.get("BuyerEmail"));
                        csvData.setEinvoiceversion(record.get("eInvoiceVersion"));
                        csvData.setEinvoicetypeCode(record.get("eInvoiceTypeCode"));
                        csvData.setEinvoicenumber(record.get("eInvoiceNumber"));
                        csvData.setEinvoicedate(record.get("eInvoiceDate"));
                        csvData.setEinvoicecurrencycode(record.get("eInvoiceCurrencyCode"));

                        repository.insertCSVData(csvData);
                    }

                    /*Delete the files from local*/
                    file.delete();

//                    log.info("MOVE PATH : {}", movePath.concat(filename));

//                    if (file.renameTo(new File(movePath.concat(filename)))){
//                        file.delete();
//                        log.info("SUCCESS MOVED FILE : {}", filename);
//                    }
                }
            }else {
                log.info("DIRECTORY IS EMPTY");
            }

        }catch (Exception ignored){

        }

        log.info("CRONJOB UPLOAD DATA FINISH TIME : {}", CommonUtils.getCurrentDate());

       }

}

