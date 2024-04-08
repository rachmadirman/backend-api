package com.izeno.backendapi.service;

import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.repository.SnowflakeRepository;
import com.izeno.backendapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Slf4j
public class CSVService {

    @Autowired
    SnowflakeRepository repository;

    public ByteArrayInputStream load(String id) throws Exception {

        List<BatchDetailTable> lists = repository.fetchBatchDetailTableByIdRejected(id);

        ByteArrayInputStream in = CommonUtils.streamToCSV(lists);

        log.info("SUCCESS CREATE INPUT STREAM");

        return in;

    }
}
