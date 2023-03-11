package com.ntnn.builder;

import lombok.Builder;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

public class RequestHeaderBuilder {
    @Builder(builderClassName = "DefaultBuilder", builderMethodName = "defaultBuilder")
    private static HttpHeaders defaultRequestHeaders(String correlationId) {
        return createRequestHeaders(correlationId);
    }

    private static HttpHeaders createRequestHeaders(String correlationId) {
        HttpHeaders httpHeader = new HttpHeaders();
        if (Objects.nonNull(correlationId)) {
            httpHeader.set("correlationId", correlationId);
        }
        return httpHeader;
    }

    public static class DefaultBuilder {
        private String correlationId = "Test123456";
    }
}
