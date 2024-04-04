package com.izeno.backendapi.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForwardRequestV2 {

    @JsonProperty("csvfilename")
    private String csvfilename;

    @JsonProperty("documents")
    private List<Documents> documents;

    @Data
    public static class Documents {
        @JsonProperty("format")
        private String format;
        @JsonProperty("document")
        private String document;
        @JsonProperty("documentHash")
        private String documentHash;
        @JsonProperty("codeNumber")
        private String codeNumber;
    }
}
