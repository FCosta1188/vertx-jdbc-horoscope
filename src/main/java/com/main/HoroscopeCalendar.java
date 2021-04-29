package com.main;

import java.awt.image.RasterOp;
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

  String generateCalendar(int year) {
    String message = "";
    //Generate horoscope for given year and return message:
    //"Generated successfully"
    //"Unable to generate for input year (is prior to the current year)"
    //"Already exists for input year

    Random rnd = new Random();
    int dailyScore = rnd.nextInt(10) + 1;

    return message;
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
