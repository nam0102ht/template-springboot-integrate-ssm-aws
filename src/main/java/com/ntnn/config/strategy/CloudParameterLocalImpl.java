package com.ntnn.config.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev || test || local"})
@Slf4j
public class CloudParameterLocalImpl implements CloudParameterStore {
    private final Params params;
    public CloudParameterLocalImpl(Params params) {
        this.params = params;
        log.info("Project is getting from profile dev with values get from CloudParameterLocal");
    }
    @Override
    public Params authorMq() {
        return params;
    }

    @Override
    public Params authorDatabase() {
        return params;
    }
}
