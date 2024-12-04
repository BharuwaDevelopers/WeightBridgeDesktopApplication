package com.bspl.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("printslip")
public class WS_PrintSlip {
    @POST
    @Consumes("application/json")
    public String getPrintSlipDetails(String slipNo) throws JSONException, Exception{
         RestAdapter restadapter=new RestAdapter();
         return restadapter.getPrintDetails(slipNo);
    }
}
