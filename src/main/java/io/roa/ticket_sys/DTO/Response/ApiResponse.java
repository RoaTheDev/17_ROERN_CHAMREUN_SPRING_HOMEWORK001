package io.roa.ticket_sys.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        T payload,
        String message,
        String status,
        LocalDateTime timestamp,
        Boolean success
) {
    public static <T> ApiResponse<T> success(T payload, String message, String status) {
        return new ApiResponse<>(
                payload,
                message,
                status,
                LocalDateTime.now(),
                true
        );
    }

    public static <T> ApiResponse<T> fail(String message, String status) {
        return new ApiResponse<>(
                null,
                message,
                status,
                LocalDateTime.now(),
                false
        );
    }
}