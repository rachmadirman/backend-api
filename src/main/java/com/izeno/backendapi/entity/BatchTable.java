package com.izeno.backendapi.entity;

import lombok.Data;

@Data
public class BatchTable {

    private String batchid;
    private String csvfilename;
    private String start_date;
    private String end_date;
    private String status;
    private String submissionuuid;
}
