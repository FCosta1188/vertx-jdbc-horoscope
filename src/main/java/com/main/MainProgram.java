package com.main;

import com.operations.PreparedStatementOperation;
import com.util.Months;
import io.vertx.core.Vertx;
import com.util.DBConnection;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainProgram {
  public static void main(String[] args) throws SQLException {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

    DBConnection dbc = new DBConnection();
    System.out.println("-----------------");
    System.out.println("DB Connection test: " + dbc.getConnection());

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();
    System.out.println("-----------------");
    System.out.println("ENUM test: " + Months.FEB.getMonthDays(currentYear + 3));

  }//psvm
}//MainProgram
