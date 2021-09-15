package com.dohung.img.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ResponseData implements Serializable {

    private Integer status;
    private String message;
    private List<?> list;
    private Object object;
    private Map<String, Object> response;

    public ResponseData(Integer status, Map<String, Object> response) {
        this.status = status;
        this.response = response;
    }

    public ResponseData(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseData(Integer status, String message, List<?> list) {
        this.status = status;
        this.message = message;
        this.list = list;
    }

    public ResponseData(Integer status, String message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public ResponseData(Integer status, List<?> list) {
        this.status = status;
        this.list = list;
    }

    public ResponseData(Integer status, Object object) {
        this.status = status;
        this.object = object;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
