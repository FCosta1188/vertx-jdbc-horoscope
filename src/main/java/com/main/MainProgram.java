package com.main;

import com.operations.PreparedStatementOperation;
import com.util.Months;
import io.vertx.core.Vertx;
import com.util.DBConnection;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.json.JsonCodec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

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
