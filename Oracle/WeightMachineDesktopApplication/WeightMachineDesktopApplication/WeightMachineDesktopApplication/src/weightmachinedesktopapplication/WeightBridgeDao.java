package weightmachinedesktopapplication;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import oracle.jbo.JboContext;
import java.io.OutputStream;
public class WeightBridgeDao {
    public Connection getStartConnection() throws Exception {
        Connection conn = null;
//                InitialContext initialContext = new InitialContext();
//                DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
//                java.sql.Connection conn = ds.getConnection();
//                return conn;
        
        
        
//                System.out.println("call purchaseOrderCall_me");
//        //String url = "http://127.0.0.1:7101/WebServiceApp/resources/SendMail?UnitCode=13001&MailType=PO";
//        String url = "http://127.0.0.1:7101/ConnectionWeightBridgeApp-ViewController-context-root/resources/view";
//               
//               try {
//                   // Create a URL object
//                   URL obj = new URL(url);
//                   // Open a connection to the URL
//                   HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//                   // Set request method to POST
//                   con.setRequestMethod("POST");
//                   // Add request header
//                   con.setRequestProperty("User-Agent", "Mozilla/5.0");
//                   con.setRequestProperty("Content-Type", "application/json");
//                   con.setDoOutput(true); // Indicate that we want to send a request body
//
//                   // Create the JSON payload
//                   String jsonInputString = "{\"empCode\":\"Admin\",\"pass\":\"Admin@123\"}";
//
//                   // Send the request body
//                   try (OutputStream os = con.getOutputStream()) {
//                       byte[] input = jsonInputString.getBytes("utf-8");
//                       os.write(input, 0, input.length);           
//                   }
//
//                   // Get the response code
//                   int responseCode = con.getResponseCode();
//                   System.out.println("\nSending 'POST' request to URL : " + url);
//                   System.out.println("Response Code : " + responseCode);
//
//                   // Read the response
//                   try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
//                       String inputLine;
//                       StringBuilder response = new StringBuilder();
//
//                       while ((inputLine = in.readLine()) != null) {
//                           response.append(inputLine);
//                       }
//
//                       // Convert the response to a string
//                       String responseBody = response.toString();
//                       System.out.println("Response Body: " + responseBody);
//                   }
//               } catch (Exception e) {
//                   e.printStackTrace();
//               }
  
        
        


////jdbc:odbc:OracleODBC
//               // String url = "jdbc:odbc:OracleODBC";
             //  String url = "jdbc:oracle:thin:@//182.16.9.100:1521/local";
              String url = "jdbc:oracle:thin:@//10.0.6.204:1521/divyadev000000";
               // System.out.println("url--->" + url);
                String user = "terms";
                String password = "terms";
               
                try {
                 
                    conn = DriverManager.getConnection(url, user, password);
                    System.out.println("Connected to the  server successfully.");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
        return conn;
    }

    public static void closeDataSourceConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
