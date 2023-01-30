package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainForm extends JFrame {

    public MainForm() {
        initComponents();
        User user = LoginForm.getUser();
        String role = null;
        if (user != null) {
            userIconLbl.setText(user.getUsername());
            role = user.getRole();
        } else {
            userIconLbl.setText("UserName");
        }
        
        if("VIZUALIZARE".equals(role)){
            noteLbl.setVisible(false);
            managementLbl.setVisible(false);
        }
    }

    private void initComponents() {

        JPanel jPanel1 = new JPanel();
        JPanel jPanel3 = new JPanel();
        userIconLbl = new JLabel();
        JButton logoutBtn = new JButton();
        JPanel jPanel4 = new JPanel();
        JPanel jPanel2 = new JPanel();
        noteLbl = new JLabel();
        JLabel visualizeLbl = new JLabel();
        managementLbl = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Meniu Principal");
        setBounds(new Rectangle(400, 200, 0, 0));

        jPanel1.setBackground(new Color(255, 255, 255));

        jPanel3.setBackground(new Color(0, 102, 102));

        userIconLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14)); // NOI18N
        userIconLbl.setForeground(new Color(255, 255, 255));
        userIconLbl.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\user.png"));
        userIconLbl.setText("UserName");
        userIconLbl.setToolTipText("");

        logoutBtn.setBackground(new Color(0, 102, 102));
        logoutBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14)); // NOI18N
        logoutBtn.setForeground(new Color(255, 255, 255));
        logoutBtn.setText("Logout");
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userIconLbl, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutBtn, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(userIconLbl, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
            .addComponent(logoutBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new Color(0, 102, 102));

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new Color(255, 255, 255));

        noteLbl.setBackground(new Color(255, 255, 255));
        noteLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        noteLbl.setForeground(new Color(0, 102, 102));
        noteLbl.setHorizontalAlignment(SwingConstants.CENTER);
        noteLbl.setText("Management Note");
        noteLbl.setBorder(new javax.swing.border.LineBorder(new Color(0, 102, 102), 1, true));
        noteLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noteLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                noteLblMouseClicked();
            }
        });

        visualizeLbl.setBackground(new Color(255, 255, 255));
        visualizeLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        visualizeLbl.setForeground(new Color(0, 102, 102));
        visualizeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        visualizeLbl.setText("Vizualizare Note");
        visualizeLbl.setBorder(new javax.swing.border.LineBorder(new Color(0, 102, 102), 1, true));
        visualizeLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        visualizeLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visualizeLblMouseClicked();
            }
        });

        managementLbl.setBackground(new Color(255, 255, 255));
        managementLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        managementLbl.setForeground(new Color(0, 102, 102));
        managementLbl.setHorizontalAlignment(SwingConstants.CENTER);
        managementLbl.setText("Management Elevi");
        managementLbl.setBorder(new javax.swing.border.LineBorder(new Color(0, 102, 102), 1, true));
        managementLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        managementLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                managementLblMouseClicked();
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(visualizeLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noteLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(managementLbl, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(managementLbl, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(visualizeLbl, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(noteLbl, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addGap(90, 90, 90))
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(248, 248, 248))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int answer = JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa parasesti sesiunea?", "Select", JOptionPane.YES_NO_OPTION);
        if (answer == 0) {
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }

    private void managementLblMouseClicked() {
        new ManagementEleviForm().setVisible(true);
        this.dispose();
    }

    private void noteLblMouseClicked() {
        new GradesForm().setVisible(true);
        this.dispose();
    }

    private void visualizeLblMouseClicked() {
        new DisplayForm().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }

    private JLabel managementLbl;
    private JLabel noteLbl;
    private JLabel userIconLbl;

}
