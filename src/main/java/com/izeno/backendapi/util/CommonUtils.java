package com.izeno.backendapi.util;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getCurrentDate(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");

        return currentDateTime.atZone(zoneId).format(formatter);
    }
}
