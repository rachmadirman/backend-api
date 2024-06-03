package com.izeno.backendapi.model;

import lombok.Data;

@Data
public class LoginReq {

    private String username;
    private String password;
}
