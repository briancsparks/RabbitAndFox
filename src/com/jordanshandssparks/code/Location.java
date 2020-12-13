package com.jordanshandssparks.code;

//Location

public class Location {
  private int col, row;

  public Location(int row, int col) {
    this.col = col;
    this.row = row;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public int getCol() {
    return col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getRow() {
    return row;
  }
}
