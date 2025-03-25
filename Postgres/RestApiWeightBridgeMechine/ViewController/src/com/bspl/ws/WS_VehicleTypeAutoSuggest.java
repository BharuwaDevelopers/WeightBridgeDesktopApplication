package com.bspl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import javax.ws.rs.QueryParam;

import org.json.JSONException;

@Path("vehicleType")
public class WS_VehicleTypeAutoSuggest {
  @GET
  @Produces("application/json")
  public String getVehicleTypeAutoSuggest(@QueryParam("machineCode") String machineCode) throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getVehicleTypeAutoSuggest(machineCode);
    }
}
