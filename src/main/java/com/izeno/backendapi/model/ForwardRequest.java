package com.izeno.backendapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForwardRequest {

    @JsonProperty("CsvFileName")
    private String csvfilename;

    @JsonProperty("CsvContent")
    private List<CsvContent> csvContents;


    @Data
    public static class CsvContent{
        @JsonProperty("supplierName")
        private String suppliername;

        @JsonProperty("buyerName")
        private String buyername;

        @JsonProperty("supplierTIN")
        private String suppliertin;

        @JsonProperty("supplierEmail")
        private String supplieremail;

        @JsonProperty("buyerTIN")
        private String buyertin;

        @JsonProperty("buyerEmail")
        private String buyeremail;

        @JsonProperty("eInvoiceVersion")
        private String einvoiceversion;

        @JsonProperty("eInvoiceTypeCode")
        private String einvoicetypecode;

        @JsonProperty("eInvoiceNumber")
        private String einvoicenumber;

        @JsonProperty("eInvoiceDate")
        private String einvoicedate;

        @JsonProperty("eInvoiceCurrencyCode")
        private String einvoicecurrencycode;
    }



}
