package com.izeno.backendapi.entity;

import lombok.Data;

@Data
public class ProvUserEntity {
    private String id;
    private String account;
    private String warehouse;
    private String database;
    private String schema;
    private String user;
    private String role;
    private String creationDate;
    private String updateDate;

}
