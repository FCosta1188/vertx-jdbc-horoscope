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

  private final int MONTH_NUMBER;
  private final String MONTH_NAME;
  private final int MONTH_DAYS;

  Months(int num, String name, int days) {
    this.MONTH_NUMBER = num;
    this.MONTH_NAME = name;
    this.MONTH_DAYS = days;
  }

  public int getMonthNumber() {
    return MONTH_NUMBER;
  }

  public String getMonthName() {
    return MONTH_NAME;
  }

  public int getMonthDays() {
    return MONTH_DAYS;
  }

  public int getMonthDays(int year) { //Leap year calculation for FEB
    if (MONTH_NUMBER == 2) {
      if (year % 4 == 0) {
        if (year % 100 == 0) {
          if (year % 400 == 0)
            return MONTH_DAYS + 1;
          else
            return MONTH_DAYS;
        } else
            return MONTH_DAYS + 1;
      } else {
          return MONTH_DAYS;
      }
    } else
        return MONTH_DAYS;
  }

  @Override
  public String toString(){
    return (MONTH_NUMBER + "," + MONTH_NAME + "," + MONTH_DAYS);
  }
}
