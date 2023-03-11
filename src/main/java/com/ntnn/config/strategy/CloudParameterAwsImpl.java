package com.ntnn.config.strategy;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnn.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@Profile({"!dev & !test & !local"})
public class CloudParameterAwsImpl implements CloudParameterStore {
    private final AWSSimpleSystemsManagement awsParamStore;
    private final Params params;

    public CloudParameterAwsImpl(AWSSimpleSystemsManagement awsParamStore, Params params) {
        this.awsParamStore = awsParamStore;
        this.params = params;
        log.info("Project is getting from profile dev with values get from CloudParameterAwsImpl");
    }

    @Override
    public Params authorMq() {
        Params auth = new Params();
        List<Params.UserMq> lst = new ArrayList<>();
        try {
            for (Params.UserMq userMq : params.getMq()) {
                CompletableFuture<String> getUsername = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(userMq.getUsername())
                                        .withWithDecryption(true))
                                .getParameter()
                                .getValue());

                CompletableFuture<String> getPassword = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(userMq.getPassword())
                                        .withWithDecryption(true))
                                .getParameter()
                                .getValue());

                CompletableFuture<String> getHostname = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(userMq.getHostname())
                                        .withWithDecryption(true))
                                .getParameter()
                                .getValue());

                CompletableFuture.allOf(getUsername, getPassword).join();
                Params.UserMq mq = new Params.UserMq();
                mq.setUsername(getUsername.get());
                mq.setPassword(getPassword.get());
                mq.setHostname(getHostname.get());
                lst.add(mq);
            }
            auth.setMq(lst);
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Error with='{}'", ex.getMessage(), ex);
        }
        log.info("Parameters of mq: {}", parseObjectToString(auth));
        return auth;
    }

    @Override
    public Params authorDatabase() {
        List<Params.AuthorDatabase> authorDatabase = params.getDatabase();
        List<Params.AuthorDatabase> authList = new ArrayList<>();
        Params paramsNew = new Params();
        try {
            for (Params.AuthorDatabase db : authorDatabase) {
                CompletableFuture<String> getUsername = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(db.getUsername())
                                        .withWithDecryption(true))
                                .getParameter().getValue());
                CompletableFuture<String> getPassword = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(db.getPassword())
                                        .withWithDecryption(true))
                                .getParameter().getValue());
                CompletableFuture<String> getHostname = CompletableFuture.supplyAsync(() ->
                        awsParamStore.getParameter(new GetParameterRequest()
                                        .withName(db.getHostname())
                                        .withWithDecryption(true))
                                .getParameter().getValue());

                CompletableFuture.allOf(getUsername, getPassword, getHostname).join();

                Params.AuthorDatabase auth = new Params.AuthorDatabase();
                auth.setUsername(getUsername.get());
                auth.setPassword(getPassword.get());
                auth.setHostname(getHostname.get());
                authList.add(auth);
            }
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Error with='{}'", ex.getMessage(), ex);
        }
        paramsNew.setDatabase(authList);
        log.info("Parameters of datase: {}", parseObjectToString(paramsNew));
        return paramsNew;
    }
    private String parseObjectToString(Object account) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            log.error("Error='{}'", e.getMessage(), e);
            return "";
        }
    }
}
