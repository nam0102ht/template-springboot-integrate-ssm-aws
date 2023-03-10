package com.ntnn.config.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev || test || local"})
@RequiredArgsConstructor
public class CloudParameterLocalImpl implements CloudParameterStore {
    private final Params params;
    @Override
    public Params authorMq() {
        return params;
    }

    @Override
    public Params authorDatabase() {
        return params;
    }
}
