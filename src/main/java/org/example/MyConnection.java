/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DATA_CONN = "jdbc:mysql://localhost:3306/catalog";
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static Connection CONN = null;

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            CONN = DriverManager.getConnection(DATA_CONN, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        return CONN;
    }
}
