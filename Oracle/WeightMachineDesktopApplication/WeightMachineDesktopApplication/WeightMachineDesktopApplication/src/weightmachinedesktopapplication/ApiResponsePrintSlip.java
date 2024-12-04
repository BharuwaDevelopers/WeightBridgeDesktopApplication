package weightmachinedesktopapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponsePrintSlip {
    @SerializedName("printSlipDetailsList")
    private List<PrintSlipDetails> printSlipDetailsList;

    public void setPrintSlipDetailsList(List<PrintSlipDetails> printSlipDetailsList) {
        this.printSlipDetailsList = printSlipDetailsList;
    }

    public List<PrintSlipDetails> getPrintSlipDetailsList() {
        return printSlipDetailsList;
    }
}
