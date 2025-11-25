package com.example.workaccounting.application.dto;

import java.util.List;

public record ApiErrorDto(
        String exceptionName,
        String exceptionMessage,
        String userMessage,
        Integer statusCode,
        List<String> stackTrace,
        String timestamp,
        String path
) {
}
