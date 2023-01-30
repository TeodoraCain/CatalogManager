package org.example;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayForm extends JFrame {
    //Dictionar
    Dictionary<Integer, String> courses = new Hashtable<>();
    private User user;
    Logger logger;

    public DisplayForm() {
        initComponents();
        user = LoginForm.getUser();

        if (user != null) {
            userIconLbl.setText(user.getUsername());
            logger = user.getLogger();
        } else {
            userIconLbl.setText("UserName");
            logger = Logger.getLogger(DisplayForm.class.getName());
        }

        initFields();
        table_update();
    }

    //init comboboxes
    private void initFields() {
        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT CODM, DENM FROM MATERIE";
            ResultSet courseCodes = statement.executeQuery(stm);

            while (courseCodes.next()) {
                courses.put(courseCodes.getInt(1), courseCodes.getString(2));
            }

            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }

        yearCbx.setSelectedIndex(0);
        semesterCbx.setSelectedIndex(0);
        courseCodeCbx.setSelectedIndex(0);
        enableFields(true);
    }

    private void table_update() {
        Connection conn = MyConnection.getConnection();
        String stm = "";
        if (user != null) {
            try {
                Statement statement = conn.createStatement();
                if (user.getRole().equals("VIZUALIZARE")) {
                    stm = "SELECT E.CODE, E.NUME, E.PRENUME, N.AN_STUDIU, N.SEMESTRU, M.DENM, N.DATA, N.NOTA FROM NOTE N, ELEV E, MATERIE M WHERE E.CODE = N.CODE AND N.CODM = M.CODM AND E.CODE =" + user.getID();
                } else if (user.getRole().equalsIgnoreCase("EDITARE") || user.getRole().equalsIgnoreCase("ADMIN")) {
                    stm = "SELECT E.CODE, E.NUME, E.PRENUME, N.AN_STUDIU, N.SEMESTRU, M.DENM, N.DATA, N.NOTA FROM NOTE N, ELEV E, MATERIE M WHERE E.CODE = N.CODE AND N.CODM = M.CODM";
                }
                ResultSet resultSet = statement.executeQuery(stm);
                int columns = resultSet.getMetaData().getColumnCount();
                DefaultTableModel model = (DefaultTableModel) dataTab.getModel();

                model.setRowCount(0);

                while (resultSet.next()) {
                    Vector note = new Vector();
                    for (int i = 0; i < columns; i++) {
                        note.add(resultSet.getInt(1));
                        note.add(resultSet.getString(2));
                        note.add(resultSet.getString(3));
                        note.add(resultSet.getInt(4));
                        note.add(resultSet.getInt(5));
                        note.add(resultSet.getString(6));
                        note.add(resultSet.getString(7));
                        note.add(resultSet.getFloat(8));
                    }
                    model.addRow(note);
                }
                conn.close();

                if (user.getRole().equals("VIZUALIZARE")) {
                    matNr.setText(model.getValueAt(1, 0).toString());
                    matNr.setEnabled(false);
                    lastName.setText(model.getValueAt(1, 1).toString());
                    lastName.setEnabled(false);
                    firstName.setText(model.getValueAt(1, 2).toString());
                    firstName.setEnabled(false);
                }
            } catch (SQLException e) {
                Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(e);
            }
            dataTab.setAutoCreateRowSorter(true);
            centerTableCells();
        } else {
            JOptionPane.showMessageDialog(this, "Nu exista rol pentru utilizator!");
        }
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
        JScrollPane jScrollPane = new JScrollPane();
        dataTab = new JTable();
        semesterCbx = new JComboBox<>();
        JLabel gradeLbl = new JLabel();
        JLabel classCodeLbl = new JLabel();
        JLabel yearLbl = new JLabel();
        JLabel birthDateLbl = new JLabel();
        courseCodeCbx = new JComboBox<>();
        minDate = new com.toedter.calendar.JDateChooser();
        course = new JTextField();
        minGrade = new JTextField();
        yearCbx = new JComboBox<>();
        JLabel initialLbl = new JLabel();
        JButton refreshBtn = new JButton();
        JButton searchBtn = new JButton();
        firstName = new JTextField();
        JLabel firstNameLbl = new JLabel();
        JLabel lastNameLbl = new JLabel();
        lastName = new JTextField();
        JLabel matNrLbl = new JLabel();
        matNr = new JTextField();
        JButton exportToExcelBtn = new JButton();
        JButton exportToPDF = new JButton();
        maxGrade = new JTextField();
        maxDate = new com.toedter.calendar.JDateChooser();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Vizualizare Note");
        setBackground(new Color(255, 255, 255));
        setBounds(new Rectangle(400, 200, 0, 0));
        setFocusable(false);

        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setNextFocusableComponent(dataPanel);
        mainPanel.setPreferredSize(new Dimension(837, 535));

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
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 540, Short.MAX_VALUE)
                .addComponent(logoutBtn, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        headPanelLayout.setVerticalGroup(
            headPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, headPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(homeBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
            .addGroup(headPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(logoutBtn, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addComponent(userIconLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        footPanel.setBackground(new Color(0, 102, 102));

        GroupLayout footPanelLayout = new GroupLayout(footPanel);
        footPanel.setLayout(footPanelLayout);
        footPanelLayout.setHorizontalGroup(
            footPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 837, Short.MAX_VALUE)
        );
        footPanelLayout.setVerticalGroup(
            footPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        dataPanel.setBackground(new Color(255, 255, 255));

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
                "<html>Nr.<br>Matricol", "Nume", "Prenume", "<html>An<br>Studiu", "Semestru", "Materie", "Data", "Nota"
            }
        ) {
            final Class[] types = new Class [] {
                Integer.class, String.class, String.class, Integer.class, Integer.class, String.class, String.class, Float.class
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

        semesterCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        semesterCbx.setModel(new DefaultComboBoxModel<>(new String[] { "", "1", "2" }));
        semesterCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gradeLbl.setBackground(new Color(255, 255, 255));
        gradeLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        gradeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        gradeLbl.setText("Semestru");
        gradeLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        classCodeLbl.setBackground(new Color(255, 255, 255));
        classCodeLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        classCodeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        classCodeLbl.setText("Nota");
        classCodeLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        yearLbl.setBackground(new Color(255, 255, 255));
        yearLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        yearLbl.setHorizontalAlignment(SwingConstants.CENTER);
        yearLbl.setText("An Studiu");
        yearLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        birthDateLbl.setBackground(new Color(255, 255, 255));
        birthDateLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        birthDateLbl.setHorizontalAlignment(SwingConstants.CENTER);
        birthDateLbl.setText("Data ");
        birthDateLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        courseCodeCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        courseCodeCbx.setModel(new DefaultComboBoxModel<>(new String[] { "", "1", "2", "3", "4", "5", "6", "7", "8" }));
        courseCodeCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));
        courseCodeCbx.addActionListener(this::courseCodeCbxActionPerformed);

        minDate.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));

        course.setEditable(false);
        course.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        course.setHorizontalAlignment(JTextField.CENTER);
        //course.setEnabled(false);

        minGrade.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        minGrade.setHorizontalAlignment(JTextField.CENTER);
        minGrade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                minGradeKeyTyped(evt);
            }
        });

        yearCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        yearCbx.setModel(new DefaultComboBoxModel<>(new String[] { "", "1", "2", "3", "4", "5", "6", "7", "8" }));
        yearCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

        initialLbl.setBackground(new Color(255, 255, 255));
        initialLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        initialLbl.setHorizontalAlignment(SwingConstants.CENTER);
        initialLbl.setText("Materie");
        initialLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        refreshBtn.setBackground(new Color(0, 102, 102));
        refreshBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        refreshBtn.setForeground(new Color(255, 255, 255));
        refreshBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\refresh.png"));
        refreshBtn.setText("Refresh");
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setIconTextGap(8);
        refreshBtn.addActionListener(this::refreshBtnActionPerformed);

        searchBtn.setBackground(new Color(0, 102, 102));
        searchBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        searchBtn.setForeground(new Color(255, 255, 255));
        searchBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\search.png"));
        searchBtn.setText("Cauta");
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setIconTextGap(8);
        searchBtn.addActionListener(this::searchBtnActionPerformed);

        firstName.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        firstName.setHorizontalAlignment(JTextField.CENTER);

        firstNameLbl.setBackground(new Color(255, 255, 255));
        firstNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        firstNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        firstNameLbl.setText("Prenume");
        firstNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        lastNameLbl.setBackground(new Color(255, 255, 255));
        lastNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        lastNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        lastNameLbl.setText("Nume");
        lastNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        lastName.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        lastName.setHorizontalAlignment(JTextField.CENTER);

        matNrLbl.setBackground(new Color(255, 255, 255));
        matNrLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        matNrLbl.setHorizontalAlignment(SwingConstants.CENTER);
        matNrLbl.setText("Nr. Matricol");
        matNrLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        matNr.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        matNr.setHorizontalAlignment(JTextField.CENTER);
        matNr.setVerifyInputWhenFocusTarget(false);
        matNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                matNrKeyTyped(evt);
            }
        });

        exportToExcelBtn.setBackground(new Color(0, 102, 102));
        exportToExcelBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        exportToExcelBtn.setForeground(new Color(255, 255, 255));
        exportToExcelBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\excel.png"));
        exportToExcelBtn.setToolTipText("Export to Excel");
        exportToExcelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportToExcelBtn.setIconTextGap(8);
        exportToExcelBtn.addActionListener(this::exportToExcelBtnActionPerformed);

        exportToPDF.setBackground(new Color(0, 102, 102));
        exportToPDF.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        exportToPDF.setForeground(new Color(255, 255, 255));
        exportToPDF.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\pdf.png"));
        exportToPDF.setToolTipText("Export to PDF");
        exportToPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportToPDF.setIconTextGap(8);
        exportToPDF.addActionListener(this::exportToPDFActionPerformed);

        maxGrade.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        maxGrade.setHorizontalAlignment(JTextField.CENTER);
        maxGrade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                maxGradeKeyTyped(evt);
            }
        });

        maxDate.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));

        jLabel1.setText("     -     ");

        jLabel2.setText("     -     ");

        GroupLayout dataPanelLayout = new GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(firstName))
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(matNr, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lastName)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(classCodeLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(dataPanelLayout.createSequentialGroup()
                                        .addComponent(minGrade, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(maxGrade, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                                    .addComponent(minDate, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maxDate, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                .addGap(30, 30, 30))
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(dataPanelLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(yearLbl, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(yearCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(dataPanelLayout.createSequentialGroup()
                                        .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(courseCodeCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(dataPanelLayout.createSequentialGroup()
                                        .addComponent(gradeLbl, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(semesterCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(course, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                                .addGap(114, 114, 114)))
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(exportToPDF, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exportToExcelBtn, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                            .addComponent(refreshBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE))
                .addContainerGap())
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yearLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gradeLbl)
                            .addComponent(semesterCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(course, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addComponent(courseCodeCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(maxGrade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(classCodeLbl)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(minGrade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(minDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(maxDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(matNr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(firstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(exportToExcelBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportToPDF, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addContainerGap())
        );

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(footPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(headPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(524, Short.MAX_VALUE)
                .addComponent(footPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addComponent(headPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 479, Short.MAX_VALUE)))
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

    private void clearFields() {
        lastName.setText("");
        firstName.setText("");
        course.setText("");
        courseCodeCbx.setSelectedIndex(0);
        yearCbx.setSelectedIndex(0);
        semesterCbx.setSelectedIndex(0);
        minGrade.setText("");
        maxGrade.setText("");
        minDate.setDate(null);
        maxDate.setDate(null);
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
        yearCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 3).toString()));
        semesterCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 4).toString()));
        course.setText(model.getValueAt(index, 5).toString());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = format.parse(model.getValueAt(index, 6).toString());
        } catch (ParseException ex) {
            Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        minDate.setDate(date);
        minGrade.setText(model.getValueAt(index, 7).toString());

        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT CODM FROM MATERIE WHERE DENM = '" + course.getText() + "'";
            ResultSet resultSet = statement.executeQuery(stm);
            while (resultSet.next()) {
                courseCodeCbx.setSelectedIndex(resultSet.getInt(1));
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
        enableFields(false);

    }

    private void maxGradeKeyTyped(java.awt.event.KeyEvent evt) {
        if (Character.isLetter(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void exportToPDFActionPerformed(java.awt.event.ActionEvent evt) {
        MessageFormat header = new MessageFormat("Note Studenti");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            dataTab.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportToExcelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        //Export to Excel
        String currentDirectoryPath = "C:\\";
        JFileChooser excelFileChooser = new JFileChooser(currentDirectoryPath);
        excelFileChooser.setDialogTitle("Save As");
        FileOutputStream excelFOS = null;
        BufferedOutputStream excelBOS = null;

        FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(filter);
        int answer = excelFileChooser.showSaveDialog(null);
        if (answer == JFileChooser.APPROVE_OPTION) {
            XSSFWorkbook excelWorkbook = new XSSFWorkbook();
            XSSFSheet excelSheet = excelWorkbook.createSheet("Note Studenti");

            for (int i = 0; i < dataTab.getRowCount(); i++) {
                XSSFRow excelRow = excelSheet.createRow(i);
                for (int j = 0; j < dataTab.getColumnCount(); j++) {
                    XSSFCell cell = excelRow.createCell(j);
                    cell.setCellValue(dataTab.getValueAt(i, j).toString());
                }
            }
            try {
                excelFOS = new FileOutputStream( excelFileChooser.getSelectedFile()+".xlsx");
                excelBOS = new BufferedOutputStream(excelFOS);
                excelWorkbook.write(excelBOS);
                JOptionPane.showMessageDialog(null, "Export realizat cu success!");
            } catch (Exception ex) {
                Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (excelBOS != null) {
                        excelBOS.close();
                    }
                    if (excelFOS != null) {
                        excelFOS.close();
                    }

                } catch (Exception ex) {
                    Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void matNrKeyTyped(java.awt.event.KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }
    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String condition = "";
        if (!matNr.getText().isEmpty()) {
            condition += " AND E.CODE = " + matNr.getText();
        }
        if (!lastName.getText().isEmpty()) {
            condition += " AND E.NUME = '" + lastName.getText().toUpperCase() + "'";
        }
        if (!firstName.getText().isEmpty()) {
            condition += " AND E.PRENUME = '" + firstName.getText().toUpperCase() + "'";
        }
        if (!yearCbx.getItemAt(yearCbx.getSelectedIndex()).isEmpty()) {
            condition += " AND N.AN_STUDIU = " + yearCbx.getItemAt(yearCbx.getSelectedIndex());
        }
        if (!semesterCbx.getItemAt(semesterCbx.getSelectedIndex()).isEmpty()) {
            condition += " AND N.SEMESTRU = " + semesterCbx.getItemAt(semesterCbx.getSelectedIndex());
        }
        if (!courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex()).isEmpty()) {
            condition += " AND N.CODM = " + courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex());
        }
        if (!minGrade.getText().isEmpty() && !maxGrade.getText().isEmpty()) {
            condition += " AND N.NOTA BETWEEN " + minGrade.getText() + " AND " + maxGrade.getText();
        } else if (!minGrade.getText().isEmpty()) {
            condition += " AND N.NOTA = " + minGrade.getText();
        } else if (!maxGrade.getText().isEmpty()) {
            condition += " AND N.NOTA <= " + maxGrade.getText();
        }
        if (minDate.getDate() != null && maxDate.getDate() != null) {
            String minDateStr = new SimpleDateFormat("dd-MMM-yy").format(new Date(minDate.getDate().getTime())).toUpperCase();
            String maxDateStr = new SimpleDateFormat("dd-MMM-yy").format(new Date(maxDate.getDate().getTime())).toUpperCase();
            condition += " AND N.DATA BETWEEN '" + minDateStr + "' AND '" + maxDateStr + "'";
        } else if (minDate.getDate() != null) {
            String minDateStr = new SimpleDateFormat("dd-MMM-yy").format(new Date(minDate.getDate().getTime())).toUpperCase();
            condition += " AND N.DATA = '" + minDateStr + "'";
        } else if (maxDate.getDate() != null) {
            String maxDateStr = new SimpleDateFormat("dd-MMM-yy").format(new Date(maxDate.getDate().getTime())).toUpperCase();
            condition += " AND N.DATA <= '" + maxDateStr + "'";
        }

        PreparedStatement statement;
        try {
            Connection conn = MyConnection.getConnection();
            statement = conn.prepareStatement("SELECT E.CODE, E.NUME, E.PRENUME, N.AN_STUDIU, N.SEMESTRU, M.DENM, N.DATA, N.NOTA FROM NOTE N, ELEV E, MATERIE M WHERE E.CODE = N.CODE AND N.CODM = M.CODM " + condition);

            ResultSet resultSet = statement.executeQuery();
            logger.log(Level.INFO, "{0}-{1} SELECT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});

            int columns = resultSet.getMetaData().getColumnCount();
            DefaultTableModel model = (DefaultTableModel) dataTab.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector note = new Vector();
                for (int i = 0; i < columns; i++) {
                    note.add(resultSet.getInt(1));
                    note.add(resultSet.getString(2));
                    note.add(resultSet.getString(3));
                    note.add(resultSet.getInt(4));
                    note.add(resultSet.getInt(5));
                    note.add(resultSet.getString(6));
                    note.add(resultSet.getString(7));
                    note.add(resultSet.getFloat(8));
                }
                model.addRow(note);
            }
            conn.close();
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Nu au fost gasite date!");
            } else {
                logger.log(Level.INFO, "{0}-{1} SELECT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
            }

        } catch (SQLException e) {
            Logger.getLogger(DisplayForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
        dataTab.setAutoCreateRowSorter(true);
        centerTableCells();

    }

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
        table_update();
        enableFields(true);
    }

    private void minGradeKeyTyped(java.awt.event.KeyEvent evt) {
        if (Character.isLetter(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void courseCodeCbxActionPerformed(java.awt.event.ActionEvent evt) {
        if (!courses.isEmpty() && courseCodeCbx.getSelectedIndex() != 0) {
            Integer key = Integer.valueOf(courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex()));
            String mat = courses.get(key);
            if (!"".equals(mat)) {
                course.setText(mat);
            }
        }
    }

    private void enableFields(boolean enabled) {
        matNr.setEnabled(enabled);
        matNr.setEditable(enabled);

        firstName.setEnabled(enabled);
        firstName.setEditable(enabled);
        lastName.setEditable(enabled);
        lastName.setEnabled(enabled);

        courseCodeCbx.setEnabled(enabled);

        minGrade.setEnabled(enabled);
        minGrade.setEditable(enabled);
        maxGrade.setEnabled(enabled);
        maxGrade.setEditable(enabled);

        minDate.setEnabled(enabled);
        maxDate.setEnabled(enabled);

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new DisplayForm().setVisible(true));
    }

    private JTextField course;
    private JComboBox<String> courseCodeCbx;
    private JTable dataTab;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField matNr;
    private com.toedter.calendar.JDateChooser maxDate;
    private JTextField maxGrade;
    private com.toedter.calendar.JDateChooser minDate;
    private JTextField minGrade;
    private JComboBox<String> semesterCbx;
    private JLabel userIconLbl;
    private JComboBox<String> yearCbx;

}
