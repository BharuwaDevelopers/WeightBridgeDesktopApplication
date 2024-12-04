package com.bspl.bean;

import java.util.ArrayList;

public class ResponseData {
   // ArrayList<VehilcleDetails> vehicleDetailslist;
    ArrayList<VehicleTypesDetails> vehicleTypeList;
    ArrayList<AutoSuggestDetails> autosuggestList;

    public ResponseData(ArrayList<VehicleTypesDetails> list2, ArrayList<AutoSuggestDetails> list3) {
         //  this.vehicleDetailslist = list1;
           this.vehicleTypeList = list2;
           this.autosuggestList = list3;
       }
}
