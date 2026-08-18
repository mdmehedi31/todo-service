package com.todom.library.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.todom.library.enums.SuccessOrFailedEnum;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private SuccessOrFailedEnum message;
    private Map<String, Object> other;
    private String customMessage;

    public ApiResponse() {
    }

    public ApiResponse(SuccessOrFailedEnum message, String customMessage) {
        this.message = message;
        this.customMessage = customMessage;
    }

    public ApiResponse(String customeMessage){
        this.customMessage =customMessage;
    }
    public ApiResponse(SuccessOrFailedEnum message) {
        this.message = message;
    }
    public SuccessOrFailedEnum getMessage() {
        return message;
    }

    public void setMessage(SuccessOrFailedEnum message) {
        this.message = message;
    }

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
