package com.izeno.backendapi.controller;

import com.izeno.backendapi.config.SnowflakeConn;
import net.snowflake.client.jdbc.SnowflakeConnection;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FileController {


    public static String uploadFiles() throws Exception {

        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.USER_TABLE";

        //create snowflake connection
        Connection connection = SnowflakeConn.snowflakeConnection();
        Statement statement = connection.createStatement();

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String path = "D:\\doc\\done";
        File[] filesInDirectory = new File(path).listFiles();

        for (File f : filesInDirectory){
            File file = new File(f.getPath());
            FileInputStream fileInputStream = new FileInputStream(file);

            connection.unwrap(SnowflakeConnection.class).uploadStream("MM2_STAGE","test upload", fileInputStream, "destfile.csv", false);
        }

//        for (File f : filesInDirectory){
//            String filepath = f.getAbsolutePath();
//            String fileExtention = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
//                if ("csv".equals(fileExtention)){
//                    System.out.println("CSV file found -> " + filepath);
//                }
//            }

        return "testing upload files";
        }

    }

