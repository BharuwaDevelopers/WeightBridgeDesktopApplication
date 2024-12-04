package com.bspl.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;

@Path("update")
public class WS_Update {
    @POST
    @Consumes("application/json")
    public String update(String jsonString) throws JSONException, Exception {
    RestAdapter restadapter=new RestAdapter();
    return restadapter.setUpdateDetails(jsonString);
    }
}
