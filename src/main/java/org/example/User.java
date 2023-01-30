/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private String username;
    private String password;
    private String role;
    private int ID;
    private Logger logger;

    public User(String username, String password, String role, int ID) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.ID = ID;
        //logger
        FileHandler fileHandler = null;
        logger = Logger.getLogger("ActivityLog");
        try {
            fileHandler = new FileHandler("ActivityLog");
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Elev.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.addHandler(fileHandler);
    }

    public int getID() {
        return ID;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", role=" + role + ", ID=" + ID + '}';
    }
}
