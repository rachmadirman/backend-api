package com.izeno.backendapi.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayloadRs {

    @JsonProperty("Status")
    private String status;
    @JsonProperty("StatusCode")
    private int statusCode;
    @JsonProperty("StatusDescription")
    private String statusDescription;
}
