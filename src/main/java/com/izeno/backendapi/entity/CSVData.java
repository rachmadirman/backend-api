package com.izeno.backendapi.entity;

import lombok.Data;

@Data
public class CSVData {

    private String suppliername;
    private String suppliertin;
    private String supplieremail;
    private String buyername;
    private String buyertin;
    private String buyeremail;
    private String einvoiceversion;
    private String einvoicetypeCode;
    private String einvoicenumber;
    private String einvoicedate;
    private String einvoicecurrencycode;
}
