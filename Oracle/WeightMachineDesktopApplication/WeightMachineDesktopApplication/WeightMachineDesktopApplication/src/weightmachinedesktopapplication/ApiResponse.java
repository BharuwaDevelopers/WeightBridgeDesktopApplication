package weightmachinedesktopapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("vehicleDetailslist")
    private List<VehicleDetails> vehicleDetailsList;
    @SerializedName("vehicleTypeList")
    private List<VehicleType> vehicleTypeList;
    @SerializedName("autosuggestList")
    private List<Autosuggest> autosuggestList;
    
    @SerializedName("printSlipDetailsList")
    private List<PrintSlipDetails> printSlipDetailsList;
    
    public void setVehicleDetailsList(List<VehicleDetails> vehicleDetailsList) {
        this.vehicleDetailsList = vehicleDetailsList;
    }

    public List<VehicleDetails> getVehicleDetailsList() {
        return vehicleDetailsList;
    }

    public void setVehicleTypeList(List<VehicleType> vehicleTypeList) {
        this.vehicleTypeList = vehicleTypeList;
    }

    public List<VehicleType> getVehicleTypeList() {
        return vehicleTypeList;
    }

    public void setAutosuggestList(List<Autosuggest> autosuggestList) {
        this.autosuggestList = autosuggestList;
    }

    public List<Autosuggest> getAutosuggestList() {
        return autosuggestList;
    }

    public void setPrintSlipDetailsList(List<PrintSlipDetails> printSlipDetailsList) {
        this.printSlipDetailsList = printSlipDetailsList;
    }

    public List<PrintSlipDetails> getPrintSlipDetailsList() {
        return printSlipDetailsList;
    }
}
