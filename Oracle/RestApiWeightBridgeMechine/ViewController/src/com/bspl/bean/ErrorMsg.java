package com.bspl.bean;

public class ErrorMsg {
    private int statusCode;
    private boolean success;
    private String message;
    private String slipNo;
    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public String getSlipNo() {
        return slipNo;
    }
   

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
   
}
