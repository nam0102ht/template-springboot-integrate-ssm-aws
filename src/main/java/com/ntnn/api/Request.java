package com.ntnn.api;

import lombok.Data;

import java.util.Date;

@Data
public class Request {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private Date datOfBirth;
    private boolean active;
}
