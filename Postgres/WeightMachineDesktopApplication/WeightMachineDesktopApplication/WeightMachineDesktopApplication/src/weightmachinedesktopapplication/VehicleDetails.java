package weightmachinedesktopapplication;

import com.google.gson.annotations.SerializedName;

public class VehicleDetails {
  @SerializedName("slip_no")
  private String slipNo;
  @SerializedName("machine_no")
  private String machineNo;
  @SerializedName("token_no")
  private String tokenNo;
  @SerializedName("process_code")
  private String processCode;
  @SerializedName("vehicle_no")
  private String vehicleNo;
  @SerializedName("rc_no")
  private String rcNo;
  @SerializedName("gross_weight")
  private String grossWeight;
  @SerializedName("tere_weight")
  private String tereWeight;
  @SerializedName("net_weight")
  private String netWeight;
  @SerializedName("final_entered_by")
  private String finalEnteredBy;
  @SerializedName("trolley_no")
  private String trolleyNo;
  @SerializedName("charge")
  private String charge;
  private String party;
  private String product;
  @SerializedName("gate_entry_number")
  private String gateEntryNumber;
  private String remarks;
  @SerializedName("created_by")
  private String createdBy;
  @SerializedName("creation_date")
  private String creationDate;
  @SerializedName("creation_time")
  private String creationTime;
  @SerializedName("comp_veh_type_code")
  private String compVehTypeCode;
  @SerializedName("charge_applicable")
  private String charge_applicable;
  @SerializedName("veh_subtype_desc")
  private String veh_subtype_desc;
  @SerializedName("trolly_req")
  private String trolly_req;
  @SerializedName("ft_tere_weight")
  private String ft_tere_weight;

  public void setCharge_applicable(String charge_applicable) {
    this.charge_applicable = charge_applicable;
  }

  public String getCharge_applicable() {
    return charge_applicable;
  }

  public void setSlipNo(String slipNo) {
    this.slipNo = slipNo;
  }

  public String getSlipNo() {
    return slipNo;
  }

  public void setMachineNo(String machineNo) {
    this.machineNo = machineNo;
  }

  public String getMachineNo() {
    return machineNo;
  }

  public void setTokenNo(String tokenNo) {
    this.tokenNo = tokenNo;
  }

  public String getTokenNo() {
    return tokenNo;
  }

  public void setProcessCode(String processCode) {
    this.processCode = processCode;
  }

  public String getProcessCode() {
    return processCode;
  }

  public void setVehicleNo(String vehicleNo) {
    this.vehicleNo = vehicleNo;
  }

  public String getVehicleNo() {
    return vehicleNo;
  }

  public void setRcNo(String rcNo) {
    this.rcNo = rcNo;
  }

  public String getRcNo() {
    return rcNo;
  }

  public void setGrossWeight(String grossWeight) {
    this.grossWeight = grossWeight;
  }

  public String getGrossWeight() {
    return grossWeight;
  }

  public void setTereWeight(String tereWeight) {
    this.tereWeight = tereWeight;
  }

  public String getTereWeight() {
    return tereWeight;
  }

  public void setNetWeight(String netWeight) {
    this.netWeight = netWeight;
  }

  public String getNetWeight() {
    return netWeight;
  }

  public void setFinalEnteredBy(String finalEnteredBy) {
    this.finalEnteredBy = finalEnteredBy;
  }

  public String getFinalEnteredBy() {
    return finalEnteredBy;
  }

  public void setTrolleyNo(String trolleyNo) {
    this.trolleyNo = trolleyNo;
  }

  public String getTrolleyNo() {
    return trolleyNo;
  }

  public void setCharge(String charge) {
    this.charge = charge;
  }

  public String getCharge() {
    return charge;
  }

  public void setParty(String party) {
    this.party = party;
  }

  public String getParty() {
    return party;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getProduct() {
    return product;
  }

  public void setGateEntryNumber(String gateEntryNumber) {
    this.gateEntryNumber = gateEntryNumber;
  }

  public String getGateEntryNumber() {
    return gateEntryNumber;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationTime(String creationTime) {
    this.creationTime = creationTime;
  }

  public String getCreationTime() {
    return creationTime;
  }

  public void setCompVehTypeCode(String compVehTypeCode) {
    this.compVehTypeCode = compVehTypeCode;
  }

  public String getCompVehTypeCode() {
    return compVehTypeCode;
  }

  public void setVeh_subtype_desc(String veh_subtype_desc) {
    this.veh_subtype_desc = veh_subtype_desc;
  }

  public String getVeh_subtype_desc() {
    return veh_subtype_desc;
  }

  public void setTrolly_req(String trolly_req) {
    this.trolly_req = trolly_req;
  }

  public String getTrolly_req() {
    return trolly_req;
  }

  public void setFt_tere_weight(String ft_tere_weight) {
    this.ft_tere_weight = ft_tere_weight;
  }

  public String getFt_tere_weight() {
    return ft_tere_weight;
  }
}
