package com.bspl.bean;

import java.util.ArrayList;

public class ResponseDataPrintSlipdetails {
    
    ArrayList<PrintSlipDetails> PrintSlipDetailsList;
    

    public ResponseDataPrintSlipdetails(ArrayList<PrintSlipDetails> list) {
         //  this.vehicleDetailslist = list1;
           this.PrintSlipDetailsList = list;
           
       }
}
