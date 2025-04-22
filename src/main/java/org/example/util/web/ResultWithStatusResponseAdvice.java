package org.example.util.web;

import org.example.web.ResultWithStatus;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Component
public class ResultWithStatusResponseAdvice
        implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // only if the controller method is annotated AND returns ResultWithStatus
        return returnType.hasMethodAnnotation(ResponseStatusFromResult.class)
                && ResultWithStatus.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof ResultWithStatus<?>) {
            HttpStatus status = ((ResultWithStatus<?>) body).getStatus();
            response.setStatusCode(status);
        }
        return body;
    }
}
