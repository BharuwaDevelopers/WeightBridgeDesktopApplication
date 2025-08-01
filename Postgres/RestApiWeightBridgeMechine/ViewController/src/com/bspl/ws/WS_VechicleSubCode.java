package com.bspl.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("vehicleCode")
public class WS_VechicleSubCode {
    @POST
    @Consumes("application/json")
    public String getVehicleCodeDetails(String subTypeName) throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getVehicleSubCodeDetails(subTypeName);
    }
}
