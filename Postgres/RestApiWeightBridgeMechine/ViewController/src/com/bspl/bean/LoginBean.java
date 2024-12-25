package com.bspl.bean;

public class LoginBean {
   private String empname;
    private int statusCode;
    private boolean success;
    private String msg;
    private String unitCd;

    public void setUnitCd(String unitCd) {
        this.unitCd = unitCd;
    }

    public String getUnitCd() {
        return unitCd;
    }
    private String ipAddress;
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }
   

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
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
  

   

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

   
}
