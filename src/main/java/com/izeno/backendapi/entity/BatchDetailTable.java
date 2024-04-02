package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"batch_id", "einvoice_number", "einvoice_document", "request_date", "validation_status", "reference_id"})
public class BatchDetailTable {
    @JsonProperty("einvoice_number")
    private String einvoicenumber;
    @JsonProperty("einvoice_document")
    private String einvoicedocument;
    @JsonProperty("request_date")
    private String requestdate;
    @JsonProperty("validation_status")
    private String validationstatus;
    @JsonProperty("batch_id")
    private String batchid;
    @JsonProperty("reference_id")
    private String uuid;
}
