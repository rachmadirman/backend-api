package com.izeno.backendapi.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class CommonUtils {

    public static String getCurrentDate(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");

        return currentDateTime.atZone(zoneId).format(formatter);
    }

    public static String stringToBase64(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
