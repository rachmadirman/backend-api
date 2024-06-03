package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"batchid", "requestdate", "validationdate", "validationstatus", "uuid", "reason"})
public class BatchDetailTableByInvoice {

    @JsonProperty("requestdate")
    private String requestdate;

    @JsonProperty("validationdate")
    private String validationdate;

    @JsonProperty("validationstatus")
    private String validationstatus;

    @JsonProperty("batchid")
    private String batchid;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("reason")
    private String reason;

}
