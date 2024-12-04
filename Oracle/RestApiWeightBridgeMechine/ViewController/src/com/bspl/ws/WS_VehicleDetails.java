package com.bspl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;

@Path("vehicleDetails")
public class WS_VehicleDetails {
    @GET
    @Produces("application/json")
    public String getOnLoadDetails() throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getVehicleDetails();
    }
}
