package com.izeno.backendapi.controller;

import com.izeno.backendapi.config.SnowflakeConn;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeConnection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;


@Slf4j
@Component
public class FileController {


    @Scheduled(cron = "1 * * * * *")
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

            if (filesInDirectory != null && filesInDirectory.length > 0){
                for (File f : filesInDirectory){
                    //File file = new File(f.getAbsolutePath());
                    File file = new File(f.getPath());

                    FileInputStream fileInputStream = new FileInputStream(file);

                    String filename = f.getName();
                    log.info("FILE NAME : {}",filename);

                    //connection.unwrap(SnowflakeConnection.class).uploadStream("MM2_STAGE","test upload", fileInputStream, "destfile.csv", false);
                    connection.unwrap(SnowflakeConnection.class).uploadStream("MM2_STAGE","", fileInputStream, filename, false);
                    log.info("SUCCESS UPLOAD TO SNOWFLAKE, FILE : {}", filename);
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

