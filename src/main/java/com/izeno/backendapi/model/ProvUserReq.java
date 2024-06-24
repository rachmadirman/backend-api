package com.izeno.backendapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProvUserReq {

    @JsonProperty("accountname")
    private String accountname;
    @JsonProperty("dbname")
    private String dbname;
    @JsonProperty("schemaname")
    private String schemaname;
}
