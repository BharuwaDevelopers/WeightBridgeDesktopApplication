package com.bspl.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("insert")
public class WS_Insert {
    @POST
    @Consumes("application/json")
    public String insert(String deatils) throws JSONException, Exception {
    RestAdapter restadapter=new RestAdapter();
    return restadapter.setInsertDetails(deatils);
    }
}
