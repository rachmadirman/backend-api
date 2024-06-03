package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"einvoicedocument","einvoicenumber","requestdate","validationdate","validationstatus","reason"})
public class BatchDetailTableAll {

    @JsonProperty("einvoicenumber")
    private String einvoicenumber;

    @JsonProperty("einvoicedocument")
    private String einvoicedocument;

    @JsonProperty("requestdate")
    private String requestdate;

    @JsonProperty("validationdate")
    private String validationdate;

    @JsonProperty("validationstatus")
    private String validationstatus;

    @JsonProperty("reason")
    private String reason;
}
