package weightmachinedesktopapplication;

import com.fazecast.jSerialComm.SerialPort;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.print.PrinterJob;

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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author lenovo
 */
public class WeightMechineJFrame extends javax.swing.JFrame {
    final String jasperPath = "C:/jasperfile/Bridge_Entry.jasper";
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

    String slipNo = null, tokenNo = null, gateNo = null, grossWeight = null, tareWeight = null, netWeight =
        null, party = null, vechileNo = null, vechileType = null, create = null, finaldate = null, charge =
        null, product = null, remarks = null, comport_no = null, machinecode = null, ftTereWeight = "0", bypass_flag =
        null;

    private JPopupMenu suggestionMenuParty;
    private JPopupMenu suggestionMenuProduct;
    // private JPopupMenu suggestionMenuRemarks;
    private JPopupMenu suggestionMenuVehicleNo;
    private JPopupMenu suggestionMenuTrollyNo;

    private List<String> suggestionsListParty;
    private List<String> suggestionsListProduct;
    // private List<String> suggestionsListRemarks;
    private List<String> suggestionsListVehicleNo;
    private List<String> suggestionsListTrollyNo;

    List<JMenuItem> partyMenuItems = new ArrayList<>();
    List<JMenuItem> productMenuItems = new ArrayList<>();
    // List<JMenuItem> remarksMenuItems = new ArrayList<>();
    List<JMenuItem> vehicleNoMenuItems = new ArrayList<>();
    List<JMenuItem> trollyNoMenuItems = new ArrayList<>();

    int[] partySelectedIndex = { -1 };
    int[] productSelectedIndex = { -1 };
    // int[] remarksSelectedIndex = { -1 };
    int[] vehicleNoSelectedIndex = { -1 };
    int[] trollyNoSelectedIndex = { -1 };

    /** Creates new form WeightMechineJFrame */
    public WeightMechineJFrame(String userName, String unitCd, String machine_code, String comPort, String bypassflag) {
        if (userName == null || userName.isEmpty() || userName == "") {
            userName = "E-001";
        }
        userNamevalue = userName;
        unitCode = unitCd;
        comport_no = comPort;
        machinecode = machine_code;
        bypass_flag = bypassflag;
        initComponents();
        onLoad();
        onLoadApiVehicleTypeAutoSugest();
        onLoadDate();
        TXT_Machine.setText(machine_code);
        autoSuggestParty();
        autoSuggestProduct();
        // autoSuggestRemarks();
        autoSuggestVehicleNo();
        autoSuggestTrollyNo();
    }

    public void autoSuggestParty() {
        suggestionMenuParty = new JPopupMenu();
        TXT_Part.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePartySuggestions(TXT_Part.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePartySuggestions(TXT_Part.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePartySuggestions(TXT_Part.getText().trim());
            }

            private void updatePartySuggestions(String input) {
                suggestionMenuParty.setVisible(false);
                suggestionMenuParty.removeAll();
                partyMenuItems.clear();
                partySelectedIndex[0] = -1;

                if (input.length() >= 4) {
                    boolean found = false;
                    int counter = 0; // Counter to track the number of suggestions added
                    for (String item : suggestionsListParty) {
                        if (item.toLowerCase().startsWith(input.toLowerCase())) {
                            if (counter >= 10) {
                                break; // Stop once 10 suggestions are added
                            }
                            JMenuItem menuItem = new JMenuItem(item);
                            menuItem.addActionListener(e -> {
                                    TXT_Part.setText(item);
                                    suggestionMenuParty.setVisible(false);
                                });
                            suggestionMenuParty.add(menuItem);
                            partyMenuItems.add(menuItem);
                            found = true;
                            counter++;
                        }
                    }
                    if (found) {
                        suggestionMenuParty.setLightWeightPopupEnabled(false);
                        suggestionMenuParty.show(TXT_Part, 0, TXT_Part.getHeight());
                        SwingUtilities.invokeLater(TXT_Part::requestFocusInWindow);
                    } else {
                        suggestionMenuParty.setVisible(false);
                    }
                }
            }
        });

        TXT_Part.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionMenuParty == null || partyMenuItems == null || partyMenuItems.isEmpty()) {
                    return;
                }

                if (suggestionMenuParty.isVisible()) {
                    int keyCode = e.getKeyCode();
                    int lastIndex = partyMenuItems.size() - 1;

                    switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        if (partySelectedIndex[0] < lastIndex) {
                            partySelectedIndex[0]++;
                        } else {
                            partySelectedIndex[0] = 0;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_UP:
                        if (partySelectedIndex[0] > 0) {
                            partySelectedIndex[0]--;
                        } else {
                            partySelectedIndex[0] = lastIndex;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (partySelectedIndex[0] >= 0 && partySelectedIndex[0] <= lastIndex) {
                            partyMenuItems.get(partySelectedIndex[0]).doClick();
                        }
                        break;

                    case KeyEvent.VK_ESCAPE:
                        suggestionMenuParty.setVisible(false);
                        break;

                    default:
                        break;
                    }
                }
            }

            private void updateSelection() {
                for (JMenuItem item : partyMenuItems) {
                    item.setArmed(false);
                }

                if (partySelectedIndex[0] >= 0 && partySelectedIndex[0] < partyMenuItems.size()) {
                    partyMenuItems.get(partySelectedIndex[0]).setArmed(true);
                }
            }
        });
    }

    public void autoSuggestProduct() {
        suggestionMenuProduct = new JPopupMenu();
        TXT_Product.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateProductSuggestions(TXT_Product.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateProductSuggestions(TXT_Product.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateProductSuggestions(TXT_Product.getText().trim());
            }

            private void updateProductSuggestions(String input) {
                suggestionMenuProduct.setVisible(false);
                suggestionMenuProduct.removeAll();
                productMenuItems.clear();
                productSelectedIndex[0] = -1;
                if (input.length() > 3) {
                    boolean found = false;
                    int counter = 0; // Counter to track the number of suggestions added
                    for (String item : suggestionsListProduct) {
                        if (item.toLowerCase().contains(input.toLowerCase())) {
                            if (counter >= 10) {
                                break; // Stop once 10 suggestions are added
                            }
                            JMenuItem menuItem = new JMenuItem(item);
                            menuItem.addActionListener(e -> {
                                    TXT_Product.setText(item);
                                    suggestionMenuProduct.setVisible(false);
                                });
                            suggestionMenuProduct.add(menuItem);
                            productMenuItems.add(menuItem);
                            found = true;
                            counter++;
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
        });

        TXT_Product.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionMenuProduct == null || productMenuItems == null || productMenuItems.isEmpty()) {
                    return;
                }

                if (suggestionMenuProduct.isVisible()) {
                    int keyCode = e.getKeyCode();
                    int lastIndex = productMenuItems.size() - 1;

                    switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        if (productSelectedIndex[0] < lastIndex) {
                            productSelectedIndex[0]++;
                        } else {
                            productSelectedIndex[0] = 0;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_UP:
                        if (productSelectedIndex[0] > 0) {
                            productSelectedIndex[0]--;
                        } else {
                            productSelectedIndex[0] = lastIndex;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (productSelectedIndex[0] >= 0 && productSelectedIndex[0] <= lastIndex) {
                            productMenuItems.get(productSelectedIndex[0]).doClick();
                        }
                        break;

                    case KeyEvent.VK_ESCAPE:
                        suggestionMenuProduct.setVisible(false);
                        break;

                    default:
                        break;
                    }
                }
            }

            private void updateSelection() {
                for (JMenuItem item : productMenuItems) {
                    item.setArmed(false);
                }

                if (productSelectedIndex[0] >= 0 && productSelectedIndex[0] < productMenuItems.size()) {
                    productMenuItems.get(productSelectedIndex[0]).setArmed(true);
                }
            }
        });

    }

    //    public void autoSuggestRemarks() {
    //        suggestionMenuRemarks = new JPopupMenu();
    //        TXT_REMARKS.getDocument().addDocumentListener(new DocumentListener() {
    //            @Override
    //            public void insertUpdate(DocumentEvent e) {
    //                updateRemarksSuggestions(TXT_REMARKS.getText().trim());
    //            }
    //
    //            @Override
    //            public void removeUpdate(DocumentEvent e) {
    //                updateRemarksSuggestions(TXT_REMARKS.getText().trim());
    //            }
    //
    //            @Override
    //            public void changedUpdate(DocumentEvent e) {
    //                updateRemarksSuggestions(TXT_REMARKS.getText().trim());
    //            }
    //
    //            private void updateRemarksSuggestions(String input) {
    //                suggestionMenuRemarks.setVisible(false);
    //                suggestionMenuRemarks.removeAll();
    //                remarksMenuItems.clear();
    //                remarksSelectedIndex[0] = -1;
    //
    //                if (input.length() > 3) {
    //                    boolean found = false;
    //                    for (String item : suggestionsListRemarks) {
    //                        if (item.toLowerCase().contains(input.toLowerCase())) {
    //                            JMenuItem menuItem = new JMenuItem(item);
    //                            menuItem.addActionListener(e -> {
    //                                    TXT_REMARKS.setText(item);
    //                                    suggestionMenuRemarks.setVisible(false);
    //                                });
    //                            suggestionMenuRemarks.add(menuItem);
    //                            remarksMenuItems.add(menuItem);
    //                            found = true;
    //                        }
    //                    }
    //                    if (found) {
    //                        suggestionMenuRemarks.setLightWeightPopupEnabled(false);
    //                        suggestionMenuRemarks.show(TXT_REMARKS, 0, TXT_REMARKS.getHeight());
    //                        SwingUtilities.invokeLater(TXT_REMARKS::requestFocusInWindow);
    //                    } else {
    //                        suggestionMenuRemarks.setVisible(false);
    //                    }
    //                }
    //            }
    //        });
    //
    //        TXT_REMARKS.addKeyListener(new KeyAdapter() {
    //            @Override
    //            public void keyPressed(KeyEvent e) {
    //                if (suggestionMenuRemarks == null || remarksMenuItems == null || remarksMenuItems.isEmpty()) {
    //                    return;
    //                }
    //
    //                if (suggestionMenuRemarks.isVisible()) {
    //                    int keyCode = e.getKeyCode();
    //                    int lastIndex = remarksMenuItems.size() - 1;
    //
    //                    switch (keyCode) {
    //                    case KeyEvent.VK_DOWN:
    //                        if (remarksSelectedIndex[0] < lastIndex) {
    //                            remarksSelectedIndex[0]++;
    //                        } else {
    //                            remarksSelectedIndex[0] = 0;
    //                        }
    //                        updateSelection();
    //                        break;
    //
    //                    case KeyEvent.VK_UP:
    //                        if (remarksSelectedIndex[0] > 0) {
    //                            remarksSelectedIndex[0]--;
    //                        } else {
    //                            remarksSelectedIndex[0] = lastIndex;
    //                        }
    //                        updateSelection();
    //                        break;
    //
    //                    case KeyEvent.VK_RIGHT:
    //                        if (remarksSelectedIndex[0] >= 0 && remarksSelectedIndex[0] <= lastIndex) {
    //                            remarksMenuItems.get(remarksSelectedIndex[0]).doClick();
    //                        }
    //                        break;
    //
    //                    case KeyEvent.VK_ESCAPE:
    //                        suggestionMenuRemarks.setVisible(false);
    //                        break;
    //
    //                    default:
    //                        break;
    //                    }
    //                }
    //            }
    //
    //            private void updateSelection() {
    //                for (JMenuItem item : remarksMenuItems) {
    //                    item.setArmed(false);
    //                }
    //
    //                if (remarksSelectedIndex[0] >= 0 && remarksSelectedIndex[0] < remarksMenuItems.size()) {
    //                    remarksMenuItems.get(remarksSelectedIndex[0]).setArmed(true);
    //                }
    //            }
    //        });
    //    }

    public void autoSuggestVehicleNo() {
        suggestionMenuVehicleNo = new JPopupMenu();
        TXT_VechileNo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateVehicleNoSuggestions(TXT_VechileNo.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateVehicleNoSuggestions(TXT_VechileNo.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateVehicleNoSuggestions(TXT_VechileNo.getText().trim());
            }

            //            private void updateVehicleNoSuggestions(String input) {
            //                suggestionMenuVehicleNo.setVisible(false);
            //                suggestionMenuVehicleNo.removeAll();
            //                vehicleNoMenuItems.clear();
            //                vehicleNoSelectedIndex[0] = -1;
            //
            //                if (input.length() > 3) {
            //                    boolean found = false;
            //                    for (String item : suggestionsListVehicleNo) {
            //                        if (item.toLowerCase().contains(input.toLowerCase())) {
            //                            JMenuItem menuItem = new JMenuItem(item);
            //                            menuItem.addActionListener(e -> {
            //                                    TXT_VechileNo.setText(item);
            //                                    suggestionMenuVehicleNo.setVisible(false);
            //                                    // textField.requestFocusInWindow();
            //                                });
            //                            suggestionMenuVehicleNo.add(menuItem);
            //                            vehicleNoMenuItems.add(menuItem);
            //                            found = true;
            //                        }
            //                    }
            //                    if (found) {
            //                        suggestionMenuVehicleNo.setLightWeightPopupEnabled(false);
            //                        suggestionMenuVehicleNo.show(TXT_VechileNo, 0, TXT_VechileNo.getHeight());
            //                        SwingUtilities.invokeLater(TXT_VechileNo::requestFocusInWindow);
            //                    } else {
            //                        suggestionMenuVehicleNo.setVisible(false);
            //                    }
            //                }
            //            }


            private void updateVehicleNoSuggestions(String input) {
                suggestionMenuVehicleNo.setVisible(false);
                suggestionMenuVehicleNo.removeAll();
                vehicleNoMenuItems.clear();
                vehicleNoSelectedIndex[0] = -1;

                if (input.length() > 3) {
                    boolean found = false;
                    int counter = 0; // Counter to track the number of suggestions added

                    for (String item : suggestionsListVehicleNo) {
                        if (item.toLowerCase().contains(input.toLowerCase())) {
                            if (counter >= 10) {
                                break; // Stop once 10 suggestions are added
                            }

                            JMenuItem menuItem = new JMenuItem(item);
                            menuItem.addActionListener(e -> {
                                    TXT_VechileNo.setText(item);
                                    suggestionMenuVehicleNo.setVisible(false);
                                    // textField.requestFocusInWindow();
                                });

                            suggestionMenuVehicleNo.add(menuItem);
                            vehicleNoMenuItems.add(menuItem);
                            found = true;
                            counter++;
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

            //----

        });

        TXT_VechileNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionMenuVehicleNo == null || vehicleNoMenuItems == null || vehicleNoMenuItems.isEmpty()) {
                    return;
                }

                if (suggestionMenuVehicleNo.isVisible()) {
                    int keyCode = e.getKeyCode();
                    int lastIndex = vehicleNoMenuItems.size() - 1;

                    switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        if (vehicleNoSelectedIndex[0] < lastIndex) {
                            vehicleNoSelectedIndex[0]++;
                        } else {
                            vehicleNoSelectedIndex[0] = 0;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_UP:
                        if (vehicleNoSelectedIndex[0] > 0) {
                            vehicleNoSelectedIndex[0]--;
                        } else {
                            vehicleNoSelectedIndex[0] = lastIndex;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (vehicleNoSelectedIndex[0] >= 0 && vehicleNoSelectedIndex[0] <= lastIndex) {
                            vehicleNoMenuItems.get(vehicleNoSelectedIndex[0]).doClick();
                        }
                        break;

                    case KeyEvent.VK_ESCAPE:
                        suggestionMenuVehicleNo.setVisible(false);
                        break;

                    default:
                        break;
                    }
                }
            }

            private void updateSelection() {
                for (JMenuItem item : vehicleNoMenuItems) {
                    item.setArmed(false);
                }

                if (vehicleNoSelectedIndex[0] >= 0 && vehicleNoSelectedIndex[0] < vehicleNoMenuItems.size()) {
                    vehicleNoMenuItems.get(vehicleNoSelectedIndex[0]).setArmed(true);
                }
            }
        });
    }

    public void autoSuggestTrollyNo() {
        suggestionMenuTrollyNo = new JPopupMenu();
        TXT_TrollyNo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTrollyNoSuggestions(TXT_TrollyNo.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTrollyNoSuggestions(TXT_TrollyNo.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTrollyNoSuggestions(TXT_TrollyNo.getText().trim());
            }

            public void updateTrollyNoSuggestions(String input) {
                suggestionMenuTrollyNo.setVisible(false);
                suggestionMenuTrollyNo.removeAll();
                trollyNoMenuItems.clear();
                trollyNoSelectedIndex[0] = -1;
                if (input.length() > 3) {
                    boolean found = false;
                    int counter = 0; // Counter to track the number of suggestions added
                    for (String item : suggestionsListTrollyNo) {
                        if (item.toLowerCase().contains(input.toLowerCase())) {
                            if (counter >= 10) {
                                break; // Stop once 10 suggestions are added
                            }
                            JMenuItem menuItem = new JMenuItem(item);
                            menuItem.addActionListener(e -> {
                                    TXT_TrollyNo.setText(item);
                                    suggestionMenuTrollyNo.setVisible(false);
                                    // textField.requestFocusInWindow();
                                });
                            suggestionMenuTrollyNo.add(menuItem);
                            trollyNoMenuItems.add(menuItem);
                            found = true;
                            counter++;
                        }
                    }
                    if (found) {
                        suggestionMenuTrollyNo.setLightWeightPopupEnabled(false);
                        suggestionMenuTrollyNo.show(TXT_TrollyNo, 0, TXT_TrollyNo.getHeight());
                        SwingUtilities.invokeLater(TXT_TrollyNo::requestFocusInWindow);
                    } else {
                        suggestionMenuTrollyNo.setVisible(false);
                    }
                }
            }
        });

        TXT_TrollyNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionMenuTrollyNo == null || trollyNoMenuItems == null || trollyNoMenuItems.isEmpty()) {
                    return;
                }

                if (suggestionMenuTrollyNo.isVisible()) {
                    int keyCode = e.getKeyCode();
                    int lastIndex = trollyNoMenuItems.size() - 1;

                    switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        if (trollyNoSelectedIndex[0] < lastIndex) {
                            trollyNoSelectedIndex[0]++;
                        } else {
                            trollyNoSelectedIndex[0] = 0;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_UP:
                        if (trollyNoSelectedIndex[0] > 0) {
                            trollyNoSelectedIndex[0]--;
                        } else {
                            trollyNoSelectedIndex[0] = lastIndex;
                        }
                        updateSelection();
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (trollyNoSelectedIndex[0] >= 0 && trollyNoSelectedIndex[0] <= lastIndex) {
                            trollyNoMenuItems.get(trollyNoSelectedIndex[0]).doClick();
                        }
                        break;

                    case KeyEvent.VK_ESCAPE:
                        suggestionMenuTrollyNo.setVisible(false);
                        break;

                    default:
                        break;
                    }
                }
            }

            private void updateSelection() {
                for (JMenuItem item : trollyNoMenuItems) {
                    item.setArmed(false);
                }

                if (trollyNoSelectedIndex[0] >= 0 && trollyNoSelectedIndex[0] < trollyNoMenuItems.size()) {
                    trollyNoMenuItems.get(trollyNoSelectedIndex[0]).setArmed(true);
                }
            }
        });
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
        todayDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy h:mm:ss.SSSSSSSSS a"));
        TXT_CreateDate.setText(getCurrentDate());
        TXT_CreateTime.setText(getCurrentTime());
    }

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

        TXT_AutoWeight.setEditable(false);
        TXT_AutoWeight.setBackground(new java.awt.Color(248, 245, 245));

        BtnTare.setBackground(new java.awt.Color(102, 204, 255));
        BtnTare.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnTare.setLabel("Tare");
        BtnTare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTareActionPerformed(evt);
            }
        });

        BtnGross.setBackground(new java.awt.Color(102, 204, 255));
        BtnGross.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnGross.setLabel("Gross");
        BtnGross.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrossActionPerformed(evt);
            }
        });

        BtnPrint.setBackground(new java.awt.Color(102, 204, 255));
        BtnPrint.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BtnPrint.setText("Print");
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });

        BtnSubmit.setBackground(new java.awt.Color(102, 204, 255));
        BtnSubmit.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
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

        VechileTypejComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        VechileTypejComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Please Select" }));
        VechileTypejComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VechileTypejComboBoxActionPerformed(evt);
            }
        });

        TXT_SlipNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
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

        TXT_VechileNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TXT_VechileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TXT_VechileNoKeyPressed(evt);
            }
        });

        TXT_TrollyNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
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
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19)
                    .addComponent(jLabel2)
                    .addComponent(jLabel22))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXT_VechileNo)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 88, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TXT_TrollyNo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)))
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
        jLabel22.getAccessibleContext().setAccessibleName("Trolley Number");

        jLabel14.setText("Remark");

        BtnActionClear.setBackground(new java.awt.Color(102, 204, 255));
        BtnActionClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
            JOptionPane.showMessageDialog(null, "Gross Weight/Tare Weight 0", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
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

        // String value = comPoartMechineConnection("SaveBtn");
        if (comPoartMechineConnection("SaveBtn").equalsIgnoreCase("N")) {
            String message = "Weight-bridge weight not match.";
            JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            if (TXT_GrossWeight.getText() == "0") {
                if (!ftTereWeight.equalsIgnoreCase("0")) {
                    if (TXT_TareWeight.getText() != "0") {
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (compVechileType.equalsIgnoreCase("N")) {
            if (TXT_Charge.getText() == "0" || TXT_Charge.getText().equalsIgnoreCase("0")) {
                String message = "This vehicle outside charge is applied, please select vehicle type.";
                JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }


        // JOptionPane.showMessageDialog(null, "valuueueueu----3", "Message", JOptionPane.INFORMATION_MESSAGE);
        String selectedItem = (String) VechileTypejComboBox.getSelectedItem();
        if (selectedItem.equalsIgnoreCase("Please Select")) {
            JOptionPane.showMessageDialog(null, "please select vehicle type", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // JOptionPane.showMessageDialog(null, "valuueueueu----4", "Message", JOptionPane.INFORMATION_MESSAGE);
        if (TXT_Part.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Party Details", "Message",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //  JOptionPane.showMessageDialog(null, "valuueueueu----5", "Message", JOptionPane.INFORMATION_MESSAGE);
        //    if (TXT_Product.getText().trim().isEmpty()) {
        //      JOptionPane.showMessageDialog(null, "Please Enter Product Details", "Message", JOptionPane.INFORMATION_MESSAGE);
        //      return;
        //    }
        // JOptionPane.showMessageDialog(null, "valuueueueu----6", "Message", JOptionPane.INFORMATION_MESSAGE);
        if (TXT_SlipNo.getText().trim().isEmpty() || TXT_SlipNo.getText().trim() == null ||
            TXT_SlipNo.getText().trim() == "" || TXT_SlipNo.getText().equalsIgnoreCase("0")) {
            insertdateCallApi();
        } else {
            //      if (TXT_REMARKS.getText().trim().isEmpty()) {
            //        JOptionPane.showMessageDialog(null, "Please Enter Remark", "Message", JOptionPane.INFORMATION_MESSAGE);
            //        return;
            //      }
            // tare and gross value is equal than bypass it
            // if (Integer.parseInt(TXT_TareWeight.getText()) == Integer.parseInt(TXT_GrossWeight.getText()))

            int grossWeight = Integer.valueOf(TXT_GrossWeight.getText().toString());
            Integer tareWeight = Integer.valueOf(TXT_TareWeight.getText().toString());
            if (tareWeight == grossWeight) {

            } else {
                if (Integer.parseInt(TXT_NetWeight.getText()) <= 0) {
                    String message = "Net weight is should be greater than 0";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    if (TXT_Product.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please Enter Product Details", "Message",
                                                      JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
            updatedateCallApi();
        }
    }//GEN-LAST:event_BtnSubmitActionPerformed

    public void insertdateCallApi() {
        try {
            //quality
            // URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/insert");
            //Production
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
            jsonObject.addProperty("entered_by", userNamevalue.toUpperCase());
            jsonObject.addProperty("final_entered_by", TXT_FinealEnterBy.getText().toUpperCase());
            jsonObject.addProperty("trolley_no", TXT_TrollyNo.getText());
            jsonObject.addProperty("charge", TXT_Charge.getText());
            jsonObject.addProperty("charge_applicable", ComboBoxChargeApplied.getSelectedItem().toString());
            jsonObject.addProperty("party", TXT_Part.getText().toUpperCase());
            jsonObject.addProperty("product", TXT_Product.getText().toUpperCase());
            jsonObject.addProperty("gate_entry_number", TXT_GateEntry.getText().toUpperCase());
            jsonObject.addProperty("remarks", TXT_REMARKS.getText().toUpperCase());
            if (TXT_CreateBy.getText() == null || TXT_CreateBy.getText().isEmpty()) {
                TXT_CreateBy.setText(userNamevalue);
            }
            jsonObject.addProperty("created_by", userNamevalue.toUpperCase());
            jsonObject.addProperty("unit_cd", unitCode);
            jsonObject.addProperty("wt_type", wt_type);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader bufferedReader =
                 new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
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
            //quality
            // URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/update");
            //prd
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
            try (BufferedReader bufferedReader =
                 new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
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
                    TXT_CreateDate.setText(getCurrentDate());
                    int grossWeight = Integer.valueOf(TXT_GrossWeight.getText().toString());
                    Integer tareWeight = Integer.valueOf(TXT_TareWeight.getText().toString());
                    if (tareWeight == grossWeight) {
                        TXT_FinealEnterBy.setText(userNamevalue);
                        TXT_FinealEnterDate.setText(getCurrentDate());
                        TXT_FinealEnterTime.setText(getCurrentTime());
                    }
                    if (Integer.parseInt(TXT_NetWeight.getText()) > 0) {
                        TXT_FinealEnterBy.setText(userNamevalue);
                        TXT_FinealEnterDate.setText(getCurrentDate());
                        TXT_FinealEnterTime.setText(getCurrentTime());
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
            TXT_FinealEnterDate.setText(getCurrentDate());
            TXT_FinealEnterTime.setText(getCurrentTime());
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
            oncallApiVehicleSlipNo(TXT_SlipNo.getText().trim());
        } catch (JSONException e) {
            e.printStackTrace();
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
                .map(Item::getValue).findFirst().orElse(null);
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
            String date = getCurrentDate();
            TXT_CreateDate.setText(date);
            if (netWeightCalculate() > 0) {
                TXT_FinealEnterBy.setText(userNamevalue);
                TXT_FinealEnterDate.setText(date);
                TXT_FinealEnterTime.setText(getCurrentTime());
            }
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
                .map(Item::getValue).findFirst().orElse(null);
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
        // ReportsJFrame weightFrame = new ReportsJFrame(userNamevalue, unitCode, machinecode, comport_no, bypass_flag);
        // weightFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // weightFrame.setSize(1150, 700);
        // weightFrame.setVisible(true);
        //  this.dispose();
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WeightMechineJFrame("", "", "", "", "").setVisible(true);
            }
        });
    }

    //    public void vehicleDetailsWithAutoSugest() {
    //        WeightBridgeDao obj = new WeightBridgeDao();
    //        suggestionsListParty = new ArrayList<>();
    //        suggestionsListProduct = new ArrayList<>();
    //        //suggestionsListRemarks = new ArrayList<>();
    //        try (Connection connection = obj.getStartConnection();
    //             Statement statement = connection.createStatement()) {
    //            String query =
    //                "SELECT V.CODE, V.TYPE_CODE, V.SUBTYPE_DESC, M.WEIGHING_RATE\n" + "FROM VEHICLE_SUBTYPE_MASTER V\n" +
    //                "FROM VEHICLE_SUBTYPE_MASTER V\n" + "FROM VEHICLE_SUBTYPE_MASTER V\n" +
    //                "FROM VEHICLE_SUBTYPE_MASTER V\n" + "FROM VEHICLE_SUBTYPE_MASTER V\n" +
    //                "INNER JOIN WEIGHING_RATE_MASTER M ON V.CODE=M.VEHICLE_SUB_TYPE_CODE\n" +
    //                "WHERE V.STATUS='Y' AND M.STATUS='Y';";
    //            try (ResultSet resultSet = statement.executeQuery(query)) {
    //                while (resultSet.next()) {
    //                    VechileTypejComboBox.addItem(resultSet.getString("SUBTYPE_DESC"));
    //                    //String value = rs.getString("WEIGHING_RATE") + "-" + rs.getString("CODE");
    //                    itemList.add(new Item(resultSet.getString("SUBTYPE_DESC"), resultSet.getString("WEIGHING_RATE")));
    //                    itemCodeList.add(new Item(resultSet.getString("SUBTYPE_DESC"), resultSet.getString("CODE")));
    //                    itemCodeNameList.add(new Item(resultSet.getString("CODE"), resultSet.getString("SUBTYPE_DESC")));
    //                }
    //            }
    //            query = "SELECT DISTINCT PARTY, PRODUCT, REMARKS FROM WEIGHING_BRIDGE;";
    //            try (ResultSet resultSet = statement.executeQuery(query)) {
    //                while (resultSet.next()) {
    //                    suggestionsListParty.add(resultSet.getString("PARTY"));
    //                    suggestionsListProduct.add(resultSet.getString("PRODUCT"));
    //                    suggestionsListRemarks.add(resultSet.getString("REMARKS"));
    //                }
    //            }
    //        } catch (Exception ex) {
    //            JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
    //        }
    //    }

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

    //  public String comPoartMechineConnection(String callBy) {
    //    SerialPort port = SerialPort.getCommPort(comport_no);
    //    try {
    //      if (!port.openPort()) {
    //        JOptionPane.showMessageDialog(null, "Failed to open port 123", "Message", JOptionPane.INFORMATION_MESSAGE);
    //        return "N";
    //      }
    //      port.setComPortParameters(9600, 8, 1, 0); // Adjust these parameters as necessary
    //      port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
    //      if (port.openPort()) {
    //        byte[] buffer = new byte[1024];
    //        while (true) {
    //          int bytesRead = port.readBytes(buffer, buffer.length);
    //          if (bytesRead > 0) {
    //            String receivedData = new String(buffer, 0, bytesRead);
    //            String processedData = receivedData.replace("k", "");
    //            processedData = processedData.trim();
    //            String firstValue = processedData.split("\\s+")[0];
    //            int value = Integer.parseInt(firstValue);
    //
    //            if (value > 0) {
    //                JOptionPane.showMessageDialog(null, "Weight match: " + value, "Message", JOptionPane.INFORMATION_MESSAGE);
    //              if (callBy.equalsIgnoreCase("SaveBtn")) {
    //                if (btnEventName.equalsIgnoreCase("grossBtnCall")) {
    //                  int grossWeight = Integer.valueOf(TXT_GrossWeight.getText());
    //                    JOptionPane.showMessageDialog(null, "gross Weight  match: "+grossWeight+"-" + value, "Message", JOptionPane.INFORMATION_MESSAGE);
    //                  if (grossWeight != value) {
    //                    TXT_GrossWeight.setText("0");
    //                    String message = "Weight bridge weight not match.";
    //                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    //                    return "N";
    //                  }
    //                }
    //                if (btnEventName.equalsIgnoreCase("tareBtnCall")) {
    //                  int tareWeight = Integer.valueOf(TXT_TareWeight.getText());
    //                    JOptionPane.showMessageDialog(null, "tareWeight Weight  match: "+tareWeight+"-" + value, "Message", JOptionPane.INFORMATION_MESSAGE);
    //                  if (tareWeight != value) {
    //                    TXT_TareWeight.setText("0");
    //                    String message = "Weight bridge weight not match.";
    //                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    //                    return "N";
    //                  }
    //                }
    //              } else {
    //                TXT_AutoWeight.setText(firstValue);
    //              }
    //            } else {
    //              TXT_AutoWeight.setText("0");
    //            }
    //          }
    //          break;
    //        }
    //      } else {
    //        JOptionPane.showMessageDialog(null, "Failed to open port: ", "Message", JOptionPane.INFORMATION_MESSAGE);
    //      }
    //    } catch (Exception ex) {
    //      ex.printStackTrace();
    //      String message = "Failed to open port: " + ex.toString();
    //      JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    //      TXT_AutoWeight.setText("0");
    //      port.closePort();
    //      return "N";
    //    } finally {
    //      port.closePort();
    //    }
    //    return "Y";
    //  }

    public String comPoartMechineConnection(String callBy) {
        SerialPort port = SerialPort.getCommPort(comport_no);
        try {
            if (!port.openPort()) {
                JOptionPane.showMessageDialog(null, "Failed to open port 123", "Message",
                                              JOptionPane.INFORMATION_MESSAGE);
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

                        if (value > 0) {
                            // JOptionPane.showMessageDialog(null, "Weight match: " + value, "Message", JOptionPane.INFORMATION_MESSAGE);
                            if (callBy.equalsIgnoreCase("SaveBtn")) {
                                if (btnEventName.equalsIgnoreCase("grossBtnCall")) {
                                    int grossWeight = Integer.valueOf(TXT_GrossWeight.getText());
                                    //  JOptionPane.showMessageDialog(null, "gross Weight  match: "+grossWeight+"-" + value, "Message", JOptionPane.INFORMATION_MESSAGE);
                                    if (grossWeight != value) {
                                        TXT_GrossWeight.setText("0");
                                        String message = "Weight bridge weight not match.";
                                        JOptionPane.showMessageDialog(null, message, "Message",
                                                                      JOptionPane.INFORMATION_MESSAGE);
                                        return "N";
                                    }
                                }
                                if (btnEventName.equalsIgnoreCase("tareBtnCall")) {
                                    int tareWeight = Integer.valueOf(TXT_TareWeight.getText());
                                    //   JOptionPane.showMessageDialog(null, "tareWeight Weight  match: "+tareWeight+"-" + value, "Message", JOptionPane.INFORMATION_MESSAGE);
                                    if (tareWeight != value) {
                                        TXT_TareWeight.setText("0");
                                        String message = "Weight bridge weight not match.";
                                        JOptionPane.showMessageDialog(null, message, "Message",
                                                                      JOptionPane.INFORMATION_MESSAGE);
                                        return "N";
                                    }
                                }
                            } else {
                                TXT_AutoWeight.setText(firstValue);
                            }
                        } else {
                            TXT_AutoWeight.setText("0");
                            return "N";
                        }
                    }
                    break;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to open port: ", "Message",
                                              JOptionPane.INFORMATION_MESSAGE);
                return "N";
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
        suggestionsListProduct = new ArrayList<>();
        // suggestionsListRemarks = new ArrayList<>();
        suggestionsListVehicleNo = new ArrayList<>();
        suggestionsListTrollyNo = new ArrayList<>();
        try {
            //quality
            // URL obj = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleType?machineCode=" + machinecode);
            //prd
            URL obj =
                new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleType?machineCode=" + machinecode);
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
                    suggestionsListProduct.add(autosuggest.getProduct());
                }
                if (autosuggest.getRemarks() != null) {
                    // suggestionsListRemarks.add(autosuggest.getRemarks());




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
        List<VehicleDetails> filteredListMachineCode = null;
        try {
            //  URL obj = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleDetails");
            URL obj = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/vehicleDetails");
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            bufferedReader.close();
            Gson gson = new Gson();
            //            ApiResponse apiResponse = gson.fromJson(stringBuilder.toString(), ApiResponse.class);
            //            List<VehicleDetails> vehicleDetailsList = apiResponse.getVehicleDetailsList();
            //            int size = vehicleDetailsList.size();
            //            if (vechileNo != null) {
            //                for (int i = 0; i < size; i++) {
            //
            //                    filteredList =
            //                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getVehicleNo().equals(vechileNo)).collect(Collectors.toList());
            //                }
            //            }
            //            if (slipNo != null) {
            //                for (int i = 0; i < size; i++) {
            //
            //                    filteredList =
            //                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getMachineNo().equalsIgnoreCase(machinecode)).filter(vehicle -> vehicle.getSlipNo().equals(slipNo)).collect(Collectors.toList());
            //
            //                }
            //            }


            ApiResponse apiResponse = gson.fromJson(stringBuilder.toString(), ApiResponse.class);
            List<VehicleDetails> vehicleDetailsList = apiResponse.getVehicleDetailsList();

            // If vehicleNo is not null, filter the list directly without using a loop
            // List<VehicleDetails> filteredList = new ArrayList<>();
            filteredList = new ArrayList<>();
            if (vechileNo != null) {

                //                filteredList =
                //                            vehicleDetailsList.stream().filter(vehicle -> vehicle.getVehicleNo().equalsIgnoreCase(vechileNo)).collect(Collectors.toList());
                filteredList =
                    vehicleDetailsList.stream().filter(vehicle -> vehicle.getMachineNo().equalsIgnoreCase(machinecode)).filter(vehicle -> vehicle.getVehicleNo().equalsIgnoreCase(vechileNo)).collect(Collectors.toList());
                if (filteredList.size() <= 0) {
                    filteredList =
                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getVehicleNo().equalsIgnoreCase(vechileNo)).collect(Collectors.toList());
                }

            }
            //            else {
            //                // If vehicleNo is null, you can either return the entire list or handle as needed
            //                filteredList = vehicleDetailsList;
            //            }
            //System.out.println("slipNo.trim()---" + slipNo.trim());
            if (slipNo != null) {
                filteredList =
                    vehicleDetailsList.stream().filter(vehicle -> vehicle.getMachineNo().equalsIgnoreCase(machinecode)).filter(vehicle -> vehicle.getSlipNo().equalsIgnoreCase(slipNo)).collect(Collectors.toList());
                if (filteredList.size() <= 0) {
                    filteredList =
                        vehicleDetailsList.stream().filter(vehicle -> vehicle.getSlipNo().equalsIgnoreCase(slipNo)).collect(Collectors.toList());
                }
            }

            //            else {
            //                filteredList = vehicleDetailsList;
            //            }

            VechileTypejComboBox.setEnabled(true);
            filteredList.forEach(System.out::println);
            for (VehicleDetails vehicle : filteredList) {
                System.out.println(vehicle.getVehicleNo() + " - " + vehicle.getTokenNo());
                if (!vehicle.getTokenNo().equalsIgnoreCase("0")) {
                    System.out.println("bypass_flag---" + bypass_flag);
                    if (bypass_flag.equalsIgnoreCase("Y")) {
                        if (!vehicle.getSlipNo().equalsIgnoreCase("0")) {
                            if (vehicle.getMachineNo().equalsIgnoreCase(TXT_Machine.getText())) {
                                withoutByPassMachine(vehicle);
                            } else {
                                ByPassMachine(vehicle);
                            }
                        } else {
                            withoutByPassMachine(vehicle);
                        }
                    } else {
                        if (!vehicle.getSlipNo().equalsIgnoreCase("0")) {
                            if (vehicle.getMachineNo().equalsIgnoreCase(TXT_Machine.getText())) {
                                TXT_Machine.setText(vehicle.getMachineNo().toUpperCase());
                            } else {
                                String message = "Slip pending on other user";
                                JOptionPane.showMessageDialog(null, message, "Message",
                                                              JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }

                        withoutByPassMachine(vehicle);
                    }
                    forPrint();
                } else {
                    String message = "Slip pending on other user!";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    resetValue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        if (filteredList.size() <= 0) {
        //            String message = "Please Enter Valid Vehicle no/Slip No";
        //            JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        //            resetValue();
        //        } else {
        //            forPrint();
        //        }
    }


    public void withoutByPassMachine(VehicleDetails vehicle) {
        if (vehicle.getVehicleNo() != null) {
            TXT_VechileNo.setText(vehicle.getVehicleNo().toUpperCase());
            if (vechileNo != null) {
                TXT_SlipNo.setEnabled(false);
            }
        }
        if (!vehicle.getTokenNo().equalsIgnoreCase("0")) {
            TXT_TokenNo.setText(vehicle.getTokenNo());
        }
        //COMMENT UPDATE HARE
        if (vehicle.getCompVehTypeCode().equalsIgnoreCase("0")) {
            VechileTypejComboBox.setEnabled(true);
            compVechileType = "N";
            ComboBoxChargeApplied.setSelectedIndex(0);
            // TXT_Charge.setText("0");
        } else {
            VechileTypejComboBox.setEnabled(false);
            ComboBoxChargeApplied.setEnabled(false);
            ComboBoxChargeApplied.setSelectedIndex(1);
            TXT_Charge.setText("0");
            compVechileType = "Y";
        }

        if (vehicle.getTrolly_req().equalsIgnoreCase("Y")) {
            TXT_TrollyNo.setEnabled(true);
            trollyReq = "Y";
            VechileTypejComboBox.setSelectedItem("TRACTOR");
            ComboBoxChargeApplied.setSelectedIndex(1);
            ComboBoxChargeApplied.setEnabled(false);
            TXT_Charge.setText("0");
            System.out.println("Slip Number is: " + TXT_SlipNo.getText());
            if (TXT_SlipNo.getText().trim() == null || TXT_SlipNo.getText().isEmpty()) {
                if (TXT_TrollyNo.getText().isEmpty() || TXT_TrollyNo.getText().trim() == null ||
                    TXT_TrollyNo.getText().trim() == "") {
                    String message = "Please enter trolley no.";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                return;
            }
        } else {
            TXT_TrollyNo.setEnabled(false);
        }

        if (!vehicle.getSlipNo().equalsIgnoreCase("0")) {
            TXT_SlipNo.setText(vehicle.getSlipNo());
            TXT_VechileNo.setEnabled(false);
        }

        if (!vehicle.getParty().equalsIgnoreCase("0")) {
            TXT_Part.setText(vehicle.getParty().toUpperCase());
        }

        if (!vehicle.getProduct().equalsIgnoreCase("0")) {
            TXT_Product.setText(vehicle.getProduct().toUpperCase());
        }

        if (vehicle.getGrossWeight().equalsIgnoreCase("0")) {
            TXT_GrossWeight.setText(vehicle.getGrossWeight());
        } else {
            Integer grossWeight = Integer.parseInt(vehicle.getGrossWeight());
            if (grossWeight > 0) {
                BtnGross.setEnabled(false);
            }
            TXT_GrossWeight.setText(vehicle.getGrossWeight());
        }

        if (vehicle.getTereWeight().equalsIgnoreCase("0")) {
            TXT_TareWeight.setText(vehicle.getTereWeight());
        } else {
            Integer tareWeight = Integer.parseInt(vehicle.getTereWeight());
            if (tareWeight > 0) {
                BtnTare.setEnabled(false);
            }
            TXT_TareWeight.setText(vehicle.getTereWeight());
        }

        if (vehicle.getNetWeight().equalsIgnoreCase("0")) {
            TXT_NetWeight.setText(vehicle.getNetWeight());
        } else {
            Integer tareWeight = Integer.parseInt(vehicle.getNetWeight());
            if (tareWeight > 0) {
                BtnSubmit.setEnabled(false);
                BtnActionClear.setEnabled(false);
            }
            TXT_TareWeight.setText(vehicle.getNetWeight());
        }

        if (!vehicle.getFinalEnteredBy().equalsIgnoreCase("0")) {
            if (Integer.parseInt(TXT_NetWeight.getText()) > 0) {
                TXT_FinealEnterBy.setText(vehicle.getFinalEnteredBy());

            } else {
                TXT_FinealEnterBy.setText(userNamevalue);
            }

        }

        if (!vehicle.getTrolleyNo().equalsIgnoreCase("0")) {
            TXT_TrollyNo.setText(vehicle.getTrolleyNo());
        }

        if (vehicle.getCharge().equalsIgnoreCase("0") || vehicle.getCharge() == "0") {
            TXT_Charge.setText("0");
        } else {
            TXT_Charge.setText(vehicle.getCharge());
        }

        if (!vehicle.getCharge_applicable().equalsIgnoreCase("0")) {
            if (vehicle.getCharge_applicable().equalsIgnoreCase("Yes")) {
                ComboBoxChargeApplied.setSelectedIndex(0);
            } else {
                ComboBoxChargeApplied.setSelectedIndex(1);
            }
        }

        if (!vehicle.getVeh_subtype_desc().equalsIgnoreCase("0")) {
            VechileTypejComboBox.setSelectedItem(vehicle.getVeh_subtype_desc());
        }

        if (!vehicle.getRemarks().equalsIgnoreCase("0")) {
            TXT_REMARKS.setText(vehicle.getRemarks().toUpperCase());
        }

        if (!vehicle.getGateEntryNumber().equalsIgnoreCase("0")) {
            TXT_GateEntry.setText(vehicle.getGateEntryNumber());
        }

        if (!vehicle.getCreatedBy().equalsIgnoreCase("0")) {
            TXT_CreateBy.setText(vehicle.getCreatedBy());
        }

        if (!vehicle.getCreationDate().equalsIgnoreCase("0")) {
            TXT_CreateDate.setText(vehicle.getCreationDate());
        }

        if (!vehicle.getCreationTime().equalsIgnoreCase("0")) {
            TXT_CreateTime.setText(vehicle.getCreationTime());
        }

        if (!vehicle.getProcessCode().equalsIgnoreCase("0")) {
            TXT_Process.setText(vehicle.getProcessCode());
        }

        if (!vehicle.getRcNo().equalsIgnoreCase("0")) {
            TXT_RC_NO.setText(vehicle.getRcNo());
        }

        //COMMENT BY SAWAN

        //                if (vehicle.getCompVehTypeCode().equalsIgnoreCase("0")) {
        //                    VechileTypejComboBox.setEnabled(true);
        //                    compVechileType = "N";
        //                    ComboBoxChargeApplied.setSelectedIndex(0);
        //                    // TXT_Charge.setText("0");
        //                } else {
        //                    VechileTypejComboBox.setEnabled(false);
        //                    ComboBoxChargeApplied.setEnabled(false);
        //                    ComboBoxChargeApplied.setSelectedIndex(1);
        //                    TXT_Charge.setText("0");
        //                    compVechileType = "Y";
        //                }

        if (!vehicle.getFt_tere_weight().equalsIgnoreCase("0")) {
            ftTereWeight = vehicle.getFt_tere_weight();
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

    public void ByPassMachine(VehicleDetails vehicle) {
        if (vehicle.getVehicleNo() != null) {
            TXT_VechileNo.setText(vehicle.getVehicleNo().toUpperCase());
            if (vechileNo != null) {
                TXT_SlipNo.setEnabled(false);
            }
        }
        if (!vehicle.getTokenNo().equalsIgnoreCase("0")) {
            TXT_TokenNo.setText(vehicle.getTokenNo());
        }
        //COMMENT UPDATE HARE
        if (vehicle.getCompVehTypeCode().equalsIgnoreCase("0")) {
            VechileTypejComboBox.setEnabled(true);
            compVechileType = "N";
            ComboBoxChargeApplied.setSelectedIndex(0);
            // TXT_Charge.setText("0");
        } else {
            VechileTypejComboBox.setEnabled(false);
            ComboBoxChargeApplied.setEnabled(false);
            ComboBoxChargeApplied.setSelectedIndex(1);
            TXT_Charge.setText("0");
            compVechileType = "Y";
        }

        if (vehicle.getTrolly_req().equalsIgnoreCase("Y")) {
            TXT_TrollyNo.setEnabled(true);
            trollyReq = "Y";
            VechileTypejComboBox.setSelectedItem("TRACTOR");
            ComboBoxChargeApplied.setSelectedIndex(1);
            ComboBoxChargeApplied.setEnabled(false);
            TXT_Charge.setText("0");
            System.out.println("Slip Number is: " + TXT_SlipNo.getText());
            if (TXT_SlipNo.getText().trim() == null || TXT_SlipNo.getText().isEmpty()) {
                if (TXT_TrollyNo.getText().isEmpty() || TXT_TrollyNo.getText().trim() == null ||
                    TXT_TrollyNo.getText().trim() == "") {
                    String message = "Please enter trolley no.";
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                return;
            }
        } else {
            TXT_TrollyNo.setEnabled(false);
        }
        if (slipNo == null) {
            if (!vehicle.getSlipNo().equalsIgnoreCase("0")) {
                TXT_SlipNo.setText(null);
                // TXT_VechileNo.setEnabled(false);
            }
        }
        if (!vehicle.getParty().equalsIgnoreCase("0")) {
            TXT_Part.setText(null);
        }

        if (!vehicle.getProduct().equalsIgnoreCase("0")) {
            TXT_Product.setText(null);
        }

        if (vehicle.getGrossWeight().equalsIgnoreCase("0")) {
            TXT_GrossWeight.setText("0");
        } else {

            TXT_GrossWeight.setText("0");
        }

        if (vehicle.getTereWeight().equalsIgnoreCase("0")) {
            TXT_TareWeight.setText("0");
        } else {

            TXT_TareWeight.setText("0");
        }

        if (vehicle.getNetWeight().equalsIgnoreCase("0")) {
            TXT_NetWeight.setText("0");
        } else {

            TXT_TareWeight.setText("0");
        }

        if (!vehicle.getFinalEnteredBy().equalsIgnoreCase("0")) {
            if (Integer.parseInt(TXT_NetWeight.getText()) > 0) {
                TXT_FinealEnterBy.setText(vehicle.getFinalEnteredBy());
            } else {
                TXT_FinealEnterBy.setText(userNamevalue);
            }
        }

        if (!vehicle.getTrolleyNo().equalsIgnoreCase("0")) {
            TXT_TrollyNo.setText(vehicle.getTrolleyNo());
        }

        if (vehicle.getCharge().equalsIgnoreCase("0") || vehicle.getCharge() == "0") {
            TXT_Charge.setText("0");
        } else {
            TXT_Charge.setText(vehicle.getCharge());
        }

        if (!vehicle.getCharge_applicable().equalsIgnoreCase("0")) {
            if (vehicle.getCharge_applicable().equalsIgnoreCase("Yes")) {
                ComboBoxChargeApplied.setSelectedIndex(0);
            } else {
                ComboBoxChargeApplied.setSelectedIndex(1);
            }
        }

        if (!vehicle.getVeh_subtype_desc().equalsIgnoreCase("0")) {
            VechileTypejComboBox.setSelectedItem(vehicle.getVeh_subtype_desc());
        }

        if (!vehicle.getRemarks().equalsIgnoreCase("0")) {
            TXT_REMARKS.setText(null);
        }

        if (!vehicle.getGateEntryNumber().equalsIgnoreCase("0")) {
            TXT_GateEntry.setText(null);
        }

        if (!vehicle.getCreatedBy().equalsIgnoreCase("0")) {
            TXT_CreateBy.setText(null);
        }

        if (!vehicle.getCreationDate().equalsIgnoreCase("0")) {
            TXT_CreateDate.setText(null);
        }

        if (!vehicle.getCreationTime().equalsIgnoreCase("0")) {
            TXT_CreateTime.setText(null);
        }

        if (!vehicle.getProcessCode().equalsIgnoreCase("0")) {
            TXT_Process.setText(null);
        }

        if (!vehicle.getRcNo().equalsIgnoreCase("0")) {
            TXT_RC_NO.setText(null);
        }

        //COMMENT BY SAWAN

        //                if (vehicle.getCompVehTypeCode().equalsIgnoreCase("0")) {
        //                    VechileTypejComboBox.setEnabled(true);
        //                    compVechileType = "N";
        //                    ComboBoxChargeApplied.setSelectedIndex(0);
        //                    // TXT_Charge.setText("0");
        //                } else {
        //                    VechileTypejComboBox.setEnabled(false);
        //                    ComboBoxChargeApplied.setEnabled(false);
        //                    ComboBoxChargeApplied.setSelectedIndex(1);
        //                    TXT_Charge.setText("0");
        //                    compVechileType = "Y";
        //                }

        if (!vehicle.getFt_tere_weight().equalsIgnoreCase("0")) {
            ftTereWeight = vehicle.getFt_tere_weight();
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

    public void trollyDeatilsApiCall() throws JSONException, IOException {
        JSONObject jsonObject = null;
        if (TXT_TrollyNo.getText().isEmpty() || TXT_VechileNo.getText() == null) {
            JOptionPane.showMessageDialog(null, "Please Trolley Number", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            // URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/trollyDtl");
            URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/trollyDtl");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString =
                "{\"vechileNo\":\"" + TXT_VechileNo.getText() + "\",\"trolly\":\"" + TXT_TrollyNo.getText() + "\"}";
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

                System.out.println("jsonResponse--" + jsonResponse.length() + "jsonResponse---" + jsonResponse.trim());

                jsonObject = new JSONObject(jsonResponse);
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

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void oncallApiVehicleSlipNo11(String slipNo) throws JSONException {
        try {
            // URL url = new URL("http://182.16.9.100:7003/RestApiWeightBridge/resources/printslip");
            // URL url = new URL("http://10.0.6.204:7003/RestApiWeightBridge/resources/printslip");
            URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/printslip");
            // URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/printslip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("slipNo", slipNo);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            StringBuilder response = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() <= 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Valid Slip No", "Message",
                                              JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject rs = jsonArray.getJSONObject(i);
                try {
                    if (rs.getString("vehicleNo") != null) {
                        vechileNo = rs.getString("vehicleNo").toUpperCase();
                        // TXT_VechileNo.setText(vechileNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("slipNo") != null) {
                        slipNo = rs.getString("slipNo").toUpperCase();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("tokenNo") != null) {
                        tokenNo = rs.getString("tokenNo").toUpperCase();
                        // TXT_TokenNo.setText(tokenNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("party") != null) {
                        party = rs.getString("party").toUpperCase();
                        // TXT_Part.setText(party);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("grossWeight") != null) {
                        grossWeight = Integer.toString(rs.getInt("grossWeight"));
                        // TXT_GrossWeight.setText(grossWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("tereWeight") != null) {
                        tareWeight = rs.getString("tereWeight").toUpperCase();
                        // TXT_TareWeight.setText(tareWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("netWeight") != null) {
                        netWeight = rs.getString("netWeight").toUpperCase();
                        // TXT_NetWeight.setText(netWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("finalEnteredDate") != null && rs.getString("finalEnteredTime") != null) {
                        finaldate =
                            rs.getString("finalEnteredDate").toUpperCase() + " " + rs.getString("finalEnteredTime");
                        // TXT_FinealEnterDate.setText(rs.getString("finalEnteredDate").toUpperCase());
                        // TXT_FinealEnterTime.setText(rs.getString("finalEnteredTime").toUpperCase());
                        // TXT_FinealEnterBy.setText(rs.getString("finalEnteredBy").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("charge") != null) {
                        charge = rs.getString("charge").toUpperCase();
                        // TXT_Charge.setText(rs.getString("charge").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("remarks") != null) {
                        remarks = rs.getString("remarks").toUpperCase();
                        // TXT_RE.setText(rs.getString("CHARGE").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("gateEntryNumber") != null) {
                        gateNo = rs.getString("gateEntryNumber").toUpperCase();
                        // TXT_GateEntry.setText(gateNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("rcNo") != null) {
                        // TXT_RC_NO.setText(rs.getString("rcNo").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("machineNo") != null) {
                        //  TXT_Machine.setText(rs.getString("machineNo").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("subtypeDesc") != null) {
                        vechileType = rs.getString("subtypeDesc").toUpperCase();
                        //  txt_.setText(vechileType);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("product") != null) {
                        product = rs.getString("product").toUpperCase();
                        //  TXT_Process.setText(rs.getString("processCode").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("createdBy") != null) {
                        //  TXT_CreateBy.setText(rs.getString("createdBy").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("creationDate") != null) {
                        //  TXT_CreateDate.setText(rs.getString("creationDate").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("creationTime") != null) {
                        //  TXT_CreateTime.setText(rs.getString("creationTime").toUpperCase());
                        create = rs.getString("creationDate") + " " + rs.getString("creationTime");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    InputStream inputStream = new FileInputStream(jasperPath);
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
                    parameters.put("M_no", rs.getString("machineNo"));


                    // Create JasperPrint
                    JasperPrint print = JasperFillManager.fillReport(design, parameters, new JREmptyDataSource());

                    // Print the report
                    //                    boolean printed =
                    //                        print.print(print.getPages(), 0); // Print the report (this will send it to the default printer)
                    //
                    //                    // Check if printing was successful
                    //                    if (printed) {
                    //                        System.out.println("Report printed successfully!");
                    //                    } else {
                    //                        System.out.println("Failed to print the report.");
                    //                    }

                    // Create JasperViewer
                    JasperViewer viewer = new JasperViewer(print, false);

                    // Close the viewer automatically after printing
                    SwingUtilities.invokeLater(() -> {
                            try {
                                Thread.sleep(4000); // Wait for 2 seconds after printing to allow printing time
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            viewer.dispose(); // Dispose of the viewer after printing
                        });

                    // Show the JasperViewer
                    viewer.setVisible(true);


                    resetValueAfterPrint();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
                } catch (JRException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void oncallApiVehicleSlipNo(String slipNo) throws JSONException {
        try {
            // URL url = new URL("http://182.16.9.100:7003/RestApiWeightBridge/resources/printslip");
            // URL url = new URL("http://10.0.6.204:7003/RestApiWeightBridge/resources/printslip");
            URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/printslip");
            // URL url = new URL("http://10.0.6.171:9090/RestApiWeightBridge/resources/printslip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("slipNo", slipNo);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            StringBuilder response = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() <= 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Valid Slip No", "Message",
                                              JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject rs = jsonArray.getJSONObject(i);
                try {
                    if (rs.getString("vehicleNo") != null) {
                        vechileNo = rs.getString("vehicleNo").toUpperCase();
                        // TXT_VechileNo.setText(vechileNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("slipNo") != null) {
                        slipNo = rs.getString("slipNo").toUpperCase();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("tokenNo") != null) {
                        tokenNo = rs.getString("tokenNo").toUpperCase();
                        // TXT_TokenNo.setText(tokenNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("party") != null) {
                        party = rs.getString("party").toUpperCase();
                        // TXT_Part.setText(party);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("grossWeight") != null) {
                        grossWeight = Integer.toString(rs.getInt("grossWeight"));
                        // TXT_GrossWeight.setText(grossWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("tereWeight") != null) {
                        tareWeight = rs.getString("tereWeight").toUpperCase();
                        // TXT_TareWeight.setText(tareWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("netWeight") != null) {
                        netWeight = rs.getString("netWeight").toUpperCase();
                        // TXT_NetWeight.setText(netWeight);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("finalEnteredDate") != null && rs.getString("finalEnteredTime") != null) {
                        finaldate =
                            rs.getString("finalEnteredDate").toUpperCase() + " " + rs.getString("finalEnteredTime");
                        // TXT_FinealEnterDate.setText(rs.getString("finalEnteredDate").toUpperCase());
                        // TXT_FinealEnterTime.setText(rs.getString("finalEnteredTime").toUpperCase());
                        // TXT_FinealEnterBy.setText(rs.getString("finalEnteredBy").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("charge") != null) {
                        charge = rs.getString("charge").toUpperCase();
                        // TXT_Charge.setText(rs.getString("charge").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("remarks") != null) {
                        remarks = rs.getString("remarks").toUpperCase();
                        // TXT_RE.setText(rs.getString("CHARGE").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("gateEntryNumber") != null) {
                        gateNo = rs.getString("gateEntryNumber").toUpperCase();
                        // TXT_GateEntry.setText(gateNo);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("rcNo") != null) {
                        // TXT_RC_NO.setText(rs.getString("rcNo").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("machineNo") != null) {
                        //  TXT_Machine.setText(rs.getString("machineNo").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("subtypeDesc") != null) {
                        vechileType = rs.getString("subtypeDesc").toUpperCase();
                        //  txt_.setText(vechileType);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("product") != null) {
                        product = rs.getString("product").toUpperCase();
                        //  TXT_Process.setText(rs.getString("processCode").toUpperCase());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("createdBy") != null) {
                        //  TXT_CreateBy.setText(rs.getString("createdBy").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("creationDate") != null) {
                        //  TXT_CreateDate.setText(rs.getString("creationDate").toUpperCase());





                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (rs.getString("creationTime") != null) {
                        //  TXT_CreateTime.setText(rs.getString("creationTime").toUpperCase());
                        create = rs.getString("creationDate") + " " + rs.getString("creationTime");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                InputStream inputStream = new FileInputStream(jasperPath);
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
                parameters.put("M_no", rs.getString("machineNo"));

                // Create JasperPrint
                JasperPrint print = JasperFillManager.fillReport(design, parameters, new JREmptyDataSource());

                // Create JasperViewer
                JasperViewer viewer = new JasperViewer(print, false);

                // Create a Print Button
                JButton printButton = new JButton("Print & Close");

                // ActionListener for the print button
                printButton.addActionListener(e -> { try {
                        // Create PrinterJob instance
                        PrinterJob printerJob = PrinterJob.getPrinterJob();
                        //  printerJob.setPrintService(print.getPrintService());

                        // Show print dialog and start printing
                        if (printerJob.printDialog()) {
                            printerJob.print();
                        }

                        // Dispose of the viewer (close it)
                        viewer.dispose(); // Close the JasperViewer window
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);

                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error during printing", "Error",
                                                      JOptionPane.ERROR_MESSAGE);
                    } });

                // Add the button to the viewer's panel (optional customization)
                JPanel panel = new JPanel();
                panel.add(printButton); // Add the button to a panel
                viewer.getContentPane().add(panel, BorderLayout.SOUTH); // Add the panel to the bottom of the viewer

                // Show the JasperViewer
                viewer.setVisible(true);

                // Optionally reset values after printing
                resetValueAfterPrint();
            }
        } catch (IOException e) {
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
