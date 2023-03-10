package com.ntnn.api;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String status;
    private String data;
}
