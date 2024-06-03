package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "einvoice_number", "request_date time"})
public class BatchDetailTable {
    @JsonProperty("einvoice_number")
    private String einvoicenumber;
    @JsonProperty("request_date time")
    private String requestdate;
}
