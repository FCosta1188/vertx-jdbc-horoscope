package com.operations;

import com.util.DBConnection;

import java.sql.*;

public class PreparedStatementOperation {

    private Connection cxn = null;
    private static final String SELECT = "SELECT signDates FROM vertx.horoscope WHERE sign = ?";
    private static final String INSERT = "INSERT user(first_name,last_name, email, country) VALUES (?, ?,?, ?)";
    private static final String UPDATE = "UPDATE user SET first_name = ?,last_name=?, email = ?, country = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM user WHERE id = ?";

    public PreparedStatementOperation() {
        try {
            cxn = DBConnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String select(String sign) {
        try {
            if (cxn != null) {
                //SELECT
                PreparedStatement pstmt = cxn.prepareStatement(SELECT);
                pstmt.setString(1, sign);
                ResultSet rs = pstmt.executeQuery();
                boolean next = rs.next();
                if (next) {
                    //int id = rs.getInt("id");
                    String dates = rs.getString("signDates");
                    return dates;
                } else {
                    return "-1";
                }
            } else {
                return "-1";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "-1";
        }
    }

    public void insert(String firstName, String lastName, String email, String country) {
        try {
            if (cxn != null) {
                //INSERT
                PreparedStatement pstmt = cxn.prepareStatement(INSERT);
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, email);
                pstmt.setString(4, country);
                int ret = pstmt.executeUpdate();
                System.out.println("Insert return: " + (ret == 1 ? "OK" : "ERROR"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(int id, String firstName, String lastName, String email, String country) {
        try {
            if (cxn != null) {
                //UPDATE
                PreparedStatement pstmt = cxn.prepareStatement(UPDATE);
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, email);
                pstmt.setString(4, country);
                pstmt.setInt(5, id);
                int ret = pstmt.executeUpdate();
                System.out.println("Update return: " + (ret == 1 ? "OK" : "ERROR"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            if (cxn != null) {
                //DELETE
                PreparedStatement pstmt = cxn.prepareStatement(DELETE);
                pstmt.setInt(1, id);
                int ret = pstmt.executeUpdate();
                System.out.println("Delete return: " + (ret == 1 ? "OK" : "ERROR"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
