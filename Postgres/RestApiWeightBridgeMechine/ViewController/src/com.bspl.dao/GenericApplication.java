package com.bspl.dao;

import com.bspl.ws.WS_Insert;
import com.bspl.ws.WS_Login;

import com.bspl.ws.WS_PrintSlip;
import com.bspl.ws.WS_ReportsDetails;
import com.bspl.ws.WS_TrollyDetails;
import com.bspl.ws.WS_Update;
import com.bspl.ws.WS_VehicleDetails;

import com.bspl.ws.WS_VehicleTypeAutoSuggest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("resources")
public class GenericApplication extends Application {
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();

    // Register root resources.
    classes.add(WS_VehicleTypeAutoSuggest.class);
    classes.add(WS_VehicleDetails.class);
    classes.add(WS_ReportsDetails.class);
    classes.add(WS_Insert.class);
    classes.add(WS_Update.class);
    classes.add(WS_TrollyDetails.class);
    classes.add(WS_PrintSlip.class);
    classes.add(WS_Login.class);

    // Register provider classes.

    return classes;
  }
}
