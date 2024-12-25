
package weightmachinedesktopapplication;

import com.fazecast.jSerialComm.SerialPort;

//import com.sun.corba.se.spi.extension.ZeroPortPolicy;

import com.google.gson.Gson;

import com.google.gson.JsonObject;

import java.awt.Dimension;

import java.awt.event.ActionEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Types;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeFormatterBuilder;

import java.time.temporal.ChronoField;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.*;

import java.util.Scanner;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.json.JSONObject;

/**
 *
 * @author lenovo
 */
public class WeightMechineJFrame extends javax.swing.JFrame {
    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<Item> itemCodeList = new ArrayList<>();
    ArrayList<Item> itemCodeNameList = new ArrayList<>();
    String todayDateTime = null;
    int BaudRate = 9600;
    int DataBits = 8;
    int StopBits = SerialPort.ONE_STOP_BIT;
    int Parity = SerialPort.NO_PARITY;
    String userNamevalue = null;
    String unitCode = "60001";
    String slipNo = null, tokenNo = null, gateNo = null, grossWeight = null, tareWeight = null, netWeight =
        null, party = null, vechileNo = null, vechileType = null, create = null, finaldate = null, charge =
        null, product = null, remarks = null;
    private JPopupMenu suggestionMenuPart;
    private JPopupMenu suggestionMenuProduct;
    private JPopupMenu suggestionMenuRemarks;
    private List<String> suggestionsListParty;
    private List<String> suggestionsListPoduct;
    private List<String> suggestionsListRemarks;

    /** Creates new form WeightMechineJFrame */
    public WeightMechineJFrame(String userName) {
        System.out.println("userName----" + userName);
        if (userName == null || userName.isEmpty() || userName == "") {
            userName = "E-001";
        }
        userNamevalue = userName;
        initComponents();
        //comPoartMechineConnection();
        // vachileDetailsWithAutoSugest();


        onLoad();
        onLoadApiVehicleTypeAutoSugest();

        onLoadDate();
        TXT_Machine.setText("L2");
       // TXT_CreateBy.setText(userName);

        autoSugestParty();
        autoSugestProduct();
        autoSugestRemarks();


        //
        //        SerialPort[] ports = SerialPort.getCommPorts();
        //        if (ports.length == 0) {
        //            System.out.println("No serial ports available.");
        //            return;
        //        }
        //
        //        System.out.println("Available serial ports:");
        //        for (int i = 0; i < ports.length; i++) {
        //            System.out.println(i + ": " + ports[i].getSystemPortName());
        //        }
        //
        //        // Select the first available port for demonstration purposes
        //        System.out.print("Select a port (0 - " + (ports.length - 1) + "): ");
        //  Scanner scanner = new Scanner(System.in);
        // int chosenPortIndex = scanner.nextInt();

        //        if (chosenPortIndex < 0 || chosenPortIndex >= ports.length) {
        //            System.out.println("Invalid port selection.");
        //            scanner.close();
        //            return;
        //        }

        // Open the selected serial port
        //SerialPort serialPort = ports[chosenPortIndex];
        //        SerialPort serialPort =SerialPort.getCommPort("COM5");
        //        if (serialPort.openPort()) {
        //            System.out.println("Port " + serialPort.getSystemPortName() + " opened successfully.");
        //        } else {
        //            System.out.println("Failed to open port.");
        //         //   scanner.close();
        //            return;
        //        }
        //
        //        // Set port parameters: baud rate, data bits, stop bits, and parity
        //        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        //        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
        //
        //        // Write data to the serial port
        //        String message = "Hello, COM port!";
        //        byte[] writeBuffer = message.getBytes();
        //        serialPort.writeBytes(writeBuffer, writeBuffer.length);
        //        System.out.println("Sent: " + message);
        //
        //        // Read data from the serial port
        //        byte[] readBuffer = new byte[1024];
        //        int numBytesRead = serialPort.readBytes(readBuffer, readBuffer.length);
        //        if (numBytesRead > 0) {
        //            String receivedData = new String(readBuffer, 0, numBytesRead);
        //            System.out.println("Received: " + receivedData);
        //            TXT_AutoWeight.setText(receivedData);
        //        } else {
        //            System.out.println("No data received.");
        //            TXT_AutoWeight.setText("0");
        //        }
        //
        //        // Close the serial port
        //        if (serialPort.closePort()) {
        //            System.out.println("Port closed successfully.");
        //        } else {
        //            System.out.println("Failed to close port.");
        //        }
        //
        //        //scanner.close();


    }

    public void autoSugestParty() {
        suggestionMenuPart = new JPopupMenu();
        TXT_Part.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePartySuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePartySuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePartySuggestions();
            }
        });

        TXT_Part.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionMenuPart.setVisible(false);
                }
            }
        });

    }

    private void updatePartySuggestions() {
        String input = TXT_Part.getText();
        if (input.isEmpty() || input.equalsIgnoreCase(null) || input.equals("")) {
            // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            System.out.println("input.length()--" + input.length());
            if (input.length() < 4) {
                System.out.println("input.length()-1-" + input.length());
                // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        suggestionMenuPart.removeAll();

        if (input.isEmpty()) {
            suggestionMenuPart.setVisible(false);
            return;
        }

        for (String suggestion : suggestionsListParty) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                JMenuItem item = new JMenuItem(suggestion);
                item.addActionListener(e -> {
                        TXT_Part.setText(suggestion);
                        suggestionMenuPart.setVisible(false);
                    });
                suggestionMenuPart.add(item);
            }
        }

        if (suggestionMenuPart.getComponentCount() > 0) {
            suggestionMenuPart.show(TXT_Part, 0, TXT_Part.getHeight());
        } else {
            suggestionMenuPart.setVisible(false);
        }
    }

    public void autoSugestProduct() {
        suggestionMenuProduct = new JPopupMenu();
        TXT_Product.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateProductSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateProductSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateProductSuggestions();
            }
        });

        TXT_Product.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionMenuProduct.setVisible(false);
                }
            }
        });

    }

    private void updateProductSuggestions() {
        String input = TXT_Product.getText();
        if (input.isEmpty() || input.equalsIgnoreCase(null) || input.equals("")) {
            // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            System.out.println("input.length()--" + input.length());
            if (input.length() < 4) {
                System.out.println("input.length()-1-" + input.length());
                // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        suggestionMenuProduct.removeAll();

        if (input.isEmpty()) {
            suggestionMenuProduct.setVisible(false);
            return;
        }

        for (String suggestion : suggestionsListPoduct) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                JMenuItem item = new JMenuItem(suggestion);
                item.addActionListener(e -> {
                        TXT_Product.setText(suggestion);
                        suggestionMenuProduct.setVisible(false);
                    });
                suggestionMenuProduct.add(item);
            }
        }

        if (suggestionMenuProduct.getComponentCount() > 0) {
            suggestionMenuProduct.show(TXT_Product, 0, TXT_Product.getHeight());
        } else {
            suggestionMenuProduct.setVisible(false);
        }
    }


    public void autoSugestRemarks() {
        suggestionMenuRemarks = new JPopupMenu();
        TXT_REMARKS.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRemarksSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRemarksSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRemarksSuggestions();
            }
        });

        TXT_REMARKS.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionMenuRemarks.setVisible(false);
                }
            }
        });

    }


    private void updateRemarksSuggestions() {
        String input = TXT_REMARKS.getText();
        if (input.isEmpty() || input.equalsIgnoreCase(null) || input.equals("")) {
            // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            System.out.println("input.length()--" + input.length());
            if (input.length() < 4) {
                System.out.println("input.length()-1-" + input.length());
                // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        suggestionMenuRemarks.removeAll();

        if (input.isEmpty()) {
            suggestionMenuRemarks.setVisible(false);
            return;
        }

        for (String suggestion : suggestionsListRemarks) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                JMenuItem item = new JMenuItem(suggestion);
                item.addActionListener(e -> {
                        TXT_REMARKS.setText(suggestion);
                        suggestionMenuRemarks.setVisible(false);
                    });
                suggestionMenuRemarks.add(item);
            }
        }

        if (suggestionMenuRemarks.getComponentCount() > 0) {
            suggestionMenuRemarks.show(TXT_REMARKS, 0, TXT_REMARKS.getHeight());
        } else {
            suggestionMenuRemarks.setVisible(false);
        }
    }

    public void onLoad() {
        TXT_AutoWeight.setText("0");
        TXT_GrossWeight.setText("0");
        TXT_TareWeight.setText("0");
        TXT_NetWeight.setText("0");
        TXT_CreateBy.setText(userNamevalue);
        TXT_Charge.setText("0");
        // TXT_FinealEnterBy.setText(userNamevalue);
        // TXT_FinealEnterDate.setText(outputDate);
        // TXT_FinealEnterTime.setText(timeOnly);

    }

    public void onLoadDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        //DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yy h:mm:ss.SSSSSSSSS a");
        todayDateTime = currentDateTime.format(formatter1);
        System.out.println("Current Date and Time: " + todayDateTime); // Example: 2024-10-16 13:32:43
        LocalDate currentDate = LocalDate.now();
        System.out.println("Current Date: " + currentDate);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Parse the input date
        LocalDate date = LocalDate.parse(currentDate.toString(), inputFormatter);
        // Format the date to the desired output format
        String outputDate = date.format(outputFormatter);
        System.out.println("outputDate--" + outputDate);
        TXT_CreateDate.setText(outputDate);
        LocalDateTime now = LocalDateTime.now();
        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeOnly = now.format(formatter);
        // Print the current date and time
        System.out.println("Current date and time: " + timeOnly);
        TXT_CreateTime.setText(timeOnly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        WeightBridgeJpanel = new javax.swing.JPanel();
        TXT_AutoWeight = new javax.swing.JTextField();
        BtnTare = new javax.swing.JButton();
        BtnGross = new javax.swing.JButton();
        BtnPrint = new javax.swing.JButton();
        BtnSubmit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TXT_FinealEnterTime = new javax.swing.JTextPane();
        TXT_FinealEnterBy = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        TXT_GateEntry = new javax.swing.JTextField();
        LBL_GateEntry = new javax.swing.JLabel();
        TXT_Process = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        TXT_Machine = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TXT_FinealEnterDate = new javax.swing.JTextPane();
        TXT_Charge = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        LBL_CreateTime = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TXT_CreateTime = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TXT_CreateDate = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        TXT_CreateBy = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TXT_TokenNo = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        VechileTypejComboBox = new javax.swing.JComboBox();
        TXT_SlipNo = new javax.swing.JTextField();
        TXT_RC_NO = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        ComboBoxChargeApplied = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TXT_GrossWeight = new javax.swing.JTextPane();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        TXT_TareWeight = new javax.swing.JTextPane();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        TXT_NetWeight = new javax.swing.JTextPane();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        TXT_VechileNo = new javax.swing.JTextField();
        TXT_TrollyNo = new javax.swing.JTextField();
        TXT_Part = new javax.swing.JTextField();
        TXT_Product = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        TXT_REMARKS = new javax.swing.JTextField();
        BtnActionClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        BtnLogOut = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Weight Bridge");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        setSize(new java.awt.Dimension(900, 400));

        WeightBridgeJpanel.setBackground(new java.awt.Color(255, 255, 255));

        BtnTare.setBackground(new java.awt.Color(102, 204, 255));
        BtnTare.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnTare.setForeground(new java.awt.Color(255, 255, 255));
        BtnTare.setLabel("Tare");
        BtnTare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTareActionPerformed(evt);
            }
        });

        BtnGross.setBackground(new java.awt.Color(102, 204, 255));
        BtnGross.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnGross.setForeground(new java.awt.Color(255, 255, 255));
        BtnGross.setLabel("Gross");
        BtnGross.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrossActionPerformed(evt);
            }
        });

        BtnPrint.setBackground(new java.awt.Color(102, 204, 255));
        BtnPrint.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnPrint.setForeground(new java.awt.Color(255, 255, 255));
        BtnPrint.setText("Print");
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });

        BtnSubmit.setBackground(new java.awt.Color(102, 204, 255));
        BtnSubmit.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        BtnSubmit.setText("Submit");
        BtnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSubmitActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("Final Entered Time");

        TXT_FinealEnterTime.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_FinealEnterTime.setEnabled(false);
        jScrollPane7.setViewportView(TXT_FinealEnterTime);

        TXT_FinealEnterBy.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_FinealEnterBy.setEnabled(false);

        jLabel16.setText("Final Entered Date");

        jLabel15.setText("Final Entered By");

        TXT_GateEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXT_GateEntryActionPerformed(evt);
            }
        });

        LBL_GateEntry.setText("Gate Entry");

        jLabel13.setText("Process");

        TXT_Machine.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_Machine.setEnabled(false);

        jLabel6.setText("Machine");

        TXT_FinealEnterDate.setEnabled(false);
        jScrollPane6.setViewportView(TXT_FinealEnterDate);

        TXT_Charge.setEditable(false);
        TXT_Charge.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_Charge.setEnabled(false);

        jLabel11.setText("Charge");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel13))
                        .addGap(62, 62, 62)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXT_Process)
                            .addComponent(TXT_Machine)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel17)))
                            .addComponent(LBL_GateEntry))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXT_GateEntry)
                            .addComponent(TXT_FinealEnterBy, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TXT_Charge, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(jScrollPane7))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXT_Machine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXT_Process, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXT_GateEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LBL_GateEntry))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(TXT_FinealEnterBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXT_Charge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(38, 38, 38))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("Vechile Type");

        LBL_CreateTime.setText("Create Time");

        TXT_CreateTime.setEditable(false);
        TXT_CreateTime.setBackground(new java.awt.Color(204, 204, 204));
        TXT_CreateTime.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_CreateTime.setEnabled(false);
        jScrollPane5.setViewportView(TXT_CreateTime);

        jLabel8.setText("Create Date");

        TXT_CreateDate.setEditable(false);
        TXT_CreateDate.setBackground(new java.awt.Color(204, 204, 204));
        TXT_CreateDate.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_CreateDate.setEnabled(false);
        jScrollPane4.setViewportView(TXT_CreateDate);

        jLabel5.setText("Token No");

        TXT_CreateBy.setEditable(false);
        TXT_CreateBy.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_CreateBy.setEnabled(false);

        jLabel7.setText("Create By");

        TXT_TokenNo.setEditable(false);
        TXT_TokenNo.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_TokenNo.setEnabled(false);
        jScrollPane3.setViewportView(TXT_TokenNo);

        jLabel4.setText("RC Number");

        jLabel3.setText("Slip Number");

        VechileTypejComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Please Select" }));
        VechileTypejComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VechileTypejComboBoxActionPerformed(evt);
            }
        });

        TXT_SlipNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TXT_SlipNoKeyPressed(evt);
            }
        });

        jLabel12.setText("Charge Applied Or Not");

        ComboBoxChargeApplied.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        ComboBoxChargeApplied.setSelectedIndex(1);
        ComboBoxChargeApplied.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxChargeAppliedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10)
                                .addComponent(LBL_CreateTime)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3)))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(TXT_CreateBy, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(VechileTypejComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TXT_SlipNo)
                            .addComponent(TXT_RC_NO)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ComboBoxChargeApplied, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXT_SlipNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TXT_RC_NO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXT_CreateBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LBL_CreateTime, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(VechileTypejComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(ComboBoxChargeApplied, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Gross Weight");

        TXT_GrossWeight.setEditable(false);
        TXT_GrossWeight.setBackground(new java.awt.Color(248, 245, 245));
        TXT_GrossWeight.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_GrossWeight.setEnabled(false);
        jScrollPane8.setViewportView(TXT_GrossWeight);

        jLabel19.setText("Tare Weight");

        TXT_TareWeight.setEditable(false);
        TXT_TareWeight.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_TareWeight.setEnabled(false);
        jScrollPane9.setViewportView(TXT_TareWeight);

        jLabel20.setText("Net Weight");

        TXT_NetWeight.setEditable(false);
        TXT_NetWeight.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TXT_NetWeight.setEnabled(false);
        jScrollPane10.setViewportView(TXT_NetWeight);

        jLabel21.setText("Vehicle Number");

        jLabel22.setText("Trolly Number");

        jLabel23.setText("Party");

        jLabel24.setText("Product");

        TXT_VechileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TXT_VechileNoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19)
                    .addComponent(jLabel2)
                    .addComponent(jLabel22))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(TXT_TrollyNo))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXT_VechileNo)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 88, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TXT_Part, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(TXT_Product)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(TXT_VechileNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(TXT_TrollyNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(TXT_Part, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(TXT_Product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel2.getAccessibleContext().setAccessibleName("");

        jLabel14.setText("Remark");

        BtnActionClear.setBackground(new java.awt.Color(102, 204, 255));
        BtnActionClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BtnActionClear.setForeground(new java.awt.Color(255, 255, 255));
        BtnActionClear.setText("Reset");
        BtnActionClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnActionClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout WeightBridgeJpanelLayout = new javax.swing.GroupLayout(WeightBridgeJpanel);
        WeightBridgeJpanel.setLayout(WeightBridgeJpanelLayout);
        WeightBridgeJpanelLayout.setHorizontalGroup(
            WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXT_REMARKS, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TXT_AutoWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, WeightBridgeJpanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(BtnTare, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(BtnGross, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(124, 124, 124)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                        .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeightBridgeJpanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(BtnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(BtnActionClear, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)))
                        .addComponent(BtnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        WeightBridgeJpanelLayout.setVerticalGroup(
            WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                        .addComponent(TXT_AutoWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BtnGross)
                                    .addComponent(BtnTare))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(WeightBridgeJpanelLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(TXT_REMARKS, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(WeightBridgeJpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnActionClear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(13, 113, 188));

        jLabel1.setFont(new java.awt.Font("DejaVu Serif Condensed", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Weight Bridge");

        BtnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weightmachinedesktopapplication/shutdown (1).png"))); // NOI18N
        BtnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnLogOut)
                .addGap(22, 22, 22))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnLogOut)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(13, 113, 188));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Bharuwa solutions Pvt Ltd.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Print");

        jMenuItem2.setText("Duplicate print");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Report");

        jMenuItem3.setText("Report1");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(WeightBridgeJpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(WeightBridgeJpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }//GEN-END:initComponents

    private void BtnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSubmitActionPerformed
        if (TXT_GrossWeight.getText().equalsIgnoreCase("0") && TXT_TareWeight.getText().equalsIgnoreCase("0")) {
            JOptionPane.showMessageDialog(null, "Gross Weight/ Tare Weight 0", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (TXT_TokenNo.getText().isEmpty() || TXT_TokenNo.getText() == null || TXT_TokenNo.getText() == "") {
            JOptionPane.showMessageDialog(null, "Token number not found", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Integer netWeight = Integer.parseInt(TXT_NetWeight.getText());
        if (netWeight < 0) {
            JOptionPane.showMessageDialog(null, "Net Weight must be greater than zero", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String selectedItem = (String) VechileTypejComboBox.getSelectedItem();
        System.out.println("Selected Item: " + selectedItem);
        if (selectedItem.equalsIgnoreCase("Please Select")) {
            JOptionPane.showMessageDialog(null, "please select vehicle type", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        System.out.println("TXT_SlipNo.getText()--" + TXT_SlipNo.getText().toString().length());
        if (TXT_SlipNo.getText().isEmpty() || TXT_SlipNo.getText() == null || TXT_SlipNo.getText() == "") {
            insertdateCallApi();
        } else {
            updatedateCallApi();
        }
        
    }//GEN-LAST:event_BtnSubmitActionPerformed
    
    
    
    public void insertdateCallApi() {
        try {
           // URL url = new URL("http://182.16.9.100:7003/RestApiWeightBridge/resources/insert");
           URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/insert");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("slip_no", "0");
            jsonObject.addProperty("machine_no", TXT_Machine.getText().toUpperCase());
            jsonObject.addProperty("token_no", TXT_TokenNo.getText().toUpperCase());
            jsonObject.addProperty("process_code", TXT_Process.getText().toUpperCase());
            jsonObject.addProperty("vehicle_no", TXT_VechileNo.getText().toUpperCase());
            jsonObject.addProperty("veh_type_code", vechileCode);
            jsonObject.addProperty("rc_no", TXT_RC_NO.getText());
            int grossWeight = Integer.parseInt(TXT_GrossWeight.getText());
            jsonObject.addProperty("gross_weight", grossWeight);
            int tareWeight = Integer.parseInt(TXT_TareWeight.getText());
            jsonObject.addProperty("tere_weight", tareWeight);
            int netWeight = Integer.parseInt(TXT_NetWeight.getText());
            jsonObject.addProperty("net_weight", netWeight);
            jsonObject.addProperty("entered_by", TXT_CreateBy.getText().toUpperCase());
            jsonObject.addProperty("final_entered_by", TXT_CreateBy.getText().toUpperCase());
            jsonObject.addProperty("trolley_no", TXT_TrollyNo.getText());
            jsonObject.addProperty("charge", TXT_Charge.getText());
            jsonObject.addProperty("charge_applicable", ComboBoxChargeApplied.getSelectedItem().toString());
            jsonObject.addProperty("party", TXT_Part.getText().toUpperCase());
            jsonObject.addProperty("product", TXT_Product.getText().toUpperCase());
            jsonObject.addProperty("gate_entry_number", TXT_GateEntry.getText().toUpperCase());
            jsonObject.addProperty("remarks", TXT_REMARKS.getText().toUpperCase());
            jsonObject.addProperty("created_by", TXT_CreateBy.getText().toUpperCase());
            jsonObject.addProperty("unit_cd", "60001");

            // Convert JsonObject to JSON string
            String jsonInputString = jsonObject.toString();


            // String jsonInputString = "{\"empCode\":\"" + TXT_USERNAME.getText() + "\",\"pass\":\"" + passwordString + "\"}";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                String jsonResponse = response.toString();
                JSONObject responseObject = new JSONObject(jsonResponse);
                // Check response status
                int status = responseObject.getInt("statusCode");
                String message = responseObject.getString("message");


                System.out.println("Response Status: " + status);
                System.out.println("Response Message: " + message);
                if (status == 200) {
                    String SlipNojson = responseObject.getString("slipNo");
                    System.out.println("Response SlipNojson: " + SlipNojson);
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    TXT_SlipNo.setText(SlipNojson);
                    forPrint();
                    resetValue();

                } else {
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                }


            }
        } catch (Exception ex) {
        }
    }


    public void updatedateCallApi() {
        try {
           // URL url = new URL("http://182.16.9.100:7003/RestApiWeightBridge/resources/update");
            URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/update");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("slip_no", TXT_SlipNo.getText());
            jsonObject.addProperty("process_code", TXT_Process.getText().toUpperCase());
            jsonObject.addProperty("veh_type_code", vechileCode);
            jsonObject.addProperty("rc_no", TXT_RC_NO.getText());
            jsonObject.addProperty("gross_weight", Integer.parseInt(TXT_GrossWeight.getText()));
            jsonObject.addProperty("tere_weight", Integer.parseInt(TXT_TareWeight.getText()));
            jsonObject.addProperty("net_weight", Integer.parseInt(TXT_NetWeight.getText()));
            jsonObject.addProperty("final_entered_by", TXT_FinealEnterBy.getText().toUpperCase());
            jsonObject.addProperty("trolley_no", TXT_TrollyNo.getText());
            jsonObject.addProperty("charge", TXT_Charge.getText());
            jsonObject.addProperty("charge_applicable", ComboBoxChargeApplied.getSelectedItem().toString());
            jsonObject.addProperty("party", TXT_Part.getText().toUpperCase());
            jsonObject.addProperty("product", TXT_Product.getText().toUpperCase());
            jsonObject.addProperty("remarks", TXT_REMARKS.getText().toUpperCase());

            // Convert JsonObject to JSON string
            String jsonInputString = jsonObject.toString();
            // String jsonInputString = "{\"empCode\":\"" + TXT_USERNAME.getText() + "\",\"pass\":\"" + passwordString + "\"}";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                String jsonResponse = response.toString();
                JSONObject responseObject = new JSONObject(jsonResponse);
                // Check response status
                int status = responseObject.getInt("statusCode");
                String message = responseObject.getString("message");

                System.out.println("Response Status: " + status);
                System.out.println("Response Message: " + message);
                if (status == 200) {
                    String SlipNojson = responseObject.getString("slipNo");
                    System.out.println("Response SlipNojson: " + SlipNojson);
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);

                   
                    forPrint();
                    TXT_SlipNo.setText(TXT_SlipNo.getText());
                    disableValueAfterSave();
                } else {
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                }


            }
        } catch (Exception ex) {
        }
    }

    private void BtnSubmitActionPerformedold(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_BtnSubmitActionPerformed
    } //GEN-LAST:event_BtnSubmitActionPerformed


    private void BtnGrossActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrossActionPerformed
        comPoartMechineConnection();
        TXT_GrossWeight.setText(TXT_AutoWeight.getText().toString());
        int netWeight = netWeightCalculate();
        TXT_NetWeight.setText(String.valueOf(netWeight)); 
        
    }//GEN-LAST:event_BtnGrossActionPerformed

    private void BtnTareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTareActionPerformed
        comPoartMechineConnection();
        TXT_TareWeight.setText(TXT_AutoWeight.getText().toString());
        int netWeight = netWeightCalculate();
        TXT_NetWeight.setText(String.valueOf(netWeight)); 
       
    }//GEN-LAST:event_BtnTareActionPerformed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if (TXT_SlipNo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Slip No", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("call jasper----");
        InputStream input;
        try {
           // input = new FileInputStream("C:/Users/LENOVO/Desktop/mechine/Bridge_Entry.jasper");
            // input = new FileInputStream("C:/Users/Patanjali/Desktop/mechine/Bridge_Entry.jasper");
            input = new FileInputStream("C:/Users/SHUBHAM/Desktop/WB/Bridge_Entry.jasper");
            JasperReport design = (JasperReport) JRLoader.loadObject(input);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("SlipNo", slipNo);
            parameters.put("TokenNo", tokenNo);
            parameters.put("GateEntry", gateNo);
            parameters.put("GrossWeight", grossWeight);
            parameters.put("TareWeight", tareWeight);
            parameters.put("NetWeight", netWeight);
            parameters.put("Party", party);
            parameters.put("Remarks", remarks);
            parameters.put("TruckNo", vechileNo);
            parameters.put("TruckType", vechileType);
            parameters.put("CreatedOn", create);
            parameters.put("Final", finaldate);
            parameters.put("Charge", charge);
            parameters.put("Product", product);
            parameters.put("Copy", "Orginal");

            //            parameters.put("SlipNo", "9393595435");
            //            parameters.put("TokenNo", "73453dgddf");
            //            parameters.put("GateEntry", "fdryeuryer");
            //            parameters.put("GrossWeight", "232424");
            //            parameters.put("TareWeight","856464");
            //            parameters.put("NetWeight", "9990");
            //            parameters.put("Party", "fghdfydffhfdfh");
            //            parameters.put("Remarks","dfhdfjhfdfhdfdfgfhdfd");
            //            parameters.put("TruckNo","PB09jyytV");
            //            parameters.put("TruckType", "10hb");
            //            parameters.put("CreatedOn", "20.09.2024 10:17:50");
            //            parameters.put("Final","20.09.2024 10:17:50");
            //            parameters.put("Charge", "90");
            //            parameters.put("Product", "dhfdfhgdfgfyfdf");
            //            parameters.put("Copy", "Orginal");
            net.sf.jasperreports.engine.JasperPrint print =
                JasperFillManager.fillReport(design, parameters, new JREmptyDataSource());
            // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
            // byte pdf[] = JasperExportManager.exportReportToPdf(print);
            // JasperExportManager.exportReportToPdfFile(print, "C:/Users/LENOVO/Desktop/mechine/report.pdf");
            // Optional: Display the report in a viewer
            JasperViewer.viewReport(print, false);
            System.out.println("Path : " + input + " -------" + design + " param:");
            resetValueAfterPrint();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
        }


        //        try {
        //            // Load the Jasper report
        //            JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/LENOVO/Desktop/mechine/name_test.jrxml");
        //            // Parameters for the report
        //            Map<String, Object> parameters = new HashMap<>();
        //            parameters.put("name", "0013130");
        //            // Fill the report
        //            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        //            // Export the report to PDF
        //            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/Users/LENOVO/Desktop/mechine/report.pdf");
        //            // Optional: Display the report in a viewer
        //            JasperViewer.viewReport(jasperPrint, false);
        //        } catch (JRException e) {
        //            e.printStackTrace();
        //        }

               
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void TXT_GateEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXT_GateEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXT_GateEntryActionPerformed
    String currentValue = "0";
    String currentSlipValue = "0";
    String vechileCode = null;
    private void VechileTypejComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VechileTypejComboBoxActionPerformed

        String selectedItem = (String) VechileTypejComboBox.getSelectedItem();
        System.out.println("Selected Item: " + selectedItem);
        if (selectedItem.equalsIgnoreCase("Please Select")) {
            TXT_Charge.setText("0");
            ComboBoxChargeApplied.setSelectedIndex(1);
        } else {
            ComboBoxChargeApplied.setSelectedIndex(0);
            String firstValue = itemList.stream().filter(item -> item.getKey().equals(selectedItem)) // Filter based on the key
                .map(Item::getValue) // Map to the values
                .findFirst() // Get the first matching value
                .orElse(null); // Return null if no match is found
            System.out.println("firstValue--+" + firstValue);

            if (firstValue == null) {
                TXT_Charge.setText("0");
            } else {
                TXT_Charge.setText(firstValue);

            }

            String codeValue = itemCodeList.stream().filter(item -> item.getKey().equals(selectedItem)) // Filter based on the key
                .map(Item::getValue) // Map to the values
                .findFirst() // Get the first matching value
                .orElse(null); // Return null if no match is found
            System.out.println("codeValue--+" + codeValue);
            if (codeValue == null) {

            } else {
                vechileCode = codeValue;
            }
        }
        onLoadDate();

        // Print the filtered values
        
    }//GEN-LAST:event_VechileTypejComboBoxActionPerformed

    private void BtnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLogOutActionPerformed
        LoginJFrame weightFrame = new LoginJFrame();
        weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        weightFrame.setSize(800, 610);
        weightFrame.setVisible(true);
        // super.setVisible(false);
        super.dispose();
    }//GEN-LAST:event_BtnLogOutActionPerformed

    private void TXT_VechileNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXT_VechileNoKeyPressed
   
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Enter key pressed!");
            String vechileNo = TXT_VechileNo.getText().toString().toUpperCase();
            // VechileDetails(vechileNo, null);
            oncallApiVehicleSlipNo(vechileNo, null);
            onLoadDate();
        }
  
    }//GEN-LAST:event_TXT_VechileNoKeyPressed

    private void TXT_SlipNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXT_SlipNoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Enter key pressed!");
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yy h:mm:ss.SSSSSSSSS a");
            todayDateTime = currentDateTime.format(formatter1);
            System.out.println("Current Date and Time: " + todayDateTime); // Example: 2024-10-16 13:32:43
            LocalDate currentDate = LocalDate.now();
            System.out.println("Current Date: " + currentDate);
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Parse the input date
            LocalDate date = LocalDate.parse(currentDate.toString(), inputFormatter);
            // Format the date to the desired output format
            String outputDate = date.format(outputFormatter);
            System.out.println("outputDate--" + outputDate);
            TXT_CreateDate.setText(outputDate);
            LocalDateTime now = LocalDateTime.now();
            // Format the date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timeOnly = now.format(formatter);
            // Print the current date and time
            System.out.println("Current date and time: " + timeOnly);
            TXT_FinealEnterBy.setText(userNamevalue);
            TXT_FinealEnterDate.setText(outputDate);
            TXT_FinealEnterTime.setText(timeOnly);
            String slipNo = TXT_SlipNo.getText().toString().toUpperCase();
            //VechileDetails(null, slipNo);
            oncallApiVehicleSlipNo(null, slipNo);

            onLoadDate();
        }
    }//GEN-LAST:event_TXT_SlipNoKeyPressed

    private void ComboBoxChargeAppliedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxChargeAppliedActionPerformed
        String selectedItemValue = (String) ComboBoxChargeApplied.getSelectedItem();
        System.out.println("Selected Item: " + selectedItemValue);
        if (selectedItemValue.equalsIgnoreCase("No")) {
            TXT_Charge.setText("0");
        } else {
            String selectedType = (String) VechileTypejComboBox.getSelectedItem();
            String firstValue = itemList.stream().filter(item -> item.getKey().equals(selectedType)) // Filter based on the key
                .map(Item::getValue) // Map to the values
                .findFirst() // Get the first matching value
                .orElse(null); // Return null if no match is found
            System.out.println("firstValue--+" + firstValue);

            if (firstValue == null) {
                TXT_Charge.setText("0");
            } else {
                TXT_Charge.setText(firstValue);
            }
        }
        onLoadDate();
    }//GEN-LAST:event_ComboBoxChargeAppliedActionPerformed

    private void BtnActionClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnActionClearActionPerformed
        onLoad();
        onLoadDate();
        resetValue();
        TXT_FinealEnterBy.setText(null);
        TXT_FinealEnterDate.setText(null);
        TXT_FinealEnterTime.setText(null);
        TXT_SlipNo.setText(null);
    }//GEN-LAST:event_BtnActionClearActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.out.println("hello");
        PrintSlip weightFrame = new PrintSlip();
        weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        weightFrame.setSize(1150, 700);
        weightFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.out.println("hello");
        ReportsJFrame weightFrame = new ReportsJFrame();
        weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        weightFrame.setSize(1150, 700);
        weightFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

   

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                                                                                        null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                                                                                        null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                                                                                        null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                                                                                        null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WeightMechineJFrame("").setVisible(true);
            }
        });


    }

    public void vachileDetailsWithAutoSugest() {
        Connection conn = null;
        Statement stmt = null;
        WeightBridgeDao obj = new WeightBridgeDao();
        suggestionsListParty = new ArrayList<>();
        suggestionsListPoduct = new ArrayList<>();
        suggestionsListRemarks = new ArrayList<>();
        try {
            conn = obj.getStartConnection();
            stmt = conn.createStatement();

            ResultSet rs =
                stmt.executeQuery("select V.CODE,V.TYPE_CODE,V.SUBTYPE_DESC,M.WEIGHING_RATE FROM  vehicle_subtype_master  V\n" +
                                  "INNER JOIN WEIGHING_RATE_MASTER M ON V.CODE=M.VEHICLE_SUB_TYPE_CODE\n" +
                                  "WHERE V.STATUS='Y' AND M.STATUS='Y'");
            while (rs.next()) {
                VechileTypejComboBox.addItem(rs.getString("SUBTYPE_DESC"));
                //String value = rs.getString("WEIGHING_RATE") + "-" + rs.getString("CODE");
                itemList.add(new Item(rs.getString("SUBTYPE_DESC"), rs.getString("WEIGHING_RATE")));
                itemCodeList.add(new Item(rs.getString("SUBTYPE_DESC"), rs.getString("CODE")));
                itemCodeNameList.add(new Item(rs.getString("CODE"), rs.getString("SUBTYPE_DESC")));

            }

            ResultSet rs1 = stmt.executeQuery("select distinct PARTY,PRODUCT,REMARKS from WEIGHING_BRIDGE");
            while (rs1.next()) {
                suggestionsListParty.add(rs1.getString("PARTY"));
                suggestionsListPoduct.add(rs1.getString("PRODUCT"));
                suggestionsListRemarks.add(rs1.getString("REMARKS"));

            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void VechileDetails(String vechileNo, String slipNo) {
        onLoadDate();
        Connection conn = null;
        Statement stmt = null;
        String query = null;
        WeightBridgeDao obj = new WeightBridgeDao();
        int count = 0;
        try {
            conn = obj.getStartConnection();
            stmt = conn.createStatement();
            if (vechileNo != null) {

                query = "SELECT * FROM vw_weighing_bridge_penddoc where VEHICLE_NO='" + vechileNo + "'";
                System.out.println("query--" + query);
            }
            if (slipNo != null) {
                query = "SELECT * FROM vw_weighing_bridge_penddoc where LOWER(SLIP_NO)=LOWER('" + slipNo + "')";
                System.out.println("query--" + query);
            }
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count++;
                if (vechileNo == null) {
                    if (rs.getString("VEHICLE_NO") != null) {
                        TXT_VechileNo.setText(rs.getString("VEHICLE_NO").toUpperCase());
                    }
                }
                if (slipNo == null) {
                    if (rs.getString("SLIP_NO") != null) {
                        TXT_SlipNo.setText(rs.getString("SLIP_NO"));

                    }
                }

                if (rs.getString("TOKEN_NO") != null) {
                    TXT_TokenNo.setText(rs.getString("TOKEN_NO"));
                }
                if (rs.getString("PARTY") != null) {
                    TXT_Part.setText(rs.getString("PARTY"));
                }
                if (rs.getString("PRODUCT") != null) {
                    TXT_Product.setText(rs.getString("PRODUCT"));
                }
                if (rs.getString("GROSS_WEIGHT") != null) {
                    TXT_GrossWeight.setText(rs.getString("GROSS_WEIGHT"));
                    Integer grossWeight = Integer.parseInt(rs.getString("GROSS_WEIGHT"));
                    if (grossWeight > 0) {
                        BtnGross.setEnabled(false);
                    }

                }
                if (rs.getString("TERE_WEIGHT") != null) {
                    TXT_TareWeight.setText(rs.getString("TERE_WEIGHT"));
                    Integer tareWeight = Integer.parseInt(rs.getString("TERE_WEIGHT"));
                    if (tareWeight > 0) {
                        BtnTare.setEnabled(false);
                    }
                }
                if (rs.getString("NET_WEIGHT") != null) {
                    TXT_NetWeight.setText(rs.getString("NET_WEIGHT"));
                    Integer tareWeight = Integer.parseInt(rs.getString("NET_WEIGHT"));
                    if (tareWeight > 0) {
                        BtnSubmit.setEnabled(false);
                        BtnActionClear.setEnabled(false);
                    }
                }
                if (rs.getString("FINAL_ENTERED_BY") != null) {
                    TXT_FinealEnterBy.setText(rs.getString("FINAL_ENTERED_BY"));
                }
                if (rs.getString("TROLLEY_NO") != null) {
                    TXT_TrollyNo.setText(rs.getString("TROLLEY_NO"));
                }
                if (rs.getString("CHARGE") == null) {
                    TXT_Charge.setText("0");
                } else {

                    TXT_Charge.setText(rs.getString("CHARGE"));
                }
                if (rs.getString("CHARGE_APPLICABLE") != null) {
                    if (rs.getString("CHARGE_APPLICABLE").equalsIgnoreCase("Y")) {
                        ComboBoxChargeApplied.setSelectedIndex(0);
                    } else {
                        ComboBoxChargeApplied.setSelectedIndex(1);
                    }


                }
                if (rs.getString("VEH_TYPE_CODE") != null || rs.getString("VEH_TYPE_CODE") != "") {

                    String value = rs.getString("VEH_TYPE_CODE");
                    String codeNameValue = itemCodeNameList.stream().filter(item -> item.getKey().equals(value)) // Filter based on the key
                        .map(Item::getValue) // Map to the values
                        .findFirst() // Get the first matching value
                        .orElse(null); // Return null if no match is found
                    System.out.println("codeNameValue--+" + codeNameValue);
                    if (codeNameValue != null) {
                        //VechileTypejComboBox.addItem(codeNameValue);
                        VechileTypejComboBox.setSelectedItem(codeNameValue);
                    }

                }
                if (rs.getString("REMARKS") != null) {
                    TXT_REMARKS.setText(rs.getString("REMARKS"));
                }
                if (rs.getString("MACHINE_NO") != null) {
                    TXT_Machine.setText(rs.getString("MACHINE_NO"));
                }
                if (rs.getString("GATE_ENTRY_NUMBER") != null) {
                    TXT_GateEntry.setText(rs.getString("GATE_ENTRY_NUMBER"));
                }

                if (rs.getString("CREATED_BY") != null) {
                    TXT_CreateBy.setText(rs.getString("CREATED_BY"));
                }
                if (rs.getString("CREATION_DATE") != null) {
                    TXT_CreateDate.setText(rs.getString("CREATION_DATE"));
                }
                if (rs.getString("CREATION_TIME") != null) {
                    TXT_CreateTime.setText(rs.getString("CREATION_TIME"));
                }

                if (rs.getString("PROCESS_CODE") != null) {
                    TXT_Process.setText(rs.getString("PROCESS_CODE"));
                }
                if (rs.getString("RC_NO") != null) {
                    TXT_RC_NO.setText(rs.getString("RC_NO"));
                }
                if (rs.getString("COMP_VEH_TYPE_CODE") != null) {
                    VechileTypejComboBox.setEnabled(false);
                    ComboBoxChargeApplied.setEnabled(false);
                    ComboBoxChargeApplied.setSelectedIndex(1);
                    TXT_Charge.setText("0");
                }


                //VechileTypejComboBox.addItem(rs.getString("CODE")+"-"+rs.getString("TYPE_DESC"));
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
            JOptionPane.showMessageDialog(null, "Please Enter Valid Vehicle no / Slip No", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);

            resetValue();
        } else {
            forPrint();
        }
    }

    public Integer netWeightCalculate() {
        Integer netWeight = 0;
        int grossWeight = Integer.valueOf(TXT_GrossWeight.getText().toString());
        Integer tareWeight = Integer.valueOf(TXT_TareWeight.getText().toString());
        if (grossWeight > 0 && tareWeight > 0) {
            netWeight = grossWeight - tareWeight;
        }
        return netWeight;
    }

    public void resetValue() {
        VechileTypejComboBox.setSelectedIndex(0);
        ComboBoxChargeApplied.setSelectedIndex(1);
        TXT_AutoWeight.setText("0");
        TXT_GrossWeight.setText("0");
        TXT_TareWeight.setText("0");
        TXT_NetWeight.setText("0");
        TXT_Charge.setText("0");
        TXT_Part.setText(null);
        TXT_GateEntry.setText(null);
        TXT_Process.setText(null);
        TXT_Product.setText(null);
        //TXT_Machine.setText(null);
        TXT_GateEntry.setText(null);
        TXT_TokenNo.setText(null);
        TXT_TrollyNo.setText(null);
        TXT_VechileNo.setText(null);
        TXT_RC_NO.setText(null);
        TXT_REMARKS.setText(null);
        TXT_RC_NO.setText(null);
        BtnActionClear.setEnabled(true);
        BtnSubmit.setEnabled(true);
        BtnGross.setEnabled(true);
        BtnTare.setEnabled(true);
    }


    public void resetValueAfterPrint() {
        onLoad();
        VechileTypejComboBox.setSelectedIndex(0);
        ComboBoxChargeApplied.setSelectedIndex(1);
        TXT_Charge.setText("0");
        TXT_Part.setText(null);
        TXT_GateEntry.setText(null);
        TXT_Process.setText(null);
        TXT_Product.setText(null);
        //TXT_Machine.setText(null);
        TXT_GateEntry.setText(null);
        TXT_TokenNo.setText(null);
        TXT_TrollyNo.setText(null);
        TXT_VechileNo.setText(null);
        TXT_RC_NO.setText(null);
        TXT_REMARKS.setText(null);
        TXT_RC_NO.setText(null);
        TXT_AutoWeight.setText("0");
        TXT_TareWeight.setText("0");
        TXT_NetWeight.setText("0");


        VechileTypejComboBox.setEnabled(true);
        ComboBoxChargeApplied.setEnabled(true);
        TXT_Charge.setEnabled(true);
        TXT_Part.setEnabled(true);
        TXT_GateEntry.setEnabled(true);
        TXT_Process.setEnabled(true);
        TXT_Product.setEnabled(true);
        TXT_SlipNo.setText(null);
        TXT_GateEntry.setEnabled(true);
        TXT_TokenNo.setEnabled(true);
        TXT_TrollyNo.setEnabled(true);
        TXT_VechileNo.setEnabled(true);
        TXT_RC_NO.setEnabled(true);
        TXT_REMARKS.setEnabled(true);
        TXT_RC_NO.setEnabled(true);
        TXT_AutoWeight.setEnabled(true);
        TXT_TareWeight.setEnabled(true);
        TXT_NetWeight.setEnabled(true);
        BtnSubmit.setEnabled(true);
        BtnActionClear.setEnabled(true);
        BtnTare.setEnabled(true);
        BtnGross.setEnabled(true);
        //BtnPrint.setEnabled(false);

    }

    public void disableValueAfterSave() {
        VechileTypejComboBox.setEnabled(false);
        ComboBoxChargeApplied.setEnabled(false);
        TXT_Charge.setEnabled(false);
        TXT_Part.setEnabled(false);
        TXT_GateEntry.setEnabled(false);
        TXT_Process.setEnabled(false);
        TXT_Product.setEnabled(false);
        //TXT_Machine.setText(null);
        TXT_GateEntry.setEnabled(false);
        TXT_TokenNo.setEnabled(false);
        TXT_TrollyNo.setEnabled(false);
        TXT_VechileNo.setEnabled(false);
        TXT_RC_NO.setEnabled(false);
        TXT_REMARKS.setEnabled(false);
        TXT_RC_NO.setEnabled(false);
        TXT_AutoWeight.setEnabled(false);
        TXT_TareWeight.setEnabled(false);
        TXT_NetWeight.setEnabled(false);
        BtnSubmit.setEnabled(false);
        BtnActionClear.setEnabled(false);
        BtnTare.setEnabled(false);
        BtnGross.setEnabled(false);

    }
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnActionClear;
    private javax.swing.JButton BtnGross;
    private javax.swing.JButton BtnLogOut;
    private javax.swing.JButton BtnPrint;
    private javax.swing.JButton BtnSubmit;
    private javax.swing.JButton BtnTare;
    private javax.swing.JComboBox ComboBoxChargeApplied;
    private javax.swing.JLabel LBL_CreateTime;
    private javax.swing.JLabel LBL_GateEntry;
    private javax.swing.JTextField TXT_AutoWeight;
    private javax.swing.JTextField TXT_Charge;
    private javax.swing.JTextField TXT_CreateBy;
    private javax.swing.JTextPane TXT_CreateDate;
    private javax.swing.JTextPane TXT_CreateTime;
    private javax.swing.JTextField TXT_FinealEnterBy;
    private javax.swing.JTextPane TXT_FinealEnterDate;
    private javax.swing.JTextPane TXT_FinealEnterTime;
    private javax.swing.JTextField TXT_GateEntry;
    private javax.swing.JTextPane TXT_GrossWeight;
    private javax.swing.JTextField TXT_Machine;
    private javax.swing.JTextPane TXT_NetWeight;
    private javax.swing.JTextField TXT_Part;
    private javax.swing.JTextField TXT_Process;
    private javax.swing.JTextField TXT_Product;
    private javax.swing.JTextField TXT_RC_NO;
    private javax.swing.JTextField TXT_REMARKS;
    private javax.swing.JTextField TXT_SlipNo;
    private javax.swing.JTextPane TXT_TareWeight;
    private javax.swing.JTextPane TXT_TokenNo;
    private javax.swing.JTextField TXT_TrollyNo;
    private javax.swing.JTextField TXT_VechileNo;
    private javax.swing.JComboBox VechileTypejComboBox;
    private javax.swing.JPanel WeightBridgeJpanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables
    
    
    public String comPoartMechineConnection() {

        try {
            SerialPort port = SerialPort.getCommPort("COM5");
            if (port.openPort()) {
                System.out.println("Port " + port.getSystemPortName() + " opened successfully.");
            } else {
                System.out.println("Failed to open port.");
                return "N";
            }
            port.setComPortParameters(9600, 8, 1, 0); // Adjust these parameters as necessary
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            if (port.openPort()) {
                System.out.println("Port is open");
                byte[] buffer = new byte[1024];
                while (true) {
                    int bytesRead = port.readBytes(buffer, buffer.length);
                    if (bytesRead > 0) {
                        String receivedData = new String(buffer, 0, bytesRead);
                        System.out.println("Data received: " + receivedData);
                        String processedData = receivedData.replace("k", "");
                        processedData = processedData.trim();
                        System.out.println("Processed Data: " + processedData);
                        String firstValue = processedData.split("\\s+")[0];
                        // Display the processed single value
                        System.out.println("Single Value: " + firstValue);
                        int value = Integer.parseInt(firstValue);
                        if (value > 0) {
                            System.out.println("weight---" + value);
                            TXT_AutoWeight.setText(firstValue);
                        } else {
                            System.out.println("weight-0--" + value);
                            TXT_AutoWeight.setText("0");
                        }

                    }
                    break;
                }
            } else {
                System.out.println("Failed to open port");
            }
            port.closePort();
            if (port.closePort()) {
                System.out.println("Port closed successfully.");
            } else {
                System.out.println("Failed to close port.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TXT_AutoWeight.setText("0");
            return "N";
        }
        return "Y";
    }

    public void forPrint() {
        slipNo = TXT_SlipNo.getText().toUpperCase();
        tokenNo = TXT_TokenNo.getText().toUpperCase();
        gateNo = TXT_GateEntry.getText().toUpperCase();
        grossWeight = TXT_GrossWeight.getText();
        tareWeight = TXT_TareWeight.getText();
        netWeight = TXT_NetWeight.getText();
        party = TXT_Part.getText();
        vechileNo = TXT_VechileNo.getText().toUpperCase();
        vechileType = VechileTypejComboBox.getSelectedItem().toString();
        create = TXT_CreateDate.getText() + " " + TXT_CreateTime.getText();
        finaldate = TXT_FinealEnterDate.getText() + " " + TXT_FinealEnterTime.getText();
        charge = TXT_Charge.getText();
        product = TXT_Product.getText();
        remarks = TXT_REMARKS.getText();

    }


    public void onLoadApiVehicleTypeAutoSugest() {
        suggestionsListParty = new ArrayList<>();
        suggestionsListPoduct = new ArrayList<>();
        suggestionsListRemarks = new ArrayList<>();
       // String url = "http://182.16.9.100:7003/RestApiWeightBridge/resources/vehicleType";
        String url = "http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleType";
        // Try-catch block to handle potential IOExceptions and other exceptions
        try {
            // Create a URL object from the URL string
            URL obj = new URL(url);
            // Open a connection to the URL
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // Set the request method to GET (optional since GET is default)
            con.setRequestMethod("GET");
            // Add headers to the request
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            // Get the HTTP response code
            int responseCode = con.getResponseCode();
            // Print the URL being called and the response code
            System.out.println("Sending 'GET' request to URL: " + url);
            System.out.println("Response Code: " + responseCode);
            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder(); // Use StringBuilder for efficient string concatenation
            // Read the response line by line
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // Close the BufferedReader
            in.close();
            // Convert the response to a string
            String jsonResponse = response.toString();

            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);


            System.out.println("\nVehicle Type List:");
            for (VehicleType rs : apiResponse.getVehicleTypeList()) {
                System.out.println(rs.getCode() + " - " + rs.getWeighingRate());
                VechileTypejComboBox.addItem(rs.getSubtypeDesc());
                //String value = rs.getString("WEIGHING_RATE") + "-" + rs.getString("CODE");
                itemList.add(new Item(rs.getSubtypeDesc(), rs.getWeighingRate()));
                itemCodeList.add(new Item(rs.getSubtypeDesc(), rs.getCode()));
                itemCodeNameList.add(new Item(rs.getCode(), rs.getSubtypeDesc()));
            }

            System.out.println("\nAutosuggest List:");
            for (Autosuggest autosuggest : apiResponse.getAutosuggestList()) {
                System.out.println(autosuggest.getParty() + " - " + autosuggest.getProduct());

                suggestionsListParty.add(autosuggest.getParty());
                suggestionsListPoduct.add(autosuggest.getProduct());
                suggestionsListRemarks.add(autosuggest.getRemarks());
            }

        } catch (IOException e) {
            // Print the exception message if an error occurs
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        }
        // Print the response
        // System.out.println("Response: " + jsonResponse);

    }


    public void oncallApiVehicleSlipNo(String vechileNo, String slipNo) {
       // String url = "http://182.16.9.100:7003/RestApiWeightBridge/resources/vehicleDetails";
        String url = "http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleDetails";
        // Try-catch block to handle potential IOExceptions and other exceptions
        List<VehicleDetails> filteredList = null;
        try {
            // Create a URL object from the URL string
            URL obj = new URL(url);
            // Open a connection to the URL
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // Set the request method to GET (optional since GET is default)
            con.setRequestMethod("GET");
            // Add headers to the request
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            // Get the HTTP response code
            int responseCode = con.getResponseCode();
            // Print the URL being called and the response code
            System.out.println("Sending 'GET' request to URL: " + url);
            System.out.println("Response Code: " + responseCode);
            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder(); // Use StringBuilder for efficient string concatenation
            // Read the response line by line
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // Close the BufferedReader
            in.close();
            // Convert the response to a string
            String jsonResponse = response.toString();

            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);

            // Print the parsed response

            List<VehicleDetails> vehicleDetailsList = apiResponse.getVehicleDetailsList();
            int size = vehicleDetailsList.size();
            if (vechileNo != null) {
                for (int i = 0; i < size; i++) {
                    filteredList =
                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getVehicleNo().equals(vechileNo)).collect(Collectors.toList());
                }
            }

            if (slipNo != null) {
                for (int i = 0; i < size; i++) {
                    filteredList =
                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getSlipNo().equals(slipNo)).collect(Collectors.toList());
                }
            }


            System.out.println("Filtered Vehicles:");
            filteredList.forEach(System.out::println);

            for (VehicleDetails rs : filteredList) {
                System.out.println(rs.getVehicleNo() + " - " + rs.getTokenNo());


                if (rs.getVehicleNo() != null) {
                    TXT_VechileNo.setText(rs.getVehicleNo().toUpperCase());
                }

                if (slipNo == null) {
                    if (rs.getSlipNo().equalsIgnoreCase("0")) {
                    } else {
                        TXT_SlipNo.setText(rs.getSlipNo());
                    }
                }

                if (rs.getTokenNo().equalsIgnoreCase("0")) {

                } else {
                    TXT_TokenNo.setText(rs.getTokenNo());
                }
                if (rs.getParty().equalsIgnoreCase("0")) {

                } else {
                    TXT_Part.setText(rs.getParty().toUpperCase());
                }
                if (rs.getProduct().equalsIgnoreCase("0")) {

                } else {
                    TXT_Product.setText(rs.getProduct().toUpperCase());
                }
                if (rs.getGrossWeight().equalsIgnoreCase("0")) {
                    TXT_GrossWeight.setText(rs.getGrossWeight());
                } else {
                    Integer grossWeight = Integer.parseInt(rs.getGrossWeight());
                    if (grossWeight > 0) {
                        BtnGross.setEnabled(false);
                    }
                    TXT_GrossWeight.setText(rs.getGrossWeight());
                }
                if (rs.getTereWeight().equalsIgnoreCase("0")) {
                    TXT_TareWeight.setText(rs.getTereWeight());

                } else {
                    Integer tareWeight = Integer.parseInt(rs.getTereWeight());
                    if (tareWeight > 0) {
                        BtnTare.setEnabled(false);
                    }
                    TXT_TareWeight.setText(rs.getTereWeight());
                }
                if (rs.getNetWeight().equalsIgnoreCase("0")) {
                    TXT_NetWeight.setText(rs.getNetWeight());

                } else {
                    Integer tareWeight = Integer.parseInt(rs.getNetWeight());
                    if (tareWeight > 0) {
                        BtnSubmit.setEnabled(false);
                        BtnActionClear.setEnabled(false);
                    }
                    TXT_TareWeight.setText(rs.getNetWeight());
                }
                if (rs.getFinalEnteredBy().equalsIgnoreCase("0")) {

                } else {
                    TXT_FinealEnterBy.setText(rs.getFinalEnteredBy());
                }
                if (rs.getTrolleyNo().equalsIgnoreCase("0")) {

                } else {
                    TXT_TrollyNo.setText(rs.getTrolleyNo());
                }
                if (rs.getCharge().equalsIgnoreCase("0") || rs.getCharge() == "0") {
                    TXT_Charge.setText("0");
                } else {

                    TXT_Charge.setText(rs.getCharge());
                }
                if (rs.getCharge_applicable().equalsIgnoreCase("0")) {


                }

                else {
                    if (rs.getCharge_applicable().equalsIgnoreCase("Yes")) {
                        ComboBoxChargeApplied.setSelectedIndex(0);
                    } else {
                        ComboBoxChargeApplied.setSelectedIndex(1);
                    }
                }
                //                if (rs.getVehicleN("VEH_TYPE_CODE") != null || rs.getString("VEH_TYPE_CODE") != "") {
                //
                //                String value = rs.getString("VEH_TYPE_CODE");
                //                String codeNameValue = itemCodeNameList.stream().filter(item -> item.getKey().equals(value)) // Filter based on the key
                //                    .map(Item::getValue) // Map to the values
                //                    .findFirst() // Get the first matching value
                //                    .orElse(null); // Return null if no match is found
                //                System.out.println("codeNameValue--+" + codeNameValue);
                //                if (codeNameValue != null) {
                //                    //VechileTypejComboBox.addItem(codeNameValue);
                //                    VechileTypejComboBox.setSelectedItem(codeNameValue);
                //                }
                //
                //                }

                if (rs.getVeh_subtype_desc().equalsIgnoreCase("0")) {

                } else {
                    VechileTypejComboBox.setSelectedItem(rs.getVeh_subtype_desc());
                }

                if (rs.getRemarks().equalsIgnoreCase("0")) {

                } else {
                    TXT_REMARKS.setText(rs.getRemarks().toUpperCase());
                }
                if (rs.getMachineNo().equalsIgnoreCase("0")) {

                } else {
                    TXT_Machine.setText(rs.getMachineNo().toUpperCase());
                }
                if (rs.getGateEntryNumber().equalsIgnoreCase("0")) {

                } else {
                    TXT_GateEntry.setText(rs.getGateEntryNumber());
                }

                if (rs.getCreatedBy().equalsIgnoreCase("0")) {

                } else {
                    TXT_CreateBy.setText(rs.getCreatedBy());
                }
                if (rs.getCreationDate().equalsIgnoreCase("0")) {

                } else {
                    TXT_CreateDate.setText(rs.getCreationDate());
                }
                if (rs.getCreationTime().equalsIgnoreCase("0")) {

                } else {
                    TXT_CreateTime.setText(rs.getCreationTime());
                }

                if (rs.getProcessCode().equalsIgnoreCase("0")) {

                } else {
                    TXT_Process.setText(rs.getProcessCode());
                }
                if (rs.getRcNo().equalsIgnoreCase("0")) {

                } else {
                    TXT_RC_NO.setText(rs.getRcNo());
                }
                if (rs.getCompVehTypeCode().equalsIgnoreCase("0")) {
                    // VechileTypejComboBox.setEnabled(false);

                } else {
                    ComboBoxChargeApplied.setEnabled(false);
                    ComboBoxChargeApplied.setSelectedIndex(1);
                    TXT_Charge.setText("0");
                }


            }

            //            System.out.println("Vehicle Details List:");
            //            for (VehicleDetails vehicle : apiResponse.getVehicleDetailsList()) {
            //                System.out.println(vehicle.getVehicleNo() + " - " + vehicle.getTokenNo());
            //            }


        } catch (IOException e) {
            // Print the exception message if an error occurs
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        }
        int count = filteredList.size();
        if (count <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Valid Vehicle no / Slip No", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);

            resetValue();
        } else {
            forPrint();
        }

    }
}
