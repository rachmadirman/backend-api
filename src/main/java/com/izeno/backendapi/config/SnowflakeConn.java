package com.izeno.backendapi.config;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Slf4j
public class SnowflakeConn {

    public static Connection snowflakeConnection() throws Exception {

        Properties props = new Properties();

        String url = "jdbc:snowflake://" + "br00321.ap-southeast-1" + ".snowflakecomputing.com/";
        if (Files.exists(Paths.get("/snowflake/session/token"))) {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", "br00321.ap-southeast-1");
            props.put("authenticator", "OAUTH");
            props.put("token", new String(Files.readAllBytes(Paths.get("/snowflake/session/token"))));
            props.put("warehouse", "COMPUTE_WH");
            props.put("db", "POC_SAPURA");
            props.put("schema", "API_INGESTION");
            url = "jdbc:snowflake://" + "br00321.ap-southeast-1.snowflakecomputing.com" + ":" + "443";
        }else {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", "br00321.ap-southeast-1");
            props.put("user", "Kennard");
            props.put("password", "891102iZeno");
            props.put("warehouse", "COMPUTE_WH");
            props.put("db", "POC_SAPURA");
            props.put("schema", "API_INGESTION");
        }
        return DriverManager.getConnection(url, props);
    }

    public static Connection snowflakeConnection(String username, String password, String account) throws Exception {

        Properties props = new Properties();

        String url = "jdbc:snowflake://" + account +".ap-southeast-1" + ".snowflakecomputing.com/";
        if (Files.exists(Paths.get("/snowflake/session/token"))) {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", "br00321.ap-southeast-1");
            props.put("authenticator", "OAUTH");
            props.put("token", new String(Files.readAllBytes(Paths.get("/snowflake/session/token"))));
            props.put("warehouse", "COMPUTE_WH");
//            props.put("db", "POC_SAPURA");
//            props.put("schema", "API_INGESTION");
            props.put("role", "sysadmin");
            url = "jdbc:snowflake://" + account +".ap-southeast-1.snowflakecomputing.com" + ":" + "443";
        }else {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", account +".ap-southeast-1");
            props.put("user", username);
            props.put("password", password);
            props.put("role", "sysadmin");
            props.put("warehouse", "COMPUTE_WH");
//            props.put("db", "POC_SAPURA");
//            props.put("schema", "API_INGESTION");
        }
        log.info("CURRENT USED URL : {}", url);
        return DriverManager.getConnection(url, props);
    }
}
