package org.example.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResultWithStatus<T> {
    private T body;
    private HttpStatus status;
    private List<String> errors;

    public static <T> ResultWithStatus<T> ok(T body) {
        return (ResultWithStatus<T>) ResultWithStatus.builder()
                .body(body)
                .status(HttpStatus.OK)
                .build();
    }

    public static <T> ResultWithStatus<T> ok() {
        return ok(null);
    }

    public static ResultWithStatus error(HttpStatus status) {
        return ResultWithStatus.builder()
                .status(status)
                .build();
    }

    public static ResultWithStatus error(HttpStatus status, String... errors) {
        return ResultWithStatus.builder()
                .status(status)
                .errors(Arrays.stream(errors).toList())
                .build();
    }

}
