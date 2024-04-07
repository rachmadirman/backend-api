package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "api_url", "client_id", "token", "environment" })
public class UserConfig {

    @JsonProperty("api_url")
    private String  apiurl;
    @JsonProperty("client_id")
    private String  clientid;
    @JsonProperty("token")
    private String  token;
    @JsonProperty("environment")
    private String  environment;
}
