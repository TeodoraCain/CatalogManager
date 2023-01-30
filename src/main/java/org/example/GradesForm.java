package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GradesForm extends JFrame {

    Dictionary<Integer, String> courses = new Hashtable<>();
    private User user;
    private Logger logger;

    public GradesForm() {
        initComponents();
        initFields();
        user = LoginForm.getUser();
        logger = user.getLogger();
        if (user != null) {
            userIconLbl.setText(user.getUsername());
        } else {
            userIconLbl.setText("UserName");
        }
        table_update();

    }

//init comboboxes
    private void initFields() {
        courseCodeCbx.removeAllItems();

        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT CODM, DENM FROM MATERIE";
            ResultSet courseCodes = statement.executeQuery(stm);

            while (courseCodes.next()) {
                courseCodeCbx.addItem(String.valueOf(courseCodes.getInt(1)));
                courses.put(courseCodes.getInt(1), courseCodes.getString(2));
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }

        yearCbx.setSelectedIndex(0);
        semesterCbx.setSelectedIndex(0);
        courseCodeCbx.setSelectedIndex(0);
    }

    private boolean isEmptyField() {
        if (lastName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog introdu numele elevului!");
            return true;
        }
        if (firstName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog introdu prenumele elevului!");
            return true;
        }
        if (course.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog selecteaza o materie!");
            return true;
        }
        if (ndate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Te rog selecteaza data!");
            return true;
        }
        if (ndate.getDate().compareTo(new java.util.Date()) > 0) {
            JOptionPane.showMessageDialog(this, "Data din viitor!");
            return true;
        }
        if (grade.getText().isEmpty()) {
            grade.setText("0");
            JOptionPane.showMessageDialog(this, "Nota trebuie sa fie intre 0 si 10!");
            return true;
        }
        if (Float.parseFloat(grade.getText()) > 10 || Float.parseFloat(grade.getText()) < 0) {
            grade.setText("0");
            JOptionPane.showMessageDialog(this, "Nota trebuie sa fie intre 0 si 10!");
            return true;
        }
        return false;
    }

    private void table_update() {
        Connection conn = MyConnection.getConnection();
        if (user != null) {
            try {
                Statement statement = conn.createStatement();

                String stm = "SELECT E.CODE, E.NUME, E.PRENUME, N.AN_STUDIU, N.SEMESTRU, M.DENM, N.DATA, N.NOTA FROM NOTE N, ELEV E, MATERIE M WHERE E.CODE = N.CODE AND N.CODM = M.CODM";
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
            } catch (SQLException e) {
                Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
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
        JPanel jPanel1 = new JPanel();
        JLabel matNrLbl = new JLabel();
        matNr = new JTextField();
        JButton searchBtn = new JButton();
        firstName = new JTextField();
        JLabel lastNameLbl = new JLabel();
        lastName = new JTextField();
        JLabel firstNameLbl = new JLabel();
        JButton searchBtn1 = new JButton();
        JPanel jPanel2 = new JPanel();
        JLabel gradeLbl = new JLabel();
        JLabel classCodeLbl = new JLabel();
        semesterCbx = new JComboBox<>();
        JLabel yearLbl = new JLabel();
        JLabel birthDateLbl = new JLabel();
        courseCodeCbx = new JComboBox<>();
        ndate = new com.toedter.calendar.JDateChooser();
        JButton addBtn = new JButton();
        JButton modifyBtn = new JButton();
        course = new JTextField();
        grade = new JTextField();
        JButton deleteBtn = new JButton();
        yearCbx = new JComboBox<>();
        JLabel initialLbl = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Note Elevi");
        setBackground(new Color(255, 255, 255));
        setBounds(new Rectangle(400, 200, 0, 0));

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
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 540, Short.MAX_VALUE)
                .addComponent(logoutBtn, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        headPanelLayout.setVerticalGroup(
            headPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(userIconLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(homeBtn, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        );

        footPanel.setBackground(new Color(0, 102, 102));

        GroupLayout footPanelLayout = new GroupLayout(footPanel);
        footPanel.setLayout(footPanelLayout);
        footPanelLayout.setHorizontalGroup(
            footPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 812, Short.MAX_VALUE)
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
            Class[] types = new Class [] {
                Integer.class, String.class, String.class, Integer.class, Integer.class, String.class, String.class, Float.class
            };
            boolean[] canEdit = new boolean [] {
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

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 102)));
        jPanel1.setToolTipText("Cautare");

        matNrLbl.setBackground(new Color(255, 255, 255));
        matNrLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        matNrLbl.setHorizontalAlignment(SwingConstants.CENTER);
        matNrLbl.setText("Nr. Matricol");
        matNrLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        matNr.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        matNr.setHorizontalAlignment(JTextField.CENTER);
        matNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                matNrKeyTyped(evt);
            }
        });

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

        lastNameLbl.setBackground(new Color(255, 255, 255));
        lastNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        lastNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        lastNameLbl.setText("Nume");
        lastNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        lastName.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        lastName.setHorizontalAlignment(JTextField.CENTER);

        firstNameLbl.setBackground(new Color(255, 255, 255));
        firstNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        firstNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        firstNameLbl.setText("Prenume");
        firstNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        searchBtn1.setBackground(new Color(0, 102, 102));
        searchBtn1.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        searchBtn1.setForeground(new Color(255, 255, 255));
        searchBtn1.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\refresh.png"));
        searchBtn1.setText("Refresh");
        searchBtn1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn1.setIconTextGap(8);
        //anon class
        searchBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtn1ActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matNr))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchBtn1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchBtn1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(matNr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(matNrLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(firstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new Color(255, 255, 255));
        jPanel2.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 102)));
        jPanel2.setToolTipText("Editare");

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

        semesterCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        semesterCbx.setModel(new DefaultComboBoxModel<>(new String[] { "1", "2" }));
        semesterCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        courseCodeCbx.setModel(new DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        courseCodeCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));
        courseCodeCbx.addActionListener(this::courseCodeCbxActionPerformed);

        ndate.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));

        addBtn.setBackground(new Color(0, 102, 102));
        addBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        addBtn.setForeground(new Color(255, 255, 255));
        addBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\add.png"));
        addBtn.setText("Adauga");
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(this::addBtnActionPerformed);

        modifyBtn.setBackground(new Color(0, 102, 102));
        modifyBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        modifyBtn.setForeground(new Color(255, 255, 255));
        modifyBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\edit.png"));
        modifyBtn.setText("Modifica");
        modifyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modifyBtn.setIconTextGap(2);
        modifyBtn.addActionListener(this::modifyBtnActionPerformed);

        course.setEditable(false);
        course.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        course.setHorizontalAlignment(JTextField.CENTER);

        grade.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
        grade.setHorizontalAlignment(JTextField.CENTER);
        grade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                gradeKeyTyped(evt);
            }
        });

        deleteBtn.setBackground(new Color(0, 102, 102));
        deleteBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        deleteBtn.setForeground(new Color(255, 255, 255));
        deleteBtn.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\minus.png"));
        deleteBtn.setText("Sterge");
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setIconTextGap(8);
        deleteBtn.addActionListener(this::deleteBtnActionPerformed);

        yearCbx.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        yearCbx.setModel(new DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        yearCbx.setCursor(new Cursor(Cursor.HAND_CURSOR));

        initialLbl.setBackground(new Color(255, 255, 255));
        initialLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 12));
        initialLbl.setHorizontalAlignment(SwingConstants.CENTER);
        initialLbl.setText("Materie");
        initialLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                            .addComponent(classCodeLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(grade)
                            .addComponent(ndate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(yearLbl, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(yearCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(courseCodeCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(course)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(gradeLbl, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(semesterCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(modifyBtn, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(deleteBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(deleteBtn)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modifyBtn)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addBtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yearLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(yearCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(gradeLbl)
                            .addComponent(semesterCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(course, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(initialLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addComponent(courseCodeCbx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(classCodeLbl)
                            .addComponent(grade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(birthDateLbl, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(ndate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        GroupLayout dataPanelLayout = new GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))
                    .addGroup(GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGroup(dataPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
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
                .addContainerGap(513, Short.MAX_VALUE)
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

    private void homeBtnActionPerformed(java.awt.event.ActionEvent ignoredEvt) {
        new MainForm().setVisible(true);
        this.dispose();
    }

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent ignoredEvt) {
        if ("ADMIN".equals(user.getRole()) || "EDITARE".equals(user.getRole())) {
            int index = dataTab.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecteaza un rand din tabel pentru a modifica in baza de date.",
                        "Select Row",
                        JOptionPane.INFORMATION_MESSAGE);

            } else if (!isEmptyField()) {
                try {
                    int code = elevExists();
                    Connection conn = MyConnection.getConnection();
                    PreparedStatement del = conn.prepareStatement("DELETE FROM NOTE WHERE CODE = ? AND CODM = ? AND DATA = ?");

                    del.setInt(1, code);
                    del.setInt(2, Integer.parseInt(courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex())));
                    del.setDate(3, new Date(ndate.getDate().getTime()));
                    del.executeUpdate();
                    logger.log(Level.INFO, "{0}-{1} DELETE {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});

                    JOptionPane.showMessageDialog(this,
                            "Nota elevului cu codul " + code + " a fost stearsa din baza de date.",
                            "Row Deleted",
                            JOptionPane.INFORMATION_MESSAGE);
                    conn.close();
                } catch (HeadlessException | NumberFormatException | SQLException e) {
                    System.out.println(e);
                }
                table_update();
            }
            enableFields(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul de " + user.getRole() + " nu se permite stergerea datelor!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void modifyBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if ("ADMIN".equals(user.getRole()) || "EDITARE".equals(user.getRole())) {
            int index = dataTab.getSelectedRow();

            if (index == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecteaza un rand din tabel pentru a modifica in baza de date.",
                        "Select Row",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                if (!isEmptyField()) {
                    try {
                        Connection conn = MyConnection.getConnection();
                        PreparedStatement upd = conn.prepareStatement("UPDATE NOTE SET NOTA = ?, AN_STUDIU = ?, SEMESTRU= ? WHERE CODE = ? AND CODM = ? AND DATA = ?");

                        upd.setFloat(1, Float.parseFloat(grade.getText()));
                        upd.setInt(2, Integer.parseInt(yearCbx.getItemAt(yearCbx.getSelectedIndex())));
                        upd.setInt(3, Integer.parseInt(semesterCbx.getItemAt(semesterCbx.getSelectedIndex())));
                        upd.setInt(4, Integer.parseInt(matNr.getText()));
                        upd.setInt(5, Integer.parseInt(courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex())));
                        upd.setDate(6, new Date(ndate.getDate().getTime()));
                        upd.executeUpdate();
                        logger.log(Level.INFO, "{0}-{1} UPDATE {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});

                        JOptionPane.showMessageDialog(this,
                                "Nota elevului " + lastName.getText().toUpperCase() + " " + firstName.getText().toUpperCase() + " a fost modificata in baza de date.",
                                "Row Inserted",
                                JOptionPane.INFORMATION_MESSAGE);
                        conn.close();

                    } catch (HeadlessException | NumberFormatException | SQLException e) {
                        Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
                        System.out.println(e);
                    }
                    table_update();
                    clearFields();
                }
            }
            enableFields(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pentru rolul de " + user.getRole() + " nu se permite modificarea datelor!",
                    "Role Restriction",
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private int elevExists() {
        int code = 0;
        if (!matNr.getText().isEmpty()) {
            code = Integer.parseInt(matNr.getText());
        }
        PreparedStatement statement;
        try {
            Connection conn = MyConnection.getConnection();
            if (firstName.getText().isEmpty()) {
                statement = conn.prepareStatement("SELECT CODE FROM ELEV WHERE CODE = ? OR NUME = ? ");
                statement.setInt(1, code);
                statement.setString(2, lastName.getText().toUpperCase());
            } else {
                statement = conn.prepareStatement("SELECT CODE FROM ELEV WHERE CODE = ? OR NUME = ? AND PRENUME = ? ");
                statement.setInt(1, code);
                statement.setString(2, lastName.getText().toUpperCase());
                statement.setString(3, firstName.getText().toUpperCase());
            }

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

        if (code == 0) {
            JOptionPane.showMessageDialog(this,
                    "Elevul nu exista in baza de date.",
                    "Eroare",
                    JOptionPane.INFORMATION_MESSAGE);
            return 0;

        } else {
            return code;
        }
    }
    private void addBtnActionPerformed(java.awt.event.ActionEvent ignoredEvt) {
        if ("ADMIN".equals(user.getRole()) || "EDITARE".equals(user.getRole())) {
            int code = elevExists();
            if (!isEmptyField() && code != 0) {
                try {
                    Connection conn = MyConnection.getConnection();
                    PreparedStatement add = conn.prepareStatement("INSERT INTO NOTE VALUES (?,?,?,?,?,?)");

                    add.setInt(1, code);
                    add.setInt(2, Integer.parseInt(courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex())));
                    add.setFloat(3, Float.parseFloat(grade.getText()));
                    add.setDate(4, new Date(ndate.getDate().getTime()));
                    add.setInt(5, Integer.parseInt(yearCbx.getItemAt(yearCbx.getSelectedIndex())));
                    add.setInt(6, Integer.parseInt(semesterCbx.getItemAt(semesterCbx.getSelectedIndex())));
                    add.executeUpdate();
                    logger.log(Level.INFO, "{0}-{1} INSERT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});

                    JOptionPane.showMessageDialog(this,
                            "Nota adaugata in baza de date.",
                            "Row Inserted",
                            JOptionPane.INFORMATION_MESSAGE);
                    conn.close();
                } catch (HeadlessException | NumberFormatException | SQLException e) {
                    Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
                    System.out.println(e);
                }
                clearFields();
                table_update();
            }
            enableFields(true);
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
        course.setText("");
        ndate.setDate(null);
        yearCbx.setSelectedIndex(0);
        grade.setText("");
        semesterCbx.setSelectedIndex(0);
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
        yearCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 3).toString()) - 1);
        semesterCbx.setSelectedIndex(Integer.parseInt(model.getValueAt(index, 4).toString()) - 1);
        course.setText(model.getValueAt(index, 5).toString());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = format.parse(model.getValueAt(index, 6).toString());
        } catch (ParseException ex) {
            Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        ndate.setDate(date);
        grade.setText(model.getValueAt(index, 7).toString());

        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT CODM FROM MATERIE WHERE DENM = '" + course.getText() + "'";
            ResultSet resultSet = statement.executeQuery(stm);
            while (resultSet.next()) {
                courseCodeCbx.setSelectedIndex(resultSet.getInt(1) - 1);
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
        enableFields(false);

    }

    private void enableFields(boolean enabled) {
        ndate.setEnabled(enabled);
        matNr.setEnabled(enabled);
        firstName.setEnabled(enabled);
        lastName.setEnabled(enabled);
        courseCodeCbx.setEnabled(enabled);
    }
    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int code = elevExists();
        if (code != 0) {
            PreparedStatement statement;
            try {
                Connection conn = MyConnection.getConnection();
                statement = conn.prepareStatement("SELECT E.CODE, E.NUME, E.PRENUME, N.AN_STUDIU, N.SEMESTRU, M.DENM, N.DATA, N.NOTA FROM NOTE N, ELEV E, MATERIE M WHERE E.CODE = N.CODE AND N.CODM = M.CODM AND E.CODE = ? ");
                statement.setInt(1, code);

                ResultSet resultSet = statement.executeQuery();
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
                if (model.getRowCount() != 0) {
                    logger.log(Level.INFO, "{0}-{1} SELECT {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
                }

                statement = conn.prepareStatement("SELECT E.CODE, E.NUME, E.PRENUME FROM ELEV E WHERE E.CODE = ? ");
                statement.setInt(1, code);

                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    matNr.setText(resultSet.getString(1));
                    lastName.setText(resultSet.getString(2));
                    firstName.setText(resultSet.getString(3));
                }
                conn.close();
            } catch (SQLException e) {
                Logger.getLogger(GradesForm.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(e);
            }
            centerTableCells();
        }
    }

    private void courseCodeCbxActionPerformed(java.awt.event.ActionEvent ignoredEvt) {
        if (!courses.isEmpty()) {
            Integer key = Integer.valueOf(courseCodeCbx.getItemAt(courseCodeCbx.getSelectedIndex()));
            String mat = courses.get(key);
            if (!"".equals(mat)) {
                course.setText(mat);
            }
        }
    }

    private void searchBtn1ActionPerformed(java.awt.event.ActionEvent ignoredEvt) {
        table_update();
        clearFields();
        enableFields(true);
    }

    private void gradeKeyTyped(java.awt.event.KeyEvent evt) {
        if (Character.isLetter(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void matNrKeyTyped(java.awt.event.KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    public static void main(String[] args) {
   //anon runnable
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GradesForm().setVisible(true);
            }
        });
    }

    private JTextField course;
    private JComboBox<String> courseCodeCbx;
    private JTable dataTab;
    private JTextField firstName;
    private JTextField grade;
    private JTextField lastName;
    private JTextField matNr;
    private com.toedter.calendar.JDateChooser ndate;
    private JComboBox<String> semesterCbx;
    private JLabel userIconLbl;
    private JComboBox<String> yearCbx;

}
