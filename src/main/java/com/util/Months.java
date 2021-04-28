package com.util;

public enum Months {

  JAN(1, "January", 31),
  FEB(2, "February", 28),
  MAR(3, "March", 31),
  APR(4, "April", 30),
  MAY(5, "May", 31),
  JUN(6, "June", 30),
  JUL(7, "July", 31),
  SEP(9, "September", 30),
  AUG(8, "August", 31),
  OCT(10, "October", 31),
  NOV(11, "November", 30),
  DEC(12, "December", 31);

  private final int monthNumber;
  private final String monthName;
  private final int monthDays;

  Months(int num, String name, int days) {
    this.monthNumber = num;
    this.monthName = name;
    this.monthDays = days;
  }

  public int getMonthNumber() {
    return monthNumber;
  }

  public String getMonthName() {
    return monthName;
  }

  public int getMonthDays() {
    return monthDays;
  }

  public int getMonthDays(int year) { //Leap year calculation for FEB
    if (monthNumber == 2) {
      if (year % 4 == 0) {
        if (year % 100 == 0) {
          if (year % 400 == 0)
            return monthDays + 1;
          else
            return monthDays;
        } else
            return monthDays + 1;
      } else {
          return monthDays;
      }
    } else
        return monthDays;
  }

  @Override
  public String toString(){
    return (monthNumber + "," + monthName + "," + monthDays);
  }
}
