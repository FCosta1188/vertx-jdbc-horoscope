package com.main;

import com.operations.PreparedStatementOperation;
import com.util.DBRow;
import com.util.Months;

import java.awt.image.RasterOp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class HoroscopeCalendar {

  private final String[] SIGNS = {
    "Aries",
    "Taurus",
    "Gemini",
    "Cancer",
    "Leo",
    "Virgo",
    "Libra",
    "Scorpio",
    "Sagittarius",
    "Capricorn",
    "Aquarius",
    "Pisces"
  };

  public HoroscopeCalendar() {
  }

  String generateYearlyCalendar(int year) {
    String msg = "";

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();

    if (year < currentYear) {
      msg = "Cannot create past calendars";
      return msg;
    }

    PreparedStatementOperation pso = new PreparedStatementOperation();
    ArrayList<DBRow> filteredRows = pso.selectRows("year", String.valueOf(year));
    System.out.println(filteredRows.toString());

    if (filteredRows.size() > 0) {
      msg = "Calendar for selected year already exists";
      return msg;
    }

    Random rnd = new Random();

    pso.resetId(1); //reset auto-increment db field, so that each year starts the id count from 1

    //Generate calendar only if year >= current calendar AND if calendar does not exist for given year
    for (String sign : SIGNS) {
      for (Months month : Months.values()) {
        for (int i = 1; i <= month.getMonthDays(year); i++) {
          int dailyScore = rnd.nextInt(10) + 1;
          String day_score = String.valueOf(i).concat(":day score ").concat(String.valueOf(dailyScore));
          //day_score += i + "-day score " + dailyScore;
          System.out.println(day_score);
          pso.insertRow(year, sign, month.getMonthName(), String.valueOf(day_score));
        }
      }
    }
    msg = "Calendar generated successfully";

    return msg;
  }

  double getMonthlyAverage(int year, String sign, String month) {
    double average = 0;
    return average;
  }

  String getBestMonth(int year,  String sign) {
    return "test";
  }

  String getBestYearlySign(int year) {
    return "test";
  }

  //Bonus task
  String getDailySentence(int year, String sign, String month, String day_score) {
    return "test";
  }

}//HoroscopeCalendar
