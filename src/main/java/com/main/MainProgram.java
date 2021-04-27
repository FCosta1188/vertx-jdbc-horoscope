package com.main;

import com.operations.PreparedStatementOperation;
import io.vertx.core.Vertx;
import com.util.DBConnection;

import java.sql.SQLException;

public class MainProgram {
  public static void main(String[] args) throws SQLException {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

    DBConnection dbc = new DBConnection();
    System.out.println(dbc.getConnection());

  }//psvm
}//MainProgram
