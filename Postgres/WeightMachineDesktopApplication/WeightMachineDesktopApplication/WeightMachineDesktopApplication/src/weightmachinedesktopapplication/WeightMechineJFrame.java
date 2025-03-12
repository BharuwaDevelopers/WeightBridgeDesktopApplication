package weightmachinedesktopapplication;

import com.fazecast.jSerialComm.SerialPort;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author lenovo
 */
public class WeightMechineJFrame extends javax.swing.JFrame {
  // String url = "http://182.16.9.100:7003/RestApiWeightBridge/resources";
  ArrayList<Item> itemList = new ArrayList<>();
  ArrayList<Item> itemCodeList = new ArrayList<>();
  ArrayList<Item> itemCodeNameList = new ArrayList<>();
  String todayDateTime = null;
  int BaudRate = 9600;
  int DataBits = 8;
  int StopBits = SerialPort.ONE_STOP_BIT;
  int Parity = SerialPort.NO_PARITY;
  String userNamevalue = null;
  String unitCode = null;
  String btnEventName = "N";
  String wt_type = null;
  String currentValue = "0";
  String currentSlipValue = "0";
  String vechileCode = null;
  String compVechileType = "N";
  String trollyReq = "N";

  String slipNo = null, tokenNo = null, gateNo = null, grossWeight = null, tareWeight = null, netWeight = null, party =
    null, vechileNo = null, vechileType = null, create = null, finaldate = null, charge = null, product =
    null, remarks = null, comport_no = null, machinecode = null, ftTereWeight = null;
  private JPopupMenu suggestionMenuPart;
  private JPopupMenu suggestionMenuProduct;
  private JPopupMenu suggestionMenuRemarks;
  private JPopupMenu suggestionMenuVehicleNo;
  private JPopupMenu suggestionMenutrollyNo;
  private List<String> suggestionsListParty;
  private List<String> suggestionsListPoduct;
  private List<String> suggestionsListRemarks;
  private List<String> suggestionsListVehicleNo;
  private List<String> suggestionsListTrollyNo;

  /** Creates new form WeightMechineJFrame */
  public WeightMechineJFrame(String userName, String unitCd, String machine_code, String comPort) {
    System.out.println("userName----" + userName);
    if (userName == null || userName.isEmpty() || userName == "") {
      userName = "E-001";
    }
    userNamevalue = userName;
    unitCode = unitCd;
    comport_no = comPort;
    machinecode = machine_code;
    initComponents();

    onLoad();
    onLoadApiVehicleTypeAutoSugest();

    onLoadDate();
    TXT_Machine.setText(machine_code);

    autoSugestParty();
    autoSugestProduct();
    autoSugestRemarks();
    autoSugestVehicleNo();
    autoSuggestTrollyNo();
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
    suggestionMenuPart.setVisible(false);
    suggestionMenuPart.removeAll();
    String input = TXT_Part.getText();
    if (input.length() > 3) {
      boolean found = false;
      for (String item : suggestionsListParty) {
        if (item.toLowerCase().contains(input.toLowerCase())) {
          JMenuItem menuItem = new JMenuItem(item);
          menuItem.addActionListener(e -> {
              TXT_Part.setText(item);
              suggestionMenuPart.setVisible(false);
              // textField.requestFocusInWindow();
            });
          suggestionMenuPart.add(menuItem);
          found = true;
        }
      }
      if (found) {
        suggestionMenuPart.setLightWeightPopupEnabled(false);
        suggestionMenuPart.show(TXT_Part, 0, TXT_Part.getHeight());
        SwingUtilities.invokeLater(TXT_Part::requestFocusInWindow);
      } else {
        suggestionMenuPart.setVisible(false);
      }
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
    suggestionMenuProduct.setVisible(false);
    suggestionMenuProduct.removeAll();
    String input = TXT_Product.getText();
    if (input.length() > 3) {
      boolean found = false;
      for (String item : suggestionsListPoduct) {
        if (item.toLowerCase().contains(input.toLowerCase())) {
          JMenuItem menuItem = new JMenuItem(item);
          menuItem.addActionListener(e -> {
              TXT_Product.setText(item);
              suggestionMenuProduct.setVisible(false);
              // textField.requestFocusInWindow();
            });
          suggestionMenuProduct.add(menuItem);
          found = true;
        }
      }
      if (found) {
        suggestionMenuProduct.setLightWeightPopupEnabled(false);
        suggestionMenuProduct.show(TXT_Product, 0, TXT_Product.getHeight());
        SwingUtilities.invokeLater(TXT_Product::requestFocusInWindow);
      } else {
        suggestionMenuProduct.setVisible(false);
      }
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
    suggestionMenuRemarks.setVisible(false);
    suggestionMenuRemarks.removeAll();
    String input = TXT_REMARKS.getText();
    if (input.length() > 3) {
      boolean found = false;
      for (String item : suggestionsListRemarks) {
        if (item.toLowerCase().contains(input.toLowerCase())) {
          JMenuItem menuItem = new JMenuItem(item);
          menuItem.addActionListener(e -> {
              TXT_REMARKS.setText(item);
              suggestionMenuRemarks.setVisible(false);
              // textField.requestFocusInWindow();
            });
          suggestionMenuRemarks.add(menuItem);
          found = true;
        }
      }
      if (found) {
        suggestionMenuRemarks.setLightWeightPopupEnabled(false);
        suggestionMenuRemarks.show(TXT_REMARKS, 0, TXT_REMARKS.getHeight());
        SwingUtilities.invokeLater(TXT_REMARKS::requestFocusInWindow);
      } else {
        suggestionMenuRemarks.setVisible(false);
      }
    }
    //        String input = TXT_REMARKS.getText();
    //        if (input.isEmpty() || input.equalsIgnoreCase(null) || input.equals("")) {
    //            // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
    //            return;
    //        } else {
    //            System.out.println("input.length()--" + input.length());
    //            if (input.length() < 4) {
    //                System.out.println("input.length()-1-" + input.length());
    //                // JOptionPane.showMessageDialog(null, "Please enter at least 4 characters", "Message", JOptionPane.ERROR_MESSAGE);
    //                return;
    //            }
    //        }
    //        suggestionMenuRemarks.removeAll();
    //
    //        if (input.isEmpty()) {
    //            suggestionMenuRemarks.setVisible(false);
    //            return;
    //        }
    //
    //        for (String suggestion : suggestionsListRemarks) {
    //            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
    //                JMenuItem item = new JMenuItem(suggestion);
    //                item.addActionListener(e -> {
    //                        TXT_REMARKS.setText(suggestion);
    //                        suggestionMenuRemarks.setVisible(false);
    //                    });
    //                suggestionMenuRemarks.add(item);
    //            }
    //        }
    //
    //        if (suggestionMenuRemarks.getComponentCount() > 0) {
    //            suggestionMenuRemarks.show(TXT_REMARKS, 0, TXT_REMARKS.getHeight());
    //        } else {
    //            suggestionMenuRemarks.setVisible(false);
    //        }
  }
  //----------------------------------------------------------------------

  public void autoSugestVehicleNo() {
    suggestionMenuVehicleNo = new JPopupMenu();
    TXT_VechileNo.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updateVehicleNoSuggestions();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateVehicleNoSuggestions();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateVehicleNoSuggestions();
      }
    });
    TXT_VechileNo.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          suggestionMenuVehicleNo.setVisible(false);
        }
      }
    });
  }


  private void updateVehicleNoSuggestions() {
    suggestionMenuVehicleNo.setVisible(false);
    suggestionMenuVehicleNo.removeAll();
    String input = TXT_VechileNo.getText();
    if (input.length() > 3) {
      boolean found = false;
      for (String item : suggestionsListVehicleNo) {
        if (item.toLowerCase().contains(input.toLowerCase())) {
          JMenuItem menuItem = new JMenuItem(item);
          menuItem.addActionListener(e -> {
              TXT_VechileNo.setText(item);
              suggestionMenuVehicleNo.setVisible(false);
              // textField.requestFocusInWindow();
            });
          suggestionMenuVehicleNo.add(menuItem);
          found = true;
        }
      }
      if (found) {
        suggestionMenuVehicleNo.setLightWeightPopupEnabled(false);
        suggestionMenuVehicleNo.show(TXT_VechileNo, 0, TXT_VechileNo.getHeight());
        SwingUtilities.invokeLater(TXT_VechileNo::requestFocusInWindow);
      } else {
        suggestionMenuVehicleNo.setVisible(false);
      }
    }
  }

  public void autoSuggestTrollyNo() {
    suggestionMenutrollyNo = new JPopupMenu();
    TXT_TrollyNo.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updateTrollyNoSuggestions();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateTrollyNoSuggestions();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateTrollyNoSuggestions();
      }
    });
    TXT_TrollyNo.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          suggestionMenutrollyNo.setVisible(false);
        }
      }
    });
  }

  public void updateTrollyNoSuggestions() {
    suggestionMenutrollyNo.setVisible(false);
    suggestionMenutrollyNo.removeAll();
    String input = TXT_TrollyNo.getText();
    if (input.length() > 3) {
      boolean found = false;
      for (String item : suggestionsListTrollyNo) {
        if (item.toLowerCase().contains(input.toLowerCase())) {
          JMenuItem menuItem = new JMenuItem(item);
          menuItem.addActionListener(e -> {
              TXT_TrollyNo.setText(item);
              suggestionMenutrollyNo.setVisible(false);
              // textField.requestFocusInWindow();
            });
          suggestionMenutrollyNo.add(menuItem);
          found = true;
        }
      }
      if (found) {
        suggestionMenutrollyNo.setLightWeightPopupEnabled(false);
        suggestionMenutrollyNo.show(TXT_TrollyNo, 0, TXT_TrollyNo.getHeight());
        SwingUtilities.invokeLater(TXT_TrollyNo::requestFocusInWindow);
      } else {
        suggestionMenutrollyNo.setVisible(false);
      }
    }
  }

  //----------------------------------------------------------------------

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

    TXT_AutoWeight.setBackground(new java.awt.Color(248, 245, 245));

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
    TXT_TokenNo.setBackground(new java.awt.Color(248, 245, 245));
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel7)
                  .addComponent(jLabel8)
                  .addComponent(jLabel5)
                  .addComponent(jLabel4)
                  .addComponent(LBL_CreateTime)))
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
          .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel8))
        .addGap(18, 18, 18)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(LBL_CreateTime))
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

    TXT_TrollyNo.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        TXT_TrollyNoKeyPressed(evt);
      }
    });

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel21)
              .addComponent(jLabel20)
              .addComponent(jLabel19)
              .addComponent(jLabel2)
              .addComponent(jLabel22))
            .addGap(27, 27, 27)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(TXT_VechileNo, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(TXT_TrollyNo)
              .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 93, Short.MAX_VALUE))))
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel23)
              .addComponent(jLabel24))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(TXT_Product, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
              .addComponent(TXT_Part))))
        .addContainerGap())
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
      JOptionPane.showMessageDialog(null, "Gross Weight/Tare Weight 0", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (TXT_TokenNo.getText().isEmpty() || TXT_TokenNo.getText() == null || TXT_TokenNo.getText() == "") {
      JOptionPane.showMessageDialog(null, "Token number not found.", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (Integer.parseInt(TXT_NetWeight.getText()) < 0) {
      String message = "Net Weight must be greater than zero.";
      JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (comPoartMechineConnection("SaveBtn").equalsIgnoreCase("N")) {
      String message = "Weight-bridge weight not match.";
      JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (!ftTereWeight.equalsIgnoreCase("0")) {
      if (TXT_TareWeight.getText() != "0" && TXT_GrossWeight.getText().equalsIgnoreCase("0")) {
        int margin = 1000;
        int tareWeight = Integer.valueOf(TXT_TareWeight.getText());
        int oldTareWeight = Integer.parseInt(ftTereWeight);

        if (tareWeight > oldTareWeight + margin || tareWeight < oldTareWeight - margin) {
          String message = "Difference is more than 1000 KGs.";
          JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
      }
    }

    if (compVechileType.equalsIgnoreCase("N")) {
      if (TXT_Charge.getText() == "0" || TXT_Charge.getText().equalsIgnoreCase("0")) {
        String message = "This vehicle outside charge is applied, please select vehicle type.";
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }
    
    String selectedItem = (String) VechileTypejComboBox.getSelectedItem();
    if (selectedItem.equalsIgnoreCase("Please Select")) {
      JOptionPane.showMessageDialog(null, "please select vehicle type", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (TXT_Part.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Please Enter Party Details", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (TXT_Product.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Please Enter Product Details", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (TXT_SlipNo.getText().trim().isEmpty() || TXT_SlipNo.getText().trim() == null ||
        TXT_SlipNo.getText().trim() == "" || TXT_SlipNo.getText().equalsIgnoreCase("0")) {
      insertdateCallApi();
    } else {
      if (TXT_REMARKS.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please Enter Remark", "Message", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (Integer.parseInt(TXT_NetWeight.getText()) <= 0) {
        String message = "Net weight is should be greater than 0";
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      updatedateCallApi();
    }
    }//GEN-LAST:event_BtnSubmitActionPerformed
    
    
    
  public void insertdateCallApi() {
    try {
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
      jsonObject.addProperty("unit_cd", unitCode);
      jsonObject.addProperty("wt_type", wt_type);

      try (OutputStream os = con.getOutputStream()) {
        byte[] input = jsonObject.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
      }
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder stringBuilder = new StringBuilder();
        String responseLine = null;
        while ((responseLine = bufferedReader.readLine()) != null) {
          stringBuilder.append(responseLine.trim());
        }
        JSONObject responseObject = new JSONObject(stringBuilder.toString());

        int status = responseObject.getInt("statusCode");
        String message = responseObject.getString("message");

        if (status == 200) {
          String SlipNojson = responseObject.getString("slipNo");
          JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
          TXT_SlipNo.setText(SlipNojson);
          forPrint();
          resetValue();
        } else {
          JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void updatedateCallApi() {
    try {
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
      jsonObject.addProperty("gross_weight", TXT_GrossWeight.getText());
      jsonObject.addProperty("tere_weight", TXT_TareWeight.getText());
      jsonObject.addProperty("net_weight", TXT_NetWeight.getText());
      jsonObject.addProperty("final_entered_by", TXT_FinealEnterBy.getText().toUpperCase());
      jsonObject.addProperty("trolley_no", TXT_TrollyNo.getText());
      jsonObject.addProperty("charge", TXT_Charge.getText());
      jsonObject.addProperty("charge_applicable", ComboBoxChargeApplied.getSelectedItem().toString());
      jsonObject.addProperty("party", TXT_Part.getText().toUpperCase());
      jsonObject.addProperty("product", TXT_Product.getText().toUpperCase());
      jsonObject.addProperty("remarks", TXT_REMARKS.getText().toUpperCase());

      try (OutputStream outputStream = con.getOutputStream()) {
        byte[] bytes = jsonObject.toString().getBytes("utf-8");
        outputStream.write(bytes, 0, bytes.length);
      }
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder stringBuilder = new StringBuilder();
        String responseLine = null;
        while ((responseLine = bufferedReader.readLine()) != null) {
          stringBuilder.append(responseLine.trim());
        }

        JSONObject responseObject = new JSONObject(stringBuilder.toString());
        int status = responseObject.getInt("statusCode");
        String message = responseObject.getString("message");
        if (status == 200) {
          JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
          TXT_CreateDate.setText(getDate(LocalDate.now()));
          if (Integer.parseInt(TXT_NetWeight.getText()) > 0) {
            TXT_FinealEnterBy.setText(userNamevalue);
            TXT_FinealEnterDate.setText(getDate(LocalDate.now()));
            TXT_FinealEnterTime.setText(getTime(LocalDateTime.now()));
          }
          forPrint();
          TXT_SlipNo.setText(TXT_SlipNo.getText());
          disableValueAfterSave();
        } else {
          JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.INFORMATION_MESSAGE);
    }
  }

    private void BtnSubmitActionPerformedold(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_BtnSubmitActionPerformed
    } //GEN-LAST:event_BtnSubmitActionPerformed


    private void BtnGrossActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrossActionPerformed
    btnEventName = "grossBtnCall";
    wt_type = "G";
    comPoartMechineConnection("GrossBtn");
    if (TXT_SlipNo.getText().trim() == null || TXT_SlipNo.getText().trim().isEmpty() ||
        TXT_SlipNo.getText().trim().equals("")) {
      TXT_TareWeight.setText("0");
    }
    TXT_GrossWeight.setText(TXT_AutoWeight.getText().toString());
    int netWeight = netWeightCalculate();
    TXT_NetWeight.setText(String.valueOf(netWeight));
    if (Integer.parseInt(TXT_NetWeight.getText()) > 0) {
      TXT_FinealEnterBy.setText(userNamevalue);
      TXT_FinealEnterDate.setText(getDate(LocalDate.now()));
      TXT_FinealEnterTime.setText(getTime(LocalDateTime.now()));
    }
    }//GEN-LAST:event_BtnGrossActionPerformed

    private void BtnTareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTareActionPerformed
    btnEventName = "tareBtnCall";
    wt_type = "T";
    comPoartMechineConnection("TareBtn");
    if (TXT_SlipNo.getText() == null || TXT_SlipNo.getText().isEmpty()) {
      TXT_GrossWeight.setText("0");
    }
    TXT_TareWeight.setText(TXT_AutoWeight.getText().toString());
    TXT_NetWeight.setText(String.valueOf(netWeightCalculate())); 
    }//GEN-LAST:event_BtnTareActionPerformed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
    if (TXT_SlipNo.getText().trim().isEmpty() || TXT_SlipNo.getText().trim() == "" ||
        TXT_SlipNo.getText().trim().equals("") || TXT_SlipNo.getText().trim().equalsIgnoreCase("0")) {
      JOptionPane.showMessageDialog(null, "Please Enter Slip No", "Message", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      InputStream inputStream = new FileInputStream("C:/jasperfile/Bridge_Entry.jasper");
      JasperReport design = (JasperReport) JRLoader.loadObject(inputStream);
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

      net.sf.jasperreports.engine.JasperPrint print =
      JasperFillManager.fillReport(design, parameters, new JREmptyDataSource());
      JasperViewer.viewReport(print, false);
      resetValueAfterPrint();
    } catch (FileNotFoundException ex) {
      JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
    } catch (JRException ex) {
      JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void TXT_GateEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXT_GateEntryActionPerformed
    // TODO add your handling code here:
    }//GEN-LAST:event_TXT_GateEntryActionPerformed
  
    private void VechileTypejComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VechileTypejComboBoxActionPerformed
    String selectedItem = (String) VechileTypejComboBox.getSelectedItem();
    if (selectedItem.equalsIgnoreCase("Please Select")) {
      TXT_Charge.setText("0");
      ComboBoxChargeApplied.setSelectedIndex(1);
    } else {
      if (compVechileType.equalsIgnoreCase("N")) {
        ComboBoxChargeApplied.setSelectedIndex(0);
        String firstValue = itemList.stream().filter(item -> item.getKey().equals(selectedItem)) // Filter based on the key
          .map(Item::getValue) // Map to the values
          .findFirst() // Get the first matching value
          .orElse(null); // Return null if no match is found

        if (firstValue == null) {
          TXT_Charge.setText("0");
        } else {
          if (compVechileType.equalsIgnoreCase("Y")) {
            ComboBoxChargeApplied.setSelectedIndex(1);
            TXT_Charge.setText("0");
          } else {
            TXT_Charge.setText(firstValue);
          }
        }
      }
      String codeValue = itemCodeList.stream().filter(item -> item.getKey().equals(selectedItem)) // Filter based on the key
        .map(Item::getValue)
        .findFirst()
        .orElse(null);
      if (codeValue != null) {
        vechileCode = codeValue;
      }
    }
    onLoadDate();
    }//GEN-LAST:event_VechileTypejComboBoxActionPerformed

    private void BtnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLogOutActionPerformed
    LoginJFrame weightFrame = new LoginJFrame();
    weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    weightFrame.setSize(800, 610);
    weightFrame.setVisible(true);
    super.dispose();
    }//GEN-LAST:event_BtnLogOutActionPerformed

    private void TXT_VechileNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXT_VechileNoKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      String vechileNo = TXT_VechileNo.getText().toString().toUpperCase();
      oncallApiVehicleSlipNo(vechileNo, null);
      onLoadDate();
    }
    }//GEN-LAST:event_TXT_VechileNoKeyPressed

    private void TXT_SlipNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXT_SlipNoKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      String date = getDate(LocalDate.now());
      TXT_CreateDate.setText(date);
      TXT_FinealEnterBy.setText(userNamevalue);
      TXT_FinealEnterDate.setText(date);
      TXT_FinealEnterTime.setText(getTime(LocalDateTime.now()));
      oncallApiVehicleSlipNo(null, TXT_SlipNo.getText().trim().toUpperCase());
      onLoadDate();
    }
    }//GEN-LAST:event_TXT_SlipNoKeyPressed

    private void ComboBoxChargeAppliedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxChargeAppliedActionPerformed
    if (compVechileType.equalsIgnoreCase("Y")) {
      ComboBoxChargeApplied.setSelectedIndex(1);
      TXT_Charge.setText("0");
      return;
    }
    String selectedItemValue = (String) ComboBoxChargeApplied.getSelectedItem();
    if (selectedItemValue.equalsIgnoreCase("No")) {
      TXT_Charge.setText("0");
    } else {
      String selectedType = (String) VechileTypejComboBox.getSelectedItem();
      String firstValue = itemList.stream().filter(item -> item.getKey().equals(selectedType)) // Filter based on the key
        .map(Item::getValue)
        .findFirst()
        .orElse(null);
      TXT_Charge.setText(firstValue == null ? "0" : firstValue);
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
    TXT_VechileNo.setEnabled(true);
    TXT_SlipNo.setEnabled(true);
    }//GEN-LAST:event_BtnActionClearActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    PrintSlip weightFrame = new PrintSlip(userNamevalue, unitCode, machinecode, comport_no);
    weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    weightFrame.setSize(1150, 700);
    weightFrame.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    ReportsJFrame weightFrame = new ReportsJFrame(userNamevalue, unitCode, machinecode, comport_no);
    weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    weightFrame.setSize(1150, 700);
    weightFrame.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

  private void TXT_TrollyNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXT_TrollyNoKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      try {
        trollyDeatilsApiCall();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }
      onLoadDate();
    }
  }//GEN-LAST:event_TXT_TrollyNoKeyPressed

  public static void main(String args[]) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                                                                                  ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                                                                                  ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                                                                                  ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(WeightMechineJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                                                                                  ex);
    }
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new WeightMechineJFrame("", "", "", "").setVisible(true);
      }
    });
  }

  public void vehicleDetailsWithAutoSugest() {
    WeightBridgeDao obj = new WeightBridgeDao();
    suggestionsListParty = new ArrayList<>();
    suggestionsListPoduct = new ArrayList<>();
    suggestionsListRemarks = new ArrayList<>();
    try (Connection connection = obj.getStartConnection();
         Statement statement = connection.createStatement();) {
      String query = "SELECT V.CODE, V.TYPE_CODE, V.SUBTYPE_DESC, M.WEIGHING_RATE\n" + 
      "FROM VEHICLE_SUBTYPE_MASTER V\n" + 
      "INNER JOIN WEIGHING_RATE_MASTER M ON V.CODE=M.VEHICLE_SUB_TYPE_CODE\n" + 
      "WHERE V.STATUS='Y' AND M.STATUS='Y';";
      try (ResultSet resultSet = statement.executeQuery(query)) {
        while (resultSet.next()) {
          VechileTypejComboBox.addItem(resultSet.getString("SUBTYPE_DESC"));
          //String value = rs.getString("WEIGHING_RATE") + "-" + rs.getString("CODE");
          itemList.add(new Item(resultSet.getString("SUBTYPE_DESC"), resultSet.getString("WEIGHING_RATE")));
          itemCodeList.add(new Item(resultSet.getString("SUBTYPE_DESC"), resultSet.getString("CODE")));
          itemCodeNameList.add(new Item(resultSet.getString("CODE"), resultSet.getString("SUBTYPE_DESC")));
        }
      }
      query = "SELECT DISTINCT PARTY, PRODUCT, REMARKS FROM WEIGHING_BRIDGE;";
      try (ResultSet resultSet = statement.executeQuery(query)) {
        while (resultSet.next()) {
          suggestionsListParty.add(resultSet.getString("PARTY"));
          suggestionsListPoduct.add(resultSet.getString("PRODUCT"));
          suggestionsListRemarks.add(resultSet.getString("REMARKS"));
        }
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
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
    VechileTypejComboBox.setEnabled(true);
    ComboBoxChargeApplied.setSelectedIndex(1);
    ComboBoxChargeApplied.setEnabled(true);
    TXT_AutoWeight.setText("0");
    TXT_GrossWeight.setText("0");
    TXT_TareWeight.setText("0");
    TXT_NetWeight.setText("0");
    TXT_Charge.setText("0");
    TXT_Part.setText(null);
    TXT_GateEntry.setText(null);
    TXT_Process.setText(null);
    TXT_Product.setText(null);
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
  }

  public void disableValueAfterSave() {
    VechileTypejComboBox.setEnabled(false);
    ComboBoxChargeApplied.setEnabled(false);
    TXT_Charge.setEnabled(false);
    TXT_Part.setEnabled(false);
    TXT_GateEntry.setEnabled(false);
    TXT_Process.setEnabled(false);
    TXT_Product.setEnabled(false);
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

  public String comPoartMechineConnection(String callBy) {
    SerialPort port = SerialPort.getCommPort(comport_no);
    try {
      if (!port.openPort()) {
        JOptionPane.showMessageDialog(null, "Failed to open port 123", "Message", JOptionPane.INFORMATION_MESSAGE);
        return "N";
      }
      port.setComPortParameters(9600, 8, 1, 0); // Adjust these parameters as necessary
      port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
      if (port.openPort()) {
        byte[] buffer = new byte[1024];
        while (true) {
          int bytesRead = port.readBytes(buffer, buffer.length);
          if (bytesRead > 0) {
            String receivedData = new String(buffer, 0, bytesRead);
            String processedData = receivedData.replace("k", "");
            processedData = processedData.trim();
            String firstValue = processedData.split("\\s+")[0];
            int value = Integer.parseInt(firstValue);
            JOptionPane.showMessageDialog(null, "Weight match: " + value, "Message", JOptionPane.INFORMATION_MESSAGE);
            if (value > 0) {
              if (callBy.equalsIgnoreCase("SaveBtn")) {
                if (btnEventName.equalsIgnoreCase("grossBtnCall")) {
                  int grossWeight = Integer.valueOf(TXT_GrossWeight.getText());
                  if (grossWeight != value) {
                    TXT_GrossWeight.setText("0");
                    String message = "Weight bridge weight not match.";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    return "N";
                  }
                }
                if (btnEventName.equalsIgnoreCase("tareBtnCall")) {
                  int tareWeight = Integer.valueOf(TXT_TareWeight.getText());
                  if (tareWeight != value) {
                    TXT_TareWeight.setText("0");
                    String message = "Weight bridge weight not match.";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    return "N";
                  }
                }
              } else {
                TXT_AutoWeight.setText(firstValue);
              }
            } else {
              TXT_AutoWeight.setText("0");
            }
          }
          break;
        }
      } else {
        JOptionPane.showMessageDialog(null, "Failed to open port: ", "Message", JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      String message = "Failed to open port: " + ex.toString();
      JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
      TXT_AutoWeight.setText("0");
      port.closePort();
      return "N";
    } finally {
      port.closePort();
    }
    return "Y";
  }

  public void forPrint() {
    slipNo = TXT_SlipNo.getText().trim().toUpperCase();
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
    suggestionsListVehicleNo = new ArrayList<>();
    suggestionsListTrollyNo = new ArrayList<>();
    try {
      URL obj = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleType");
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String inputLine = null;
      while ((inputLine = bufferedReader.readLine()) != null) {
        stringBuilder.append(inputLine);
      }
      bufferedReader.close();
      Gson gson = new Gson();
      ApiResponse apiResponse = gson.fromJson(stringBuilder.toString(), ApiResponse.class);

      for (VehicleType vehicleType : apiResponse.getVehicleTypeList()) {
        VechileTypejComboBox.addItem(vehicleType.getSubtypeDesc());
        itemList.add(new Item(vehicleType.getSubtypeDesc(), vehicleType.getWeighingRate()));
        itemCodeList.add(new Item(vehicleType.getSubtypeDesc(), vehicleType.getCode()));
        itemCodeNameList.add(new Item(vehicleType.getCode(), vehicleType.getSubtypeDesc()));
      }

      for (Autosuggest autosuggest : apiResponse.getAutosuggestList()) {
        if (autosuggest.getParty() != null) {
          suggestionsListParty.add(autosuggest.getParty());
        }
        if (autosuggest.getProduct() != null) {
          suggestionsListPoduct.add(autosuggest.getProduct());
        }
        if (autosuggest.getRemarks() != null) {
          suggestionsListRemarks.add(autosuggest.getRemarks());
        }
        if (autosuggest.getVehicleNo() != null) {
          suggestionsListVehicleNo.add(autosuggest.getVehicleNo());
        }
        if (autosuggest.getTrollyNo() != null) {
          suggestionsListTrollyNo.add(autosuggest.getTrollyNo());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void oncallApiVehicleSlipNo(String vechileNo, String slipNo) {
    List<VehicleDetails> filteredList = null;
    try {
      URL obj = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleDetails");
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder stringBuilder = new StringBuilder(); // Use StringBuilder for efficient string concatenation
      while ((inputLine = bufferedReader.readLine()) != null) {
        stringBuilder.append(inputLine);
      }
      bufferedReader.close();
      String jsonResponse = stringBuilder.toString();
      Gson gson = new Gson();
      ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);
      List<VehicleDetails> vehicleDetailsList = apiResponse.getVehicleDetailsList();
      int size = vehicleDetailsList.size();
      if (vechileNo != null) {
        for (int i = 0; i < size; i++) {
          filteredList = vehicleDetailsList.stream()
            .filter(vehicle -> vehicle.getVehicleNo().equals(vechileNo))
            .collect(Collectors.toList());
        }
      }

      if (slipNo != null) {
        for (int i = 0; i < size; i++) {
          filteredList = vehicleDetailsList.stream()
            .filter(vehicle -> vehicle.getSlipNo().equals(slipNo))
            .collect(Collectors.toList());
        }
      }

      VechileTypejComboBox.setEnabled(true);
      filteredList.forEach(System.out::println);
      for (VehicleDetails rs : filteredList) {
        System.out.println(rs.getVehicleNo() + " - " + rs.getTokenNo());
        if (!rs.getMachineNo().equalsIgnoreCase("0")) {
          if (rs.getMachineNo().equalsIgnoreCase(TXT_Machine.getText())) {
            TXT_Machine.setText(rs.getMachineNo().toUpperCase());
          } else {
            String message = "Please Enter Valid Vehicle no/Slip No";
            JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
          }
        }
        if (rs.getVehicleNo() != null) {
          TXT_VechileNo.setText(rs.getVehicleNo().toUpperCase());
          if (vechileNo != null) {
            TXT_SlipNo.setEnabled(false);
          }
        }
        if (!rs.getTokenNo().equalsIgnoreCase("0")) {
          TXT_TokenNo.setText(rs.getTokenNo());
        }
        if (rs.getTrolly_req().equalsIgnoreCase("Y")) {
          TXT_TrollyNo.setEnabled(true);
          trollyReq = "Y";
          VechileTypejComboBox.setSelectedItem("TRACTOR");
          ComboBoxChargeApplied.setSelectedIndex(1);
          ComboBoxChargeApplied.setEnabled(false);
          TXT_Charge.setText("0");
          System.out.println("Slip Number is: " + TXT_SlipNo.getText());
          if (TXT_SlipNo.getText().trim() == null || TXT_SlipNo.getText().isEmpty()) {
            if (TXT_TrollyNo.getText().isEmpty() || 
                TXT_TrollyNo.getText().trim() == null ||
                TXT_TrollyNo.getText().trim() == "") {
              String message = "Please enter trolly no.";
              JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
          }
        } else {
          TXT_TrollyNo.setEnabled(false);
        }
        if (slipNo == null) {
          if (!rs.getSlipNo().equalsIgnoreCase("0")) {
            TXT_SlipNo.setText(rs.getSlipNo());
            TXT_VechileNo.setEnabled(false);
          }
        }        
        if (!rs.getParty().equalsIgnoreCase("0")) {
          TXT_Part.setText(rs.getParty().toUpperCase());
        }
        
        if (!rs.getProduct().equalsIgnoreCase("0")) {
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
        
        if (!rs.getFinalEnteredBy().equalsIgnoreCase("0")) {
          TXT_FinealEnterBy.setText(rs.getFinalEnteredBy());
        }
        
        if (!rs.getTrolleyNo().equalsIgnoreCase("0")) {
          TXT_TrollyNo.setText(rs.getTrolleyNo());
        }
        
        if (rs.getCharge().equalsIgnoreCase("0") || rs.getCharge() == "0") {
          TXT_Charge.setText("0");
        } else {
          TXT_Charge.setText(rs.getCharge());
        }
        
        if (!rs.getCharge_applicable().equalsIgnoreCase("0")) {
          if (rs.getCharge_applicable().equalsIgnoreCase("Yes")) {
            ComboBoxChargeApplied.setSelectedIndex(0);
          } else {
            ComboBoxChargeApplied.setSelectedIndex(1);
          }
        }

        if (!rs.getVeh_subtype_desc().equalsIgnoreCase("0")) {
          VechileTypejComboBox.setSelectedItem(rs.getVeh_subtype_desc());
        }

        if (!rs.getRemarks().equalsIgnoreCase("0")) {
          TXT_REMARKS.setText(rs.getRemarks().toUpperCase());
        }

        if (!rs.getGateEntryNumber().equalsIgnoreCase("0")) {
          TXT_GateEntry.setText(rs.getGateEntryNumber());
        }

        if (!rs.getCreatedBy().equalsIgnoreCase("0")) {
          TXT_CreateBy.setText(rs.getCreatedBy());
        }
        
        if (!rs.getCreationDate().equalsIgnoreCase("0")) {
          TXT_CreateDate.setText(rs.getCreationDate());
        }
        
        if (!rs.getCreationTime().equalsIgnoreCase("0")) {
          TXT_CreateTime.setText(rs.getCreationTime());
        }

        if (!rs.getProcessCode().equalsIgnoreCase("0")) {
          TXT_Process.setText(rs.getProcessCode());
        }
        
        if (!rs.getRcNo().equalsIgnoreCase("0")) {
          TXT_RC_NO.setText(rs.getRcNo());
        }
        
        if (rs.getCompVehTypeCode().equalsIgnoreCase("0")) {
          VechileTypejComboBox.setEnabled(true);
          compVechileType = "N";
          ComboBoxChargeApplied.setSelectedIndex(0);
          TXT_Charge.setText("0");
        } else {
          VechileTypejComboBox.setEnabled(false);
          ComboBoxChargeApplied.setEnabled(false);
          ComboBoxChargeApplied.setSelectedIndex(1);
          TXT_Charge.setText("0");
          compVechileType = "Y";
        }

        if (!rs.getFt_tere_weight().equalsIgnoreCase("0")) {
          ftTereWeight = rs.getFt_tere_weight();
        }
      }

      if (TXT_SlipNo.getText() == null || TXT_SlipNo.getText().equalsIgnoreCase("0") ||
          TXT_SlipNo.getText().isEmpty() || TXT_SlipNo.getText() == "" || TXT_SlipNo.getText().equals("")) {
        VechileTypejComboBox.setEnabled(true);
        ComboBoxChargeApplied.setEnabled(true);
      } else {
        VechileTypejComboBox.setEnabled(false);
        ComboBoxChargeApplied.setEnabled(false);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    if (filteredList.size() <= 0) {
      String message = "Please Enter Valid Vehicle no/Slip No";
      JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
      resetValue();
    } else {
      forPrint();
    }
  }

  public void trollyDeatilsApiCall() throws JSONException, IOException {
    if (TXT_TrollyNo.getText().isEmpty() || TXT_VechileNo.getText() == null) {
      JOptionPane.showMessageDialog(null, "Please Enter User Name", "Message", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    try {
      URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/trollyDtl");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setDoOutput(true);
      String jsonInputString = "{\"vechileNo\":\"" + TXT_VechileNo.getText() + "\",\"trolly\":\"" + TXT_TrollyNo.getText() + "\"}";
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
        String jsonResponse = response.toString();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject == null) {
          onLoad();
          onLoadDate();
          resetValue();
          TXT_FinealEnterBy.setText(null);
          TXT_FinealEnterDate.setText(null);
          TXT_FinealEnterTime.setText(null);
          TXT_SlipNo.setText(null);
          TXT_VechileNo.setEnabled(true);
          TXT_SlipNo.setEnabled(true);
          return;
        }

        if (jsonObject.getString("slip_no") != "0") {
          TXT_SlipNo.setText(jsonObject.getString("slip_no"));
        }

        if (jsonObject.getString("machine_no") != "0") {
          TXT_Machine.setText(jsonObject.getString("machine_no"));
        }

        if (jsonObject.getString("trolly_req") != "0") {
          if (jsonObject.getString("trolly_req").equalsIgnoreCase("Y")) {
            trollyReq = "Y";
          }
        }

        if (jsonObject.getString("token_no") != "0") {
          TXT_TokenNo.setText(jsonObject.getString("token_no"));
        }

        if (jsonObject.getString("party") != "0") {
          TXT_Part.setText(jsonObject.getString("party").toUpperCase());
        }

        if (jsonObject.getString("product") != "0") {
          TXT_Product.setText(jsonObject.getString("product").toUpperCase());
        }

        if (jsonObject.getString("gross_weight").equalsIgnoreCase("0")) {
          TXT_GrossWeight.setText(jsonObject.getString("gross_weight"));
        } else {
          Integer grossWeight = Integer.parseInt(jsonObject.getString("gross_weight"));
          if (grossWeight > 0) {
            BtnGross.setEnabled(false);
          }
          TXT_GrossWeight.setText(jsonObject.getString("gross_weight"));
        }

        if (jsonObject.getString("tere_weight").equalsIgnoreCase("0")) {
          TXT_TareWeight.setText(jsonObject.getString("tere_weight"));
        } else {
          Integer tareWeight = Integer.parseInt(jsonObject.getString("tere_weight"));
          if (tareWeight > 0) {
            BtnTare.setEnabled(false);
          }
          TXT_TareWeight.setText(jsonObject.getString("tere_weight"));
        }

        if (jsonObject.getString("net_weight").equalsIgnoreCase("0")) {
          TXT_NetWeight.setText(jsonObject.getString("net_weight"));
        } else {
          Integer tareWeight = Integer.parseInt(jsonObject.getString("net_weight"));
          if (tareWeight > 0) {
            BtnSubmit.setEnabled(false);
            BtnActionClear.setEnabled(false);
          }
          TXT_TareWeight.setText(jsonObject.getString("net_weight"));
        }

        if (jsonObject.getString("final_entered_by") != "0") {
          TXT_FinealEnterBy.setText(jsonObject.getString("final_entered_by"));
        }

        if (jsonObject.getString("trolley_no") != "0") {
          TXT_TrollyNo.setText(jsonObject.getString("trolley_no"));
        }

        if (jsonObject.getString("charge").equalsIgnoreCase("0") || jsonObject.getString("charge") == "0") {
          TXT_Charge.setText("0");
        } else {
          TXT_Charge.setText(jsonObject.getString("charge"));
        }

        if (jsonObject.getString("charge_applicable") != "0") {
          if (jsonObject.getString("charge_applicable").equalsIgnoreCase("Yes")) {
            ComboBoxChargeApplied.setSelectedIndex(0);
          } else {
            ComboBoxChargeApplied.setSelectedIndex(1);
          }
        }

        if (jsonObject.getString("veh_subtype_desc").equalsIgnoreCase("0")) {
          VechileTypejComboBox.setSelectedItem(jsonObject.getString("veh_subtype_desc"));
        }

        if (jsonObject.getString("remarks") != "0") {
          TXT_REMARKS.setText(jsonObject.getString("remarks").toUpperCase());
        }

        if (jsonObject.getString("gate_entry_number") != "0") {
          TXT_GateEntry.setText(jsonObject.getString("gate_entry_number"));
        }

        if (!jsonObject.getString("created_by").equalsIgnoreCase("0")) {
          TXT_CreateBy.setText(jsonObject.getString("created_by"));
        }

        if (!jsonObject.getString("creation_date").equalsIgnoreCase("0")) {
          TXT_CreateDate.setText(jsonObject.getString("creation_date"));
        }

        if (!jsonObject.getString("creation_time").equalsIgnoreCase("0")) {
          TXT_CreateTime.setText(jsonObject.getString("creation_time"));
        }

        if (!jsonObject.getString("process_code").equalsIgnoreCase("0")) {
          TXT_Process.setText(jsonObject.getString("process_code"));
        }

        if (!jsonObject.getString("rc_no").equalsIgnoreCase("0")) {
          TXT_RC_NO.setText(jsonObject.getString("rc_no"));
        }
        if (jsonObject.getString("comp_veh_type_code").equalsIgnoreCase("0")) {
          VechileTypejComboBox.setEnabled(true);
          compVechileType = "N";
          ComboBoxChargeApplied.setSelectedIndex(0);
          TXT_Charge.setText("0");
        } else {
          VechileTypejComboBox.setEnabled(false);
          ComboBoxChargeApplied.setEnabled(false);
          ComboBoxChargeApplied.setSelectedIndex(1);
          TXT_Charge.setText("0");
          compVechileType = "Y";
        }

        if (TXT_SlipNo.getText() == null || TXT_SlipNo.getText().equalsIgnoreCase("0") ||
            TXT_SlipNo.getText().isEmpty() || TXT_SlipNo.getText() == "" || TXT_SlipNo.getText().equals("")) {
          VechileTypejComboBox.setEnabled(true);
          ComboBoxChargeApplied.setEnabled(true);
        } else {
          VechileTypejComboBox.setEnabled(false);
          ComboBoxChargeApplied.setEnabled(false);
        }
      }
    } catch (MalformedURLException e) {
    }
  }

  public static String getDate(LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public static String getTime(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }
}
