package com.util;

public enum Months {

  JAN(1, "January", 31),
  FEB(2, "February", 28);

  private final int monthNumber;
  private final String monthName;
  private int monthDays;

  Months(int num, String name, int days) {
    this.monthNumber = num;
    this.monthName = name;
    this.monthDays = days;
  }

  public int getMonthDays() {
    return monthDays;
  }

  public int getMonthDays(int year) {
    if (monthNumber == 2) {
      if (year % 4 == 0) {
        if (year % 100 == 0) {
          if (year % 400 == 0)
            monthDays = 29;
          else
            monthDays = 28;
        } else
          monthDays = 29;
      } else {
        monthDays = 28;
      }
    }

    return monthDays;
  }

  @Override
  public String toString(){
    return (monthNumber + "," + monthName + "," + monthDays);
  }
}
