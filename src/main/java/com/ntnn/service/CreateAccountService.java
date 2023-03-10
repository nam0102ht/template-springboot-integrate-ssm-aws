package com.ntnn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnn.api.Request;
import com.ntnn.api.Response;
import com.ntnn.entity.Account;
import com.ntnn.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountService {
    private final AccountRepository accountRepository;
    private final Queue queueAccount;
    private final RabbitTemplate rabbitTemplate;

    public Response createAccount(Request request) throws JsonProcessingException {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setDatOfBirth(request.getDatOfBirth() == null ? new Date() : request.getDatOfBirth());
        account.setActive(true);
        Account accountSave = accountRepository.save(account);
        Response response = new Response();
        response.setCode(200);
        response.setStatus("Success");
        response.setData(parseObjectToString(accountSave));
        log.info("Processing message to step payment with data='{}'", parseObjectToString(accountSave));
        rabbitTemplate.convertAndSend(queueAccount.getName(), parseObjectToString(accountSave));
        log.info("Sent success");
        return response;
    }

    private String parseObjectToString(Account account) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(account);
    }
}
