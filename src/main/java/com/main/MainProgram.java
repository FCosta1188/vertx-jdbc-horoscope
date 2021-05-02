package com.main;

import io.vertx.core.Vertx;
import com.util.DBConnection;
import java.sql.SQLException;

public class MainProgram {
  public static void main(String[] args) throws SQLException {

    System.out.println("=====Main Program: START=====");

    DBConnection dbc = new DBConnection();
    System.out.println("DB Connection: " + dbc.getConnection());

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

    System.out.println("=====Main Program: END=====");

  }//psvm
}//MainProgram
