package com.bspl.ws;

import com.bspl.bean.AutoSuggestDetails;
import com.bspl.bean.ErrorMsg;
import com.bspl.bean.LoginBean;
import com.bspl.bean.PrintSlipDetails;
import com.bspl.bean.ReportDetails;
import com.bspl.bean.ResponseData;
import com.bspl.bean.ResponseDataPrintSlipdetails;
import com.bspl.bean.ResponseDataVehicleDetails;
import com.bspl.bean.VehicleTypesDetails;
import com.bspl.bean.VehilcleDetails;
import com.bspl.dao.RestAdapterDao;

import com.google.gson.Gson;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.sql.Timestamp;
import java.sql.Types;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestAdapter {
    
   
    
    public String getLoginDetails(String empLoginDetails) throws JSONException, Exception {
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        String json = "";
        String user_name = null;
        LoginBean logindetails = new LoginBean();
        try {
            if (empLoginDetails == null || empLoginDetails == "") {
                empLoginDetails = "Invalid Json";
            } else {
                JSONObject mJson = new JSONObject(empLoginDetails);
                try {
                    RestAdapterDao obj = new RestAdapterDao();
                    conn = obj.getConnection();
                    String query = "{? = call DEC_PASSWORD_POST(?,?)}";
                    CallableStatement callableStatement = conn.prepareCall(query);
                    callableStatement.registerOutParameter(1, Types.VARCHAR);
                    callableStatement.setString(2, mJson.getString("empName").trim());
                    callableStatement.setString(3, mJson.getString("empPass").trim());
                    callableStatement.execute();
                    
                    
                    //System.out.println("callableStatement.getString(1)--->" + callableStatement.getString(1).toString());
                    if (!callableStatement.getString(1).equals(mJson.getString("empPass").trim())) {
                        logindetails.setStatusCode(500);
                        logindetails.setSuccess(false);
                        logindetails.setMsg("Invalid username or password Login Failed");
                    } else {
                        stmt = conn.createStatement();
                        stmt1 = conn.createStatement();
                        ResultSet rs =
                            stmt.executeQuery("SELECT  u.user_id,u.unit_cd, u.user_name, u.user_fname,    u.emp_id,    u.unit_cd,    u.valid_to,    u.validity,    w.machine_code,    w.ip_address,w.mode, w.comport, w.setting FROM \n" + 
                            "    user_master u JOIN     public.weighing_machine_user_map w ON     u.user_id = w.user_id where u.user_name='" +
                                              mJson.getString("empName").trim() + "'");
                        if (rs.next()) {
                            logindetails.setStatusCode(200);
                            logindetails.setSuccess(true);
                            user_name = rs.getString("USER_NAME");
                            logindetails.setEmpname(user_name);
                            if(rs.getString("ip_address")==null||rs.getString("ip_address")=="" || rs.getString("ip_address").isEmpty()){
                                logindetails.setIpAddress("NA"); 
                            }else{
                                logindetails.setIpAddress(rs.getString("ip_address"));
                                logindetails.setUnitCd(rs.getString("unit_cd")); 
                                logindetails.setMachine_code(rs.getString("machine_code"));
                                logindetails.setComport(rs.getString("comport")); 
                            }
                            logindetails.setMsg("Login Successfully");
                        } else {
                            logindetails.setStatusCode(500);
                            logindetails.setSuccess(false);
                            logindetails.setMsg("Invalid username or password Login Failed");
                        }
                        stmt.close();
                    }


               
                    //System.out.println("password---" + password);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logindetails.setStatusCode(500);
                    logindetails.setSuccess(false);
                    logindetails.setMsg(ex.toString());
                }

                conn.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            logindetails.setStatusCode(500);
            logindetails.setSuccess(false);
            logindetails.setMsg(ex.toString());
        } finally {
            try {

                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return ex.toString();
            }
        }
        // result.setSucess(true);
        Gson gson = new Gson();
        json = gson.toJson(logindetails);
        return json;
    }


    public String getVehicleTypeAutoSuggest() {

        Connection conn = null;
        Statement stmt = null;
        String query = null;
        RestAdapterDao obj = new RestAdapterDao();
        int count = 0;
        VehicleTypesDetails vehicleTypesDetailsObj = null;
        AutoSuggestDetails autoSuggestDetailsobj = null;
        ArrayList<VehicleTypesDetails> vehicleTypeDetailsList = new ArrayList<VehicleTypesDetails>();
        ArrayList<AutoSuggestDetails> autoSuggestDetailsList = new ArrayList<AutoSuggestDetails>();
        try {
            conn = obj.getConnection();
            stmt = conn.createStatement();
            ResultSet rs1 =
                stmt.executeQuery("select V.CODE,V.TYPE_CODE,V.SUBTYPE_DESC,M.WEIGHING_RATE FROM  vehicle_subtype_master  V\n" +
                                  "INNER JOIN WEIGHING_RATE_MASTER M ON V.CODE=M.VEHICLE_SUB_TYPE_CODE\n" +
                                  "WHERE V.STATUS='Y' AND M.STATUS='Y'");
            while (rs1.next()) {
                vehicleTypesDetailsObj = new VehicleTypesDetails();
                if (rs1.getString("SUBTYPE_DESC") == null) {
                    vehicleTypesDetailsObj.setSubtype_desc("0");
                } else {
                    vehicleTypesDetailsObj.setSubtype_desc(rs1.getString("SUBTYPE_DESC"));
                }
                if (rs1.getString("CODE") == null) {
                    vehicleTypesDetailsObj.setCode("0");
                } else {
                    vehicleTypesDetailsObj.setCode(rs1.getString("CODE"));
                }
                if (rs1.getString("TYPE_CODE") == null) {
                    vehicleTypesDetailsObj.setType_code("0");
                } else {
                    vehicleTypesDetailsObj.setType_code(rs1.getString("TYPE_CODE"));
                }
                if (rs1.getString("WEIGHING_RATE") == null) {
                    vehicleTypesDetailsObj.setWeighing_rate("0");
                } else {
                    vehicleTypesDetailsObj.setWeighing_rate(rs1.getString("WEIGHING_RATE"));
                }

                vehicleTypeDetailsList.add(vehicleTypesDetailsObj);
            }

            ResultSet rs2 = stmt.executeQuery("select distinct PARTY,'PARTY'  AS COLUMN_NAME from WEIGHING_BRIDGE union all select distinct PRODUCT,'PRODUCT'  AS COLUMN_NAME from WEIGHING_BRIDGE union all select distinct REMARKS,'REMARKS'  AS COLUMN_NAME from WEIGHING_BRIDGE union all select distinct vehicle_no,'vehicle_no'  AS COLUMN_NAME from WEIGHING_BRIDGE");
            while (rs2.next()) {

                autoSuggestDetailsobj = new AutoSuggestDetails();
                if (rs2.getString("COLUMN_NAME").equalsIgnoreCase("PARTY")) {
                    autoSuggestDetailsobj.setParty(rs2.getString("PARTY"));
                }
                if (rs2.getString("COLUMN_NAME").equalsIgnoreCase("PRODUCT")) {
                    autoSuggestDetailsobj.setProduct(rs2.getString("PARTY"));
                }
                if (rs2.getString("COLUMN_NAME").equalsIgnoreCase("REMARKS")) {
                    autoSuggestDetailsobj.setRemarks(rs2.getString("PARTY"));
                }
                if (rs2.getString("COLUMN_NAME").equalsIgnoreCase("vehicle_no")) {
                    autoSuggestDetailsobj.setVehicleNo(rs2.getString("PARTY"));
                }
                autoSuggestDetailsList.add(autoSuggestDetailsobj);

            }
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception ex) {

            }
        }
        if (count <= 0) {
            //  JOptionPane.showMessageDialog(null, "Please Enter Valid Vehicle no / Slip No", "Message",  JOptionPane.INFORMATION_MESSAGE);






        }


        // Create the custom class instance
        ResponseData responseData = new ResponseData(vehicleTypeDetailsList, autoSuggestDetailsList);

        // Convert the custom class object to JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);


        //        Gson gson = new Gson();
        //        String jsonResponse = gson.toJson(SocietyMstDetailsList);
        return jsonResponse;
    }


    public String getVehicleDetails() {

        Connection conn = null;
        Statement stmt = null;
        String query = null;
        RestAdapterDao obj = new RestAdapterDao();
        int count = 0;
        VehilcleDetails result = null;
        ArrayList<VehilcleDetails> vehicleDetails = new ArrayList<VehilcleDetails>();

        try {
            conn = obj.getConnection();
            stmt = conn.createStatement();

            query = "SELECT * FROM vw_weighing_bridge_penddoc";
            System.out.println("query--" + query);

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count++;
                result = new VehilcleDetails();
                if (rs.getString("VEHICLE_NO") != null) {
                    result.setVehicle_no(rs.getString("VEHICLE_NO"));
                } else {
                    result.setVehicle_no("0");
                }


                if (rs.getString("SLIP_NO") != null) {
                    result.setSlip_no(rs.getString("SLIP_NO"));
                } else {
                    result.setSlip_no("0");
                }

                if (rs.getString("TOKEN_NO") != null) {
                    result.setToken_no(rs.getString("TOKEN_NO"));
                } else {
                    result.setToken_no("0");
                }
                if (rs.getString("PARTY") != null) {
                    result.setParty(rs.getString("PARTY"));
                } else {
                    result.setParty("0");
                }
                if (rs.getString("PRODUCT") != null) {
                    result.setProduct(rs.getString("PRODUCT"));
                } else {
                    result.setProduct("0");
                }
                if (rs.getString("GROSS_WEIGHT") != null) {
                    result.setGross_weight(rs.getString("GROSS_WEIGHT"));

                } else {
                    result.setGross_weight("0");
                }
                if (rs.getString("TERE_WEIGHT") != null) {
                    result.setTere_weight(rs.getString("TERE_WEIGHT"));

                } else {
                    result.setTere_weight("0");
                }
                if (rs.getString("NET_WEIGHT") != null) {
                    result.setNet_weight(rs.getString("NET_WEIGHT"));

                } else {
                    result.setNet_weight("0");
                }
                if (rs.getString("FINAL_ENTERED_BY") != null) {
                    result.setFinal_entered_by(rs.getString("FINAL_ENTERED_BY"));
                } else {
                    result.setFinal_entered_by("0");
                }
                if (rs.getString("TROLLEY_NO") != null) {
                    result.setTrolley_no(rs.getString("TROLLEY_NO"));
                } else {
                    result.setTrolley_no("0");
                }

                if (rs.getString("CHARGE") == null) {
                    result.setCharge("0");
                } else {

                    result.setCharge(rs.getString("CHARGE"));
                }
                if (rs.getString("CHARGE_APPLICABLE") != null) {
                    if (rs.getString("CHARGE_APPLICABLE").equalsIgnoreCase("Y")) {
                        // ComboBoxChargeApplied.setSelectedIndex(0);
                        result.setCharge_applicable(rs.getString("CHARGE_APPLICABLE"));
                    } else {
                        result.setCharge_applicable("N");
                    }
                } else {
                    result.setCharge_applicable("N");
                }
                if (rs.getString("VEH_TYPE_CODE") != null || rs.getString("VEH_TYPE_CODE") != "") {

                    String value = rs.getString("VEH_TYPE_CODE");
                    result.setVeh_type_code(value);

                } else {
                    result.setVeh_type_code("0");
                }
                if (rs.getString("REMARKS") != null) {
                    result.setRemarks(rs.getString("REMARKS"));
                } else {
                    result.setRemarks("0");
                }
                if (rs.getString("MACHINE_NO") != null) {
                    result.setMachine_no(rs.getString("MACHINE_NO"));
                } else {
                    result.setMachine_no("0");
                }
                if (rs.getString("GATE_ENTRY_NUMBER") != null) {
                    result.setGate_entry_number(rs.getString("GATE_ENTRY_NUMBER"));
                } else {
                    result.setGate_entry_number("0");
                }

                if (rs.getString("CREATED_BY") != null) {
                    result.setCreated_by(rs.getString("CREATED_BY"));
                } else {
                    result.setCreated_by("0");
                }
                if (rs.getString("CREATION_DATE") != null) {
                    result.setCreation_date(rs.getString("CREATION_DATE"));
                } else {
                    result.setCreation_date("0");
                }
                if (rs.getString("CREATION_TIME") != null) {
                    result.setCreation_time(rs.getString("CREATION_TIME"));
                } else {
                    result.setCreation_time("0");
                }

                if (rs.getString("PROCESS_CODE") != null) {
                    result.setProcess_code(rs.getString("PROCESS_CODE"));
                } else {
                    result.setProcess_code("0");
                }
                if (rs.getString("RC_NO") != null) {
                    result.setRc_no(rs.getString("RC_NO"));
                } else {
                    result.setRc_no("0");
                }
                if (rs.getString("COMP_VEH_TYPE_CODE") != null) {
                    result.setComp_veh_type_code(rs.getString("COMP_VEH_TYPE_CODE"));
                } else {
                    result.setComp_veh_type_code("0");
                }

                if (rs.getString("VEH_SUBTYPE_DESC") != null) {
                    result.setVeh_subtype_desc(rs.getString("VEH_SUBTYPE_DESC"));
                } else {
                    result.setVeh_subtype_desc("0");
                }

                vehicleDetails.add(result);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception ex) {

            }
        }
        if (count <= 0) {
            //  JOptionPane.showMessageDialog(null, "Please Enter Valid Vehicle no / Slip No", "Message",  JOptionPane.INFORMATION_MESSAGE);






        }


        // Create the custom class instance
        ResponseDataVehicleDetails responseData = new ResponseDataVehicleDetails(vehicleDetails);

        // Convert the custom class object to JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);


        //        Gson gson = new Gson();
        //        String jsonResponse = gson.toJson(SocietyMstDetailsList);
        return jsonResponse;
    }


    public String setInsertDetails(String jsonString) throws JSONException, Exception {

        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        String json = "";
        String insertQuery = "";
        String password = null;
        String user_name = null;
        String chargeApplied = "N";
        CallableStatement callableStatement = null;
        PreparedStatement preparedStatement = null;
        RestAdapterDao obj = new RestAdapterDao();
        if (jsonString == null || jsonString == "") {
            jsonString = "Invalid Json";
        } else {
            ErrorMsg objError = new ErrorMsg();
            JSONObject responseObject = new JSONObject(jsonString);

            // Retrieve the values using getString() method
            String slipNo = responseObject.getString("slip_no");
            String machineNo = responseObject.getString("machine_no");
            String tokenNo = responseObject.getString("token_no");
            String processCode = responseObject.getString("process_code");
            String vehicleNo = responseObject.getString("vehicle_no");
            String vehTypeCode = responseObject.getString("veh_type_code");
            String rcNo = responseObject.getString("rc_no");
            String grossWeight = responseObject.getString("gross_weight");
            String tereWeight = responseObject.getString("tere_weight");
            String netWeight = responseObject.getString("net_weight");
            String enteredBy = responseObject.getString("entered_by");
            String finalEnteredBy = responseObject.getString("final_entered_by");
            String trolleyNo = responseObject.getString("trolley_no");
            String charge = responseObject.getString("charge");
            String chargeApplicable = responseObject.getString("charge_applicable");
            String party = responseObject.getString("party");
            String product = responseObject.getString("product");
            String gateEntryNumber = responseObject.getString("gate_entry_number");
            String remarks = responseObject.getString("remarks");
            String createdBy = responseObject.getString("created_by");
            String unitCd = responseObject.getString("unit_cd");

            // Print the values
            System.out.println("Slip Number: " + slipNo);
            System.out.println("Machine Number: " + machineNo);
            System.out.println("Token Number: " + tokenNo);
            System.out.println("Process Code: " + processCode);
            System.out.println("Vehicle Number: " + vehicleNo);
            System.out.println("Vehicle Type Code: " + vehTypeCode);
            System.out.println("RC Number: " + rcNo);
            System.out.println("Gross Weight: " + grossWeight);
            System.out.println("Tere Weight: " + tereWeight);
            System.out.println("Net Weight: " + netWeight);
            System.out.println("Entered By: " + enteredBy);
            System.out.println("Final Entered By: " + finalEnteredBy);
            System.out.println("Trolley Number: " + trolleyNo);
            System.out.println("Charge: " + charge);
            System.out.println("Charge Applicable: " + chargeApplicable);
            System.out.println("Party: " + party);
            System.out.println("Product: " + product);
            System.out.println("Gate Entry Number: " + gateEntryNumber);
            System.out.println("Remarks: " + remarks);
            System.out.println("Created By: " + createdBy);
            System.out.println("Unit Code: " + unitCd);
            conn = obj.getConnection();
            try {
                // conn.setAutoCommit(false);
                stmt = conn.createStatement();
                String sqlFuncation = "{ ? = call FN_GEN_WBRIDGE_SLIP_NO(?,?) }";
                callableStatement = conn.prepareCall(sqlFuncation);
                callableStatement.registerOutParameter(1,
                                                       java.sql.Types.VARCHAR); // Update type based on the return type
                // Set the input parameters
                callableStatement.setString(2, unitCd); // Replace "inputParameter1" with actual value
                callableStatement.setString(3, machineNo); // Replace "inputParameter2" with actual value
                // Execute the function
                callableStatement.execute();
                // Retrieve the result
                String SlipNo = callableStatement.getString(1);
                System.out.println("Generated Slip Number: " + SlipNo);
                insertQuery =
                    "INSERT INTO WEIGHING_BRIDGE (SLIP_NO,MACHINE_NO,TOKEN_NO,PROCESS_CODE,VEHICLE_NO,VEH_TYPE_CODE,RC_NO,GROSS_WEIGHT,TERE_WEIGHT,NET_WEIGHT,ENTERED_BY,FINAL_ENTERED_BY," +
                    "TROLLEY_NO,CHARGE,CHARGE_APPLICABLE,PARTY,PRODUCT,GATE_ENTRY_NUMBER,REMARKS,CREATED_BY,UNIT_CD) VALUES (?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)";
                preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setString(1, SlipNo.toUpperCase());
                preparedStatement.setString(2, machineNo.toUpperCase());
                preparedStatement.setString(3, tokenNo.toUpperCase());
                preparedStatement.setString(4, processCode.toUpperCase());
                preparedStatement.setString(5, vehicleNo);
                preparedStatement.setString(6, vehTypeCode);
                preparedStatement.setString(7, rcNo.toUpperCase());
                preparedStatement.setInt(8, Integer.parseInt(grossWeight));
                preparedStatement.setInt(9, Integer.parseInt(tereWeight));
                preparedStatement.setInt(10, Integer.parseInt(netWeight));
                preparedStatement.setString(11, createdBy.toUpperCase());
                preparedStatement.setString(12, createdBy.toUpperCase());
                preparedStatement.setString(13, trolleyNo);
                preparedStatement.setInt(14, Integer.parseInt(charge));
                if (chargeApplicable.equalsIgnoreCase("Yes")) {
                    chargeApplied = "Y";
                }
                preparedStatement.setString(15, chargeApplied);
                preparedStatement.setString(16, party);
                preparedStatement.setString(17, product.toUpperCase());
                preparedStatement.setString(18, gateEntryNumber.toUpperCase());
                preparedStatement.setString(19, remarks.toUpperCase());
                preparedStatement.setString(20, createdBy.toUpperCase());
                preparedStatement.setString(21, unitCd);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    objError.setStatusCode(200);
                    objError.setMessage("Record Save Successfully");
                    objError.setSuccess(true);
                    objError.setSlipNo(SlipNo);
                } else {
                    objError.setStatusCode(500);
                    objError.setMessage("Something want to wrong! Please try Again");
                    objError.setSuccess(false);
                    objError.setSlipNo("0");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                objError.setStatusCode(500);
                objError.setMessage(ex.toString());
                objError.setSuccess(false);
                objError.setSlipNo(null);
                objError.setSlipNo("0");
                // WriteToFile(ex.toString());
                // return null;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.getMessage();
                    ex.printStackTrace();
                    objError.setStatusCode(500);
                    objError.setMessage(ex.toString());
                    objError.setSuccess(false);
                    objError.setSlipNo(null);
                    objError.setSlipNo("0");
                    // WriteToFile(ex.toString());
                }
            }

            try {
                Gson gson = new Gson();
                json = gson.toJson(objError);
            } catch (Exception ex) {

            }

        }

        return json;

    }

    public String setUpdateDetails(String jsonString) throws JSONException, Exception {

        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        String json = "";
        String insertQuery = "";
        String password = null;
        String user_name = null;
        String chargeApplied = "N";
        CallableStatement callableStatement = null;
        PreparedStatement preparedStatement = null;
        RestAdapterDao obj = new RestAdapterDao();
        if (jsonString == null || jsonString == "") {
            jsonString = "Invalid Json";
        } else {
            String grossWeight=null;
            ErrorMsg objError = new ErrorMsg();
            JSONObject responseObject = new JSONObject(jsonString);

            // Retrieve the values using getString() method
            String slipNo = responseObject.getString("slip_no");
            String vehTypeCode = responseObject.getString("veh_type_code");
            String rcNo = responseObject.getString("rc_no");
            try{
             grossWeight = responseObject.getString("gross_weight");
            }catch(Exception ex){
                ex.printStackTrace();
            }
            String tereWeight = responseObject.getString("tere_weight");
            String netWeight = responseObject.getString("net_weight");
            String finalEnteredBy = responseObject.getString("final_entered_by");
            String charge = responseObject.getString("charge");
            String chargeApplicable = responseObject.getString("charge_applicable");
            String party = responseObject.getString("party");
            String product = responseObject.getString("product");
            String remarks = responseObject.getString("remarks");
            // Print the values
            System.out.println("Slip Number: " + slipNo);


            // System.out.println("Vehicle Number: " + vehicleNo);
            System.out.println("Vehicle Type Code: " + vehTypeCode);
            System.out.println("RC Number: " + rcNo);
            System.out.println("Gross Weight: " + grossWeight);
            System.out.println("Tere Weight: " + tereWeight);
            System.out.println("Net Weight: " + netWeight);

            System.out.println("Final Entered By: " + finalEnteredBy);

            System.out.println("Charge: " + charge);
            System.out.println("Charge Applicable: " + chargeApplicable);
            System.out.println("Party: " + party);
            System.out.println("Product: " + product);

            System.out.println("Remarks: " + remarks);

            conn = obj.getConnection();
            try {
                // conn.setAutoCommit(false);
                stmt = conn.createStatement();

                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String todayDateTime = currentDateTime.format(formatter1);
                Timestamp timestamp = Timestamp.valueOf(todayDateTime);
                String sqlInsertUpdate =
                    "UPDATE  WEIGHING_BRIDGE set GROSS_WEIGHT=?,TERE_WEIGHT=?,NET_WEIGHT=?,FINAL_ENTERED_BY=?,FINAL_ENTERED_DATE=?,REMARKS=?, LAST_UPDATED_BY=?,LAST_UPDATE_DATE=?,CHARGE_APPLICABLE=?,CHARGE=?,VEH_TYPE_CODE=?,PARTY=?,PRODUCT=?,RC_NO=? where SLIP_NO=? ";
                preparedStatement = conn.prepareStatement(sqlInsertUpdate);
                preparedStatement.setInt(1,Integer.parseInt( grossWeight)); // PROCESS_COD
                preparedStatement.setInt(2, Integer.parseInt(tereWeight)); // GROSS_WEIGHT
                preparedStatement.setInt(3, Integer.parseInt(netWeight)); // TERE_WEIGHT
                preparedStatement.setString(4, finalEnteredBy);
                preparedStatement.setTimestamp(5, timestamp); //
                preparedStatement.setString(6, remarks.toUpperCase()); // REMARKS
                preparedStatement.setString(7, finalEnteredBy.toUpperCase());
                preparedStatement.setTimestamp(8, timestamp);
                if (chargeApplicable.equalsIgnoreCase("Yes")) {
                    chargeApplied = "Y";
                }
                preparedStatement.setString(9, chargeApplied);
                preparedStatement.setInt(10, Integer.parseInt(charge));
                preparedStatement.setString(11, vehTypeCode);
                preparedStatement.setString(12, party.toUpperCase());
                preparedStatement.setString(13, product.toUpperCase());
                preparedStatement.setString(14, rcNo.toUpperCase());
                preparedStatement.setString(15, slipNo); // SLIP_NO (WHERE clause)
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    objError.setStatusCode(200);
                    objError.setMessage("Record Save Successfully");
                    objError.setSuccess(true);
                    objError.setSlipNo(slipNo);
                } else {
                    objError.setStatusCode(500);
                    objError.setMessage("Something want to wrong! Please try Again");
                    objError.setSuccess(false);
                    objError.setSlipNo("0");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                objError.setStatusCode(500);
                objError.setMessage(ex.toString());
                objError.setSuccess(false);
                objError.setSlipNo("0");
                // WriteToFile(ex.toString());
                // return null;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.getMessage();
                    ex.printStackTrace();
                    objError.setStatusCode(500);
                    objError.setMessage(ex.toString());
                    objError.setSuccess(false);
                    objError.setSlipNo("0");
                    // WriteToFile(ex.toString());
                }
            }

            try {
                Gson gson = new Gson();
                json = gson.toJson(objError);
            } catch (Exception ex) {

            }

        }

        return json;

    }


    public String getPrintDetails(String jsonString) {
        Connection conn = null;
        Statement stmt = null;
        String query = null;
        RestAdapterDao obj = new RestAdapterDao();
        int count = 0;
        PrintSlipDetails result = null;
        ArrayList<PrintSlipDetails> printSlipDetails = new ArrayList<PrintSlipDetails>();
        try {

            if (jsonString == null || jsonString == "") {
                jsonString = "Invalid Json";
            } else {

                JSONObject responseObject = new JSONObject(jsonString);
                conn = obj.getConnection();
                stmt = conn.createStatement();
                query =
                    "SELECT B.SLIP_NO, B.MACHINE_NO,COALESCE(B.TOKEN_NO,A.TOKEN_NO) TOKEN_NO, COALESCE(B.UNIT_CD,a.UNIT_CD) UNIT_CD, B.PROCESS_CODE, COALESCE(B.VEHICLE_NO,A.VEHICLE_NO) VEHICLE_NO, B.VEH_TYPE_CODE, B.RC_NO, B.WEIGHING_TYPE, B.GROSS_WEIGHT, \n" +
                    "B.TERE_WEIGHT, B.NET_WEIGHT, B.ENTERED_BY, B.FINAL_ENTERED_BY, B.TROLLEY_NO, B.CHARGE, B.CHARGE_APPLICABLE,\n" +
                    "B.PARTY,PRODUCT, B.GATE_ENTRY_NUMBER, B.REMARKS, B.CREATED_BY, TO_CHAR(B.CREATION_DATE, 'DD/MM/YYYY') CREATION_DATE, TO_CHAR(B.CREATION_DATE, 'HH24:MI:SS') CREATION_TIME,\n" +
                    "TO_CHAR(B.FINAL_ENTERED_DATE, 'DD/MM/YYYY') FINAL_ENTERED_DATE, TO_CHAR(B.FINAL_ENTERED_DATE, 'HH24:MI:SS') FINAL_ENTERED_TIME,V.SUBTYPE_DESC\n" +
                    " FROM TOKEN_INWARD A\n" + " LEFT JOIN WEIGHING_BRIDGE B ON (A.TOKEN_NO = B.TOKEN_NO )\n" +
                    "  LEFT JOIN VEHICLE_SUBTYPE_MASTER V ON (V.CODE = B.VEH_TYPE_CODE )\n" +
                    " WHERE  B.SLIP_NO is not null and SLIP_NO='" + responseObject.getString("slipNo") + "'";
                System.out.println("query--" + query);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    result = new PrintSlipDetails();
                    count++;
                    if (rs.getString("VEHICLE_NO") != null) {

                        result.setVehicleNo(rs.getString("VEHICLE_NO").toUpperCase());

                    }
                    if (rs.getString("SLIP_NO") != null) {
                        result.setSlipNo(rs.getString("SLIP_NO").toUpperCase());
                    }
                    if (rs.getString("TOKEN_NO") != null) {
                        result.setTokenNo(rs.getString("TOKEN_NO").toUpperCase());
                    }
                    if (rs.getString("PARTY") != null) {
                        result.setParty(rs.getString("PARTY").toUpperCase());
                    }
                    if (rs.getString("PRODUCT") != null) {
                        result.setProduct(rs.getString("PRODUCT").toUpperCase());
                    }
                    if (rs.getString("GROSS_WEIGHT") != null) {
                        result.setGrossWeight(rs.getString("GROSS_WEIGHT").toUpperCase());
                    }
                    if (rs.getString("TERE_WEIGHT") != null) {
                        result.setTereWeight(rs.getString("TERE_WEIGHT").toUpperCase());
                    }
                    if (rs.getString("NET_WEIGHT") != null) {
                        result.setNetWeight(rs.getString("NET_WEIGHT").toUpperCase());
                    }
                    if (rs.getString("FINAL_ENTERED_DATE") != null && rs.getString("FINAL_ENTERED_TIME") != null) {
                        result.setFinalEnteredBy(rs.getString("ENTERED_BY").toUpperCase());
                        result.setFinalEnteredDate(rs.getString("FINAL_ENTERED_DATE").toUpperCase());
                        result.setFinalEnteredTime(rs.getString("FINAL_ENTERED_TIME").toUpperCase());

                    }
                    if (rs.getString("CHARGE") != null) {
                        result.setCharge(rs.getString("CHARGE").toUpperCase());

                    }
                    if (rs.getString("REMARKS") != null) {
                        result.setRemarks(rs.getString("REMARKS").toUpperCase());
                    }
                    if (rs.getString("GATE_ENTRY_NUMBER") != null) {
                        result.setGateEntryNumber(rs.getString("GATE_ENTRY_NUMBER").toUpperCase());
                    }
                    if (rs.getString("RC_NO") != null) {

                        result.setRcNo(rs.getString("RC_NO").toUpperCase());
                    }
                    if (rs.getString("MACHINE_NO") != null) {

                        result.setMachineNo(rs.getString("MACHINE_NO").toUpperCase());
                    }
                    if (rs.getString("SUBTYPE_DESC") != null) {
                        result.setSubtypeDesc(rs.getString("SUBTYPE_DESC").toUpperCase());
                    }
                    if (rs.getString("PROCESS_CODE") != null) {

                        result.setProcessCode(rs.getString("PROCESS_CODE").toUpperCase());
                    }
                    if (rs.getString("CREATED_BY") != null) {

                        result.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
                    }
                    if (rs.getString("CREATION_DATE") != null) {

                        result.setCreationDate(rs.getString("CREATION_DATE").toUpperCase());
                    }
                    if (rs.getString("CREATION_TIME") != null) {

                        result.setCreationTime(rs.getString("CREATION_TIME").toUpperCase());
                    }
                    //VechileTypejComboBox.addItem(rs.getString("CODE")+"-"+rs.getString("TYPE_DESC"));
                    printSlipDetails.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception ex) {

            }
        }
        // Create the custom class instance
        // ResponseDataPrintSlipdetails responseData = new ResponseDataPrintSlipdetails(printSlipDetails);

        // Convert the custom class object to JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(printSlipDetails);
        return jsonResponse;
    }


    public String getReportsDetails() {
        Connection conn = null;
        Statement stmt = null;
        String query = null;
        RestAdapterDao obj = new RestAdapterDao();
        int count = 0;
        ReportDetails result = null;
        ArrayList<ReportDetails> reportsDetailsList = new ArrayList<ReportDetails>();
        try {
            conn = obj.getConnection();
            stmt = conn.createStatement();
            query =
                "SELECT B.SLIP_NO, B.MACHINE_NO,COALESCE(B.TOKEN_NO,A.TOKEN_NO) TOKEN_NO, COALESCE(B.UNIT_CD,a.UNIT_CD) UNIT_CD, B.PROCESS_CODE, COALESCE(B.VEHICLE_NO,A.VEHICLE_NO) VEHICLE_NO, B.VEH_TYPE_CODE, B.RC_NO, B.WEIGHING_TYPE, B.GROSS_WEIGHT, \n" +
                "B.TERE_WEIGHT, B.NET_WEIGHT, B.ENTERED_BY, B.FINAL_ENTERED_BY, B.TROLLEY_NO, B.CHARGE, B.CHARGE_APPLICABLE,\n" +
                "B.PARTY,PRODUCT, B.GATE_ENTRY_NUMBER, B.REMARKS, B.CREATED_BY, TO_CHAR(B.CREATION_DATE, 'DD/MM/YYYY') CREATION_DATE, TO_CHAR(B.CREATION_DATE, 'HH24:MI:SS') CREATION_TIME,\n" +
                "TO_CHAR(B.FINAL_ENTERED_DATE, 'DD/MM/YYYY') FINAL_ENTERED_DATE, TO_CHAR(B.FINAL_ENTERED_DATE, 'HH24:MI:SS') FINAL_ENTERED_TIME,V.SUBTYPE_DESC\n" +
                " FROM TOKEN_INWARD A\n" + " LEFT JOIN WEIGHING_BRIDGE B ON (A.TOKEN_NO = B.TOKEN_NO )\n" +
                "  LEFT JOIN VEHICLE_SUBTYPE_MASTER V ON (V.CODE = B.VEH_TYPE_CODE )\n" +
                " WHERE A.STATUS='I' and B.SLIP_NO is not null and  COALESCE(b.net_weight,0)>0";
            System.out.println("query--" + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result = new ReportDetails();
                count++;
                if (rs.getString("VEHICLE_NO") != null) {

                    result.setVehicleNo(rs.getString("VEHICLE_NO").toUpperCase());

                }
                if (rs.getString("SLIP_NO") != null) {
                    result.setSlipNo(rs.getString("SLIP_NO").toUpperCase());
                }
                if (rs.getString("TOKEN_NO") != null) {
                    result.setTokenNo(rs.getString("TOKEN_NO").toUpperCase());
                }
                if (rs.getString("PARTY") != null) {
                    result.setParty(rs.getString("PARTY").toUpperCase());
                }
                if (rs.getString("PRODUCT") != null) {
                    result.setProduct(rs.getString("PRODUCT").toUpperCase());
                }
                if (rs.getString("GROSS_WEIGHT") != null) {
                    result.setGrossWeight(rs.getString("GROSS_WEIGHT").toUpperCase());
                }
                if (rs.getString("TERE_WEIGHT") != null) {
                    result.setTereWeight(rs.getString("TERE_WEIGHT").toUpperCase());
                }
                if (rs.getString("NET_WEIGHT") != null) {
                    result.setNetWeight(rs.getString("NET_WEIGHT").toUpperCase());
                }
                if (rs.getString("FINAL_ENTERED_DATE") != null && rs.getString("FINAL_ENTERED_TIME") != null) {
                    result.setFinalEnteredBy(rs.getString("ENTERED_BY").toUpperCase());
                    result.setFinalEnteredDate(rs.getString("FINAL_ENTERED_DATE").toUpperCase());
                    result.setFinalEnteredTime(rs.getString("FINAL_ENTERED_TIME").toUpperCase());

                }
                if (rs.getString("CHARGE") != null) {
                    result.setCharge(rs.getString("CHARGE").toUpperCase());

                }
                if (rs.getString("REMARKS") != null) {
                    result.setRemarks(rs.getString("REMARKS").toUpperCase());
                }
                if (rs.getString("GATE_ENTRY_NUMBER") != null) {
                    result.setGateEntryNumber(rs.getString("GATE_ENTRY_NUMBER").toUpperCase());
                }
                if (rs.getString("RC_NO") != null) {

                    result.setRcNo(rs.getString("RC_NO").toUpperCase());
                }
                if (rs.getString("MACHINE_NO") != null) {

                    result.setMachineNo(rs.getString("MACHINE_NO").toUpperCase());
                }
                if (rs.getString("SUBTYPE_DESC") != null) {
                    result.setSubtypeDesc(rs.getString("SUBTYPE_DESC").toUpperCase());
                }
                if (rs.getString("PROCESS_CODE") != null) {

                    result.setProcessCode(rs.getString("PROCESS_CODE").toUpperCase());
                }
                if (rs.getString("CREATED_BY") != null) {

                    result.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
                }
                if (rs.getString("CREATION_DATE") != null) {

                    result.setCreationDate(rs.getString("CREATION_DATE").toUpperCase());
                }
                if (rs.getString("CREATION_TIME") != null) {

                    result.setCreationTime(rs.getString("CREATION_TIME").toUpperCase());
                }
                //VechileTypejComboBox.addItem(rs.getString("CODE")+"-"+rs.getString("TYPE_DESC"));
                reportsDetailsList.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception ex) {

            }
        }
        // Create the custom class instance
        // ResponseDataPrintSlipdetails responseData = new ResponseDataPrintSlipdetails(printSlipDetails);

        // Convert the custom class object to JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(reportsDetailsList);
        return jsonResponse;
    }
}
