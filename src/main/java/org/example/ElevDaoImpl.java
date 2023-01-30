package org.example;

import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElevDaoImpl implements ElevDao {

    List<Elev> eleviList;

    public ElevDaoImpl() {
        eleviList = new ArrayList<>();
        try {
            Connection conn = MyConnection.getConnection();
            Statement statement = conn.createStatement();

            String stm = "SELECT * FROM ELEV";
            ResultSet elevi = statement.executeQuery(stm);

            while (elevi.next()) {
                Elev elev = new Elev(elevi.getInt(1), elevi.getString(2), elevi.getString(3), elevi.getString(4), elevi.getString(5), elevi.getInt(6), elevi.getFloat(7), elevi.getInt(8));
                eleviList.add(elev);
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
    }

    @Override
    public List<Elev> getElevList() {
        return eleviList;
    }

    @Override
    public Elev getElev(int code) {
        return eleviList.get(code);
    }

    public Date sqlDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                                            Locale.ENGLISH);
        java.util.Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date sqlDate = new Date(date.getTime());
        return sqlDate;
    }

    @Override
    public void updateElev(Elev elev) {
        try {
            Connection conn = MyConnection.getConnection();

            PreparedStatement add = conn.prepareStatement("UPDATE ELEV SET NUME = ?, PRENUME = ?, INIT_TATA = ?, DATAN = ?, AN_STUDIU = ?, MEDIA= ?, CODC = ? WHERE CODE = ?");
            add.setString(1, elev.getNume());
            add.setString(2, elev.getPrenume());
            add.setString(3, elev.getInitTata());
            add.setDate(4, sqlDateFromString(elev.getDatan()));
            add.setInt(5, elev.getAnStudiu());
            add.setFloat(6, elev.getMedia());
            add.setInt(7, elev.getCodc());
            add.setInt(8, elev.getCode());

            add.executeUpdate();
            conn.close();
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }

    }

    @Override
    public void deleteElev(int code) {
        try {
            Connection conn = MyConnection.getConnection();
            PreparedStatement del = conn.prepareStatement("DELETE FROM ELEV WHERE CODE = " + code);
            del.executeUpdate();
            conn.close();
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void insertElev(Elev elev) {
        try {
            Connection conn = MyConnection.getConnection();

            PreparedStatement add = conn.prepareStatement("INSERT INTO ELEV VALUES (?,?,?,?,?,?,?,?)");
            add.setInt(1, elev.getCode());
            add.setString(2, elev.getNume());
            add.setString(3, elev.getPrenume());
            add.setString(4, elev.getInitTata());
            add.setDate(5, sqlDateFromString(elev.getDatan()));
            add.setInt(6, elev.getAnStudiu());
            add.setFloat(7, elev.getMedia());
            add.setInt(8, elev.getCodc());

            add.executeUpdate();
            conn.close();
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            Logger.getLogger(ManagementEleviForm.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e);
        }
    }

}
