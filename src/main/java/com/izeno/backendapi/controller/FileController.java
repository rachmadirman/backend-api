package com.izeno.backendapi.controller;

import java.io.File;

public class FileController {


    public static String uploadFiles(){

        String path = "D:\\doc\\done";
        File[] filesInDirectory = new File(path).listFiles();
        for (File f : filesInDirectory){
            String filepath = f.getAbsolutePath();
            String fileExtention = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
                if ("csv".equals(fileExtention)){
                    System.out.println("CSV file found -> " + filepath);
                }
            }

        return "testing upload files";
        }

    }

