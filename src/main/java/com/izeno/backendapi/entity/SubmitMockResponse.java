package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitMockResponse {
    @JsonProperty("submissionUID")
    private String submissionUID;
    @JsonProperty("acceptedDocuments")
    private List<AcceptedDocuments> acceptedDocuments;
    @JsonProperty("rejectedDocuments")
    private List<RejectedDocuments> rejectedDocuments;

    @Data
    public static class AcceptedDocuments {
        @JsonProperty("uuid")
        private String uuid;
        @JsonProperty("invoiceCodeNumber")
        private String invoiceCodeNumber;
    }

    @Data
    public static class RejectedDocuments {
        @JsonProperty("invoiceCodeNumber")
        private String invoiceCodeNumber;
        @JsonProperty("error")
        private Error error;
    }

    @Data
    public static class Error {
        @JsonProperty("code")
        private String code;
        @JsonProperty("message")
        private String message;
        @JsonProperty("target")
        private String target;
    }
}
