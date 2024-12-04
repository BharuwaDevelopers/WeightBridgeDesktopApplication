package weightmachinedesktopapplication;

import com.google.gson.annotations.SerializedName;

public class VehicleType {
    @SerializedName("code")
    private String code;
    @SerializedName("type_code")
    private String typeCode;
    @SerializedName("subtype_desc")
    private String subtypeDesc;
    @SerializedName("weighing_rate")
    private String weighingRate;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setSubtypeDesc(String subtypeDesc) {
        this.subtypeDesc = subtypeDesc;
    }

    public String getSubtypeDesc() {
        return subtypeDesc;
    }

    public void setWeighingRate(String weighingRate) {
        this.weighingRate = weighingRate;
    }

    public String getWeighingRate() {
        return weighingRate;
    }
}
