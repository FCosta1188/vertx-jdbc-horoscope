package com.main;

import com.operations.PreparedStatementOperation;
import com.util.DBRow;
import com.util.Months;

import java.awt.image.RasterOp;
import java.time.LocalDate;
import java.util.*;

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

  private final String[] SENTENCE_DAY_QUALITY_bySCORE = {
          "Awful,Horrible,Atrocious", //day score = 1
          "Terrible,test2,test3",//2
          "Shitty,test2,test3",//3
          "Bad,test2,test3",//4
          "Regular,test2,test3",//5
          "Excellent,test2,test3",//6
          "Great,test2,test3",//7
          "Awesome,test2,test3",//8
          "Incredible,test2,test3",//9
          "Superb,test2,test3",//10
  };

  private final String[] SENTENCE_SUBJECTS = {"a pidgeon", "a lion", "a friend"};

  private final String[] SENTENCE_OBJECTS = {"your head", "your car", "your wife"};

  private final String[] SENTENCE_DAY_VERB_bySCORE = {
          "shit on, eat, fuck", //day score = 1
          "test1,test2,test3",//2
          "test1,test2,test3",//3
          "test1,test2,test3",//4
          "test1,test2,test3",//5
          "test1,test2,test3",//6
          "test1,test2,test3",//7
          "test1,test2,test3",//8
          "test1,test2,test3",//9
          "test1,test2,test3",//10
  };

  public HoroscopeCalendar() {
  }

  public String[] getSIGNS() {
    return SIGNS;
  }

  private int getAverage(int valueSum, int valueNum) {
    return (int) Math.round((double) valueSum / (double) valueNum);
  }

  private int getAverage(int[] values) {
    int valueSum = 0;

    for (int value : values) {
      valueSum += value;
    }

    return (int) Math.round((double) valueSum / (double) values.length);
  }

  String generateYearlyCalendar(int year) {
    String msg = "";

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();

    if (year < currentYear) {
      msg = "Cannot generate calendars preceding " + currentYear;
      return msg;
    }

    ArrayList<DBRow> filteredRows = pso.selectRows("year", String.valueOf(year));

    if (filteredRows.size() > 0) {
      msg = "No new calendar generated (already exists for " + year + ")";
      return msg;
    }

    Random rnd = new Random(System.currentTimeMillis());

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
    msg = "Calendar generated for " + year;

    return msg;
  }

  ArrayList<String> getBestSign(int year) {
    ArrayList<String> signAverages = new ArrayList<>();
    ArrayList<String> bestAverageSigns = new ArrayList<>();
    int bestAverage = 0;

    for (String sign : SIGNS) {
      int signAverageSum = 0;
      for (Months month : Months.values()) {
        ArrayList<DBRow> monthRows = pso.selectRows(year, sign, month.getMonthName());
        int monthScoreSum = 0;
        for (DBRow row : monthRows) {
          monthScoreSum += row.getScore();
        }
        int monthlyAverage = getAverage(monthScoreSum, month.getMonthDays(year));
        signAverageSum += monthlyAverage;
      }
      signAverages.add(sign + "," + getAverage(signAverageSum, 12));
    }

    //get highest average
    for (String str : signAverages) {
      String[] strArr = str.split(",");
      int average = Integer.parseInt(strArr[1]);

      if (average >= bestAverage) {
        bestAverageSigns.add(str);
        bestAverage = average;
      }
    }

    //clean up bestAverageSigns from possible average values lower than the max value
    ArrayList<String> removeSigns = new ArrayList<>();
    for (String str : bestAverageSigns) {
      String[] strArr = str.split(",");
      int a = Integer.parseInt(strArr[1]);

      if (a < bestAverage)
        removeSigns.add(str);
    }
    for (String s : removeSigns) {
      bestAverageSigns.remove(s);
    }

    return bestAverageSigns;
  }

  ArrayList<String> getBestMonth(int year,  String sign) {
    ArrayList<Integer> monthlyAverages = new ArrayList<>();
    ArrayList<String> bestAverageMonths = new ArrayList<>();
    int bestAverage = 0;

    for (Months month : Months.values()) {

      ArrayList<DBRow> monthRows = pso.selectRows(year, sign, month.getMonthName());
      int monthScoreSum = 0;
        for (DBRow row : monthRows) {
          monthScoreSum += row.getScore();
        }

      monthlyAverages.add(getAverage(monthScoreSum, month.getMonthDays(year)));
      }

    //get highest average
    for (Months month : Months.values()) {
      int average = monthlyAverages.get(month.getMonthNumber() - 1); //ArrayList indexes start from 0, month indexes start from 1

      if (average >= bestAverage) {
        bestAverageMonths.add(month.getMonthName() + "," + average);
        bestAverage = average;
      }

    }

    //clean up bestAverageMonths from possible average values lower than the max value
    ArrayList<String> removeMonths = new ArrayList<>();
    for (String str : bestAverageMonths) {
      String[] strArr = str.split(",");
      int a = Integer.parseInt(strArr[1]);

      if (a < bestAverage)
        removeMonths.add(str);
    }
    for (String m : removeMonths) {
      bestAverageMonths.remove(m);
    }

    return bestAverageMonths;
  }

  //Bonus task
  String getDailySentence(int year, String sign, String month, int day) {
    DBRow dayRow = pso.selectRow(year, sign, month, day);
    Random rnd = new Random(System.currentTimeMillis());

    //day
    String dayQualityStr = SENTENCE_DAY_QUALITY_bySCORE[dayRow.getScore()-1];
    String[] dayQualityStrArr = dayQualityStr.split(",");
    String dayQuality = dayQualityStrArr[rnd.nextInt(3)];

    //subj
    String[] subjStrArr = SENTENCE_SUBJECTS;
    String subj = subjStrArr[rnd.nextInt(3)];

    //verb
    String verbStr = SENTENCE_DAY_VERB_bySCORE[dayRow.getScore()-1];
    String[] verbStrArr = verbStr.split(",");
    String verb = verbStrArr[rnd.nextInt(3)];

    //obj
    String[] objStrArr = SENTENCE_OBJECTS;
    String obj = objStrArr[rnd.nextInt(3)];

    return dayQuality + " day: " + subj + " will " + verb + " " + obj;
  }

}//HoroscopeCalendar
