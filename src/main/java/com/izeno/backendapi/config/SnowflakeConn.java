package com.izeno.backendapi.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

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
}
