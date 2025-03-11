package com.bspl.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("trollyDtl")
public class WS_TrollyDetails {
  RestAdapter obj=new RestAdapter();

  @POST
  @Produces("application/json")
  public String getTrollyDetails(String trollyDetails){
     return obj.getTrollyDetails(trollyDetails);
  }
}
