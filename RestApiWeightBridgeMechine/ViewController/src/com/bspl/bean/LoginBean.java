package com.bspl.bean;

public class LoginBean {
   private String empname;
    private int statusCode;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
    private boolean success;
    private String msg;
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
  

   

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

   
}
