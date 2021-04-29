package com.util;

public class DBRow {

  private int id;
  private int year;
  private String sign;
  private String month;
  private String day_score;

  public DBRow(int id, int year, String sign, String month, String day_score) {
    this.id = id;
    this.year = year;
    this.sign = sign;
    this.month = month;
    this.day_score = day_score;
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

  public String getDay_score() {
    return day_score;
  }

  @Override
  public String toString() {
    return "DBRow{" +
            "id=" + id +
            ", year=" + year +
            ", sign='" + sign + '\'' +
            ", month='" + month + '\'' +
            ", day_score='" + day_score + '\'' +
            '}';
  }

}//DBRow
