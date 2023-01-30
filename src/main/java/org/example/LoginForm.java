package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginForm extends JFrame {

    private boolean show = false;
    private static User user;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {

        JPanel jPanel1 = new JPanel();
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel();
        JLabel jLabel1 = new JLabel();
        userName = new JTextField();
        passWord = new JPasswordField();
        JLabel userNameLbl = new JLabel();
        JLabel passwordLbl = new JLabel();
        JButton loginBtn = new JButton();

        JPanel footerPanel = new JPanel();
        showPassword = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login Catalog");
        setBounds(new Rectangle(400, 200, 0, 0));
        setResizable(false);

        jPanel1.setBackground(new Color(255, 255, 255));

        titlePanel.setBackground(new Color(0, 102, 102));

        title.setFont(new Font("Tempus Sans ITC", Font.BOLD, 24)); // NOI18N
        title.setForeground(new Color(255, 255, 255));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setText("Catalog Online");
        title.setHorizontalTextPosition(SwingConstants.CENTER);

        jLabel1.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\student.png"));

        GroupLayout titlePanelLayout = new GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addComponent(jLabel1)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(title, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        userName.setHorizontalAlignment(JTextField.CENTER);

        passWord.setHorizontalAlignment(JTextField.CENTER);

        userNameLbl.setBackground(new Color(255, 255, 255));
        userNameLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        userNameLbl.setForeground(new Color(0, 102, 102));
        userNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLbl.setLabelFor(userName);
        userNameLbl.setText("UserName");
        userNameLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        passwordLbl.setBackground(new Color(255, 255, 255));
        passwordLbl.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        passwordLbl.setForeground(new Color(0, 102, 102));
        passwordLbl.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLbl.setLabelFor(passWord);
        passwordLbl.setText("Parola");
        passwordLbl.setHorizontalTextPosition(SwingConstants.CENTER);

        loginBtn.setBackground(new Color(0, 102, 102));
        loginBtn.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        loginBtn.setForeground(new Color(255, 255, 255));
        loginBtn.setText("Logare");
        loginBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new Color(0, 102, 102), new Color(255, 255, 255), null, null));
        loginBtn.addActionListener(this::loginBtnActionPerformed);

        footerPanel.setBackground(new Color(0, 102, 102));

        GroupLayout footerPanelLayout = new GroupLayout(footerPanel);
        footerPanel.setLayout(footerPanelLayout);
        footerPanelLayout.setHorizontalGroup(
            footerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 484, Short.MAX_VALUE)
        );
        footerPanelLayout.setVerticalGroup(
            footerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        showPassword.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\eye.png"));
        showPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPasswordMouseClicked();
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(userName)
                    .addComponent(passWord)
                    .addComponent(passwordLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userNameLbl, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(loginBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showPassword)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(footerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(userNameLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userName, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(passwordLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passWord, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(showPassword, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(footerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 481, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void showPasswordMouseClicked() {
        if (!show) {
            passWord.setEchoChar((char) 0);
            showPassword.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\closedEye.png"));
            show = true;
        } else {
            passWord.setEchoChar('*');
            showPassword.setIcon(new ImageIcon("C:\\Users\\teo_l\\IdeaProjects\\CatalogManager\\src\\main\\java\\org\\example\\Images\\eye.png"));
            show = false;
        }

    }

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (!isEmptyField()) {
            Connection conn = MyConnection.getConnection();
            PreparedStatement statement;
            String username = userName.getText().toUpperCase();
            String password = String.valueOf(passWord.getPassword()).toUpperCase();
    //try-catch
            try {
                statement = conn.prepareStatement("SELECT * FROM CATALOG_USERS WHERE USERNAME = ? AND PAROLA= ? ");
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    user = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                    user.getLogger().log(Level.INFO, "{0}-{1} LOGARE {2}", new Object[]{user.getUsername(), user.getRole(), new java.util.Date()});
                    new MainForm().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Nume utilizator sau parola gresita!");
                }

            } catch (SQLException ex) {
                Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static User getUser() {
        return user;
    }

    private boolean isEmptyField() {
        if (userName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date lipsa!");
            return true;
        }
        if (String.valueOf(passWord.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date lipsa!");
            return true;
        }
        return false;

    }

    public static void main(String[] args) {
    //lambda runnable
        EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    private JPasswordField passWord;
    private JLabel showPassword;
    private JTextField userName;

}
