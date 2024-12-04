package com.bspl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("vehicleType")
public class WS_VehicleTypeAutoSuggest {
    @GET
    public String getgetVehicleTypeAutoSuggest() throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getVehicleTypeAutoSuggest();
    }
}
