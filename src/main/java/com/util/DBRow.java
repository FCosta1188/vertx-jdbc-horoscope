package com.util;

public class DBRow {

  private int id;
  private int year;
  private String sign;
  private String month;
  private int day;
  private int score;

  public DBRow(int id, int year, String sign, String month, int day, int score) {
    this.id = id;
    this.year = year;
    this.sign = sign;
    this.month = month;
    this.day = day;
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public int getYear() {
    return year;
  }

  public String getSign() {
    return sign;
  }

  public String getMonth() {
    return month;
  }

  public int getDay() {
    return day;
  }

  public int getScore() {
    return score;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "DBRow{" +
            "id=" + id +
            ", year=" + year +
            ", sign='" + sign + '\'' +
            ", month='" + month + '\'' +
            ", day=" + day +
            ", score=" + score +
            '}';
  }

}//DBRow
