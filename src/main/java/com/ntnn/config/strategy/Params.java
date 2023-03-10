package com.ntnn.config.strategy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "infrastructure.services.aws.params")
public class Params {
    private List<UserMq> mq = new ArrayList<>();
    private List<AuthorDatabase> database = new ArrayList<>();

    @Getter
    @Setter
    public static class AuthorDatabase {
        String username;
        String password;
        String hostname;
    }

    @Getter
    @Setter
    public static class UserMq {
        String username;
        String password;
        String hostname;
    }
}
