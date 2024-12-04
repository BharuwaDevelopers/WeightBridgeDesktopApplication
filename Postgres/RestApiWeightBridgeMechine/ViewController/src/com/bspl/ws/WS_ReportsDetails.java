package com.bspl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;

@Path("reports")
public class WS_ReportsDetails {
    @GET
    @Produces("application/json")
    public String getReport() throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getReportsDetails();
    }
}
