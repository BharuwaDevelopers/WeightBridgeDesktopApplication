package com.bspl.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("Login")
public class WS_Login {
   
    @POST
    @Consumes("application/json")
    public String getLoginDetails(String empDetails) throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getLoginDetails(empDetails);
    }
   
}
