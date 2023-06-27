package com.turborvip.security.adapter.web.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestData<T> {
    private RestStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)//@JsonInclude : This property will include Json if it different null
    private String userMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String devMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestData(T data){
        this.status = RestStatus.SUCCESS;
        this.data = data;
    }
    public RestData(RestStatus status, String userMessage, T data) {
        this.status = status;
        this.userMessage = userMessage;
        this.data = data;
    }
    public static RestData<?> error(String userMessage , String devMessage) {
        return new RestData<>(RestStatus.ERROR, userMessage, devMessage, null);
    }
    public static RestData<?> error(String userMessage) {
        return new RestData<>(RestStatus.ERROR, userMessage, null);
    }
}