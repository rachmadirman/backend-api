package com.izeno.backendapi.usecase;

import com.izeno.backendapi.config.SnowflakeConn;
import com.izeno.backendapi.repository.SnowflakeRepository;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.sql.Connection;


@Component
@Slf4j
public class DownloadUsecase {

    @Autowired
    SnowflakeRepository repository;

    public InputStreamResource downloadCSVFromSnowflake(String filename)  {

        try {
            Connection connection = SnowflakeConn.snowflakeConnection();
            InputStream out = connection.unwrap(SnowflakeConnection.class).downloadStream("MM2_STAGE", filename, false);

            return new InputStreamResource(out);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
