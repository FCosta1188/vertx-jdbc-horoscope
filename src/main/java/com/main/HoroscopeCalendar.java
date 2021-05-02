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

  private final String[] SENTENCE_DAY_QUALITY = {
    "Awful,Horrible,Atrocious", //day score = 1
    "Terrible,Dreadful,Adverse",//2
    "Very bad,Unlucky,Unfavourable",//3
    "Bad,Nasty,Unpleasant",//4
    "Regular,Normal,Average",//5
    "Easy,Good,Nice",//6
    "Great,Very good,Very nice",//7
    "Awesome,Beautiful,Excellent",//8
    "Incredible,Marvelous,Outstanding",//9
    "Superb,Uncanny,Astonishing",//10
  };

  private final String[] SENTENCE_SUBJECTS = {
    "an asteroid,a lion,the police", //day score = 1
    "a shark,the doctor,a bus",//2
    "a gangster,a snake,a truck",//3
    "a pidgeon,a jerk,a taxi",//4
    "a bully,a stranger,a train",//5
    "a butterfly,a comedian,a boat",//6
    "a dog,your mom,a superhero",//7
    "a celebrity,a dragon,an alien",//8
    "a friend,a model,the government",//9
    "the love of your life,your family,your best friend",//10
  };

  private final String[] SENTENCE_VERBS = {
    "shit on,eat,beat", //day score = 1
    "lose,worsen,kidnap",//2
    "cry,decrease,hurt",//3
    "forget,run over,crash on",//4
    "bite,punish,fight",//5
    "increase,repay,encourage",//6
    "improve,offer,motivate",//7
    "kiss,dream,reward",//8
    "give,award,honor",//9
    "win,love,donate",//10
  };

  private final String[] SENTENCE_OBJECTS = {
    "you,your money,your partner", //day score = 1
    "you,your head,a stinky sock",//2
    "you,your legs,a broken car",//3
    "you,your head,a rotten apple",//4
    "you,your head,a pineapple pizza",//5
    "you,your muscles,a concert",//6
    "you,a motorbike,a gift",//7
    "you,a pool,your brain",//8
    "you,the lottery,a villa",//9
    "you,a lot of money,a house",//10
  };

  public HoroscopeCalendar() {
  }

  public PreparedStatementOperation getPso() {
    return pso;
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

  //Shows which Zodiac sign has the best score overall for a given year.
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

  //Shows which month is the best on average (by score) for a Zodiac sign in a given year.
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

  //Bonus task: generate a sentence that describes what happens to an astrological sign on a given day.
  String getDailySentence(int year, String sign, String month, int day) {
    DBRow dayRow = pso.selectRow(year, sign, month, day);
    Random rnd = new Random(System.currentTimeMillis());

    //day
    String dayQualityStr = SENTENCE_DAY_QUALITY[dayRow.getScore()-1];
    String[] dayQualityStrArr = dayQualityStr.split(",");
    String dayQuality = dayQualityStrArr[rnd.nextInt(3)];

    //subj
    String subjStr = SENTENCE_SUBJECTS[dayRow.getScore()-1];
    String[] subjStrArr = subjStr.split(",");
    String subj = subjStrArr[rnd.nextInt(3)];


    //verb
    String verbStr = SENTENCE_VERBS[dayRow.getScore()-1];
    String[] verbStrArr = verbStr.split(",");
    String verb = verbStrArr[rnd.nextInt(3)];

    //obj
    String objStr = SENTENCE_OBJECTS[dayRow.getScore()-1];
    String[] objStrArr = objStr.split(",");
    String obj = objStrArr[rnd.nextInt(3)];

    return dayQuality + " day: " + subj + " will " + verb + " " + obj + ".";
  }

}//HoroscopeCalendar
