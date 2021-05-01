package com.main;

import com.operations.PreparedStatementOperation;
import com.util.DBRow;
import com.util.Months;

import java.awt.image.RasterOp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HoroscopeCalendar {
  private PreparedStatementOperation pso = new PreparedStatementOperation();

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

  private final String[] SENTENCE_DAY_QUALITY = {
          "Awful", //day score = 1
          "Terrible",//2
          "Shitty",//3
          "Bad",//4
          "Regular",//5
          "Excellent",//6
          "Great",//7
          "Awesome",//8
          "Incredible",//9
          "Superb",//10
  };

  private final String[] SENTENCE_DAY_ACTION = {
          "Stay home!", //day score = 1
          "test",//2
          "test",//3
          "test",//4
          "test",//5
          "test",//6
          "test",//7
          "test",//8
          "test",//9
          "Go buy lottery tickets!",//10
  };

  public HoroscopeCalendar() {
  }

  String generateYearlyCalendar(int year) {
    String msg = "";

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();

    if (year < currentYear) {
      msg = "Cannot generate calendars before " + currentYear;
      return msg;
    }

    ArrayList<DBRow> filteredRows = pso.selectRows("year", String.valueOf(year));
    //System.out.println(filteredRows.toString());

    if (filteredRows.size() > 0) {
      msg = "No new calendar generated (already exists)";
      return msg;
    }

    Random rnd = new Random();

    pso.resetId(1); //reset auto-increment db field, so that each year starts the id count from 1

    //Generate calendar only if year >= current calendar AND if calendar does not exist for given year
    for (String sign : SIGNS) {
      for (Months month : Months.values()) {
        for (int i = 1; i <= month.getMonthDays(year); i++) {
          int dailyScore = rnd.nextInt(10) + 1;
          pso.insertRow(year, sign, month.getMonthName(), i, dailyScore);
        }
      }
    }
    msg = "New calendar generated";

    return msg;
  }

  int getMonthlyAverage(int monthlyScoresSum, int monthDays) {
    return (int) Math.round((double) monthlyScoresSum / (double) monthDays);
  }

  int getMonthlyAverage(int[] monthlyScores) {
    int scoreSum = 0;

    for (int score : monthlyScores) {
      scoreSum += score;
    }

    return (int) Math.round((double) scoreSum / (double) monthlyScores.length);
  }

  ArrayList<String> getBestMonth(int year,  String sign) {
    ArrayList<Integer> monthlyAverages = new ArrayList<>();
    ArrayList<String> sameAverageMonths = new ArrayList<>();
    int bestAverage = 0;

    for (Months month : Months.values()) {

      ArrayList<DBRow> monthRows = pso.selectRows(year, sign, month.getMonthName());
      int monthScoreSum = 0;
        for (DBRow row : monthRows) {
          monthScoreSum += row.getScore();
        }

      monthlyAverages.add(getMonthlyAverage(monthScoreSum, month.getMonthDays(year)));
      }

    for (Months month : Months.values()) {
      int average = monthlyAverages.get(month.getMonthNumber() - 1); //ArrayList indexes start from 0, month indexes start from 1

      if (average >= bestAverage) {
        sameAverageMonths.add(month.getMonthName() + "," + average);
        bestAverage = average;
      }

    }

    //clean up monthlyAverages from possible average values lower than the max value
    ArrayList<String> removeMonths = new ArrayList<>();
    for (String str : sameAverageMonths) {
      String[] strArr = str.split(",");
      int a = Integer.parseInt(strArr[1]);

      if (a < bestAverage)
        removeMonths.add(str);
    }
    for (String m : removeMonths) {
      sameAverageMonths.remove(m);
    }

    return sameAverageMonths;
  }

  String getBestSign(int year) {
    return "Test sign: test score";
  }

  //Bonus task
  String getDailySentence(int year, String sign, String month, int day) {

    DBRow row = pso.selectRow(year, sign, month, day);

    return SENTENCE_DAY_QUALITY[row.getScore()-1] + " day ahead, " + SENTENCE_DAY_ACTION[row.getScore()-1];
  }

}//HoroscopeCalendar
