package com.izeno.backendapi.entity;

import lombok.Data;

@Data
public class BatchDetailTable {

    private String einvoicenumber;
    private String einvoicedocument;
    private String requestdate;
    private String validationstatus;
    private String batchid;
    private String uuid;
}
