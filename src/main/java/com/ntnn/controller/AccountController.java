package com.ntnn.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnn.api.Request;
import com.ntnn.api.Response;
import com.ntnn.service.CreateAccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/v2/account")
@Slf4j
public class AccountController {

    @Autowired
    private CreateAccountService createAccountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/create")
    @ResponseBody
    public Callable<ResponseEntity<Response>> createUser(@RequestHeader HttpHeaders httpHeaders, @RequestBody final Request request) throws JsonProcessingException {
        MDC.put("correlationID", UUID.randomUUID().toString());
        final Response response = createAccountService.createAccount(request);
        log.info("message response='{}'", parseObjectToString(response));
        return () -> new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    private String parseObjectToString(Object account) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(account);
    }
}
