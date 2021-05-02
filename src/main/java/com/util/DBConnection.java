package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

  private static Connection connection;

  //constructor
  public DBConnection() {
  }

  public static Connection getConnection() throws SQLException {
    //Singleton pattern: create a connection only if it does not exists yet (to avoid multiple unnecessary connections)
    if (connection == null) {
      try {
        final String url = "jdbc:mysql://localhost:3306";
        final String user = "root";
        final String pass = "guevara88";
        // DriverManager.registerDriver(); // Legacy: needed before ver.4
        connection = DriverManager.getConnection(url, user, pass);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return connection;
  }//getConnection()

}//DBConnection

