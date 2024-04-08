//package com.izeno.backendapi.usecase;
//
//import com.izeno.backendapi.entity.BatchDetailTable;
//import com.izeno.backendapi.repository.SnowflakeRepository;
//import com.opencsv.CSVWriter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@Slf4j
//public class DownloadUsecase {
//
//    @Autowired
//    SnowflakeRepository repository;
//
//    public String downloadData(String batchid) throws Exception {
//
//        File file = new File("D:\\doc\\done");
//
//
//            FileWriter outputfile = new FileWriter(file);
//
//            CSVWriter writer = new CSVWriter(outputfile);
//
//            List<BatchDetailTable> batchDetailTables = repository.fetchBatchDetailTableByIdRejected(batchid);
//
//            //Create Header
//            String[] header = {"einvoicenumber", "requestdate",  "validationstatus", "batchid", "reason"};
//
//            List<String[]>  data = new ArrayList<String[]>();
//            data.add(header);
//
//            for (BatchDetailTable b : batchDetailTables){
//                data.add(new String[]{b.getEinvoicenumber(), b.getRequestdate(), b.getValidationstatus(), b.getBatchid(), b.getReason()});
//            }
//
//            writer.writeAll(data);
//            writer.close();
//
//
//            return "";
//    }
//}
