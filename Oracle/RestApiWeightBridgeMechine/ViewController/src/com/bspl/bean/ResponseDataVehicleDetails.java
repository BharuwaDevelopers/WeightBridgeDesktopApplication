package com.bspl.bean;

import java.util.ArrayList;

public class ResponseDataVehicleDetails {
    public ResponseDataVehicleDetails() {
        super();
    }
    
     ArrayList<VehilcleDetails> vehicleDetailslist;
   

     public ResponseDataVehicleDetails(ArrayList<VehilcleDetails> list) {
          //  this.vehicleDetailslist = list1;
            this.vehicleDetailslist = list;
            
        }
}
