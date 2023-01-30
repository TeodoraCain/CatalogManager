package org.example;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagementEleviForm extends JFrame {

    private User user;
    private Logger logger;
    private ElevDaoImpl eleviImpl;

    public ManagementEleviForm() {
        initComponents();
        initFields();
        this.logger = null;
        user = LoginForm.getUser();
        if (user != null) {
            userIconLbl.setText(user.getUsername());
            logger = user.getLogger();
        } else {
            userIconLbl.setText("UserName");
        }
        table_update();
    }

//init comboboxes
    private void initFields() {
        Connection conn = MyConnection.getConnection();

        classCodeCbx.removeAllItems();

        try {
            Statement statement = conn.createStatement();
            String stm = "SELECT CODC FROM CLASA";
            ResultSet classCodes = statement.executeQuery(stm);

            while (classCodes.next()) {
                classCodeCbx.addItem(String.valueOf(classCodes.getInt(1)));

            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }

        yearCbx.setSelectedIndex(0);
        classCodeCbx.setSelectedIndex(0);

    }

    private int getMax() {
        int code = 0;
        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT MAX(CODE) FROM ELEV";
            ResultSet resultSet = statement.executeQuery(stm);

            while (resultSet.next()) {
                code = resultSet.getInt(1);
            }

            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
        return code + 1;
    }

    private boolean isEmptyField() {

        if (lastName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog introdu numele elevului!");
            return false;
        }
        if (firstName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog introdu prenumele elevului!");
            return false;
        }
        if (initial.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog introdu initiala tatalui!");
            return false;
        }
        if (birthDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Te rog selecteaza data de nastere a elevului!");
            return false;
        }
        if (birthDate.getDate().compareTo(new java.util.Date()) > 0) {
            JOptionPane.showMessageDialog(this, "Data de nastere incorecta!");
            return false;
        }
        return true;
    }

    private void table_update() {
        eleviImpl = new ElevDaoImpl();
        matNr.setText(String.valueOf(getMax()));

        DefaultTableModel model = (DefaultTableModel) dataTab.getModel();
        model.setRowCount(0);

        List<Elev> elevi = eleviImpl.getElevList();
        for (Elev elev : elevi) {
            Vector eleviV = new Vector();
            eleviV.add(elev.getCode());
            eleviV.add(elev.getNume());
            eleviV.add(elev.getPrenume());
            eleviV.add(elev.getInitTata());
            eleviV.add(elev.getDatan());
            eleviV.add(elev.getAnStudiu());
            eleviV.add(elev.getMedia());
            eleviV.add(elev.getCodc());
            model.addRow(eleviV);
        }
        dataTab.setAutoCreateRowSorter(true);
        centerTableCells();
    }

    public void centerTableCells() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEADING);
        dataTab.setDefaultRenderer(String.class, centerRenderer);
        dataTab.setDefaultRenderer(Integer.class, centerRenderer);
        dataTab.setDefaultRenderer(Float.class, centerRenderer);
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel();
        JPanel headPanel = new JPanel();
        userIconLbl = new JLabel();
        JButton logoutBtn = new JButton();
        JButton homeBtn = new JButton();
        JPanel footPanel = new JPanel();
        JPanel dataPanel = new JPanel();
        JLabel gradeLbl = new JLabel();
        firstName = new JTextField();
        lastName = new JTextField();
        yearCbx = new JComboBox<>();
        JLabel firstNameLbl = new JLabel();
        matNr = new JTextField();
        JLabel classCodeLbl = new JLabel();
        JLabel birthDateLbl = new JLabel();
        JLabel initialLbl = new JLabel();
        JLabel matNrLbl = new JLabel();
        JLabel lastNameLbl = new JLabel();
        JLabel yearLbl = new JLabel();
        initial = new JTextField();
        classCodeCbx = new JComboBox<>();
        JScrollPane jScrollPane = new JScrollPane();
        dataTab = new JTable();
        JButton deleteBtn = new JButton();
        JButton modifyBtn = new JButton();
        JButton addBtn = new JButton();
        birthDate = new com.toedter.calendar.JDateChooser();
        grade = new JTextField();
        JButton addBtn1 = new JButton();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Management Elevi");
        setBackground(new Color(255, 255, 255));
        setBounds(new Rectangle(400, 200, 0, 0));
        setPreferredSize(new Dimension(775, 475));

        mainPanel.setBackground(new Color(255, 255, 255));

        headPanel.setBackground(new Color(0, 102, 102));
        headPanel.setPreferredSize(new Dimension(479, 45));

        userIconLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        userIconLbl.setForeground(new Color(255, 255, 255));
        userIconLbl.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\user.png"));
        userIconLbl.setText("UserName");
        userIconLbl.setToolTipText("");

        logoutBtn.setBackground(new Color(0, 102, 102));
        logoutBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(255, 255, 255));
        logoutBtn.setText("Logout");
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);

        homeBtn.setBackground(new Color(0, 102, 102));
        homeBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        homeBtn.setForeground(new Color(255, 255, 255));
        homeBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\home.png"));

        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.setIconTextGap(6);
        homeBtn.addActionListener(this::homeBtnActionPerformed);

        GroupLayout headPanelLayout = new GroupLayout(headPanel);
        headPanel.setLayout(headPanelLayout);
        headPanelLayout.setHorizontalGroup(
            headPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(headPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(homeBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userIconLbl, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 482, Short.MAX_VALUE)
                .addComponent(logoutBtn, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        headPanelLayout.setVerticalGroup(
            headPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, headPanelLayout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(homeBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
            .addGroup(headPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(logoutBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userIconLbl, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
        );

        footPanel.setBackground(new Color(0, 102, 102));

        GroupLayout footPanelLayout = new GroupLayout(footPanel);
        footPanel.setLayout(footPanelLayout);
        footPanelLayout.setHorizontalGroup(
            footPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );
        footPanelLayout.setVerticalGroup(
            footPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        dataPanel.setBackground(new Color(255, 255, 255));

        gradeLbl.setBackground(new Color(255, 255, 255));
        gradeLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        gradeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        gradeLbl.setText("Media Generala");
        gradeLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        firstName.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        firstName.setHorizontalAlignment(JTextField.CENTER);
        firstName.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        lastName.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        lastName.setHorizontalAlignment(JTextField.CENTER);
        lastName.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        yearCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        yearCbx.setModel(new DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        yearCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

        firstNameLbl.setBackground(new Color(255, 255, 255));
        firstNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        firstNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        firstNameLbl.setText("Prenume");
        firstNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        matNr.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        matNr.setHorizontalAlignment(JTextField.CENTER);
        matNr.setEnabled(false);

        classCodeLbl.setBackground(new Color(255, 255, 255));
        classCodeLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        classCodeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        classCodeLbl.setText("Cod Clasa");
        classCodeLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        birthDateLbl.setBackground(new Color(255, 255, 255));
        birthDateLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        birthDateLbl.setHorizontalAlignment(SwingConstants.CENTER);
        birthDateLbl.setText("Data Nastere");
        birthDateLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        initialLbl.setBackground(new Color(255, 255, 255));
        initialLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        initialLbl.setHorizontalAlignment(SwingConstants.CENTER);
        initialLbl.setText("Initiala Tata");
        initialLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        matNrLbl.setBackground(new Color(255, 255, 255));
        matNrLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        matNrLbl.setHorizontalAlignment(SwingConstants.CENTER);
        matNrLbl.setText("Nr. Matricol");
        matNrLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        lastNameLbl.setBackground(new Color(255, 255, 255));
        lastNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        lastNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        lastNameLbl.setText("Nume");
        lastNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        yearLbl.setBackground(new Color(255, 255, 255));
        yearLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        yearLbl.setHorizontalAlignment(SwingConstants.CENTER);
        yearLbl.setText("An Studiu");
        yearLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        initial.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        initial.setHorizontalAlignment(JTextField.CENTER);

        classCodeCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        classCodeCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jScrollPane.setBackground(new Color(0, 102, 102));
        jScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 153)));
        jScrollPane.setForeground(new Color(0, 102, 102));
        jScrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jScrollPane.setFont(new Font("Tempus Sans ITC", Font.BOLD, 11));

        dataTab.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 102)));
        dataTab.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "<html>Nr.<br>Matricol", "Nume", "Prenume", "<html>Init.<br>Tata", "<html>Data<br>Nastere", "<html>An<br>Studiu", "<html>Media<br>Generala", "<html>Cod<br>Clasa"
            }
        ) {
            final Class[] types = new Class [] {
                Integer.class, String.class, String.class, String.class, String.class, Integer.class, Float.class, Integer.class
            };
            final boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTab.setGridColor(new Color(0, 102, 102));
        dataTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTabMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(dataTab);
        if (dataTab.getColumnModel().getColumnCount() > 0) {
            dataTab.getColumnModel().getColumn(7).setHeaderValue("<html>Cod<br>Clasa");
        }

        deleteBtn.setBackground(new Color(0, 102, 102));
        deleteBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        deleteBtn.setForeground(new Color(255, 255, 255));
        deleteBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\minus.png"));
        deleteBtn.setText("Sterge");
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setIconTextGap(8);
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        modifyBtn.setBackground(new Color(0, 102, 102));
        modifyBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        modifyBtn.setForeground(new Color(255, 255, 255));
        modifyBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\edit.png"));
        modifyBtn.setText("Modifica");
        modifyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modifyBtn.setIconTextGap(2);
        modifyBtn.addActionListener(this::modifyBtnActionPerformed);

        addBtn.setBackground(new Color(0, 102, 102));
        addBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        addBtn.setForeground(new Color(255, 255, 255));
        addBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\add.png"));
        addBtn.setText("Adauga");
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(this::addBtnActionPerformed);

        birthDate.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));

        grade.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        grade.setHorizontalAlignment(JTextField.CENTER);
        grade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                gradeKeyTyped(evt);
            }
        });

        addBtn1.setBackground(new Color(0, 102, 102));
        addBtn1.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        addBtn1.setForeground(new Color(255, 255, 255));
        addBtn1.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\excel.png"));
        addBtn1.setToolTipText("Add data from Excel");
        addBtn1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn1.addActionListener(this::addBtn1ActionPerformed);

        GroupLayout dataPanelLayout = new GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matNr, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initial)))
                .addGap(49, 49, 49)
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(classCodeLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(classCodeCbx, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(yearLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yearCbx, 0, 117, Short.MAX_VALUE))
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(gradeLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(grade)))
                        .addGap(64, 64, 64))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(birthDate, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                        .addGap(49, 49, 49)))
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(addBtn, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(addBtn1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                    .addComponent(deleteBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modifyBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38))
            .addGroup(GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane)
                .addContainerGap())
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yearLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(yearCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(gradeLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(grade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(classCodeLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(classCodeCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(matNr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(lastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(firstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(initial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(deleteBtn)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modifyBtn)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(addBtn)
                            .addComponent(addBtn1))))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addContainerGap())
        );

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(footPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(headPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(491, Short.MAX_VALUE)
                .addComponent(footPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addComponent(headPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 457, Short.MAX_VALUE)))
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        new MainForm().setVisible(true);
        this.dispose();
    }

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if ("ADMIN".equals(user.getRole())) {
            int code = Integer.parseInt(matNr.getText());
            if (code >= getMax()) {
                JOptionPane.showMessageDialog(this,
                        "Selecteaza un elev pentru a fi sters din baza de date.",
                        "Select Row",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                eleviImpl.deleteElev(code);
                logger.log(Level.INFO, "{0}-{1} DELETE {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
                JOptionPane.showMessageDialog(this,
                        "Stergerea a avut loc cu success!",
                        "Row Deleted",
                        JOptionPane.INFORMATION_MESSAGE);

                table_update();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul " + user.getRole() + " nu se permite stergerea!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void modifyBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if ("ADMIN".equals(user.getRole())) {
            if (isEmptyField()) {
                int code = Integer.parseInt(matNr.getText());
                if (code >= getMax()) {
                    JOptionPane.showMessageDialog(this,
                            "Selecteaza un elev pentru a fi modificat in baza de date.",
                            "Select Row",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Elev elev = new Elev(Integer.parseInt(matNr.getText()), lastName.getText().toUpperCase(), firstName.getText().toUpperCase(), initial.getText().toUpperCase(), birthDate.getDate().toString(), Integer.parseInt(yearCbx.getItemAt(yearCbx.getSelectedIndex())), Float.parseFloat(grade.getText()), Integer.parseInt(classCodeCbx.getItemAt(classCodeCbx.getSelectedIndex())));

                    eleviImpl.updateElev(elev);
                    logger.log(Level.INFO, "{0}-{1} UPDATE {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
                    JOptionPane.showMessageDialog(this,
                            "Elevul " + lastName.getText().toUpperCase() + " " + firstName.getText().toUpperCase() + " a fost modificat in baza de date.",
                            "Row Inserted",
                            JOptionPane.INFORMATION_MESSAGE);

                    clearFields();
                    table_update();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul de " + user.getRole() + " nu se permite modificarea datelor!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if ("ADMIN".equals(user.getRole())) {
            if (isEmptyField()) {
                int code = Integer.parseInt(matNr.getText());
                if (elevExists(code) != 0) {
                    JOptionPane.showMessageDialog(this,
                            "Elevul cu codul " + code + " exista deja in baza de date!",
                            "Select Row",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Elev elev = new Elev(Integer.parseInt(matNr.getText()), lastName.getText().toUpperCase(), firstName.getText().toUpperCase(), initial.getText().toUpperCase(), birthDate.getDate().toString(), Integer.parseInt(yearCbx.getItemAt(yearCbx.getSelectedIndex())), Float.parseFloat(grade.getText()), Integer.parseInt(classCodeCbx.getItemAt(classCodeCbx.getSelectedIndex())));

                    eleviImpl.insertElev(elev);
                    clearFields();
                    table_update();

                    logger.log(Level.INFO, "{0}-{1} INSERT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
                    JOptionPane.showMessageDialog(this,
                            "Un nou elev a fost adaugat in baza de date.",
                            "Row Inserted",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul de " + user.getRole() + " nu se permite adaugarea datelor!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }
    private void clearFields() {
        lastName.setText("");
        firstName.setText("");
        initial.setText("");
        birthDate.setDate(null);
        yearCbx.setSelectedIndex(0);
        grade.setText("");
        classCodeCbx.setSelectedIndex(0);
    }
    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int answer = JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa parasesti sesiunea?", "Logout", JOptionPane.YES_NO_OPTION);
        if (answer == 0) {
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }
    private void dataTabMouseClicked(java.awt.event.MouseEvent ignoredEvt) {
        DefaultTableModel model = (DefaultTableModel) dataTab.getModel();
        int index = dataTab.getSelectedRow();
        matNr.setText(model.getValueAt(index, 0).toString());
        lastName.setText(model.getValueAt(index, 1).toString());
        firstName.setText(model.getValueAt(index, 2).toString());
        initial.setText(model.getValueAt(index, 3).toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = format.parse(model.getValueAt(index, 4).toString());
        } catch (ParseException ex) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        birthDate.setDate(date);
        yearCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 5).toString()) - 1);
        grade.setText(model.getValueAt(index, 6).toString());
        classCodeCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 7).toString()) - 25);
    }

    private void gradeKeyTyped(java.awt.event.KeyEvent evt) {
        if (Character.isLetter(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void addBtn1ActionPerformed(java.awt.event.ActionEvent evt) {
        if ("ADMIN".equals(user.getRole())) {
            String currentDirectoryPath = "C:\\";
            FileInputStream excelFIS;
            BufferedInputStream excelBIS;
            XSSFWorkbook excelImportWorkbook;
            XSSFSheet excelSheet;
            JFileChooser excelFileChooser = new JFileChooser(currentDirectoryPath);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
            excelFileChooser.setFileFilter(filter);
            DefaultTableModel importExcel = (DefaultTableModel) dataTab.getModel();
            importExcel.setRowCount(0);
            DataFormatter format = new DataFormatter();
            java.util.Date birthday = null;

            int answer = excelFileChooser.showOpenDialog(null);
            StringBuilder errors = new StringBuilder();
            if (answer == JFileChooser.APPROVE_OPTION) {
                try {
                    File excelFile = excelFileChooser.getSelectedFile();
                    excelFIS = new FileInputStream(excelFile);
                    excelBIS = new BufferedInputStream(excelFIS);
                    excelImportWorkbook = new XSSFWorkbook(excelBIS);
                    excelSheet = excelImportWorkbook.getSheetAt(0);
                    for (int i = 1; i <= excelSheet.getLastRowNum(); i++) {
                        XSSFRow excelRow = excelSheet.getRow(i);
                        int idCell = Integer.parseInt(format.formatCellValue(excelRow.getCell(0)));

                        if (elevExists(idCell) != 0) {
                            errors.append("Nr matricol exista deja in baza de date (" + "row").append(i).append(").\n");
                            continue;
                        }
                        XSSFCell lastNameCell = excelRow.getCell(1);
                        XSSFCell firstNameCell = excelRow.getCell(2);
                        XSSFCell initialCell = excelRow.getCell(3);
                        XSSFCell birthDateCell = excelRow.getCell(4);
                        int yearCell = Integer.parseInt(format.formatCellValue(excelRow.getCell(5)));
                        float gradeCell = Float.parseFloat(format.formatCellValue(excelRow.getCell(6)));
                        int classCodeCell = Integer.parseInt(format.formatCellValue(excelRow.getCell(7)));

                        importExcel.addRow(new Object[]{idCell, lastNameCell, firstNameCell, initialCell, birthDateCell, yearCell, gradeCell, classCodeCell});
                    }
                    if (!"".equals(errors.toString())) {
                        JOptionPane.showMessageDialog(this,
                                errors.toString(),
                                "Errors",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    Connection conn = MyConnection.getConnection();

                    for (int i = 0; i < dataTab.getRowCount(); i++) {
                        PreparedStatement add = conn.prepareStatement("INSERT INTO ELEV VALUES (?,?,?,?,?,?,?,?)");
                        add.setInt(1, (int) dataTab.getValueAt(i, 0));
                        add.setString(2, dataTab.getValueAt(i, 1).toString().toUpperCase());
                        add.setString(3, dataTab.getValueAt(i, 2).toString().toUpperCase());
                        add.setString(4, dataTab.getValueAt(i, 3).toString().toUpperCase());
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                        try {
                            birthday = format1.parse(dataTab.getValueAt(i, 4).toString());
                        } catch (ParseException ex) {
                            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        add.setDate(5, new Date(Objects.requireNonNull(birthday).getTime()));
                        add.setInt(6, (Integer) dataTab.getValueAt(i, 5));
                        add.setFloat(7, (Float) dataTab.getValueAt(i, 6));
                        add.setInt(8, (Integer) dataTab.getValueAt(i, 7));

                        add.executeUpdate();
                    }
                    logger.log(Level.INFO, "{0}-{1} INSERT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});

                    conn.close();
                    JOptionPane.showMessageDialog(this,
                            "Adaugare completa!",
                            "Finalizare!",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (HeadlessException | SQLException e) {
                    Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
                    System.out.println(e);
                }

                table_update();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul de " + user.getRole() + " nu se permite adaugarea datelor!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }

    }
    private int elevExists(int code) {
        PreparedStatement statement;
        try {
            Connection conn = MyConnection.getConnection();
            statement = conn.prepareStatement("SELECT CODE FROM ELEV WHERE CODE = ? ");
            statement.setInt(1, code);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                code = resultSet.getInt(1);
            } else {
                code = 0;
            }

            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }

        return code;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new ManagementEleviForm().setVisible(true));
    }

    private com.toedter.calendar.JDateChooser birthDate;
    private JComboBox<String> classCodeCbx;
    private JTable dataTab;
    private JTextField firstName;
    private JTextField grade;
    private JTextField initial;
    private JTextField lastName;
    private JTextField matNr;
    private JLabel userIconLbl;
    private JComboBox<String> yearCbx;

}
