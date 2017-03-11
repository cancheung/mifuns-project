package com.mifuns.common.web.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by miguangying on 2017/3/9.
 */
public class ResponseEntity<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7090468272956295090L;

    private T data;
    private Date responseDate;
    private int responseCode;
    private String responseMessage = "";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    @Override
    public String toString() {
        return "ResponseEntity{" +
                "data=" + data +
                ", responseDate=" + responseDate +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
