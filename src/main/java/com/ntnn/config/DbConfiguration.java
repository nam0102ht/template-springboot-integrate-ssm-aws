package com.ntnn.config;

import com.ntnn.config.strategy.CloudParameterStore;
import com.ntnn.config.strategy.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DbConfiguration {
    @Autowired
    private CloudParameterStore cloudParameterStore;

    @Bean
    public DataSource dataSource() {
        Params authorDatabase = cloudParameterStore.authorDatabase();
        List<Params.AuthorDatabase> lst = authorDatabase.getDatabase();
        Params.AuthorDatabase database = lst.get(0);
        return DataSourceBuilder
                .create()
                .url(database.getHostname())
                .password(database.getPassword())
                .username(database.getUsername())
                .build();
    }
}
