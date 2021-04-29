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

public class MainProgram {
  public static void main(String[] args) throws SQLException {

    System.out.println("=====Test Program: START=====");

    DBConnection dbc = new DBConnection();
    System.out.println("-----------------");
    System.out.println("DB Connection test: " + dbc.getConnection());

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

    //System.out.println("-----------------");
    //PreparedStatementOperation pso = new PreparedStatementOperation();
    //System.out.println("-----------------");
    //System.out.println("DB INSERT test: " + pso.insertRow(2022, "Gemini", "May", "29:10"));

    //HoroscopeCalendar horoscope = new HoroscopeCalendar();
    //System.out.println("-----------------");
    //System.out.println("GENERATE YEARLY CALENDAR test: " + horoscope.generateYearlyCalendar(2025));
    //System.out.println("DAILY SENTENCE test: " + horoscope.getDailySentence(2026, "Aries", "January", 13));
    //System.out.println("-----------------");
    //System.out.println("DB SELECT_ALL test: " + pso.selectRows("", "").size());
    //System.out.println("-----------------");
    //System.out.println("DB SELECT_ROW test: " + pso.selectRow(2026, "Aries", "January", 12).toString());

    System.out.println("=====Test Program: END=====");

  }//psvm
}//MainProgram
